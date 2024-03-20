package me.hsgamer.bettereconomy;

import com.google.common.reflect.TypeToken;
import io.github.projectunified.minelib.plugin.base.BasePlugin;
import io.github.projectunified.minelib.plugin.command.CommandComponent;
import me.hsgamer.bettereconomy.command.BalanceCommand;
import me.hsgamer.bettereconomy.command.BalanceTopCommand;
import me.hsgamer.bettereconomy.command.MainCommand;
import me.hsgamer.bettereconomy.command.PayCommand;
import me.hsgamer.bettereconomy.config.MainConfig;
import me.hsgamer.bettereconomy.config.MessageConfig;
import me.hsgamer.bettereconomy.config.converter.StringObjectMapConverter;
import me.hsgamer.bettereconomy.hook.HookProvider;
import me.hsgamer.bettereconomy.listener.JoinListener;
import me.hsgamer.bettereconomy.provider.EconomyHandlerProvider;
import me.hsgamer.bettereconomy.top.TopRunnable;
import me.hsgamer.hscore.bukkit.config.BukkitConfig;
import me.hsgamer.hscore.bukkit.utils.MessageUtils;
import me.hsgamer.hscore.config.annotation.converter.manager.DefaultConverterManager;
import me.hsgamer.hscore.config.proxy.ConfigGenerator;
import org.bstats.bukkit.Metrics;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public final class BetterEconomy extends BasePlugin {
    static {
        //noinspection UnstableApiUsage
        DefaultConverterManager.registerConverter(new TypeToken<Map<String, Object>>() {
        }.getType(), new StringObjectMapConverter());
    }

    @Override
    protected List<Object> getComponents() {
        return Arrays.asList(
                ConfigGenerator.newInstance(MainConfig.class, new BukkitConfig(this)),
                ConfigGenerator.newInstance(MessageConfig.class, new BukkitConfig(this, "messages.yml")),
                new EconomyHandlerProvider(this),
                new TopRunnable(this),
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
