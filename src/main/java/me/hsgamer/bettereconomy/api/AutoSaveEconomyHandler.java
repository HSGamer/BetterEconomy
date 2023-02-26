package me.hsgamer.bettereconomy.api;

import me.hsgamer.bettereconomy.BetterEconomy;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

import java.util.concurrent.atomic.AtomicBoolean;

public abstract class AutoSaveEconomyHandler extends EconomyHandler implements Runnable {
    private final AtomicBoolean needSaving = new AtomicBoolean();
    private BukkitTask task;

    protected AutoSaveEconomyHandler(BetterEconomy instance) {
        super(instance);
        int period = instance.getMainConfig().getSaveFilePeriod();
        if (period >= 0) {
            task = Bukkit.getScheduler().runTaskTimerAsynchronously(
                    instance, this,
                    instance.getMainConfig().getSaveFilePeriod(),
                    instance.getMainConfig().getSaveFilePeriod()
            );
        }
    }

    @Override
    public final void run() {
        if (!needSaving.get()) {
            return;
        }
        Bukkit.getScheduler().runTask(instance, () -> {
            this.save();
            needSaving.set(false);
        });
    }

    protected abstract void save();

    protected void enableSave() {
        needSaving.lazySet(true);
    }

    @Override
    public void disable() {
        if (task != null) {
            task.cancel();
        }
        save();
    }
}
