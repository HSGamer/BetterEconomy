package me.hsgamer.bettereconomy.hook.placeholderapi;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.hsgamer.bettereconomy.BetterEconomy;
import me.hsgamer.bettereconomy.config.MainConfig;
import me.hsgamer.bettereconomy.holder.EconomyHolder;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

public class EconomyPlaceholder extends PlaceholderExpansion {
    private final BetterEconomy instance;

    public EconomyPlaceholder(BetterEconomy instance) {
        this.instance = instance;
    }

    @Override
    public @NotNull String getIdentifier() {
        return "bettereconomy";
    }

    @Override
    public @NotNull String getAuthor() {
        return Arrays.toString(instance.getDescription().getAuthors().toArray());
    }

    @Override
    public @NotNull String getVersion() {
        return instance.getDescription().getVersion();
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public @Nullable String onRequest(OfflinePlayer player, @NotNull String params) {
        EconomyHolder holder = instance.get(EconomyHolder.class);

        String lower = params.toLowerCase(Locale.ROOT);
        if (lower.startsWith("top_")) {
            String query = lower.substring(4);
            if (query.isEmpty()) {
                return null;
            }

            String index;
            Function<Map.Entry<UUID, Double>, String> function;
            if (query.startsWith("name_")) {
                index = query.substring(5);
                function = snapshot -> Bukkit.getOfflinePlayer(snapshot.getKey()).getName();
            } else if (query.startsWith("uuid_")) {
                index = query.substring(5);
                function = snapshot -> snapshot.getKey().toString();
            } else if (query.startsWith("balance_formatted_")) {
                index = query.substring(18);
                function = snapshot -> instance.get(MainConfig.class).format(snapshot.getValue());
            } else if (query.startsWith("balance_")) {
                index = query.substring(8);
                function = snapshot -> String.valueOf(snapshot.getValue());
            } else {
                return null;
            }

            int i;
            try {
                i = Integer.parseInt(index);
            } catch (NumberFormatException e) {
                return null;
            }

            return holder.getSnapshotAgent().getSnapshotByIndex(i - 1)
                    .map(function)
                    .orElse(null);
        }

        if (player == null) return null;

        switch (lower) {
            case "balance":
                return String.valueOf(holder.get(player.getUniqueId()));
            case "balance_formatted":
                return instance.get(MainConfig.class).format(holder.get(player.getUniqueId()));
            case "top":
                return String.valueOf(holder.getSnapshotAgent().getSnapshotIndex(player.getUniqueId()) + 1);
        }

        return null;
    }
}
