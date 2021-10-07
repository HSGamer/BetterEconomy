package me.hsgamer.bettereconomy;

import org.bukkit.Bukkit;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

public final class Permissions {
    public static final Permission BALANCE = new Permission("bettereconomy.balance", PermissionDefault.TRUE);
    public static final Permission BALANCE_OTHERS = new Permission("bettereconomy.balance.others", PermissionDefault.TRUE);
    public static final Permission BALANCE_TOP = new Permission("bettereconomy.balancetop", PermissionDefault.TRUE);
    public static final Permission HELP = new Permission("bettereconomy.help", PermissionDefault.TRUE);
    public static final Permission PAY = new Permission("bettereconomy.pay", PermissionDefault.TRUE);
    public static final Permission SET = new Permission("bettereconomy.set", PermissionDefault.OP);
    public static final Permission RELOAD = new Permission("bettereconomy.pay", PermissionDefault.OP);

    static {
        addPermission(BALANCE);
        addPermission(BALANCE_OTHERS);
        addPermission(BALANCE_TOP);
        addPermission(HELP);
        addPermission(PAY);
        addPermission(SET);
        addPermission(RELOAD);
    }

    private Permissions() {
        // EMPTY
    }

    private static void addPermission(Permission permission) {
        Bukkit.getPluginManager().addPermission(permission);
    }
}
