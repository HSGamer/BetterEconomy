package me.hsgamer.bettereconomy.command.sub;

import me.hsgamer.bettereconomy.BetterEconomy;
import me.hsgamer.hscore.bukkit.utils.MessageUtils;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

import java.util.Optional;

public class SetSubCommand extends ChangeMoneySubCommand {
    public SetSubCommand(BetterEconomy instance) {
        super(instance, "set", "Set money for the player", "/eco set <player> <amount>");
    }

    @Override
    protected void executeChange(CommandSender sender, OfflinePlayer offlinePlayer, double amount) {
        if (instance.getEconomyHandler().set(offlinePlayer, amount)) {
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
