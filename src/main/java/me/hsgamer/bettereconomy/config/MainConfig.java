package me.hsgamer.bettereconomy.config;

import lombok.Getter;
import me.hsgamer.hscore.bukkit.config.BukkitConfig;
import me.hsgamer.hscore.config.AnnotatedConfig;
import me.hsgamer.hscore.config.annotation.ConfigPath;
import org.bukkit.plugin.Plugin;

@Getter
public class MainConfig extends AnnotatedConfig {
    private @ConfigPath("handler-type") String handlerType = "file";
    private @ConfigPath("currency.singular") String currencySingular = "$";
    private @ConfigPath("currency.plural") String currencyPlural = "$";
    private @ConfigPath("currency.format-fractional-digits") int fractionalDigits = 2;
    private @ConfigPath("balance.top-update-period") long updateBalanceTopPeriod = 100;
    private @ConfigPath("balance.file-save-period") long saveFilePeriod = 200;
    private @ConfigPath("balance.start-amount") double startAmount = 0;

    public MainConfig(Plugin plugin) {
        super(new BukkitConfig(plugin, "config.yml"));
    }
}
