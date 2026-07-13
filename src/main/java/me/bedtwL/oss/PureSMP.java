package me.bedtwL.oss;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.java.JavaPlugin;

public final class PureSMP extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        // Plugin startup logic
        getServer().getPluginManager().registerEvents(this,this);
    }
    @EventHandler
    public void onMobAttack(EntityDamageByEntityEvent e) {
        Entity damager = e.getDamager();
        if (damager.getLocation().getWorld().toString().endsWith("nether")||damager.getLocation().getWorld().toString().endsWith("end")) {
            if (damager instanceof Projectile projectile) {
                if (projectile.getShooter() instanceof Creature) {
                    double originalDamage = e.getDamage();
                    e.setDamage(originalDamage * 3);
                    return;
                }
            }
            if (damager instanceof Creature) {
                if (!(e.getEntity() instanceof Player)) return;
                double originalDamage = e.getDamage();
                e.setDamage(originalDamage * 3);
            }
        }
    }
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        Player victim=e.getEntity();
        Entity killer=victim.getLastDamageCause().getDamageSource().getCausingEntity();
        if (!e.getKeepInventory()) {
            World world=victim.getLocation().getWorld();
            if (killer !=null) {
                if (killer instanceof Player||killer instanceof EnderDragon||killer instanceof EnderCrystal||killer instanceof Wither) {
                    e.getDrops().add(getPlayerHead(victim));
                    return;
                }
            }
            if (!world.toString().endsWith("nether")) {
                e.setKeepInventory(true);
                e.setDroppedExp(0);
                e.getDrops().clear();
            }
        }
    }
    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        if (e == null) return;
        Player p = e.getPlayer();
        ItemStack inHand = e.getItem();
        if (e.getAction() == Action.RIGHT_CLICK_AIR) {
            if (inHand == null) return;
            switch (inHand.getType()) {
                case CRAFTING_TABLE:
                    e.setCancelled(true);
                    p.openWorkbench(null, true);
                    break;
                case ENDER_CHEST:
                    e.setCancelled(true);
                    p.openInventory(p.getEnderChest());
                    break;
                case SMITHING_TABLE:
                    e.setCancelled(true);
                    p.openInventory(Bukkit.createInventory(null, InventoryType.SMITHING));
                    break;
            }
        }
    }
    public ItemStack getPlayerHead(Player player) {
        ItemStack skull = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) skull.getItemMeta();
        meta.setOwningPlayer(player);
        meta.setDisplayName("§r§6"+player.getName());
        skull.setItemMeta(meta);
        return skull;
    }
    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
