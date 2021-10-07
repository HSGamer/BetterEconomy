package me.hsgamer.bettereconomy.config;

import lombok.Getter;
import me.hsgamer.hscore.bukkit.config.BukkitConfig;
import me.hsgamer.hscore.config.AnnotatedConfig;
import me.hsgamer.hscore.config.annotation.ConfigPath;import org.bukkit.plugin.Plugin;

@Getter
public class MessageConfig extends AnnotatedConfig {
    private @ConfigPath("prefix") String prefix = "&f[&6BetterEconomy&f] &r";
    private @ConfigPath("player-not-found") String playerNotFound = "&cThe player is not found";
    private @ConfigPath("player-only") String playerOnly = "&cThis command is for player only";
    private @ConfigPath("balance-output") String balanceOutput = "&eBalance: &f{balance}";
    private @ConfigPath("balance-top-output") String balanceTopOutput = "&f#{place} &e{name}: &f{balance}";
    private @ConfigPath("empty-balance-top") String emptyBalanceTop = "&eThe balance top is empty";

    public MessageConfig(Plugin plugin) {
        super(new BukkitConfig(plugin, "messages.yml"));
    }
}
