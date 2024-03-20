package me.hsgamer.bettereconomy.provider;

import io.github.projectunified.minelib.plugin.base.Loadable;
import lombok.Getter;
import me.hsgamer.bettereconomy.BetterEconomy;
import me.hsgamer.bettereconomy.api.EconomyHandler;
import me.hsgamer.bettereconomy.config.MainConfig;
import me.hsgamer.bettereconomy.handler.FlatFileEconomyHandler;
import me.hsgamer.bettereconomy.handler.JsonEconomyHandler;
import me.hsgamer.bettereconomy.handler.MySqlEconomyHandler;
import me.hsgamer.bettereconomy.handler.SqliteEconomyHandler;
import me.hsgamer.hscore.builder.Builder;

@Getter
public class EconomyHandlerProvider implements Loadable {
    private final BetterEconomy instance;
    private final Builder<BetterEconomy, EconomyHandler> builder = new Builder<>();
    private EconomyHandler economyHandler;

    public EconomyHandlerProvider(BetterEconomy instance) {
        this.instance = instance;
    }

    @Override
    public void load() {
        builder.register(FlatFileEconomyHandler::new, "flat-file", "flatfile", "file");
        builder.register(MySqlEconomyHandler::new, "mysql");
        builder.register(SqliteEconomyHandler::new, "sqlite");
        builder.register(JsonEconomyHandler::new, "json");
    }

    @Override
    public void enable() {
        economyHandler = builder.build(instance.get(MainConfig.class).getHandlerType(), instance).orElseGet(() -> {
            instance.getLogger().warning("Cannot find an economy handler from the config. FlatFile will be used");
            return new FlatFileEconomyHandler(instance);
        });
    }

    @Override
    public void disable() {
        if (economyHandler != null) {
            economyHandler.disable();
        }
    }
}
