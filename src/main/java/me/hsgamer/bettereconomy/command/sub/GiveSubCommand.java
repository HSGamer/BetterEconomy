package me.hsgamer.bettereconomy.command.sub;

import me.hsgamer.bettereconomy.BetterEconomy;
import me.hsgamer.hscore.bukkit.utils.MessageUtils;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

import java.util.Optional;

public class GiveSubCommand extends ChangeMoneySubCommand {
    public GiveSubCommand(BetterEconomy instance) {
        super(instance, "give", "Give money to the player", "/eco give <player> <amount>");
    }

    @Override
    protected boolean tryChange(CommandSender sender, OfflinePlayer offlinePlayer, double amount) {
        return instance.getEconomyHandler().deposit(offlinePlayer.getUniqueId(), amount);
    }

    @Override
    protected void sendSuccessMessage(CommandSender sender, OfflinePlayer offlinePlayer, double amount) {
        MessageUtils.sendMessage(sender,
                instance.getMessageConfig().getGiveSuccess()
                        .replace("{balance}", Double.toString(amount))
                        .replace("{name}", Optional.ofNullable(offlinePlayer.getName()).orElse(offlinePlayer.getUniqueId().toString()))
        );
    }

    @Override
    protected void sendFailMessage(CommandSender sender, OfflinePlayer offlinePlayer, double amount) {
        MessageUtils.sendMessage(sender,
                instance.getMessageConfig().getGiveFail()
                        .replace("{balance}", Double.toString(amount))
                        .replace("{name}", Optional.ofNullable(offlinePlayer.getName()).orElse(offlinePlayer.getUniqueId().toString()))
        );
    }
}
