package me.hsgamer.bettereconomy.listener;

import me.hsgamer.bettereconomy.BetterEconomy;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.UUID;

public class JoinListener implements Listener {
    private final BetterEconomy instance;

    public JoinListener(BetterEconomy instance) {
        this.instance = instance;
    }

    @EventHandler
    public void onPreLogin(PlayerJoinEvent event) {
        UUID uuid = event.getPlayer().getUniqueId();
        if (!instance.getEconomyHandler().hasAccount(uuid)) {
            instance.getEconomyHandler().createAccount(uuid);
        }
    }
}
