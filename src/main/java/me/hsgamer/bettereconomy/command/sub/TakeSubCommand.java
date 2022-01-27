package me.hsgamer.bettereconomy.command.sub;

import me.hsgamer.bettereconomy.BetterEconomy;
import me.hsgamer.hscore.bukkit.utils.MessageUtils;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

import java.util.Optional;

public class TakeSubCommand extends ChangeMoneySubCommand {
    public TakeSubCommand(BetterEconomy instance) {
        super(instance, "take", "Take money from the player", "/eco take <player> <amount>");
    }

    @Override
    protected void executeChange(CommandSender sender, OfflinePlayer offlinePlayer, double amount) {
        if (instance.getEconomyHandler().withdraw(offlinePlayer.getUniqueId(), amount)) {
            MessageUtils.sendMessage(sender,
                    instance.getMessageConfig().getGiveSuccess()
                            .replace("{balance}", Double.toString(amount))
                            .replace("{name}", Optional.ofNullable(offlinePlayer.getName()).orElse(offlinePlayer.getUniqueId().toString()))
            );
        } else {
            MessageUtils.sendMessage(sender, instance.getMessageConfig().getCannotDo());
        }
    }
}
