package me.hsgamer.bettereconomy.hook.placeholderapi;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.hsgamer.bettereconomy.BetterEconomy;
import me.hsgamer.bettereconomy.api.EconomyHandler;
import me.hsgamer.bettereconomy.config.MainConfig;
import me.hsgamer.bettereconomy.provider.EconomyHandlerProvider;
import me.hsgamer.bettereconomy.top.PlayerBalanceSnapshot;
import me.hsgamer.bettereconomy.top.TopRunnable;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
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
        String lower = params.toLowerCase(Locale.ROOT);
        if (lower.startsWith("top_")) {
            String query = lower.substring(4);
            if (query.isEmpty()) {
                return null;
            }

            String index;
            Function<PlayerBalanceSnapshot, String> function;
            if (query.startsWith("name_")) {
                index = query.substring(5);
                function = snapshot -> Bukkit.getOfflinePlayer(snapshot.getUuid()).getName();
            } else if (query.startsWith("uuid_")) {
                index = query.substring(5);
                function = snapshot -> snapshot.getUuid().toString();
            } else if (query.startsWith("balance_formatted_")) {
                index = query.substring(18);
                function = snapshot -> instance.get(MainConfig.class).format(snapshot.getBalance());
            } else if (query.startsWith("balance_")) {
                index = query.substring(8);
                function = snapshot -> String.valueOf(snapshot.getBalance());
            } else {
                return null;
            }

            List<PlayerBalanceSnapshot> top = instance.get(TopRunnable.class).getTopList();
            int i;
            try {
                i = Integer.parseInt(index);
            } catch (NumberFormatException e) {
                return null;
            }
            if (i < 1 || i > top.size()) {
                return null;
            }
            return function.apply(top.get(i - 1));
        }

        if (player == null) return null;

        EconomyHandler economyHandler = instance.get(EconomyHandlerProvider.class).getEconomyHandler();
        switch (lower) {
            case "balance":
                return String.valueOf(economyHandler.get(player.getUniqueId()));
            case "balance_formatted":
                return instance.get(MainConfig.class).format(economyHandler.get(player.getUniqueId()));
            case "top":
                return String.valueOf(instance.get(TopRunnable.class).getTopIndex(player.getUniqueId()) + 1);
        }

        return null;
    }
}
