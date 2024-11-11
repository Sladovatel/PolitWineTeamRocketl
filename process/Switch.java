package net.politwineteamrocketl.process;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import java.util.HashMap;
import java.util.Map;

public class Switch {
    public static final Map<Player, Integer> PlayerSwitch = new HashMap<>();

    //Switch player firework color | Смена цвета феерверка для игрока

    public static void SwitchPlayerFire(Player player, Player target) {
        if (PlayerSwitch.get(player) == null) {
            PlayerSwitch.put(player, 0);
            target.sendMessage(ChatColor.GRAY + "Player transferred to " + ChatColor.BLUE + "blue " + ChatColor.GRAY + "mode");
        } else if (PlayerSwitch.get(player) == 0) {
            PlayerSwitch.put(player, 1);
            target.sendMessage(ChatColor.GRAY + "Player transferred to " + ChatColor.RED + "red " + ChatColor.GRAY + "mode");
        } else if (PlayerSwitch.get(player) == 1) {
            PlayerSwitch.remove(player);
            target.sendMessage(ChatColor.GRAY + "Player transferred to " + ChatColor.WHITE + "white " + ChatColor.GRAY + "mode");
        }
    }
}
