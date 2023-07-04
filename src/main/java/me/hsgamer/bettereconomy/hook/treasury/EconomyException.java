package me.hsgamer.bettereconomy.hook.treasury;

public class EconomyException extends RuntimeException {
    private final FailureReasons failureReason;

    public EconomyException(FailureReasons failureReason) {
        super(failureReason.getDescription());
        this.failureReason = failureReason;
    }

    public EconomyException(FailureReasons failureReason, Throwable cause) {
        super(failureReason.getDescription(), cause);
        this.failureReason = failureReason;
    }

    public FailureReasons getFailureReason() {
        return failureReason;
    }
}
