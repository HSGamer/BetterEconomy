package me.hsgamer.bettereconomy.hook.treasury;

import me.hsgamer.bettereconomy.BetterEconomy;
import me.hsgamer.bettereconomy.Utils;
import me.lokka30.treasury.api.economy.account.PlayerAccount;
import me.lokka30.treasury.api.economy.currency.Currency;
import me.lokka30.treasury.api.economy.response.EconomyException;
import me.lokka30.treasury.api.economy.response.EconomyFailureReason;
import me.lokka30.treasury.api.economy.response.EconomySubscriber;
import me.lokka30.treasury.api.economy.transaction.EconomyTransaction;
import me.lokka30.treasury.api.economy.transaction.EconomyTransactionInitiator;
import me.lokka30.treasury.api.economy.transaction.EconomyTransactionType;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.time.temporal.Temporal;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

public class TreasuryAccount implements PlayerAccount {
    private final BetterEconomy instance;
    private final UUID uuid;

    public TreasuryAccount(BetterEconomy instance, UUID uuid) {
        this.instance = instance;
        this.uuid = uuid;
    }

    @Override
    public @NotNull UUID getUniqueId() {
        return uuid;
    }

    @Override
    public Optional<String> getName() {
        return Optional.ofNullable(Bukkit.getOfflinePlayer(uuid).getName());
    }

    @Override
    public void retrieveBalance(@NotNull Currency currency, @NotNull EconomySubscriber<BigDecimal> subscription) {
        if (!currency.getIdentifier().equals(TreasuryEconomyHook.CURRENCY_IDENTIFIER)) {
            subscription.fail(new EconomyException(EconomyFailureReason.CURRENCY_NOT_FOUND));
        } else {
            Utils.schedule(() -> {
                double amount = instance.getEconomyHandler().get(uuid);
                subscription.succeed(BigDecimal.valueOf(amount));
            });
        }
    }

    @Override
    public void setBalance(@NotNull BigDecimal amount, @NotNull EconomyTransactionInitiator<?> initiator, @NotNull Currency currency, @NotNull EconomySubscriber<BigDecimal> subscription) {
        if (!currency.getIdentifier().equals(TreasuryEconomyHook.CURRENCY_IDENTIFIER)) {
            subscription.fail(new EconomyException(EconomyFailureReason.CURRENCY_NOT_FOUND));
        } else {
            Utils.schedule(() -> {
                double amountDouble = amount.doubleValue();
                boolean status = instance.getEconomyHandler().set(uuid, amountDouble);
                if (!status) {
                    subscription.fail(new EconomyException(EconomyFailureReason.NEGATIVE_AMOUNT_SPECIFIED));
                } else {
                    subscription.succeed(BigDecimal.valueOf(amountDouble));
                }
            });
        }
    }

    @Override
    public void doTransaction(@NotNull EconomyTransaction economyTransaction, EconomySubscriber<BigDecimal> subscription) {
        Utils.schedule(() -> {
            if (!economyTransaction.getCurrencyID().equals(TreasuryEconomyHook.CURRENCY_IDENTIFIER)) {
                subscription.fail(new EconomyException(EconomyFailureReason.CURRENCY_NOT_FOUND));
                return;
            }
            EconomyTransactionType type = economyTransaction.getTransactionType();
            BigDecimal amount = economyTransaction.getTransactionAmount();
            double amountDouble = amount.doubleValue();
            if (amountDouble < 0) {
                subscription.fail(new EconomyException(EconomyFailureReason.NEGATIVE_AMOUNT_SPECIFIED));
                return;
            }
            boolean status = false;
            if (type == EconomyTransactionType.DEPOSIT) {
                status = instance.getEconomyHandler().deposit(uuid, amountDouble);
            } else if (type == EconomyTransactionType.WITHDRAWAL) {
                status = instance.getEconomyHandler().withdraw(uuid, amountDouble);
            }
            if (!status) {
                subscription.fail(new EconomyException(EconomyFailureReason.NEGATIVE_AMOUNT_SPECIFIED));
            } else {
                double balance = instance.getEconomyHandler().get(uuid);
                subscription.succeed(BigDecimal.valueOf(balance));
            }
        });
    }

    @Override
    public void deleteAccount(@NotNull EconomySubscriber<Boolean> subscription) {
        Utils.schedule(() -> {
            boolean status = instance.getEconomyHandler().deleteAccount(uuid);
            subscription.succeed(status);
        });
    }

    @Override
    public void retrieveHeldCurrencies(@NotNull EconomySubscriber<Collection<String>> subscription) {
        subscription.succeed(Collections.singletonList(TreasuryEconomyHook.CURRENCY_IDENTIFIER));
    }

    @Override
    public void retrieveTransactionHistory(int transactionCount, @NotNull Temporal from, @NotNull Temporal to, @NotNull EconomySubscriber<Collection<EconomyTransaction>> subscription) {
        subscription.fail(new EconomyException(EconomyFailureReason.FEATURE_NOT_SUPPORTED));
    }
}
