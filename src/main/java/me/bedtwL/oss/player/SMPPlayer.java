package me.bedtwL.oss.player;

import me.bedtwL.oss.PureSMP;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

public class SMPPlayer {
    public final Player player;
    public final UUID uuid;

    public SMPPlayer(Player player) {
        this.player = player;
        this.uuid = player.getUniqueId();
    }

    public void teleport(Location location) {
        final int[] i = {5};
        new BukkitRunnable() {
            @Override
            public void run() {
                if (i[0] <= 0) {
                    player.teleport(location);
                    cancel();
                }
                player.sendActionBar(Component.text("在" + i[0] + "後傳送...").color(NamedTextColor.GREEN));
                i[0]--;
            }
        }.runTaskTimer(PureSMP.instance, 0, 20);
    }
}
