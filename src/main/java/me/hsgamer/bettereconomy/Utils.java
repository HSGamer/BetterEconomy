package me.hsgamer.bettereconomy;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.java.JavaPlugin;

public final class Utils {
    private Utils() {
        // EMPTY
    }

    @SuppressWarnings("deprecation")
    public static OfflinePlayer getOfflinePlayer(String player) {
        return Bukkit.getOfflinePlayer(player);
    }

    public static void schedule(Runnable runnable) {
        Bukkit.getScheduler().runTask(JavaPlugin.getProvidingPlugin(Utils.class), runnable);
    }

    public static void scheduleAsync(Runnable runnable) {
        Bukkit.getScheduler().runTaskAsynchronously(JavaPlugin.getProvidingPlugin(Utils.class), runnable);
    }
}
