package me.hsgamer.bettereconomy.handler;

import me.hsgamer.bettereconomy.BetterEconomy;
import me.hsgamer.hscore.config.gson.GsonConfig;

import java.io.File;

public class JsonEconomyHandler extends FlatFileEconomyHandler {
    public JsonEconomyHandler(BetterEconomy instance) {
        super(instance, new GsonConfig(new File(instance.getDataFolder(), "balances.json")));
    }
}
