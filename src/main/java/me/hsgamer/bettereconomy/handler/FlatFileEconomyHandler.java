package me.hsgamer.bettereconomy.handler;

import me.hsgamer.bettereconomy.BetterEconomy;
import me.hsgamer.bettereconomy.api.EconomyHandler;
import me.hsgamer.hscore.bukkit.config.BukkitConfig;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

public class FlatFileEconomyHandler extends EconomyHandler {
    private final BukkitConfig storageFile;
    private final AtomicBoolean needSaving = new AtomicBoolean();
    private final BukkitTask task;

    public FlatFileEconomyHandler(BetterEconomy instance) {
        super(instance);
        storageFile = new BukkitConfig(instance, "balances.yml");
        storageFile.setup();
        task = new BukkitRunnable() {
            @Override
            public void run() {
                if (!needSaving.get()) {
                    return;
                }
                needSaving.set(false);
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        storageFile.save();
                    }
                }.runTask(instance);
            }
        }.runTaskTimerAsynchronously(instance, instance.getMainConfig().getSaveFilePeriod(), instance.getMainConfig().getSaveFilePeriod());
    }

    @Override
    public boolean hasAccount(UUID uuid) {
        return storageFile.contains(uuid.toString());
    }

    @Override
    public double get(UUID uuid) {
        return storageFile.getInstance(uuid.toString(), 0, Number.class).doubleValue();
    }

    @Override
    public boolean set(UUID uuid, double amount) {
        if (amount < 0) {
            return false;
        }
        storageFile.set(uuid.toString(), amount);
        needSaving.lazySet(true);
        return true;
    }

    @Override
    public boolean createAccount(UUID uuid, double startAmount) {
        if (hasAccount(uuid)) {
            return false;
        }
        storageFile.set(uuid.toString(), startAmount);
        needSaving.lazySet(true);
        return true;
    }

    @Override
    public void disable() {
        task.cancel();
        storageFile.save();
    }
}
