package me.hsgamer.bettereconomy.api;

import me.hsgamer.bettereconomy.BetterEconomy;
import me.hsgamer.hscore.bukkit.scheduler.Scheduler;
import me.hsgamer.hscore.bukkit.scheduler.Task;

import java.util.concurrent.atomic.AtomicBoolean;

public abstract class AutoSaveEconomyHandler extends EconomyHandler implements Runnable {
    private final AtomicBoolean needSaving = new AtomicBoolean();
    private Task task;

    protected AutoSaveEconomyHandler(BetterEconomy instance) {
        super(instance);
        int period = instance.getMainConfig().getSaveFilePeriod();
        if (period >= 0) {
            task = Scheduler.plugin(instance).async().runTaskTimer(
                    this,
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
        Scheduler.plugin(instance).sync().runTask(() -> {
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
