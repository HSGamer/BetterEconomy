package me.hsgamer.bettereconomy.hook.treasury;

import me.hsgamer.bettereconomy.BetterEconomy;
import me.lokka30.treasury.api.economy.account.accessor.AccountAccessor;
import me.lokka30.treasury.api.economy.account.accessor.NonPlayerAccountAccessor;
import me.lokka30.treasury.api.economy.account.accessor.PlayerAccountAccessor;
import org.jetbrains.annotations.NotNull;

public class TreasuryAccountAccessor implements AccountAccessor {
    private final BetterEconomy instance;

    public TreasuryAccountAccessor(BetterEconomy instance) {
        this.instance = instance;
    }

    @Override
    public @NotNull PlayerAccountAccessor player() {
        return new TreasuryPlayerAccountAccessor(instance);
    }

    @Override
    public @NotNull NonPlayerAccountAccessor nonPlayer() {
        return new TreasuryNonPlayerAccountAccessor();
    }
}
