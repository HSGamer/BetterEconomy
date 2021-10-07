package me.hsgamer.bettereconomy;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

public final class Utils {
    private Utils() {
        // EMPTY
    }

    @SuppressWarnings("deprecation")
    public static OfflinePlayer getOfflinePlayer(String player) {
        return Bukkit.getOfflinePlayer(player);
    }
}
