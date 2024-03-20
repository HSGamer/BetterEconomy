package me.hsgamer.bettereconomy.command;

import me.hsgamer.bettereconomy.BetterEconomy;
import me.hsgamer.bettereconomy.Permissions;
import me.hsgamer.bettereconomy.Utils;
import me.hsgamer.bettereconomy.config.MainConfig;
import me.hsgamer.bettereconomy.config.MessageConfig;
import me.hsgamer.bettereconomy.top.PlayerBalanceSnapshot;
import me.hsgamer.bettereconomy.top.TopRunnable;
import me.hsgamer.hscore.bukkit.utils.MessageUtils;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class BalanceTopCommand extends Command {
    private final BetterEconomy instance;

    public BalanceTopCommand(BetterEconomy instance) {
        super("balancetop", "Show the balance top", "/balancetop [page]", Collections.singletonList("baltop"));
        this.instance = instance;
        setPermission(Permissions.BALANCE_TOP.getName());
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, String[] args) {
        if (!testPermission(sender)) {
            return false;
        }
        List<PlayerBalanceSnapshot> list = instance.get(TopRunnable.class).getTopList();
        int page = 0;
        if (args.length > 0) {
            try {
                page = Integer.parseInt(args[0]);
            } catch (Exception e) {
                // IGNORED
            }
        }
        if (list.isEmpty()) {
            MessageUtils.sendMessage(sender, instance.get(MessageConfig.class).getEmptyBalanceTop());
            return true;
        }
        int startIndex = (10 * page) % list.size();
        int endIndex = Math.min(list.size(), startIndex + 10);
        for (int index = startIndex; index < endIndex; index++) {
            PlayerBalanceSnapshot snapshot = list.get(index);
            OfflinePlayer offlinePlayer = Utils.getOfflinePlayer(snapshot.getUuid());
            MessageUtils.sendMessage(
                    sender,
                    instance.get(MessageConfig.class).getBalanceTopOutput()
                            .replace("{place}", Integer.toString(index + 1))
                            .replace("{name}", Optional.ofNullable(offlinePlayer.getName()).orElse(Utils.getUniqueId(offlinePlayer).toString()))
                            .replace("{balance}", instance.get(MainConfig.class).format(snapshot.getBalance()))
            );
        }
        return true;
    }
}
