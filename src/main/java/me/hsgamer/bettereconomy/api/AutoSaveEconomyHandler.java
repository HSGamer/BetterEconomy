package me.hsgamer.bettereconomy.api;

import me.hsgamer.bettereconomy.BetterEconomy;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

import java.util.concurrent.atomic.AtomicBoolean;

public abstract class AutoSaveEconomyHandler extends EconomyHandler {
    private final AtomicBoolean needSaving = new AtomicBoolean();
    private final BukkitTask task;

    protected AutoSaveEconomyHandler(BetterEconomy instance) {
        super(instance);
        task = Bukkit.getScheduler().runTaskTimerAsynchronously(
                instance, this::executeSaveTask,
                instance.getMainConfig().getSaveFilePeriod(),
                instance.getMainConfig().getSaveFilePeriod()
        );
    }

    private void executeSaveTask() {
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
        task.cancel();
        save();
    }
}
