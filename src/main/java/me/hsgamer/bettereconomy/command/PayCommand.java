package me.hsgamer.bettereconomy.command;

import me.hsgamer.bettereconomy.BetterEconomy;
import me.hsgamer.bettereconomy.Permissions;
import me.hsgamer.bettereconomy.Utils;
import me.hsgamer.bettereconomy.config.MainConfig;
import me.hsgamer.bettereconomy.config.MessageConfig;
import me.hsgamer.bettereconomy.holder.EconomyHolder;
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
import java.util.UUID;

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
            MessageUtils.sendMessage(sender, instance.get(MessageConfig.class).getPlayerOnly());
            return false;
        }

        EconomyHolder holder = instance.get(EconomyHolder.class);

        Player player = (Player) sender;
        OfflinePlayer receiver = Utils.getOfflinePlayer(args[0]);
        if (receiver == player) {
            MessageUtils.sendMessage(sender, instance.get(MessageConfig.class).getCannotDo());
            return false;
        }
        UUID playerUUID = Utils.getUniqueId(player);
        UUID receiverUUID = Utils.getUniqueId(receiver);
        if (!holder.hasAccount(receiverUUID)) {
            MessageUtils.sendMessage(sender, instance.get(MessageConfig.class).getPlayerNotFound());
            return false;
        }

        Optional<Double> optionalAmount = Validate.getNumber(args[1])
                .map(BigDecimal::doubleValue)
                .filter(value -> value > 0)
                .filter(value -> holder.has(playerUUID, value));
        if (!optionalAmount.isPresent()) {
            MessageUtils.sendMessage(sender, instance.get(MessageConfig.class).getInvalidAmount());
            return false;
        }
        double amount = optionalAmount.get();

        holder.withdraw(playerUUID, amount);
        holder.deposit(receiverUUID, amount);
        MessageUtils.sendMessage(sender,
                instance.get(MessageConfig.class).getGiveSuccess()
                        .replace("{balance}", instance.get(MainConfig.class).format(amount))
                        .replace("{name}", Optional.ofNullable(receiver.getName()).orElse(receiverUUID.toString()))
        );
        MessageUtils.sendMessage(receiverUUID,
                instance.get(MessageConfig.class).getReceive()
                        .replace("{balance}", instance.get(MainConfig.class).format(amount))
                        .replace("{name}", player.getName())
        );
        return true;
    }
}
