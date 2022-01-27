package me.hsgamer.bettereconomy.handler;

import me.hsgamer.bettereconomy.BetterEconomy;
import me.hsgamer.bettereconomy.api.EconomyHandler;
import me.hsgamer.hscore.database.Driver;
import me.hsgamer.hscore.database.Setting;
import me.hsgamer.hscore.database.client.sql.PreparedStatementContainer;
import me.hsgamer.hscore.database.client.sql.java.JavaSqlClient;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;
import java.util.logging.Level;

public abstract class SqlEconomyHandler extends EconomyHandler {

    private final Connection connection;

    SqlEconomyHandler(BetterEconomy instance, Setting setting, Driver driver) {
        super(instance);
        try {
            JavaSqlClient client = new JavaSqlClient(setting, driver);
            connection = client.getConnection();
        } catch (ClassNotFoundException | SQLException e) {
            throw new IllegalStateException("constructor()#connection", e);
        }
        try (Statement statement = connection.createStatement()) {
            statement.execute("CREATE TABLE IF NOT EXISTS `economy` (`uuid` varchar(36) NOT NULL UNIQUE, `balance` double DEFAULT 0);");
        } catch (SQLException e) {
            throw new IllegalStateException("constructor()#createTable", e);
        }
    }

    @Override
    public boolean hasAccount(UUID uuid) {
        try (
                PreparedStatementContainer container = PreparedStatementContainer.of(
                        connection, "SELECT * FROM `economy` WHERE `uuid` = ?",
                        uuid.toString()
                );
                ResultSet resultSet = container.query()
        ) {
            return resultSet.next();
        } catch (SQLException e) {
            instance.getLogger().log(Level.SEVERE, "hasAccount()", e);
            return false;
        }
    }

    @Override
    public double get(UUID uuid) {
        try (
                PreparedStatementContainer container = PreparedStatementContainer.of(
                        connection, "SELECT `balance` FROM `economy` WHERE `uuid` = ?",
                        uuid.toString()
                );
                ResultSet resultSet = container.query()
        ) {
            return resultSet.next() ? resultSet.getDouble("balance") : 0.0;
        } catch (SQLException e) {
            instance.getLogger().log(Level.SEVERE, "get()", e);
            return 0;
        }
    }

    @Override
    public boolean set(UUID uuid, double amount) {
        if (amount < 0) {
            return false;
        }
        try (
                PreparedStatementContainer container = PreparedStatementContainer.of(
                        connection, "UPDATE `economy` SET `balance`= ? WHERE `uuid`= ?",
                        amount, uuid.toString()
                )
        ) {
            return container.update() > 0;
        } catch (SQLException e) {
            instance.getLogger().log(Level.SEVERE, "set()", e);
            return false;
        }
    }

    @Override
    public boolean createAccount(UUID uuid, double startAmount) {
        if (hasAccount(uuid)) {
            return false;
        }
        try (
                PreparedStatementContainer container = PreparedStatementContainer.of(
                        connection, "INSERT INTO `economy` (`uuid`, `balance`) VALUES ( ? , ? )",
                        uuid.toString(), startAmount
                )
        ) {
            return container.update() > 0;
        } catch (SQLException e) {
            instance.getLogger().log(Level.SEVERE, "createAccount()", e);
            return false;
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
