package me.hsgamer.bettereconomy.command;

import me.hsgamer.bettereconomy.BetterEconomy;
import me.hsgamer.bettereconomy.Permissions;
import me.hsgamer.bettereconomy.Utils;
import me.hsgamer.hscore.bukkit.utils.MessageUtils;
import me.hsgamer.hscore.common.Validate;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Optional;

public class PayCommand extends Command {
    private final BetterEconomy instance;

    public PayCommand(BetterEconomy instance) {
        super("pay", "Transfer money to the player", "/pay <player> <amount>", Collections.emptyList());
        this.instance = instance;
        setPermission(Permissions.PAY.getName());
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
        if (!testPermission(sender)) {
            return false;
        }
        if (args.length < 2) {
            MessageUtils.sendMessage(sender, getUsage());
            return false;
        }
        if (!(sender instanceof Player)) {
            MessageUtils.sendMessage(sender, instance.getMessageConfig().getPlayerOnly());
            return false;
        }

        Player player = (Player) sender;
        OfflinePlayer receiver = Utils.getOfflinePlayer(args[0]);
        if (receiver == player) {
            MessageUtils.sendMessage(sender, instance.getMessageConfig().getCannotDo());
            return false;
        }
        if (!instance.getEconomyHandler().hasAccount(receiver)) {
            MessageUtils.sendMessage(sender, instance.getMessageConfig().getPlayerNotFound());
            return false;
        }

        Optional<Double> optionalAmount = Validate.getNumber(args[1])
                .map(BigDecimal::doubleValue)
                .filter(value -> value > 0)
                .filter(value -> instance.getEconomyHandler().has(player, value));
        if (!optionalAmount.isPresent()) {
            MessageUtils.sendMessage(sender, instance.getMessageConfig().getInvalidAmount());
            return false;
        }
        double amount = optionalAmount.get();

        instance.getEconomyHandler().withdraw(player, amount);
        instance.getEconomyHandler().deposit(receiver, amount);
        MessageUtils.sendMessage(sender,
                instance.getMessageConfig().getGiveSuccess()
                        .replace("{balance}", Double.toString(amount))
                        .replace("{name}", Optional.ofNullable(receiver.getName()).orElse(receiver.getUniqueId().toString()))
        );
        return true;
    }
}
