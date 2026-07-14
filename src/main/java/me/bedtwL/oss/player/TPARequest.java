package me.bedtwL.oss.player;

import me.bedtwL.oss.PureSMP;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

public class TPARequest {
    public final UUID from;
    public final UUID to;
    public final String fromName;
    public final String toName;
    private final BukkitRunnable runnable;
    public final boolean tpa;
    public boolean expired = false;

    public TPARequest(UUID from, UUID uuid, BukkitRunnable runnable, String fromName, String toName, boolean tpa) {
        this.from = from;
        this.to = uuid;
        this.fromName = fromName;
        this.toName = toName;
        this.tpa = tpa;
        this.runnable = new BukkitRunnable() {
            @Override
            public void run() {
                expire();
            }
        };
    }

    public void start() {
        this.runnable.runTaskLater(PureSMP.instance, 20 * 60L);
    }

    public void expire() {
        Player from = Bukkit.getPlayer(this.from);
        Player to = Bukkit.getPlayer(this.to);
        if (from != null) from.sendMessage(ChatColor.RED + "對 " + toName + " 的" + (tpa ? "TPA" : "TPAHERE") + "請求已過期!");
        if (to != null) to.sendMessage(ChatColor.RED + "來自 " + fromName + " 的" + (tpa ? "TPA" : "TPAHERE") + "請求已過期!");
        this.expired = true;
        if (runnable != null && !runnable.isCancelled()) runnable.cancel();
    }

}
