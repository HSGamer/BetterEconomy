package me.hsgamer.bettereconomy.config;

import lombok.Getter;
import me.hsgamer.hscore.bukkit.config.BukkitConfig;
import me.hsgamer.hscore.config.AnnotatedConfig;
import me.hsgamer.hscore.config.annotation.ConfigPath;
import org.bukkit.plugin.Plugin;

@Getter
public class MessageConfig extends AnnotatedConfig {
    private @ConfigPath("prefix") String prefix = "&f[&6BetterEconomy&f] &r";
    private @ConfigPath("player-not-found") String playerNotFound = "&cThe player is not found";
    private @ConfigPath("player-only") String playerOnly = "&cThis command is for player only";
    private @ConfigPath("balance-output") String balanceOutput = "&eBalance: &f{balance}";
    private @ConfigPath("balance-top-output") String balanceTopOutput = "&f#{place} &e{name}: &f{balance}";
    private @ConfigPath("empty-balance-top") String emptyBalanceTop = "&eThe balance top is empty";
    private @ConfigPath("arg-not-found") String argNotFound = "&cInvalid Argument";
    private @ConfigPath("give-success") String giveSuccess = "&aSuccessfully give {balance} to {name}";
    private @ConfigPath("take-success") String takeSuccess = "&aSuccessfully take {balance} from {name}";
    private @ConfigPath("set-success") String setSuccess = "&aSuccessfully set {balance} to {name}";
    private @ConfigPath("success") String success = "&aSuccess";
    private @ConfigPath("cannot-do") String cannotDo = "&aCannot do this action";

    public MessageConfig(Plugin plugin) {
        super(new BukkitConfig(plugin, "messages.yml"));
    }
}
