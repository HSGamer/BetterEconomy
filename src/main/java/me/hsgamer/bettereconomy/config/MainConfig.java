package me.hsgamer.bettereconomy.config;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Collections;
import java.util.Map;

import me.hsgamer.hscore.config.annotation.ConfigPath;

public interface MainConfig {
    @ConfigPath(value = "handler-type")
    default String getHandlerType() {
        return "file";
    }

    @ConfigPath(value = { "currency", "singular" }, priority = 1)
    default String getCurrencySingular() {
        return "$";
    }

    @ConfigPath(value = { "currency", "plural" }, priority = 1)
    default String getCurrencyPlural() {
        return "$";
    }

    @ConfigPath(value = { "currency", "symbol" }, priority = 1)
    default String getCurrencySymbol() {
        return "$";
    }

    @ConfigPath(value = { "currency", "format-fractional-digits" }, priority = 1)
    default int getFractionalDigits() {
        return 2;
    }

    @ConfigPath(value = { "currency", "decimal-point" }, priority = 1)
    default String getDecimalPoint() {
        return ".";
    }

    @ConfigPath(value = { "currency", "use-thousands-separator" }, priority = 1)
    default boolean isUseThousandsSeparator() {
        return true;
    }

    @ConfigPath(value = { "currency", "thousands-separator" }, priority = 1)
    default String getThousandsSeparator() {
        return ",";
    }

    @ConfigPath(value = { "balance", "top-update-period" }, priority = 2)
    default int getUpdateBalanceTopPeriod() {
        return 100;
    }

    @ConfigPath(value = { "balance", "file-save-period" }, priority = 2)
    default int getSaveFilePeriod() {
        return 200;
    }

    @ConfigPath(value = { "balance", "start-amount" }, priority = 2)
    default double getStartAmount() {
        return 0;
    }

    @ConfigPath(value = { "balance", "min-amount" }, priority = 2)
    default double getMinimumAmount() {
        return 0;
    }

    @ConfigPath(value = { "database", "mysql", "host" }, priority = 3)
    default String getMysqlHost() {
        return "localhost";
    }

    @ConfigPath(value = { "database", "mysql", "port" }, priority = 3)
    default String getMysqlPort() {
        return "3306";
    }

    @ConfigPath(value = { "database", "mysql", "dbname" }, priority = 3)
    default String getMysqlDatabaseName() {
        return "";
    }

    @ConfigPath(value = { "database", "mysql", "username" }, priority = 3)
    default String getMysqlUsername() {
        return "root";
    }

    @ConfigPath(value = { "database", "mysql", "password" }, priority = 3)
    default String getMysqlPassword() {
        return "";
    }

    @ConfigPath(value = { "database", "sqlite", "dbname" }, priority = 3)
    default String getSqliteDatabaseName() {
        return "balances";
    }

    @ConfigPath(value = { "database", "common", "client-settings" }, priority = 3)
    default Map<String, Object> getDatabaseClientSettings() {
        return Collections.emptyMap();
    }

    @ConfigPath(value = { "database", "common", "driver-settings" }, priority = 3)
    default Map<String, Object> getDatabaseDriverSettings() {
        return Collections.emptyMap();
    }

    void reloadConfig();

    default String format(double amount) {
        return format(BigDecimal.valueOf(amount), getFractionalDigits());
    }

    default String format(BigDecimal amount) {
        return format(amount, getFractionalDigits());
    }

    default String formatShort(double amount) {
        return formatShort(BigDecimal.valueOf(amount), getFractionalDigits());
    }

    default String format(BigDecimal amount, int scale) {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setDecimalSeparator(getActualDecimalPoint());
        symbols.setGroupingSeparator(getActualThousandsSeparator());

        DecimalFormat format = new DecimalFormat();
        format.setRoundingMode(RoundingMode.HALF_EVEN);
        format.setGroupingUsed(isUseThousandsSeparator());
        format.setMinimumFractionDigits(0);
        format.setMaximumFractionDigits(scale);
        format.setDecimalFormatSymbols(symbols);
        return format.format(amount);
    }

    default String formatShort(BigDecimal amount, int scale) {
        BigDecimal absAmount = amount.abs();
        BigDecimal divisor = BigDecimal.ONE;
        String suffix = "";

        if (absAmount.compareTo(BigDecimal.valueOf(1_000_000_000_000L)) >= 0) {
            divisor = BigDecimal.valueOf(1_000_000_000_000L);
            suffix = "T";
        } else if (absAmount.compareTo(BigDecimal.valueOf(1_000_000_000)) >= 0) {
            divisor = BigDecimal.valueOf(1_000_000_000);
            suffix = "B";
        } else if (absAmount.compareTo(BigDecimal.valueOf(1_000_000)) >= 0) {
            divisor = BigDecimal.valueOf(1_000_000);
            suffix = "M";
        } else if (absAmount.compareTo(BigDecimal.valueOf(1_000)) >= 0) {
            divisor = BigDecimal.valueOf(1_000);
            suffix = "k";
        }

        BigDecimal shortened = amount.divide(divisor, scale, RoundingMode.HALF_EVEN);

        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setDecimalSeparator('.');
        symbols.setGroupingSeparator(',');

        DecimalFormat format = new DecimalFormat();
        format.setDecimalFormatSymbols(symbols);
        format.setGroupingUsed(false);
        format.setMinimumFractionDigits(1);
        format.setMaximumFractionDigits(scale);
        format.setRoundingMode(RoundingMode.HALF_EVEN);

        return format.format(shortened) + suffix;
    }

    default char getActualDecimalPoint() {
        String point = getDecimalPoint().trim();
        return point.isEmpty() ? '.' : point.charAt(0);
    }

    default char getActualThousandsSeparator() {
        String separator = getThousandsSeparator().trim();
        return separator.isEmpty() ? ',' : separator.charAt(0);
    }
}
