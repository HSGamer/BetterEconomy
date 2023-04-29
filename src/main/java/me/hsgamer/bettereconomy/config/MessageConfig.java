package me.hsgamer.bettereconomy.config;

import me.hsgamer.hscore.config.annotation.ConfigPath;

public interface MessageConfig {
    @ConfigPath("prefix")
    default String getPrefix() {
        return "&f[&6BetterEconomy&f] &r";
    }

    @ConfigPath("player-not-found")
    default String getPlayerNotFound() {
        return "&cThe player is not found";
    }

    @ConfigPath("player-only")
    default String getPlayerOnly() {
        return "&cThis command is for player only";
    }

    @ConfigPath("empty-player-selector")
    default String getEmptyPlayerSelector() {
        return "&cNo player is selected";
    }

    @ConfigPath("balance-output")
    default String getBalanceOutput() {
        return "&eBalance: &f{balance}";
    }

    @ConfigPath("balance-top-output")
    default String getBalanceTopOutput() {
        return "&f#{place} &e{name}: &f{balance}";
    }

    @ConfigPath("empty-balance-top")
    default String getEmptyBalanceTop() {
        return "&eThe balance top is empty";
    }

    @ConfigPath("invalid-amount")
    default String getInvalidAmount() {
        return "&cInvalid Amount";
    }

    @ConfigPath("give-success")
    default String getGiveSuccess() {
        return "&aSuccessfully give {balance} to {name}";
    }

    @ConfigPath("receive")
    default String getReceive() {
        return "&aYou received {balance} from {name}";
    }

    @ConfigPath("take-success")
    default String getTakeSuccess() {
        return "&aSuccessfully take {balance} from {name}";
    }

    @ConfigPath("set-success")
    default String getSetSuccess() {
        return "&aSuccessfully set {balance} to {name}";
    }

    @ConfigPath("give-fail")
    default String getGiveFail() {
        return "&cFailed to give {balance} to {name}";
    }

    @ConfigPath("take-fail")
    default String getTakeFail() {
        return "&cFailed to take {balance} from {name}";
    }

    @ConfigPath("set-fail")
    default String getSetFail() {
        return "&cFailed to set {balance} to {name}";
    }

    @ConfigPath("success")
    default String getSuccess() {
        return "&aSuccess";
    }

    @ConfigPath("cannot-do")
    default String getCannotDo() {
        return "&cCannot do this action";
    }

    void reloadConfig();
}
