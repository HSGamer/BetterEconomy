package me.hsgamer.bettereconomy.handler;

import me.hsgamer.bettereconomy.BetterEconomy;
import me.hsgamer.bettereconomy.api.EconomyHandler;
import me.hsgamer.bettereconomy.config.MainConfig;
import me.hsgamer.hscore.database.Setting;
import me.hsgamer.hscore.database.client.sql.SqlClient;
import me.hsgamer.hscore.database.client.sql.StatementBuilder;
import me.hsgamer.hscore.database.client.sql.java.JavaSqlClient;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Level;

public abstract class SqlEconomyHandler extends EconomyHandler {

    private final SqlClient<?> client;
    private final AtomicReference<Connection> connectionReference = new AtomicReference<>();

    SqlEconomyHandler(BetterEconomy instance, Setting setting) {
        super(instance);

        setting
                .setClientProperties(instance.get(MainConfig.class).getDatabaseClientSettings())
                .setDriverProperties(instance.get(MainConfig.class).getDatabaseDriverSettings());

        try {
            client = new JavaSqlClient(setting);
            connectionReference.set(client.getConnection());
        } catch (SQLException e) {
            throw new IllegalStateException("constructor()#connection", e);
        }
        StatementBuilder.create(getConnection())
                .setStatement("CREATE TABLE IF NOT EXISTS `economy` (`uuid` varchar(36) NOT NULL UNIQUE, `balance` double DEFAULT 0);")
                .updateSafe();
    }

    private Connection getConnection() {
        Connection connection = this.connectionReference.get();
        try {
            if (connection == null || connection.isClosed()) {
                connection = client.getConnection();
                this.connectionReference.set(connection);
            }
        } catch (SQLException e) {
            throw new IllegalStateException("getConnection()", e);
        }
        return connection;
    }

    @Override
    public boolean hasAccount(UUID uuid) {
        return StatementBuilder.create(getConnection())
                .setStatement("SELECT * FROM `economy` WHERE `uuid` = ?")
                .addValues(uuid.toString())
                .querySafe(ResultSet::next)
                .orElse(false);
    }

    @Override
    public double get(UUID uuid) {
        return StatementBuilder.create(getConnection())
                .setStatement("SELECT `balance` FROM `economy` WHERE `uuid` = ?")
                .addValues(uuid.toString())
                .querySafe(resultSet -> resultSet.next() ? resultSet.getDouble("balance") : 0.0)
                .orElse(0D);
    }

    @Override
    public boolean set(UUID uuid, double amount) {
        if (amount < instance.get(MainConfig.class).getMinimumAmount()) {
            return false;
        } else {
            return StatementBuilder.create(getConnection())
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
            return StatementBuilder.create(getConnection())
                    .setStatement("INSERT INTO `economy` (`uuid`, `balance`) VALUES ( ? , ? )")
                    .addValues(uuid.toString(), startAmount)
                    .updateSafe() > 0;
        }
    }

    @Override
    public void disable() {
        try {
            getConnection().close();
        } catch (SQLException e) {
            instance.getLogger().log(Level.SEVERE, "disable()", e);
        }
    }
}
