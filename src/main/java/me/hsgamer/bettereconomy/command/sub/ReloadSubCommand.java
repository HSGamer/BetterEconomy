package me.hsgamer.bettereconomy.command.sub;

import me.hsgamer.bettereconomy.BetterEconomy;
import me.hsgamer.bettereconomy.Permissions;
import me.hsgamer.hscore.bukkit.subcommand.SubCommand;
import me.hsgamer.hscore.bukkit.utils.MessageUtils;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class ReloadSubCommand extends SubCommand {
    private final BetterEconomy instance;

    public ReloadSubCommand(BetterEconomy instance) {
        super("reload", "Reload the plugin", "/eco reload", Permissions.RELOAD.getName(), true);
        this.instance = instance;
    }

    @Override
    public void onSubCommand(@NotNull CommandSender sender, @NotNull String label, @NotNull String... args) {
        instance.getMainConfig().reload();
        instance.getMessageConfig().reload();
        MessageUtils.sendMessage(sender, instance.getMessageConfig().getSuccess());
    }

    @Override
    public boolean isProperUsage(@NotNull CommandSender sender, @NotNull String label, @NotNull String... args) {
        return true;
    }

    @Override
    public @NotNull List<String> onTabComplete(@NotNull CommandSender sender, @NotNull String label, @NotNull String... args) {
        return Collections.emptyList();
    }
}
