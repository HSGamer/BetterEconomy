package me.hsgamer.bettereconomy;

import lombok.experimental.UtilityClass;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

@UtilityClass
public final class Permissions {
    public static final Permission BALANCE = new Permission("bettereconomy.balance", PermissionDefault.TRUE);
    public static final Permission BALANCE_OTHERS = new Permission("bettereconomy.balance.others", PermissionDefault.TRUE);
    public static final Permission BALANCE_TOP = new Permission("bettereconomy.balancetop", PermissionDefault.TRUE);
    public static final Permission PAY = new Permission("bettereconomy.pay", PermissionDefault.TRUE);
    public static final Permission ADMIN = new Permission("bettereconomy.set", PermissionDefault.OP);
    public static final Permission RELOAD = new Permission("bettereconomy.reload", PermissionDefault.OP);
}
