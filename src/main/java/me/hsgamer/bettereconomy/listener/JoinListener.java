package me.hsgamer.bettereconomy.listener;

import io.github.projectunified.minelib.plugin.listener.ListenerComponent;
import me.hsgamer.bettereconomy.BetterEconomy;
import me.hsgamer.bettereconomy.holder.EconomyHolder;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class JoinListener implements ListenerComponent {
    private final BetterEconomy instance;

    public JoinListener(BetterEconomy instance) {
        this.instance = instance;
    }

    @Override
    public JavaPlugin getPlugin() {
        return instance;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        instance.get(EconomyHolder.class).createAccount(event.getPlayer().getUniqueId());
    }
}
