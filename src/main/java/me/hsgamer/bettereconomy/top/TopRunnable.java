package me.hsgamer.bettereconomy.top;

import me.hsgamer.bettereconomy.BetterEconomy;
import me.hsgamer.bettereconomy.Utils;
import org.bukkit.Bukkit;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public class TopRunnable implements Runnable {
    private final BetterEconomy instance;
    private final AtomicReference<List<PlayerBalanceSnapshot>> topList = new AtomicReference<>(Collections.emptyList());

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
    }

    public List<PlayerBalanceSnapshot> getTopList() {
        return topList.get();
    }
}
