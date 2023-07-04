package me.hsgamer.bettereconomy.hook.treasury;

import me.hsgamer.bettereconomy.BetterEconomy;
import me.lokka30.treasury.api.economy.account.PlayerAccount;
import me.lokka30.treasury.api.economy.account.accessor.PlayerAccountAccessor;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public class TreasuryPlayerAccountAccessor extends PlayerAccountAccessor {
    private static final Map<UUID, TreasuryAccount> accountMap = new ConcurrentHashMap<>();
    private final BetterEconomy instance;

    public TreasuryPlayerAccountAccessor(BetterEconomy instance) {
        this.instance = instance;
    }

    @Override
    protected @NotNull CompletableFuture<PlayerAccount> getOrCreate(@NotNull PlayerAccountCreateContext context) {
        return CompletableFuture.supplyAsync(() -> accountMap.computeIfAbsent(context.getUniqueId(), uuid -> new TreasuryAccount(instance, context.getUniqueId())));
    }
}
