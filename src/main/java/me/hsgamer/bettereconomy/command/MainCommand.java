package me.hsgamer.bettereconomy.command;

import me.hsgamer.bettereconomy.BetterEconomy;
import me.hsgamer.bettereconomy.Permissions;
import me.hsgamer.bettereconomy.command.sub.GiveSubCommand;
import me.hsgamer.bettereconomy.command.sub.ReloacSubCommand;
import me.hsgamer.bettereconomy.command.sub.SetSubCommand;
import me.hsgamer.bettereconomy.command.sub.TakeSubCommand;
import me.hsgamer.hscore.bukkit.subcommand.SubCommandManager;
import me.hsgamer.hscore.bukkit.utils.MessageUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class MainCommand extends Command {
    private final SubCommandManager subCommandManager;

    public MainCommand(BetterEconomy instance) {
        super(instance.getName().toLowerCase(Locale.ROOT), "Main Command", "/" + instance.getName().toLowerCase(Locale.ROOT), Collections.singletonList("eco"));
        this.subCommandManager = new SubCommandManager() {
            @Override
            public void sendHelpMessage(@NotNull CommandSender sender, @NotNull String label, @NotNull String... args) {
                if (sender.hasPermission(Permissions.ADMIN)) {
                    subcommands.forEach((subLabel, subCommand) -> MessageUtils.sendMessage(sender, formatCommand(label + " " + subLabel, subCommand.getDescription())));
                }
                MessageUtils.sendMessage(sender, formatCommand("balancetop", "Show the balance top"));
                MessageUtils.sendMessage(sender, formatCommand("balance", "Get the balance of a player"));
            }

            private String formatCommand(String command, String description) {
                return "&e/{command}: &f{description}".replace("{command}", command).replace("{description}", description);
            }

            @Override
            public void sendArgNotFoundMessage(@NotNull CommandSender sender, @NotNull String label, @NotNull String... args) {
                MessageUtils.sendMessage(sender, instance.getMessageConfig().getArgNotFound());
            }
        };
        this.subCommandManager.registerSubcommand(new GiveSubCommand(instance));
        this.subCommandManager.registerSubcommand(new TakeSubCommand(instance));
        this.subCommandManager.registerSubcommand(new SetSubCommand(instance));
        this.subCommandManager.registerSubcommand(new ReloacSubCommand(instance));
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, String[] args) {
        return subCommandManager.onCommand(sender, commandLabel, args);
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) throws IllegalArgumentException {
        return subCommandManager.onTabComplete(sender, alias, args);
    }
}
