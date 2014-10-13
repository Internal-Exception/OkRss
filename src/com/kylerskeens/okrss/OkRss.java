package com.kylerskeens.okrss;

import com.gmail.filoghost.holograms.api.Hologram;
import com.gmail.filoghost.holograms.api.HolographicDisplaysAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author Kyler
 */
public class OkRss extends JavaPlugin {
    private final RssFetcher fetcher;
    private Hologram hologram;

    public OkRss() {
        fetcher = new RssFetcher("http://orekingdom.com/tppi/m/22384323/rss/false");
    }

    public static void main(String[] args) {
        RssFetcher fetcher = new RssFetcher("http://orekingdom.com/rules/m/26920536/rss/true");
        //System.out.println(fetcher.getFeed());
        System.out.println(new HtmlParser().parseHtml(fetcher.getFeed()));
    }

    public void reloadHologram() {
        hologram.clearLines();
        String[] lines = fetcher.getFeed().split("\n");
        hologram.addLine(lines[0]);
        hologram.addLine("");
        hologram.addLine("");
        for (int i = 1; i < lines.length; i++) {
            hologram.addLine(lines[i]);
        }
        hologram.update();
    }

    @Override
    public void onEnable() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
            public void run() {
                if (fetcher.update())
                    reloadHologram();
            }
        }, 0L, 60 * 20L);
        hologram = HolographicDisplaysAPI.createHologram(this, new Location(Bukkit.getWorld("world"), 267, 67, 45), "Test hologram");
        reloadHologram();
    }

    @Override

    public boolean onCommand(CommandSender sender, Command command, String str, String[] args) {
        if (args.length > 0) {
            if (args[0].equalsIgnoreCase("reload")) {
                if (sender.isOp()) {
                    if (fetcher.update())
                        reloadHologram();
                    sender.sendMessage(fetcher.getFeed());
                    return true;
                } else {
                    sender.sendMessage(ChatColor.RED + "okRSS: You need to be OP to reload okRSS.");
                }
            } else if (args[0].equalsIgnoreCase("view")) {
                sender.sendMessage(fetcher.getFeed());
                return true;
            } else {
                sender.sendMessage(ChatColor.RED + "okRSS: Unknown Command.");
            }
        } else {
            sender.sendMessage(ChatColor.GOLD + "okRSS: Coded by Kyler Skeens for orekingdom.com");
        }
        return false;
    }
}
