package me.hsgamer.bettereconomy.command.sub;

import me.hsgamer.bettereconomy.BetterEconomy;
import me.hsgamer.bettereconomy.Utils;
import me.hsgamer.hscore.bukkit.utils.MessageUtils;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

import java.util.Optional;

public class SetSubCommand extends ChangeMoneySubCommand {
    public SetSubCommand(BetterEconomy instance) {
        super(instance, "set", "Set money for the player", "/<label> set <player> <amount>");
    }

    @Override
    protected boolean tryChange(CommandSender sender, OfflinePlayer offlinePlayer, double amount) {
        return instance.getEconomyHandler().set(Utils.getUniqueId(offlinePlayer), amount);
    }

    @Override
    protected void sendSuccessMessage(CommandSender sender, OfflinePlayer offlinePlayer, double amount) {
        MessageUtils.sendMessage(sender,
                instance.getMessageConfig().getSetSuccess()
                        .replace("{balance}", instance.getMainConfig().format(amount))
                        .replace("{name}", Optional.ofNullable(offlinePlayer.getName()).orElse(Utils.getUniqueId(offlinePlayer).toString()))
        );
    }

    @Override
    protected void sendFailMessage(CommandSender sender, OfflinePlayer offlinePlayer, double amount) {
        MessageUtils.sendMessage(sender,
                instance.getMessageConfig().getSetFail()
                        .replace("{balance}", instance.getMainConfig().format(amount))
                        .replace("{name}", Optional.ofNullable(offlinePlayer.getName()).orElse(Utils.getUniqueId(offlinePlayer).toString()))
        );
    }
}
