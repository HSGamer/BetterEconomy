package me.hsgamer.bettereconomy.api;

import me.hsgamer.bettereconomy.BetterEconomy;
import org.bukkit.OfflinePlayer;

public abstract class EconomyHandler {
    protected final BetterEconomy instance;

    protected EconomyHandler(BetterEconomy instance) {
        this.instance = instance;
    }

    public abstract boolean hasAccount(OfflinePlayer player);

    public abstract double get(OfflinePlayer player);

    public abstract boolean has(OfflinePlayer player, double amount);

    public abstract boolean set(OfflinePlayer player, double amount);

    public boolean withdraw(OfflinePlayer player, double amount) {
        return set(player, get(player) - amount);
    }

    public boolean deposit(OfflinePlayer player, double amount) {
        return set(player, get(player) + amount);
    }

    public abstract boolean createAccount(OfflinePlayer player, double startAmount);

    public boolean createAccount(OfflinePlayer player) {
        return createAccount(player, instance.getMainConfig().getStartAmount());
    }

    public void disable() {
        // EMPTY
    }
}
