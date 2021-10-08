package me.hsgamer.bettereconomy.handler;

import me.hsgamer.bettereconomy.BetterEconomy;
import me.hsgamer.hscore.database.Setting;
import me.hsgamer.hscore.database.driver.SqliteDriver;

public class SqliteEconomyHandler extends SqlEconomyHandler {
    public SqliteEconomyHandler(BetterEconomy instance) {
        super(
                instance,
                new Setting().setDatabaseName(instance.getMainConfig().getSqliteDatabaseName()),
                new SqliteDriver(instance.getDataFolder())
        );
    }
}
