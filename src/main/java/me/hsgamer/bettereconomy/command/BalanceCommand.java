package me.hsgamer.bettereconomy.command;

import me.hsgamer.bettereconomy.BetterEconomy;
import me.hsgamer.bettereconomy.Permissions;
import me.hsgamer.bettereconomy.Utils;
import me.hsgamer.bettereconomy.config.MainConfig;
import me.hsgamer.bettereconomy.config.MessageConfig;
import me.hsgamer.bettereconomy.provider.EconomyHandlerProvider;
import me.hsgamer.hscore.bukkit.utils.MessageUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.UUID;

public class BalanceCommand extends Command {
    private final BetterEconomy instance;

    public BalanceCommand(BetterEconomy instance) {
        super("balance", "Get the balance of a player", "/balance [player]", Collections.singletonList("bal"));
        this.instance = instance;
        setPermission(Permissions.BALANCE.getName());
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, String[] args) {
        if (!testPermission(sender)) {
            return false;
        }
        UUID uuid;
        if (args.length > 0 && sender.hasPermission(Permissions.BALANCE_OTHERS)) {
            uuid = Utils.getUniqueId(args[0]);
        } else if (sender instanceof Player) {
            uuid = ((Player) sender).getUniqueId();
        } else {
            MessageUtils.sendMessage(sender, instance.get(MessageConfig.class).getPlayerOnly());
            return false;
        }
        if (!instance.get(EconomyHandlerProvider.class).getEconomyHandler().hasAccount(uuid)) {
            MessageUtils.sendMessage(sender, instance.get(MessageConfig.class).getPlayerNotFound());
            return false;
        }
        MessageUtils.sendMessage(sender,
                instance.get(MessageConfig.class).getBalanceOutput()
                        .replace("{balance}", instance.get(MainConfig.class).format(instance.get(EconomyHandlerProvider.class).getEconomyHandler().get(uuid)))
                        .replace("{name}", Optional.ofNullable(args[0]).orElse(uuid.toString()))
        );
        return true;
    }
}
