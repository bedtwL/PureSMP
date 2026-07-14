package me.bedtwL.oss;

import me.bedtwL.oss.listener.PlayerListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.filter.AbstractFilter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

public final class PureSMP extends JavaPlugin {
    public static NamespacedKey initializedKey;
    public static PureSMP instance;

    @Override
    public void onLoad() {
        ((Logger) LogManager.getRootLogger()).addFilter(new AbstractFilter() {
            @Override
            public Result filter(LogEvent event) {
                if (event == null || event.getMessage() == null) return Result.NEUTRAL;
                String msg = event.getMessage().getFormattedMessage();
                if (msg != null && msg.startsWith("Ignored advancement '") && msg.contains("doesn't exist anymore")) {
                    return Result.DENY;
                }
                return Result.NEUTRAL;
            }
        });
    }
    @Override
    public void onEnable() {
        instance = this;
        initializedKey = new NamespacedKey(this, "recipe_initialized");
        getServer().getPluginManager().registerEvents(new PlayerListener(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
