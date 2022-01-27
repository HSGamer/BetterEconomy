package me.hsgamer.bettereconomy.hook.treasury;

import me.hsgamer.bettereconomy.BetterEconomy;
import me.hsgamer.bettereconomy.Utils;
import me.hsgamer.hscore.bukkit.utils.BukkitUtils;
import me.lokka30.treasury.api.economy.EconomyProvider;
import me.lokka30.treasury.api.economy.account.Account;
import me.lokka30.treasury.api.economy.account.PlayerAccount;
import me.lokka30.treasury.api.economy.currency.Currency;
import me.lokka30.treasury.api.economy.misc.EconomyAPIVersion;
import me.lokka30.treasury.api.economy.misc.OptionalEconomyApiFeature;
import me.lokka30.treasury.api.economy.response.EconomyException;
import me.lokka30.treasury.api.economy.response.EconomyFailureReason;
import me.lokka30.treasury.api.economy.response.EconomySubscriber;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;

public class TreasuryEconomyHook implements EconomyProvider {
    public static final String CURRENCY_IDENTIFIER = "better_economy_currency";
    private final BetterEconomy instance;
    private final Currency currency;

    public TreasuryEconomyHook(BetterEconomy instance) {
        this.instance = instance;
        this.currency = new TreasuryCurrency(instance);
    }

    @Override
    public @NotNull EconomyAPIVersion getSupportedAPIVersion() {
        return EconomyAPIVersion.v1_0;
    }

    @Override
    public @NotNull Set<OptionalEconomyApiFeature> getSupportedOptionalEconomyApiFeatures() {
        return Collections.emptySet();
    }

    @Override
    public void hasPlayerAccount(@NotNull UUID accountId, @NotNull EconomySubscriber<Boolean> subscription) {
        Utils.schedule(() -> {
            boolean status = instance.getEconomyHandler().hasAccount(accountId);
            subscription.succeed(status);
        });
    }

    @Override
    public void retrievePlayerAccount(@NotNull UUID accountId, @NotNull EconomySubscriber<PlayerAccount> subscription) {
        Utils.schedule(() -> {
            if (instance.getEconomyHandler().hasAccount(accountId)) {
                subscription.succeed(new TreasuryAccount(instance, accountId));
            } else {
                subscription.fail(new EconomyException(EconomyFailureReason.ACCOUNT_NOT_FOUND));
            }
        });
    }

    @Override
    public void createPlayerAccount(@NotNull UUID accountId, @NotNull EconomySubscriber<PlayerAccount> subscription) {
        Utils.schedule(() -> {
            instance.getEconomyHandler().createAccount(accountId);
            subscription.succeed(new TreasuryAccount(instance, accountId));
        });
    }

    @Override
    public void retrievePlayerAccountIds(@NotNull EconomySubscriber<Collection<UUID>> subscription) {
        Utils.scheduleAsync(() -> {
            Collection<UUID> uuids = BukkitUtils.getAllUniqueIds()
                    .parallelStream()
                    .filter(uuid -> instance.getEconomyHandler().hasAccount(uuid))
                    .collect(Collectors.toList());
            subscription.succeed(uuids);
        });
    }

    @Override
    public void hasAccount(@NotNull String identifier, @NotNull EconomySubscriber<Boolean> subscription) {
        subscription.fail(new EconomyException(EconomyFailureReason.FEATURE_NOT_SUPPORTED));
    }

    @Override
    public void retrieveAccount(@NotNull String identifier, @NotNull EconomySubscriber<Account> subscription) {
        subscription.fail(new EconomyException(EconomyFailureReason.FEATURE_NOT_SUPPORTED));
    }

    @Override
    public void createAccount(@Nullable String name, @NotNull String identifier, @NotNull EconomySubscriber<Account> subscription) {
        subscription.fail(new EconomyException(EconomyFailureReason.FEATURE_NOT_SUPPORTED));
    }

    @Override
    public void retrieveAccountIds(@NotNull EconomySubscriber<Collection<String>> subscription) {
        Utils.scheduleAsync(() -> {
            Collection<String> identifiers = BukkitUtils.getAllUniqueIds()
                    .parallelStream()
                    .filter(uuid -> instance.getEconomyHandler().hasAccount(uuid))
                    .map(uuid -> new TreasuryAccount(instance, uuid))
                    .map(PlayerAccount::getIdentifier)
                    .collect(Collectors.toList());
            subscription.succeed(identifiers);
        });
    }

    @Override
    public void retrieveNonPlayerAccountIds(@NotNull EconomySubscriber<Collection<String>> subscription) {
        subscription.succeed(Collections.emptyList());
    }

    @Override
    public @NotNull Currency getPrimaryCurrency() {
        return currency;
    }

    @Override
    public Optional<Currency> findCurrency(@NotNull String identifier) {
        return currency.getIdentifier().equals(CURRENCY_IDENTIFIER) ? Optional.of(currency) : Optional.empty();
    }

    @Override
    public Set<Currency> getCurrencies() {
        return Collections.singleton(currency);
    }

    @Override
    public void registerCurrency(@NotNull Currency currency, @NotNull EconomySubscriber<Boolean> subscription) {
        subscription.succeed(false);
    }
}
