package com.kylerskeens.okrss;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author Kyler
 */
public class OkRss extends JavaPlugin {
    private final RssFetcher fetcher;

    public OkRss() {
        fetcher = new RssFetcher("http://orekingdom.com/tppi/m/22384323/rss/true");
    }

    public static void main(String[] args) {
        new OkRss().onEnable();
    }

    @Override
    public void onEnable() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
            public void run() {
                fetcher.update();
            }
        }, 0L, 60 * 20L);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String str, String[] args) {
        if (args.length > 0) {
            if (args[0].equalsIgnoreCase("reload")) {
                if (sender.isOp()) {
                    fetcher.update();
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
