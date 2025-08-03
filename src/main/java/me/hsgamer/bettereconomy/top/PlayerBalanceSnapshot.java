package me.hsgamer.bettereconomy.top;

import java.util.UUID;

import lombok.Data;

@Data
public class PlayerBalanceSnapshot {
    private final UUID uuid;
    private final double balance;
}
