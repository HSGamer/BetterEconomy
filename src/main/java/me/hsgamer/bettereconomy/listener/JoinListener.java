package me.hsgamer.bettereconomy.listener;

import me.hsgamer.bettereconomy.BetterEconomy;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

import java.util.UUID;

public class JoinListener implements Listener {
    private final BetterEconomy instance;

    public JoinListener(BetterEconomy instance) {
        this.instance = instance;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPreLogin(AsyncPlayerPreLoginEvent event) {
        if (event.getLoginResult() != AsyncPlayerPreLoginEvent.Result.ALLOWED) return;
        UUID uuid = event.getUniqueId();
        if (!instance.getEconomyHandler().hasAccount(uuid)) {
            instance.getEconomyHandler().createAccount(uuid);
        }
    }
}
