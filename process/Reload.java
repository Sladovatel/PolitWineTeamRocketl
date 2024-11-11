package net.politwineteamrocketl.process;

import net.politwineteamrocketl.PolitWineTeamRocketl;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import java.util.*;

public class Reload implements Listener {
    public static final Map<UUID, ItemStack> itemRegistry = new HashMap<>();
    private static PolitWineTeamRocketl plugin = null;
    public Reload(PolitWineTeamRocketl plugin) {
        this.plugin = plugin;
    }

    //Checking the player has ammo and reload cycle | Проверка на наличие зарядов у игрока и цикл перезарядки

    public static void MainReload(Player player, ItemStack item) {
        Integer value = Reload.getCustomMetadata(item);
        if (value != 4) {
            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                @Override
                public void run() {
                    setCustomMetadata(item, value + 1);
                    Integer value = Reload.getCustomMetadata(item);
                    player.sendActionBar("Cartridges: " + value + "/4");
                    String soundName = "rocket_reload";
                    Sound.playSoundInRadius(player.getLocation(), 8, soundName);
                    CheckAmmo(player, item);
                    TakeAmmo(player);
                }
            }, 25L);
        } else if (value == 4) {
            player.sendActionBar("Cartridges: 4/4");
            UUID playerId = player.getUniqueId();
            itemRegistry.remove(playerId);
            player.getInventory().setItemInMainHand(item);
        }
    }

    public static void CheckAmmo(Player player, ItemStack item) {
        UUID playerId = player.getUniqueId();
        boolean hasammo = false;
        for (ItemStack item1 : player.getInventory().getContents()) {
            if (item1 != null && item1.getType() == Material.PURPLE_DYE) {
                ItemMeta meta = item1.getItemMeta();
                if (meta != null && meta.hasCustomModelData() && meta.getCustomModelData() == 1) {
                    hasammo = true;
                    break;
                }
            }
        }

        if (hasammo) {
            MainReload(player, item);
            if (itemRegistry.containsKey(playerId)) {
                itemRegistry.put(playerId, item);
            }
        } else {
            player.sendActionBar("No cartridges");
            itemRegistry.remove(playerId);
            player.getInventory().setItemInMainHand(item);
        }
    }

    //Work this item MetaData | Работа с MetaData предмета

    public static void setCustomMetadata(ItemStack item, int value) {
        ItemMeta meta = item.getItemMeta();
        meta.getPersistentDataContainer().set(new NamespacedKey(plugin, "custom_value"), PersistentDataType.INTEGER, value);
        item.setItemMeta(meta);
    }

    public static Integer getCustomMetadata(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        return meta.getPersistentDataContainer().get(new NamespacedKey(plugin, "custom_value"), PersistentDataType.INTEGER);
    }

    //Player handlers | Обработчики игрока

    public static void TakeAmmo(Player player) {
        for (ItemStack item : player.getInventory().getContents()) {
            if (item != null && item.getType() == Material.PURPLE_DYE) {
                ItemMeta meta = item.getItemMeta();
                if (meta != null && meta.hasCustomModelData() && meta.getCustomModelData() == 1) {
                    if (item.getAmount() > 1) {
                        item.setAmount(item.getAmount() - 1);
                    } else {
                        player.getInventory().remove(item);
                    }
                    player.updateInventory();
                    return;
                }
            }
        }
    }

    public static void SetReloadItem(Player player) {
        ItemStack tempItem = new ItemStack(Material.PURPLE_DYE);
        ItemMeta meta = tempItem.getItemMeta();
        meta.setCustomModelData(1489);
        meta.setDisplayName(ChatColor.WHITE + "Rocketl");
        tempItem.setItemMeta(meta);
        player.getInventory().setItemInMainHand(tempItem);
    }
}
