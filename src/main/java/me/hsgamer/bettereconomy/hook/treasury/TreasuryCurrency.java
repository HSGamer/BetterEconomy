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
        return instance.getMainConfig().getActualDecimalPoint();
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
        StringBuilder valueBuilder = new StringBuilder();
        StringBuilder currencyBuilder = new StringBuilder();

        boolean hadDot = false;
        for (char c : formatted.toCharArray()) {
            if (Character.isWhitespace(c)) {
                continue;
            }

            if (!Character.isDigit(c) && !isSeparator(c)) {
                currencyBuilder.append(c);
            } else if (Character.isDigit(c)) {
                valueBuilder.append(c);
            } else if (isSeparator(c)) {
                if (c == getDecimal()) {
                    boolean nowChanged = false;
                    if (!hadDot) {
                        hadDot = true;
                        nowChanged = true;
                    }

                    if (!nowChanged) {
                        valueBuilder = new StringBuilder();
                        break;
                    }
                }
                valueBuilder.append('.');
            }
        }

        if (currencyBuilder.length() == 0) {
            subscription.fail(new EconomyException(FailureReasons.INVALID_CURRENCY));
            return;
        }

        String currency = currencyBuilder.toString();
        if (!matches(currency)) {
            subscription.fail(new EconomyException(FailureReasons.INVALID_CURRENCY));
            return;
        }

        if (valueBuilder.length() == 0) {
            subscription.fail(new EconomyException(FailureReasons.INVALID_VALUE));
            return;
        }

        try {
            double value = Double.parseDouble(valueBuilder.toString());
            if (value < 0) {
                subscription.fail(new EconomyException(EconomyFailureReason.NEGATIVE_BALANCES_NOT_SUPPORTED));
                return;
            }

            subscription.succeed(BigDecimal.valueOf(value));
        } catch (NumberFormatException e) {
            subscription.fail(new EconomyException(FailureReasons.INVALID_VALUE, e));
        }
    }

    private boolean matches(String currency) {
        if (currency.length() == 1) {
            return currency.charAt(0) == getDecimal();
        } else {
            return currency.equalsIgnoreCase(getSymbol())
                    || currency.equalsIgnoreCase(getDisplayNameSingular())
                    || currency.equalsIgnoreCase(getDisplayNamePlural());
        }
    }

    private boolean isSeparator(char c) {
        return c == getDecimal() || c == instance.getMainConfig().getActualThousandsSeparator();
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
