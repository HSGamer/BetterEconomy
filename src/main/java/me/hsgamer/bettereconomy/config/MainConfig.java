package me.hsgamer.bettereconomy.config;

import lombok.Getter;
import me.hsgamer.hscore.bukkit.config.BukkitConfig;
import me.hsgamer.hscore.config.AnnotatedConfig;
import me.hsgamer.hscore.config.annotation.ConfigPath;
import org.bukkit.plugin.Plugin;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

@Getter
@SuppressWarnings("all")
public class MainConfig extends AnnotatedConfig {
    private @ConfigPath("metrics")
    boolean metrics = true;
    private @ConfigPath("handler-type")
    String handlerType = "file";
    private @ConfigPath("hook-enabled")
    boolean hookEnabled = true;
    private @ConfigPath("currency.singular")
    String currencySingular = "$";
    private @ConfigPath("currency.plural")
    String currencyPlural = "$";
    private @ConfigPath("currency.symbol")
    String currencySymbol = "$";
    private @ConfigPath("currency.format-fractional-digits")
    int fractionalDigits = 2;
    private @ConfigPath("currency.decimal-point")
    String decimalPoint = ".";
    private @ConfigPath("currency.use-thousands-separator")
    boolean useThousandsSeparator = true;
    private @ConfigPath("currency.thousands-separator")
    String thousandsSeparator = ",";
    private @ConfigPath("balance.top-update-period")
    long updateBalanceTopPeriod = 100;
    private @ConfigPath("balance.file-save-period")
    long saveFilePeriod = 200;
    private @ConfigPath("balance.start-amount")
    double startAmount = 0;

    private @ConfigPath("database.mysql.host")
    String mysqlHost = "localhost";
    private @ConfigPath("database.mysql.port")
    String mysqlPort = "3306";
    private @ConfigPath("database.mysql.dbname")
    String mysqlDatabaseName = "";
    private @ConfigPath("database.mysql.username")
    String mysqlUsername = "root";
    private @ConfigPath("database.mysql.password")
    String mysqlPassword = "";

    private @ConfigPath("database.sqlite.dbname")
    String sqliteDatabaseName = "balances";

    public MainConfig(Plugin plugin) {
        super(new BukkitConfig(plugin, "config.yml"));
    }

    public String format(double amount) {
        return format(BigDecimal.valueOf(amount), fractionalDigits);
    }

    public String format(BigDecimal amount) {
        return format(amount, fractionalDigits);
    }

    public String format(BigDecimal amount, int scale) {
        DecimalFormat format = new DecimalFormat();
        format.setRoundingMode(RoundingMode.HALF_EVEN);
        format.setGroupingUsed(useThousandsSeparator);
        format.setMinimumFractionDigits(0);
        format.setMaximumFractionDigits(scale);
        format.setDecimalFormatSymbols(new DecimalFormatSymbols() {
            {
                setDecimalSeparator(getDecimalPoint());
                setGroupingSeparator(getThousandsSeparator());
            }
        });
        return format.format(amount);
    }

    public char getDecimalPoint() {
        String point = decimalPoint.trim();
        return point.isEmpty() ? '.' : point.charAt(0);
    }

    public char getThousandsSeparator() {
        String separator = thousandsSeparator.trim();
        return separator.isEmpty() ? ',' : separator.charAt(0);
    }
}
