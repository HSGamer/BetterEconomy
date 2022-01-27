package me.hsgamer.bettereconomy.hook.treasury;

import me.lokka30.treasury.api.common.response.FailureReason;

public enum FailureReasons implements FailureReason {
    INVALID_VALUE("Invalid value inputted!"),
    INVALID_CURRENCY("Invalid currency inputted!");

    private final String description;

    FailureReasons(String description) {
        this.description = description;
    }

    @Override
    public String getDescription() {
        return description;
    }
}
