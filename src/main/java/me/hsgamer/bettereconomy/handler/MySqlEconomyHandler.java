package me.hsgamer.bettereconomy.handler;

import me.hsgamer.bettereconomy.BetterEconomy;
import me.hsgamer.bettereconomy.config.MainConfig;
import me.hsgamer.hscore.database.Setting;
import me.hsgamer.hscore.database.driver.mysql.MySqlDriver;

public class MySqlEconomyHandler extends SqlEconomyHandler {
    public MySqlEconomyHandler(BetterEconomy instance) {
        super(
                instance,
                Setting.create(new MySqlDriver())
                        .setHost(instance.get(MainConfig.class).getMysqlHost())
                        .setPort(instance.get(MainConfig.class).getMysqlPort())
                        .setDatabaseName(instance.get(MainConfig.class).getMysqlDatabaseName())
                        .setUsername(instance.get(MainConfig.class).getMysqlUsername())
                        .setPassword(instance.get(MainConfig.class).getMysqlPassword())
        );
    }
}
