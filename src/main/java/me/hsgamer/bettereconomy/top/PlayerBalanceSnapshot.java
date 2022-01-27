package me.hsgamer.bettereconomy.top;

import lombok.Data;

import java.util.UUID;

@Data(staticConstructor = "of")
public class PlayerBalanceSnapshot {
    private final UUID uuid;
    private final double balance;
}
