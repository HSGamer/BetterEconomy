package me.hsgamer.bettereconomy.handler;

import me.hsgamer.bettereconomy.BetterEconomy;
import me.hsgamer.bettereconomy.config.MainConfig;
import me.hsgamer.hscore.database.Setting;
import me.hsgamer.hscore.database.driver.sqlite.SqliteFileDriver;

public class SqliteEconomyHandler extends SqlEconomyHandler {
    public SqliteEconomyHandler(BetterEconomy instance) {
        super(
                instance,
                Setting.create(new SqliteFileDriver(instance.getDataFolder())).setDatabaseName(instance.get(MainConfig.class).getSqliteDatabaseName())
        );
    }
}
