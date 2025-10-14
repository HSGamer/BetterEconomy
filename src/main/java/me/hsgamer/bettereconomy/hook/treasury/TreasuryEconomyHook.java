package me.hsgamer.bettereconomy.hook.treasury;

import me.hsgamer.bettereconomy.BetterEconomy;
import me.hsgamer.bettereconomy.holder.EconomyHolder;
import me.lokka30.treasury.api.common.NamespacedKey;
import me.lokka30.treasury.api.common.misc.FutureHelper;
import me.lokka30.treasury.api.common.misc.TriState;
import me.lokka30.treasury.api.economy.EconomyProvider;
import me.lokka30.treasury.api.economy.account.AccountData;
import me.lokka30.treasury.api.economy.account.accessor.AccountAccessor;
import me.lokka30.treasury.api.economy.currency.Currency;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.CompletableFuture;

public class TreasuryEconomyHook implements EconomyProvider {
    public static final String CURRENCY_IDENTIFIER = "better_economy_currency";
    private final BetterEconomy instance;
    private final Currency currency;
    private final AccountAccessor accountAccessor;

    public TreasuryEconomyHook(BetterEconomy instance) {
        this.instance = instance;
        this.currency = new TreasuryCurrency(instance);
        this.accountAccessor = new TreasuryAccountAccessor(instance);
    }

    @Override
    public @NotNull AccountAccessor accountAccessor() {
        return accountAccessor;
    }

    @Override
    public @NotNull CompletableFuture<Boolean> hasAccount(@NotNull AccountData accountData) {
        if (accountData.isPlayerAccount()) {
            return CompletableFuture.supplyAsync(() -> instance.get(EconomyHolder.class).hasAccount(accountData.getPlayerIdentifier().get()));
        } else {
            return FutureHelper.failedFuture(FailureReasons.FEATURE_NOT_SUPPORTED.toException());
        }
    }

    @Override
    public @NotNull CompletableFuture<Collection<UUID>> retrievePlayerAccountIds() {
        return CompletableFuture.supplyAsync(() -> instance.get(EconomyHolder.class).getEntryMap().keySet());
    }

    @Override
    public @NotNull CompletableFuture<Collection<NamespacedKey>> retrieveNonPlayerAccountIds() {
        return CompletableFuture.completedFuture(Collections.emptyList());
    }

    @Override
    public @NotNull Currency getPrimaryCurrency() {
        return currency;
    }

    @Override
    public @NotNull Optional<Currency> findCurrency(@NotNull String identifier) {
        return currency.getIdentifier().equals(CURRENCY_IDENTIFIER) ? Optional.of(currency) : Optional.empty();
    }

    @Override
    public @NotNull Set<Currency> getCurrencies() {
        return Collections.singleton(currency);
    }

    @Override
    public @NotNull CompletableFuture<TriState> registerCurrency(@NotNull Currency currency) {
        return CompletableFuture.completedFuture(TriState.FALSE);
    }

    @Override
    public @NotNull CompletableFuture<TriState> unregisterCurrency(@NotNull Currency currency) {
        return CompletableFuture.completedFuture(TriState.FALSE);
    }
}
