package me.hsgamer.bettereconomy.top;

import io.github.projectunified.minelib.plugin.base.Loadable;
import io.github.projectunified.minelib.scheduler.async.AsyncScheduler;
import io.github.projectunified.minelib.scheduler.common.task.Task;
import me.hsgamer.bettereconomy.BetterEconomy;
import me.hsgamer.bettereconomy.Utils;
import me.hsgamer.bettereconomy.api.EconomyHandler;
import me.hsgamer.bettereconomy.config.MainConfig;
import me.hsgamer.bettereconomy.provider.EconomyHandlerProvider;
import org.bukkit.Bukkit;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TopRunnable implements Runnable, Loadable {
    private final BetterEconomy instance;
    private final AtomicReference<List<PlayerBalanceSnapshot>> topList = new AtomicReference<>(Collections.emptyList());
    private final AtomicReference<Map<UUID, Integer>> topIndex = new AtomicReference<>(Collections.emptyMap());
    private Task task;

    public TopRunnable(BetterEconomy instance) {
        this.instance = instance;
    }

    @Override
    public void run() {
        EconomyHandler economyHandler = instance.get(EconomyHandlerProvider.class).getEconomyHandler();
        List<PlayerBalanceSnapshot> list = Arrays.stream(Bukkit.getOfflinePlayers())
                .parallel()
                .map(Utils::getUniqueId)
                .filter(economyHandler::hasAccount)
                .map(uuid -> new PlayerBalanceSnapshot(uuid, economyHandler.get(uuid)))
                .sorted(Comparator.comparingDouble(PlayerBalanceSnapshot::getBalance).reversed())
                .collect(Collectors.toList());
        topList.lazySet(list);

        Map<UUID, Integer> position = IntStream.range(0, list.size())
                .boxed()
                .collect(Collectors.toMap(i -> list.get(i).getUuid(), i -> i, (a, b) -> b));
        topIndex.lazySet(position);
    }

    @Override
    public void enable() {
        task = AsyncScheduler.get(instance).runTimer(this, 0, instance.get(MainConfig.class).getUpdateBalanceTopPeriod());
    }

    @Override
    public void disable() {
        if (task != null) {
            task.cancel();
        }
    }

    public List<PlayerBalanceSnapshot> getTopList() {
        return topList.get();
    }

    public int getTopIndex(UUID uuid) {
        return topIndex.get().getOrDefault(uuid, -1);
    }
}
