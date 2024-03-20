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
import me.hsgamer.bettereconomy.hook.placeholderapi.EconomyPlaceholder;
import me.hsgamer.bettereconomy.hook.treasury.TreasuryEconomyHook;
import me.hsgamer.bettereconomy.hook.vault.VaultEconomyHook;
import me.hsgamer.bettereconomy.listener.JoinListener;
import me.hsgamer.bettereconomy.provider.EconomyHandlerProvider;
import me.hsgamer.bettereconomy.top.TopRunnable;
import me.hsgamer.hscore.bukkit.config.BukkitConfig;
import me.hsgamer.hscore.bukkit.utils.MessageUtils;
import me.hsgamer.hscore.config.annotation.converter.manager.DefaultConverterManager;
import me.hsgamer.hscore.config.proxy.ConfigGenerator;
import me.lokka30.treasury.api.common.service.ServiceRegistry;
import me.lokka30.treasury.api.economy.EconomyProvider;
import net.milkbowl.vault.economy.Economy;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.plugin.ServicePriority;

import java.util.ArrayList;
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
        List<Object> list = new ArrayList<>(Arrays.asList(
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
                new JoinListener(this)
        ));

        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            list.add(new EconomyPlaceholder(this));
        }

        return list;
    }

    @Override
    public void load() {
        MessageUtils.setPrefix(get(MessageConfig.class)::getPrefix);

        if (get(MainConfig.class).isHookEnabled()) {
            if (getServer().getPluginManager().getPlugin("Vault") != null) {
                Bukkit.getServicesManager().register(
                        Economy.class,
                        new VaultEconomyHook(this),
                        this,
                        ServicePriority.High
                );
            }
            if (getServer().getPluginManager().getPlugin("Treasury") != null) {
                ServiceRegistry.INSTANCE.registerService(
                        EconomyProvider.class,
                        new TreasuryEconomyHook(this),
                        getName(),
                        me.lokka30.treasury.api.common.service.ServicePriority.NORMAL
                );
            }
        }
    }

    @Override
    public void enable() {
        new Metrics(this, 12981);
    }
}
