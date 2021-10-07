package me.hsgamer.bettereconomy.top;

import lombok.Data;
import org.bukkit.OfflinePlayer;

@Data(staticConstructor = "of")
public class PlayerBalanceSnapshot {
    private final OfflinePlayer offlinePlayer;
    private final double balance;
}
