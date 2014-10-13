package com.kylerskeens.okrss;

import org.bukkit.ChatColor;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class HtmlParser {
    /*
    * Shiver
    * */
    public String parseHtml(String input) {
        String formattedStr = "";
        boolean inTag = false;
        String tagName = "";
        char[] chars = input.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            char c = chars[i];
            if (c == '<') {
                inTag = true;
                tagName = "";
                continue;
            }
            if (inTag) {
                if (tagName.endsWith(" ")) {
                    tagName = tagName.toLowerCase().trim().replaceAll(" ", "");
                    if (tagName.equals("div")) {
                        formattedStr += "\n";

                        for (; i < chars.length; i++) {
                            if ((c = chars[i]) == '>') {
                                break;
                            }
                        }
                    } else if (tagName.equals("span")) {
                        tagName = "";
                        for (; i < chars.length; i++) {
                            tagName += chars[i];
                            if (tagName.endsWith("rgb(")) {
                                break;
                            }
                        }
                        tagName = "";
                        for (; i < chars.length; i++) {
                            tagName += chars[i];
                            if (tagName.endsWith(")")) {
                                break;
                            }
                        }
                        String rgb = tagName.replaceAll(" ", "");
                        rgb = rgb.substring(1, rgb.length() - 1);
                        String rgbSplit[] = rgb.split(",");
                        int r = Integer.parseInt(rgbSplit[0]);
                        int g = Integer.parseInt(rgbSplit[1]);
                        int b = Integer.parseInt(rgbSplit[2]);
                        formattedStr += rgbToChatColor(r, g, b);

                        for (; i < chars.length; i++) {
                            if ((c = chars[i]) == '>') {
                                break;
                            }
                        }
                    } else {

                    }
                } else {
                    tagName += c;
                }
            } else {
                formattedStr += c;
            }
            if (c == '>')
                inTag = false;
        }

        String formattedFinal = "";
        for (String str : formattedStr.split("\n")) {
            if (str.trim().length() > 2) {
                formattedFinal += "\n" + str.trim();
            }
        }

        return formattedFinal;
    }

    private ChatColor rgbToChatColor(int r, int g, int b) {
        HashMap<ChatColor, Color> hash = new HashMap<ChatColor, Color>();
        hash.put(ChatColor.BLACK, new Color(0, 0, 0));
        hash.put(ChatColor.DARK_BLUE, new Color(0, 0, 170));
        hash.put(ChatColor.DARK_GREEN, new Color(0, 170, 0));
        hash.put(ChatColor.DARK_AQUA, new Color(0, 170, 170));
        hash.put(ChatColor.DARK_GRAY, new Color(85, 85, 85));
        hash.put(ChatColor.BLUE, new Color(85, 85, 255));
        hash.put(ChatColor.GREEN, new Color(85, 255, 85));
        hash.put(ChatColor.AQUA, new Color(85, 255, 255));
        hash.put(ChatColor.DARK_RED, new Color(170, 0, 0));
        hash.put(ChatColor.DARK_PURPLE, new Color(170, 0, 170));
        hash.put(ChatColor.GRAY, new Color(170, 170, 170));
        hash.put(ChatColor.RED, new Color(255, 85, 85));
        hash.put(ChatColor.LIGHT_PURPLE, new Color(255, 85, 255));
        hash.put(ChatColor.GOLD, new Color(255, 170, 0));
        hash.put(ChatColor.YELLOW, new Color(255, 255, 85));
        hash.put(ChatColor.WHITE, new Color(255, 255, 255));

        double minDeviation = Float.MAX_VALUE;
        ChatColor bestColor = ChatColor.WHITE;
        for (Map.Entry<ChatColor, Color> e : hash.entrySet()) {
            double avg = (Math.abs(e.getValue().getRed() - r) + Math.abs(e.getValue().getGreen() - g) + Math.abs(e.getValue().getBlue() - b)) / 3.0;
            if (avg < minDeviation) {
                bestColor = e.getKey();
                minDeviation = avg;
            }
        }

        System.out.println(minDeviation);

        return bestColor;
    }
}
