package me.hsgamer.bettereconomy.hook.treasury;

import me.lokka30.treasury.api.common.response.FailureReason;
import org.jetbrains.annotations.NotNull;

public enum FailureReasons implements FailureReason {
    INVALID_VALUE("Invalid value inputted!"),
    INVALID_CURRENCY("Invalid currency inputted!");

    private final String description;

    FailureReasons(String description) {
        this.description = description;
    }

    @Override
    public @NotNull String getDescription() {
        return description;
    }
}
