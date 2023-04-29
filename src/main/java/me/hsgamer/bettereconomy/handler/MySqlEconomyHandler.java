package me.hsgamer.bettereconomy.handler;

import me.hsgamer.bettereconomy.BetterEconomy;
import me.hsgamer.hscore.database.Setting;
import me.hsgamer.hscore.database.driver.mysql.MySqlDriver;

public class MySqlEconomyHandler extends SqlEconomyHandler {
    public MySqlEconomyHandler(BetterEconomy instance) {
        super(
                instance,
                Setting.create(new MySqlDriver())
                        .setHost(instance.getMainConfig().getMysqlHost())
                        .setPort(instance.getMainConfig().getMysqlPort())
                        .setDatabaseName(instance.getMainConfig().getMysqlDatabaseName())
                        .setUsername(instance.getMainConfig().getMysqlUsername())
                        .setPassword(instance.getMainConfig().getMysqlPassword())
        );
    }
}
