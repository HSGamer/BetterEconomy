package me.hsgamer.bettereconomy;

import io.github.projectunified.minelib.plugin.base.BasePlugin;
import io.github.projectunified.minelib.plugin.command.CommandComponent;
import me.hsgamer.bettereconomy.command.BalanceCommand;
import me.hsgamer.bettereconomy.command.BalanceTopCommand;
import me.hsgamer.bettereconomy.command.MainCommand;
import me.hsgamer.bettereconomy.command.PayCommand;
import me.hsgamer.bettereconomy.config.MainConfig;
import me.hsgamer.bettereconomy.config.MessageConfig;
import me.hsgamer.bettereconomy.holder.EconomyHolder;
import me.hsgamer.bettereconomy.hook.HookProvider;
import me.hsgamer.bettereconomy.listener.JoinListener;
import me.hsgamer.hscore.bukkit.config.BukkitConfig;
import me.hsgamer.hscore.bukkit.utils.MessageUtils;
import me.hsgamer.hscore.config.proxy.ConfigGenerator;
import org.bstats.bukkit.Metrics;

import java.util.Arrays;
import java.util.List;

public final class BetterEconomy extends BasePlugin {
    @Override
    protected List<Object> getComponents() {
        MainConfig mainConfig = ConfigGenerator.newInstance(MainConfig.class, new BukkitConfig(this));
        return Arrays.asList(
                mainConfig,
                ConfigGenerator.newInstance(MessageConfig.class, new BukkitConfig(this, "messages.yml")),
                new EconomyHolder(this, mainConfig),
                new Permissions(this),
                new CommandComponent(this,
                        new BalanceCommand(this),
                        new BalanceTopCommand(this),
                        new MainCommand(this),
                        new PayCommand(this)
                ),
                new JoinListener(this),
                new HookProvider(this)
        );
    }

    @Override
    public void load() {
        MessageUtils.setPrefix(get(MessageConfig.class)::getPrefix);
    }

    @Override
    public void enable() {
        new Metrics(this, 12981);
    }
}
