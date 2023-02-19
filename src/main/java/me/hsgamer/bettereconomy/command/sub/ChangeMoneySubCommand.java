package me.hsgamer.bettereconomy.command.sub;

import me.hsgamer.bettereconomy.BetterEconomy;
import me.hsgamer.bettereconomy.Permissions;
import me.hsgamer.bettereconomy.Utils;
import me.hsgamer.hscore.bukkit.command.sub.SubCommand;
import me.hsgamer.hscore.bukkit.utils.MessageUtils;
import me.hsgamer.hscore.common.Validate;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.HumanEntity;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public abstract class ChangeMoneySubCommand extends SubCommand {
    protected final BetterEconomy instance;

    protected ChangeMoneySubCommand(BetterEconomy instance, @NotNull String name, @NotNull String description, @NotNull String usage) {
        super(name, description, usage, Permissions.ADMIN.getName(), true);
        this.instance = instance;
    }

    protected abstract boolean tryChange(CommandSender sender, OfflinePlayer offlinePlayer, double amount);

    protected abstract void sendSuccessMessage(CommandSender sender, OfflinePlayer offlinePlayer, double amount);

    protected abstract void sendFailMessage(CommandSender sender, OfflinePlayer offlinePlayer, double amount);

    @Override
    public void onSubCommand(@NotNull CommandSender sender, @NotNull String label, @NotNull String... args) {
        OfflinePlayer offlinePlayer = Utils.getOfflinePlayer(args[0]);
        if (!instance.getEconomyHandler().hasAccount(Utils.getUniqueId(offlinePlayer))) {
            MessageUtils.sendMessage(sender, instance.getMessageConfig().getPlayerNotFound());
            return;
        }
        Optional<Double> amountOptional = Validate.getNumber(args[1]).map(BigDecimal::doubleValue).filter(value -> value >= 0);
        if (!amountOptional.isPresent()) {
            MessageUtils.sendMessage(sender, instance.getMessageConfig().getInvalidAmount());
            return;
        }
        double amount = amountOptional.get();
        if (tryChange(sender, offlinePlayer, amount)) {
            sendSuccessMessage(sender, offlinePlayer, amount);
        } else {
            sendFailMessage(sender, offlinePlayer, amount);
        }
    }

    @Override
    public boolean isProperUsage(@NotNull CommandSender sender, @NotNull String label, @NotNull String... args) {
        return args.length >= 2;
    }

    @Override
    public @NotNull List<String> onTabComplete(@NotNull CommandSender sender, @NotNull String label, @NotNull String... args) {
        if (args.length == 1) {
            String name = args[0].trim();
            return Bukkit.getOnlinePlayers().stream()
                    .map(HumanEntity::getName)
                    .filter(playerName -> name.isEmpty() || playerName.startsWith(name))
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }
}
