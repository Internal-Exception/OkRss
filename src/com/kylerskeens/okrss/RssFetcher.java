package com.kylerskeens.okrss;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import java.io.IOException;
import java.net.URL;
import java.util.List;

/**
 * Created by Kyler on 10/12/2014.
 */
public class RssFetcher {
    private final String link;
    private String feed;

    public RssFetcher(String link) {
        this.link = link;
        update();
    }

    public String getFeed() {
        return feed;
    }

    public boolean update() {
        String newFeed = "";
        try {
            SAXBuilder builder = new SAXBuilder();
            URL url = new URL(link);
            Document root = builder.build(url);
            Element rootN = root.getRootElement();
            Element channel = rootN.getChild("channel");
            Element item = channel.getChild("item");
            String title = item.getChildText("title");
            String content = "";
            for (Element element : (List<Element>) item.getChildren()) {
                if (element.getNamespacePrefix().equals("content")) {
                    content = element.getText();
                }
            }
            newFeed = title + new HtmlParser().parseHtml(content);


        } catch (IOException e) {
            System.out.println(e.getMessage());
        } catch (JDOMException e) {
            System.out.println(e.getMessage());
        }
        if (newFeed.equals(feed)) {
            return false;
        } else {
            feed = newFeed;
            return true;
        }
    }

    /*private String formatEntry(String rawStr) {
        rawStr = rawStr.replaceAll("</div>", "\n");
        rawStr = rawStr.replaceAll("<br>", "\n");
        rawStr = rawStr.replaceAll("&#160;", " ");

        String formatted = "";
        boolean inTag = false;
        for (char c : rawStr.toCharArray()) {
            if (c == '<') {
                inTag = true;
            }
            if (!inTag) {
                formatted += c;
            }
            if (c == '>') {
                inTag = false;
            }
        }

        String formattedFinal = "";
        for (String str : formatted.split("\n")) {
            if (!str.trim().equals("")) {
                formattedFinal += "\n" + str.trim();
            }
        }

        return formattedFinal;
    }*/
}
