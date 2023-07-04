package me.hsgamer.bettereconomy.hook.treasury;

import org.jetbrains.annotations.NotNull;

public enum FailureReasons {
    INVALID_VALUE("Invalid value inputted!"),
    INVALID_CURRENCY("Invalid currency inputted!"),
    NEGATIVE_BALANCES_NOT_SUPPORTED("Negative balances are not supported!"),
    FEATURE_NOT_SUPPORTED("Unsupported feature!"),
    CURRENCY_NOT_FOUND("Currency not found!");

    private final String description;

    FailureReasons(String description) {
        this.description = description;
    }

    public @NotNull String getDescription() {
        return description;
    }

    public EconomyException toException() {
        return new EconomyException(this);
    }

    public EconomyException toException(Throwable cause) {
        return new EconomyException(this, cause);
    }
}
