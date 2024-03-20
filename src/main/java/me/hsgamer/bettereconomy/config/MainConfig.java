package me.hsgamer.bettereconomy.config;

import me.hsgamer.hscore.config.annotation.ConfigPath;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Collections;
import java.util.Map;

public interface MainConfig {
    @ConfigPath("handler-type")
    default String getHandlerType() {
        return "file";
    }

    @ConfigPath({"currency", "singular"})
    default String getCurrencySingular() {
        return "$";
    }

    @ConfigPath({"currency", "plural"})
    default String getCurrencyPlural() {
        return "$";
    }

    @ConfigPath({"currency", "symbol"})
    default String getCurrencySymbol() {
        return "$";
    }

    @ConfigPath({"currency", "format-fractional-digits"})
    default int getFractionalDigits() {
        return 2;
    }

    @ConfigPath({"currency", "decimal-point"})
    default String getDecimalPoint() {
        return ".";
    }

    @ConfigPath({"currency", "use-thousands-separator"})
    default boolean isUseThousandsSeparator() {
        return true;
    }

    @ConfigPath({"currency", "thousands-separator"})
    default String getThousandsSeparator() {
        return ",";
    }

    @ConfigPath({"balance", "top-update-period"})
    default int getUpdateBalanceTopPeriod() {
        return 100;
    }

    @ConfigPath({"balance", "file-save-period"})
    default int getSaveFilePeriod() {
        return 200;
    }

    @ConfigPath({"balance", "start-amount"})
    default double getStartAmount() {
        return 0;
    }

    @ConfigPath({"balance", "min-amount"})
    default double getMinimumAmount() {
        return 0;
    }

    @ConfigPath({"database", "mysql", "host"})
    default String getMysqlHost() {
        return "localhost";
    }

    @ConfigPath({"database", "mysql", "port"})
    default String getMysqlPort() {
        return "3306";
    }

    @ConfigPath({"database", "mysql", "dbname"})
    default String getMysqlDatabaseName() {
        return "";
    }

    @ConfigPath({"database", "mysql", "username"})
    default String getMysqlUsername() {
        return "root";
    }

    @ConfigPath({"database", "mysql", "password"})
    default String getMysqlPassword() {
        return "";
    }

    @ConfigPath({"database", "sqlite", "dbname"})
    default String getSqliteDatabaseName() {
        return "balances";
    }

    @ConfigPath({"database", "common", "client-settings"})
    default Map<String, Object> getDatabaseClientSettings() {
        return Collections.emptyMap();
    }

    @ConfigPath({"database", "common", "driver-settings"})
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

    default char getActualDecimalPoint() {
        String point = getDecimalPoint().trim();
        return point.isEmpty() ? '.' : point.charAt(0);
    }

    default char getActualThousandsSeparator() {
        String separator = getThousandsSeparator().trim();
        return separator.isEmpty() ? ',' : separator.charAt(0);
    }
}
