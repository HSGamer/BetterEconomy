package me.hsgamer.bettereconomy.top;

import me.hsgamer.bettereconomy.BetterEconomy;
import me.hsgamer.bettereconomy.Utils;
import org.bukkit.Bukkit;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TopRunnable implements Runnable {
    private final BetterEconomy instance;
    private final AtomicReference<List<PlayerBalanceSnapshot>> topList = new AtomicReference<>(Collections.emptyList());
    private final AtomicReference<Map<UUID, Integer>> topIndex = new AtomicReference<>(Collections.emptyMap());

    public TopRunnable(BetterEconomy instance) {
        this.instance = instance;
    }

    @Override
    public void run() {
        List<PlayerBalanceSnapshot> list = Arrays.stream(Bukkit.getOfflinePlayers())
                .parallel()
                .map(Utils::getUniqueId)
                .filter(instance.getEconomyHandler()::hasAccount)
                .map(uuid -> new PlayerBalanceSnapshot(uuid, instance.getEconomyHandler().get(uuid)))
                .sorted(Comparator.comparingDouble(PlayerBalanceSnapshot::getBalance).reversed())
                .collect(Collectors.toList());
        topList.lazySet(list);

        Map<UUID, Integer> position = IntStream.range(0, list.size())
                .boxed()
                .collect(Collectors.toMap(i -> list.get(i).getUuid(), i -> i, (a, b) -> b));
        topIndex.lazySet(position);
    }

    public List<PlayerBalanceSnapshot> getTopList() {
        return topList.get();
    }

    public int getTopIndex(UUID uuid) {
        return topIndex.get().getOrDefault(uuid, -1);
    }
}
