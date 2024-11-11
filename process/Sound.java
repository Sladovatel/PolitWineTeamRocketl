package net.politwineteamrocketl.process;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class Sound {

    //Play sound from item | Воиспроизведение звука от предмета

    public static void playSoundInRadius(Location center, double radius, String soundName) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            Location playerLocation = center;
            if (playerLocation.distance(center) <= radius) {
                player.playSound(playerLocation, soundName, 1.0f, 1.0f);
            }
        }
    }

    //Play sound from the player | Воиспроизведение звука от игрока

    public static void playSoundInRadiusf(Location center, double radius, String soundName) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            Location playerLocation = player.getLocation();
            if (playerLocation.distance(center) <= radius) {
                player.playSound(center, soundName, 1.0f, 1.0f);
            }
        }
    }
}
