package me.hsgamer.bettereconomy.handler;

import me.hsgamer.bettereconomy.BetterEconomy;
import me.hsgamer.hscore.database.Setting;
import me.hsgamer.hscore.database.driver.MySqlDriver;

public class MySqlEconomyHandler extends SqlEconomyHandler {
    public MySqlEconomyHandler(BetterEconomy instance) {
        super(
                instance,
                new Setting()
                        .setHost(instance.getMainConfig().getMysqlHost())
                        .setPort(instance.getMainConfig().getMysqlPort())
                        .setDatabaseName(instance.getMainConfig().getMysqlDatabaseName())
                        .setUsername(instance.getMainConfig().getMysqlUsername())
                        .setPassword(instance.getMainConfig().getMysqlPassword()),
                new MySqlDriver()
        );
    }
}
