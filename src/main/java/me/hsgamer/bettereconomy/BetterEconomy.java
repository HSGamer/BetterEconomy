package me.hsgamer.bettereconomy;

import lombok.Getter;
import me.hsgamer.bettereconomy.api.EconomyHandler;
import me.hsgamer.bettereconomy.command.BalanceCommand;
import me.hsgamer.bettereconomy.command.BalanceTopCommand;
import me.hsgamer.bettereconomy.config.MainConfig;
import me.hsgamer.bettereconomy.config.MessageConfig;
import me.hsgamer.bettereconomy.handler.FlatFileEconomyHandler;
import me.hsgamer.bettereconomy.hook.VaultEconomyHook;
import me.hsgamer.bettereconomy.listener.JoinListener;
import me.hsgamer.bettereconomy.top.TopRunnable;
import me.hsgamer.hscore.builder.Builder;
import me.hsgamer.hscore.bukkit.baseplugin.BasePlugin;
import me.hsgamer.hscore.bukkit.utils.MessageUtils;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.ServicePriority;

public final class BetterEconomy extends BasePlugin {
    public static final Builder<BetterEconomy, EconomyHandler> ECONOMY_HANDLER_BUILDER = new Builder<>();

    @Getter
    private final MainConfig mainConfig = new MainConfig(this);
    @Getter
    private final MessageConfig messageConfig = new MessageConfig(this);
    @Getter
    private final TopRunnable topRunnable = new TopRunnable(this);
    @Getter
    private EconomyHandler economyHandler;

    @Override
    public void preLoad() {
        MessageUtils.setPrefix(messageConfig::getPrefix);
    }

    @Override
    public void load() {
        getServer().getServicesManager().register(Economy.class, new VaultEconomyHook(this), this, ServicePriority.Highest);

        mainConfig.setup();
        messageConfig.setup();

        ECONOMY_HANDLER_BUILDER.register(FlatFileEconomyHandler::new, "flat-file", "flatfile", "file");
    }

    @Override
    public void enable() {
        economyHandler = ECONOMY_HANDLER_BUILDER.build(mainConfig.getHandlerType(), this).orElseGet(() -> {
            getLogger().warning("Cannot find an economy handler from the config. FlatFile will be used");
            return new FlatFileEconomyHandler(this);
        });
        registerCommand(new BalanceCommand(this));
        registerCommand(new BalanceTopCommand(this));
        registerListener(new JoinListener(this));
    }

    @Override
    public void postEnable() {
        topRunnable.runTaskTimerAsynchronously(this, 0, mainConfig.getUpdateBalanceTopPeriod());
    }

    @Override
    public void disable() {
        economyHandler.disable();
        ECONOMY_HANDLER_BUILDER.unregisterAll();
    }
}
