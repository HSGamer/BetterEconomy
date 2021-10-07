package me.hsgamer.bettereconomy.handler;

import me.hsgamer.bettereconomy.BetterEconomy;
import me.hsgamer.bettereconomy.api.EconomyHandler;
import me.hsgamer.hscore.database.Setting;
import me.hsgamer.hscore.database.client.sql.java.JavaSqlClient;
import me.hsgamer.hscore.database.driver.MySqlDriver;
import org.bukkit.OfflinePlayer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;

public class MySQLEconomyHandler extends EconomyHandler {

  private Connection connection;

  public MySQLEconomyHandler(BetterEconomy instance) {
    super(instance);
    final Setting setting = new Setting();
    setting.setHost(instance.getMainConfig().getMysqlHost());
    setting.setPort(instance.getMainConfig().getMysqlPort());
    setting.setDatabaseName(instance.getMainConfig().getMysqlDatabaseName());
    setting.setUsername(instance.getMainConfig().getMysqlUsername());
    setting.setPassword(instance.getMainConfig().getMysqlPassword());
    try {
      JavaSqlClient client = new JavaSqlClient(setting, new MySqlDriver());
      connection = client.getConnection();
    } catch (ClassNotFoundException | SQLException e) {
      instance.getLogger().log(Level.SEVERE, "constructor()#connection", e);
      return;
    }
    final String sql = "CREATE TABLE IF NOT EXISTS `economy` (`uuid` varchar(36) NOT NULL UNIQUE, `balance` double DEFAULT 0);";
    try (PreparedStatement stmt = connection.prepareStatement(sql)) {
      stmt.execute();
    } catch (SQLException e) {
      instance.getLogger().log(Level.SEVERE, "constructor()#createTable", e);
    }
  }

  @Override
  public boolean hasAccount(OfflinePlayer player) {
    final String sql = "SELECT * FROM `economy` WHERE `uuid` = ?";
    try (PreparedStatement stmt = connection.prepareStatement(sql)) {
      stmt.setString(1, player.getUniqueId().toString());
      return stmt.executeQuery().next();
    } catch (SQLException e) {
      instance.getLogger().log(Level.SEVERE, "hasAccount()", e);
      return false;
    }
  }

  @Override
  public double get(OfflinePlayer player) {
    final String sql = "SELECT `balance` FROM `economy` WHERE `uuid` = ?";
    try (PreparedStatement stmt = connection.prepareStatement(sql)) {
      stmt.setString(1, player.getUniqueId().toString());
      ResultSet result = stmt.executeQuery();
      return result.next() ? result.getDouble("balance") : 0.0;
    } catch (SQLException e) {
      instance.getLogger().log(Level.SEVERE, "get()", e);
      return 0;
    }
  }

  @Override
  public boolean has(OfflinePlayer player, double amount) {
    return get(player) >= amount;
  }

  @Override
  public boolean set(OfflinePlayer player, double amount) {
    if (amount < 0) {
      return false;
    }
    final String sql = "UPDATE `economy` SET `balance`= ? WHERE `uuid`= ?";
    try (PreparedStatement stmt = connection.prepareStatement(sql)) {
      stmt.setDouble(1, amount);
      stmt.setString(2, player.getUniqueId().toString());
      stmt.executeUpdate();
    } catch (SQLException e) {
      instance.getLogger().log(Level.SEVERE, "set()", e);
    }
    return true;
  }

  @Override
  public boolean createAccount(OfflinePlayer player) {
    if (hasAccount(player)) {
      return false;
    }
    final String sql = "INSERT INTO `economy`  (`uuid`, `balance`) VALUES ( ? , ? ) ";
    try (PreparedStatement stmt = connection.prepareStatement(sql)) {
      stmt.setString(1, player.getUniqueId().toString());
      stmt.setDouble(2, instance.getMainConfig().getStartAmount());
      stmt.executeUpdate();
    } catch (SQLException e) {
      instance.getLogger().log(Level.SEVERE, "createAccount()", e);
    }
    return true;
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
