package me.hsgamer.bettereconomy.hook.treasury;

import me.lokka30.treasury.api.common.misc.FutureHelper;
import me.lokka30.treasury.api.economy.account.NonPlayerAccount;
import me.lokka30.treasury.api.economy.account.accessor.NonPlayerAccountAccessor;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class TreasuryNonPlayerAccountAccessor extends NonPlayerAccountAccessor {
    @Override
    protected @NotNull CompletableFuture<NonPlayerAccount> getOrCreate(@NotNull NonPlayerAccountCreateContext context) {
        return FutureHelper.failedFuture(FailureReasons.FEATURE_NOT_SUPPORTED.toException());
    }
}
