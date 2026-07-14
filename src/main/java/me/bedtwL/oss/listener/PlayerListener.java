package me.bedtwL.oss.listener;

import me.bedtwL.oss.PureSMP;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wither;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataType;

public class PlayerListener implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (!player.getPersistentDataContainer().has(PureSMP.initializedKey, PersistentDataType.BOOLEAN)) {
            PureSMP.instance.getServer().recipeIterator().forEachRemaining(recipe -> {
                NamespacedKey recipeKey = recipe.getResult().getType().getKey();
                if (!player.hasDiscoveredRecipe(recipeKey)) {
                    player.discoverRecipe(recipeKey);
                }
            });
            player.getPersistentDataContainer().set(PureSMP.initializedKey, PersistentDataType.BOOLEAN, true);
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        Player victim = e.getEntity();
        World world = victim.getLocation().getWorld();
        Entity killer = victim.getKiller();
        if (killer != null && victim.getLastDamageCause() != null) {
            killer = victim.getLastDamageCause().getDamageSource().getCausingEntity();
        }
        if (!e.getKeepInventory()) {
            if (killer != null) {
                if (killer instanceof Player || killer instanceof EnderDragon || killer instanceof Wither) {
                    e.getDrops().add(getPlayerHead(victim));
                    return;
                }
            }
            if (world != null && !world.getName().toLowerCase().endsWith("nether")) {
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
        skull.setItemMeta(meta);
        return skull;
    }
}
