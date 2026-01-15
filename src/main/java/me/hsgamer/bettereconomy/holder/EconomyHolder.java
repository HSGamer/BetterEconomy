package me.hsgamer.bettereconomy.holder;

import io.github.projectunified.minelib.plugin.base.Loadable;
import io.github.projectunified.minelib.scheduler.async.AsyncScheduler;
import me.hsgamer.bettereconomy.BetterEconomy;
import me.hsgamer.bettereconomy.config.MainConfig;
import me.hsgamer.hscore.bukkit.config.BukkitConfig;
import me.hsgamer.hscore.config.Config;
import me.hsgamer.hscore.config.gson.GsonConfig;
import me.hsgamer.hscore.database.client.sql.java.JavaSqlClient;
import me.hsgamer.topper.agent.core.Agent;
import me.hsgamer.topper.agent.core.AgentHolder;
import me.hsgamer.topper.agent.core.DataEntryAgent;
import me.hsgamer.topper.agent.snapshot.SnapshotAgent;
import me.hsgamer.topper.agent.storage.StorageAgent;
import me.hsgamer.topper.data.core.DataEntry;
import me.hsgamer.topper.data.simple.SimpleDataHolder;
import me.hsgamer.topper.spigot.agent.runnable.SpigotRunnableAgent;
import me.hsgamer.topper.storage.core.DataStorage;
import me.hsgamer.topper.storage.flat.configfile.ConfigFileDataStorage;
import me.hsgamer.topper.storage.flat.converter.NumberFlatValueConverter;
import me.hsgamer.topper.storage.flat.converter.UUIDFlatValueConverter;
import me.hsgamer.topper.storage.sql.converter.NumberSqlValueConverter;
import me.hsgamer.topper.storage.sql.converter.UUIDSqlValueConverter;
import me.hsgamer.topper.storage.sql.mysql.MySqlDataStorageSupplier;
import me.hsgamer.topper.storage.sql.sqlite.SqliteDataStorageSupplier;

import java.io.File;
import java.util.*;

public class EconomyHolder extends SimpleDataHolder<UUID, Double> implements AgentHolder<UUID, Double>, Loadable {
    private final BetterEconomy instance;
    private final MainConfig mainConfig;
    private final List<Agent> agents;
    private final List<DataEntryAgent<UUID, Double>> entryAgents;

    private final StorageAgent<UUID, Double> storageAgent;
    private final SnapshotAgent<UUID, Double> snapshotAgent;

    public EconomyHolder(BetterEconomy instance, MainConfig mainConfig) {
        this.instance = instance;
        this.mainConfig = mainConfig;

        storageAgent = new StorageAgent<>(getStorage());
        snapshotAgent = SnapshotAgent.create(this);
        snapshotAgent.setComparator(Comparator.reverseOrder());
        snapshotAgent.setFilter(entry -> entry.getValue() != null);

        this.agents = Arrays.asList(
                storageAgent,
                storageAgent.getLoadAgent(this),
                new SpigotRunnableAgent(storageAgent, AsyncScheduler.get(instance), mainConfig.getSaveFilePeriod()),

                snapshotAgent,
                new SpigotRunnableAgent(snapshotAgent, AsyncScheduler.get(instance), mainConfig.getUpdateBalanceTopPeriod())

        );
        this.entryAgents = Collections.singletonList(
                storageAgent
        );
    }

    private DataStorage<UUID, Double> getStorage() {
        String type = mainConfig.getHandlerType();
        UUIDFlatValueConverter keyConverter = new UUIDFlatValueConverter();
        NumberFlatValueConverter<Double> valueConverter = new NumberFlatValueConverter<>(Number::doubleValue);
        UUIDSqlValueConverter sqlKeyConverter = new UUIDSqlValueConverter("uuid");
        NumberSqlValueConverter<Double> sqlValueConverter = new NumberSqlValueConverter<>("balance", true, Number::doubleValue);
        switch (type.toLowerCase(Locale.ROOT)) {
            case "mysql": {
                MySqlDataStorageSupplier supplier = new MySqlDataStorageSupplier(mainConfig.getSqlDatabaseSetting(false), JavaSqlClient::new);
                return supplier.getStorage("economy", sqlKeyConverter, sqlValueConverter);
            }
            case "sqlite": {
                SqliteDataStorageSupplier supplier = new SqliteDataStorageSupplier(instance.getDataFolder(), mainConfig.getSqlDatabaseSetting(true), JavaSqlClient::new);
                return supplier.getStorage("economy", sqlKeyConverter, sqlValueConverter);
            }
            case "json":
                return new ConfigFileDataStorage<UUID, Double>(instance.getDataFolder(), "balances", keyConverter, valueConverter) {
                    @Override
                    protected Config getConfig(File file) {
                        return new GsonConfig(file);
                    }

                    @Override
                    protected String getConfigName(String name) {
                        return name + ".json";
                    }
                };
            default:
                return new ConfigFileDataStorage<UUID, Double>(instance.getDataFolder(), "balances", keyConverter, valueConverter) {
                    @Override
                    protected Config getConfig(File file) {
                        return new BukkitConfig(file);
                    }

                    @Override
                    protected String getConfigName(String name) {
                        return name + ".yml";
                    }
                };
        }
    }

    @Override
    public List<Agent> getAgents() {
        return agents;
    }

    @Override
    public List<DataEntryAgent<UUID, Double>> getEntryAgents() {
        return entryAgents;
    }

    @Override
    public void enable() {
        register();
    }

    @Override
    public void disable() {
        unregister();
    }

    public double get(UUID uuid) {
        return getOrCreateEntry(uuid).getValue();
    }

    public boolean hasAccount(UUID uuid) {
        return getEntry(uuid).map(DataEntry::getValue).isPresent();
    }

    public boolean createAccount(UUID uuid) {
        if (hasAccount(uuid)) {
            return false;
        }
        getOrCreateEntry(uuid).setValue(mainConfig.getStartAmount());
        return true;
    }

    public boolean deleteAccount(UUID uuid) {
        if (!hasAccount(uuid)) {
            return false;
        }
        getOrCreateEntry(uuid).setValue((Double) null);
        return true;
    }

    public boolean has(UUID uuid, double amount) {
        return get(uuid) >= amount;
    }

    public boolean set(UUID uuid, double amount) {
        if (amount < mainConfig.getMinimumAmount()) {
            return false;
        }
        getOrCreateEntry(uuid).setValue(amount);
        return true;
    }

    public boolean withdraw(UUID uuid, double amount) {
        return set(uuid, get(uuid) - amount);
    }

    public boolean deposit(UUID uuid, double amount) {
        return set(uuid, get(uuid) + amount);
    }

    public StorageAgent<UUID, Double> getStorageAgent() {
        return this.storageAgent;
    }

    public SnapshotAgent<UUID, Double> getSnapshotAgent() {
        return this.snapshotAgent;
    }
}
