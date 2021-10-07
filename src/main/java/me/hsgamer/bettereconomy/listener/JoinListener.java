package me.hsgamer.bettereconomy.listener;

import me.hsgamer.bettereconomy.BetterEconomy;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinListener implements Listener {
    private final BetterEconomy instance;

    public JoinListener(BetterEconomy instance) {
        this.instance = instance;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        if (!instance.getEconomyHandler().hasAccount(event.getPlayer())) {
            instance.getEconomyHandler().createAccount(event.getPlayer());
        }
    }
}
