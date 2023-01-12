package me.hsgamer.bettereconomy.command;

import me.hsgamer.bettereconomy.BetterEconomy;
import me.hsgamer.bettereconomy.Permissions;
import me.hsgamer.bettereconomy.Utils;
import me.hsgamer.hscore.bukkit.utils.MessageUtils;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.UUID;

public class BalanceCommand extends Command {
    private final BetterEconomy instance;

    public BalanceCommand(BetterEconomy instance) {
        super("balance", "Get the balance of a player", "/balance [player]", Collections.singletonList("bal", "coins", "saldo", "money"));
        this.instance = instance;
        setPermission(Permissions.BALANCE.getName());
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, String[] args) {
        if (!testPermission(sender)) {
            return false;
        }
        OfflinePlayer who;
        if (args.length > 0 && sender.hasPermission(Permissions.BALANCE_OTHERS)) {
            who = Utils.getOfflinePlayer(args[0]);
        } else if (sender instanceof Player) {
            who = (OfflinePlayer) sender;
        } else {
            MessageUtils.sendMessage(sender, instance.getMessageConfig().getPlayerOnly());
            return false;
        }
        UUID uuid = who.getUniqueId();
        if (!instance.getEconomyHandler().hasAccount(uuid)) {
            MessageUtils.sendMessage(sender, instance.getMessageConfig().getPlayerNotFound());
            return false;
        }
        MessageUtils.sendMessage(sender, instance.getMessageConfig().getBalanceOutput().replace("{balance}", instance.getMainConfig().format(instance.getEconomyHandler().get(uuid))));
        return true;
    }
}
