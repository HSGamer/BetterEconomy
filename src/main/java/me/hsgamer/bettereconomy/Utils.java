package me.hsgamer.bettereconomy;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Entity;

import java.util.Optional;
import java.util.UUID;

public final class Utils {
    private Utils() {
        // EMPTY
    }

    @SuppressWarnings("deprecation")
    public static OfflinePlayer getOfflinePlayer(String player) {
        return Optional.<OfflinePlayer>ofNullable(Bukkit.getPlayer(player)).orElseGet(() -> Bukkit.getOfflinePlayer(player));
    }

    public static OfflinePlayer getOfflinePlayer(UUID uuid) {
        return Optional.<OfflinePlayer>ofNullable(Bukkit.getPlayer(uuid)).orElseGet(() -> Bukkit.getOfflinePlayer(uuid));
    }

    public static UUID getUniqueId(OfflinePlayer offlinePlayer) {
        return Optional.ofNullable(offlinePlayer.getPlayer()).map(Entity::getUniqueId).orElseGet(offlinePlayer::getUniqueId);
    }

    public static UUID getUniqueId(String player) {
        return getUniqueId(getOfflinePlayer(player));
    }
}
