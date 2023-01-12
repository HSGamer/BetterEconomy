package me.hsgamer.bettereconomy;

import com.google.common.reflect.TypeToken;
import lombok.Getter;
import me.hsgamer.bettereconomy.api.EconomyHandler;
import me.hsgamer.bettereconomy.command.BalanceCommand;
import me.hsgamer.bettereconomy.command.BalanceTopCommand;
import me.hsgamer.bettereconomy.command.MainCommand;
import me.hsgamer.bettereconomy.command.PayCommand;
import me.hsgamer.bettereconomy.config.MainConfig;
import me.hsgamer.bettereconomy.config.MessageConfig;
import me.hsgamer.bettereconomy.config.converter.StringObjectMapConverter;
import me.hsgamer.bettereconomy.handler.FlatFileEconomyHandler;
import me.hsgamer.bettereconomy.handler.JsonEconomyHandler;
import me.hsgamer.bettereconomy.handler.MySqlEconomyHandler;
import me.hsgamer.bettereconomy.handler.SqliteEconomyHandler;
import me.hsgamer.bettereconomy.hook.treasury.TreasuryEconomyHook;
import me.hsgamer.bettereconomy.hook.vault.VaultEconomyHook;
import me.hsgamer.bettereconomy.listener.JoinListener;
import me.hsgamer.bettereconomy.top.TopRunnable;
import me.hsgamer.hscore.builder.Builder;
import me.hsgamer.hscore.bukkit.baseplugin.BasePlugin;
import me.hsgamer.hscore.bukkit.config.BukkitConfig;
import me.hsgamer.hscore.bukkit.utils.MessageUtils;
import me.hsgamer.hscore.config.annotation.converter.manager.DefaultConverterManager;
import me.hsgamer.hscore.config.proxy.ConfigGenerator;
import me.lokka30.treasury.api.common.service.ServiceRegistry;
import me.lokka30.treasury.api.economy.EconomyProvider;
import net.milkbowl.vault.economy.Economy;
import org.bstats.bukkit.Metrics;
import org.bukkit.plugin.ServicePriority;

import java.util.Map;

@Getter
public final class BetterEconomy extends BasePlugin {
    public static final Builder<BetterEconomy, EconomyHandler> ECONOMY_HANDLER_BUILDER = new Builder<>();

    static {
        DefaultConverterManager.registerConverter(new TypeToken<Map<String, Object>>() {
        }.getType(), new StringObjectMapConverter());
    }

    private final MainConfig mainConfig = ConfigGenerator.newInstance(MainConfig.class, new BukkitConfig(this, "config.yml"));
    private final MessageConfig messageConfig = ConfigGenerator.newInstance(MessageConfig.class, new BukkitConfig(this, "messages.yml"));
    private final TopRunnable topRunnable = new TopRunnable(this);
    private EconomyHandler economyHandler;

    @Override
    public void load() {
        MessageUtils.setPrefix(messageConfig::getPrefix);

        if (mainConfig.isHookEnabled()) {
            if (getServer().getPluginManager().getPlugin("Vault") != null) {
                registerProvider(Economy.class, new VaultEconomyHook(this), ServicePriority.High);
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

        ECONOMY_HANDLER_BUILDER.register(FlatFileEconomyHandler::new, "flat-file", "flatfile", "file");
        ECONOMY_HANDLER_BUILDER.register(MySqlEconomyHandler::new, "mysql");
        ECONOMY_HANDLER_BUILDER.register(SqliteEconomyHandler::new, "sqlite");
        ECONOMY_HANDLER_BUILDER.register(JsonEconomyHandler::new, "json");
    }

    @Override
    public void enable() {
        economyHandler = ECONOMY_HANDLER_BUILDER.build(mainConfig.getHandlerType(), this).orElseGet(() -> {
            getLogger().warning("Cannot find an economy handler from the config. FlatFile will be used");
            return new FlatFileEconomyHandler(this);
        });
        registerCommand(new BalanceCommand(this));
        registerCommand(new BalanceTopCommand(this));
        registerCommand(new PayCommand(this));
        registerCommand(new MainCommand(this));
        registerListener(new JoinListener(this));
        if (mainConfig.isMetrics()) {
            new Metrics(this, 12981);
        }
    }

    @Override
    public void postEnable() {
        topRunnable.runTaskTimerAsynchronously(this, 0, mainConfig.getUpdateBalanceTopPeriod());
    }

    @Override
    public void disable() {
        ECONOMY_HANDLER_BUILDER.clear();
    }

    @Override
    public void postDisable() {
        economyHandler.disable();
    }
}
