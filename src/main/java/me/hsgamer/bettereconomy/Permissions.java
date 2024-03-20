package me.hsgamer.bettereconomy;

import io.github.projectunified.minelib.plugin.base.BasePlugin;
import io.github.projectunified.minelib.plugin.permission.PermissionComponent;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

public final class Permissions extends PermissionComponent {
    public static final Permission BALANCE = new Permission("bettereconomy.balance", PermissionDefault.TRUE);
    public static final Permission BALANCE_OTHERS = new Permission("bettereconomy.balance.others", PermissionDefault.TRUE);
    public static final Permission BALANCE_TOP = new Permission("bettereconomy.balancetop", PermissionDefault.TRUE);
    public static final Permission PAY = new Permission("bettereconomy.pay", PermissionDefault.TRUE);
    public static final Permission ADMIN = new Permission("bettereconomy.set", PermissionDefault.OP);
    public static final Permission RELOAD = new Permission("bettereconomy.reload", PermissionDefault.OP);

    public Permissions(BasePlugin plugin) {
        super(plugin);
    }
}
