package net.politwineteamrocketl.process;

import net.politwineteamrocketl.PolitWineTeamRocketl;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FireworkExplodeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.FireworkEffect;
import org.bukkit.entity.Firework;
import org.bukkit.util.Vector;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

public class Fire implements Listener {
    private static Set<UUID> activeFireworks = new HashSet<>();
    private final PolitWineTeamRocketl plugin;
    public Fire(PolitWineTeamRocketl plugin) {
        this.plugin = plugin;
    }

    //Do main shoot process | Выполнение процесса выстрела

    public static void Shoot(Player player, ItemStack item) {
        Integer value = Reload.getCustomMetadata(item);

        if (value == null) {
            player.sendActionBar("MetaData not found");
            Bukkit.getLogger().warning("MetaData not found for item: " + item.toString());
            return;
        }

        if (value == 0) {
            player.sendActionBar("Bandolier empty");
            return;
        } else if (value == 1) {
            Reload.setCustomMetadata(item, 0);
            player.sendActionBar("Cartridges: 0/4");
        } else if (value == 2) {
            Reload.setCustomMetadata(item, 1);
            player.sendActionBar("Cartridges: 1/4");
        } else if (value == 3) {
            Reload.setCustomMetadata(item, 2);
            player.sendActionBar("Cartridges: 2/4");
        } else if (value == 4) {
            Reload.setCustomMetadata(item, 3);
            player.sendActionBar("Cartridges: 3/4");
        }
        String soundName = "shoot";
        Sound.playSoundInRadiusf(player.getLocation(), 12, soundName);
        launchFirework(player);
    }

    //Launch custom firework | Запуск кастомного феерверка

    private static void launchFirework(Player player) {
        FireworkEffect effect;
        Location loc = player.getEyeLocation();
        Firework firework = (Firework) loc.getWorld().spawnEntity(loc, EntityType.FIREWORK);
        FireworkMeta meta = firework.getFireworkMeta();

        if (Switch.PlayerSwitch.get(player) == null) {
            effect = FireworkEffect.builder()
                    .withColor(Color.WHITE)
                    .flicker(false)
                    .build();
        } else if (Switch.PlayerSwitch.get(player) == 0) {
            effect = FireworkEffect.builder()
                    .withColor(Color.BLUE)
                    .with(FireworkEffect.Type.STAR)
                    .flicker(true)
                    .build();
        } else if (Switch.PlayerSwitch.get(player) == 1) {
            effect = FireworkEffect.builder()
                    .withColor(Color.RED)
                    .with(FireworkEffect.Type.BALL_LARGE)
                    .flicker(true)
                    .build();
        }  else {
            effect = FireworkEffect.builder()
                    .withColor(Color.WHITE)
                    .flicker(false)
                    .build();
        }

        meta.addEffect(effect);
        meta.setPower(1);
        firework.setFireworkMeta(meta);

        Vector direction = loc.getDirection().normalize().multiply(1.5);
        firework.setVelocity(direction);

        activeFireworks.add(firework.getUniqueId());
    }

    //Explosion firework and player action | Взрыв феерверка и выполнения действия с игроком

    private void bouncePlayer(Player player) {
        Location loc = player.getLocation();
        Vector direction = loc.getDirection().normalize().multiply(-1);
        player.setVelocity(direction.multiply(1.5));
    }

    @EventHandler
    public void onFireworkExplode(FireworkExplodeEvent event) {
        if (activeFireworks.contains(event.getEntity().getUniqueId())) {
            Location explosionLocation = event.getEntity().getLocation();
            for (Player player : explosionLocation.getWorld().getPlayers()) {
                if (player.getLocation().distance(explosionLocation) <= 4) {
                    bouncePlayer(player);
                }
            }
            activeFireworks.remove(event.getEntity().getUniqueId());
            String soundName = "explosion1";
            Random random = new Random();
            int randomn = random.nextInt(3);
            if (randomn == 0) {
                soundName = "explosion1";
            } else if (randomn == 1) {
                soundName = "explosion2";
            } else if (randomn == 2) {
                soundName = "explosion3";
            } else {
                soundName = "explosion3";
            }
            Sound.playSoundInRadiusf(explosionLocation, 40, soundName);
        }
    }
}
