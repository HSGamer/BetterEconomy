package me.hsgamer.bettereconomy.handler;

import me.hsgamer.bettereconomy.BetterEconomy;
import me.hsgamer.bettereconomy.api.EconomyHandler;
import me.hsgamer.hscore.database.Driver;
import me.hsgamer.hscore.database.Setting;
import me.hsgamer.hscore.database.client.sql.StatementBuilder;
import me.hsgamer.hscore.database.client.sql.java.JavaSqlClient;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import java.util.logging.Level;

public abstract class SqlEconomyHandler extends EconomyHandler {

    private final Connection connection;

    SqlEconomyHandler(BetterEconomy instance, Setting setting, Driver driver) {
        super(instance);
        try {
            JavaSqlClient client = new JavaSqlClient(setting, driver);
            connection = client.getConnection();
        } catch (SQLException e) {
            throw new IllegalStateException("constructor()#connection", e);
        }
        StatementBuilder.create(connection)
                .setStatement("CREATE TABLE IF NOT EXISTS `economy` (`uuid` varchar(36) NOT NULL UNIQUE, `balance` double DEFAULT 0);")
                .updateSafe();
    }

    @Override
    public boolean hasAccount(UUID uuid) {
        return StatementBuilder.create(connection)
                .setStatement("SELECT * FROM `economy` WHERE `uuid` = ?")
                .addValues(uuid.toString())
                .querySafe(ResultSet::next)
                .orElse(false);
    }

    @Override
    public double get(UUID uuid) {
        return StatementBuilder.create(connection)
                .setStatement("SELECT `balance` FROM `economy` WHERE `uuid` = ?")
                .addValues(uuid.toString())
                .querySafe(resultSet -> resultSet.next() ? resultSet.getDouble("balance") : 0.0)
                .orElse(0D);
    }

    @Override
    public boolean set(UUID uuid, double amount) {
        if (amount < 0) {
            return false;
        } else {
            return StatementBuilder.create(connection)
                    .setStatement("UPDATE `economy` SET `balance`= ? WHERE `uuid`= ?")
                    .addValues(amount, uuid.toString())
                    .updateSafe() > 0;
        }
    }

    @Override
    public boolean createAccount(UUID uuid, double startAmount) {
        if (hasAccount(uuid)) {
            return false;
        } else {
            return StatementBuilder.create(connection)
                    .setStatement("INSERT INTO `economy` (`uuid`, `balance`) VALUES ( ? , ? )")
                    .addValues(uuid.toString(), startAmount)
                    .updateSafe() > 0;
        }
    }

    @Override
    public void disable() {
        try {
            connection.close();
        } catch (SQLException e) {
            instance.getLogger().log(Level.SEVERE, "disable()", e);
        }
    }
}
