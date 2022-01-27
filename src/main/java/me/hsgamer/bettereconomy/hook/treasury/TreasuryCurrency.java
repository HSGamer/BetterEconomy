package me.hsgamer.bettereconomy.hook.treasury;

import me.hsgamer.bettereconomy.BetterEconomy;
import me.lokka30.treasury.api.economy.currency.Currency;
import me.lokka30.treasury.api.economy.response.EconomyException;
import me.lokka30.treasury.api.economy.response.EconomyFailureReason;
import me.lokka30.treasury.api.economy.response.EconomySubscriber;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.util.Locale;
import java.util.UUID;

public class TreasuryCurrency implements Currency {
    private final BetterEconomy instance;

    public TreasuryCurrency(BetterEconomy instance) {
        this.instance = instance;
    }

    @Override
    public @NotNull String getIdentifier() {
        return TreasuryEconomyHook.CURRENCY_IDENTIFIER;
    }

    @Override
    public @NotNull String getSymbol() {
        return instance.getMainConfig().getCurrencySymbol();
    }

    @Override
    public char getDecimal() {
        String symbol = getSymbol();
        return symbol.isEmpty() ? '$' : symbol.charAt(0);
    }

    @Override
    public @NotNull String getDisplayNameSingular() {
        return instance.getMainConfig().getCurrencySingular();
    }

    @Override
    public @NotNull String getDisplayNamePlural() {
        return instance.getMainConfig().getCurrencyPlural();
    }

    @Override
    public int getPrecision() {
        return instance.getMainConfig().getFractionalDigits();
    }

    @Override
    public boolean isPrimary() {
        return true;
    }

    @Override
    public void to(@NotNull Currency currency, @NotNull BigDecimal amount, @NotNull EconomySubscriber<BigDecimal> subscription) {
        // There is only one currency here
        subscription.fail(new EconomyException(EconomyFailureReason.FEATURE_NOT_SUPPORTED));
    }

    @Override
    public void parse(@NotNull String formatted, @NotNull EconomySubscriber<BigDecimal> subscription) {
        // TODO: A way to do this?
        subscription.fail(new EconomyException(EconomyFailureReason.FEATURE_NOT_SUPPORTED));
    }

    @Override
    public @NotNull BigDecimal getStartingBalance(@Nullable UUID playerID) {
        return BigDecimal.valueOf(instance.getMainConfig().getStartAmount());
    }

    @Override
    public @NotNull String format(@NotNull BigDecimal amount, @Nullable Locale locale) {
        return instance.getMainConfig().format(amount);
    }

    @Override
    public @NotNull String format(@NotNull BigDecimal amount, @Nullable Locale locale, int precision) {
        return instance.getMainConfig().format(amount, precision);
    }
}
