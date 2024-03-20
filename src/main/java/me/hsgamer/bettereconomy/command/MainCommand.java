package me.hsgamer.bettereconomy.command;

import io.github.projectunified.minelib.util.subcommand.SubCommandManager;
import me.hsgamer.bettereconomy.BetterEconomy;
import me.hsgamer.bettereconomy.Permissions;
import me.hsgamer.bettereconomy.command.sub.GiveSubCommand;
import me.hsgamer.bettereconomy.command.sub.ReloadSubCommand;
import me.hsgamer.bettereconomy.command.sub.SetSubCommand;
import me.hsgamer.bettereconomy.command.sub.TakeSubCommand;
import org.bukkit.ChatColor;
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
                    super.sendHelpMessage(sender, label, args);
                }
                sendCommand(sender, "/balancetop", "Show the balance top");
                sendCommand(sender, "/balance", "Get the balance of a player");
            }

            private void sendCommand(CommandSender sender, String usage, String description) {
                sender.sendMessage(ChatColor.YELLOW + usage);
                sender.sendMessage(ChatColor.WHITE + "  " + description);
            }
        };
        this.subCommandManager.registerSubcommand(new GiveSubCommand(instance));
        this.subCommandManager.registerSubcommand(new TakeSubCommand(instance));
        this.subCommandManager.registerSubcommand(new SetSubCommand(instance));
        this.subCommandManager.registerSubcommand(new ReloadSubCommand(instance));
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
