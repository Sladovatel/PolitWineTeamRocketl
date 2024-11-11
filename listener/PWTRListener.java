package net.politwineteamrocketl.listener;

import net.politwineteamrocketl.PolitWineTeamRocketl;
import net.politwineteamrocketl.process.Fire;
import net.politwineteamrocketl.process.Reload;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PWTRListener implements Listener {
    private Map<UUID, Long> lastInteractTime = new HashMap<>();
    private final PolitWineTeamRocketl plugin;
    public PWTRListener(PolitWineTeamRocketl plugin) {
        this.plugin = plugin;
    }

    //Handler rocketl event | Обработчик событий ракетницы

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getPlayer().getInventory().getItemInMainHand().getType() == Material.PURPLE_DYE && event.getPlayer().getInventory().getItemInMainHand().getItemMeta().getCustomModelData() == 1488) {
            if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                UUID uuid = event.getPlayer().getUniqueId();
                long currentTime = System.currentTimeMillis();
                if (lastInteractTime.containsKey(uuid) && currentTime - lastInteractTime.get(uuid) < 800) {
                    return;
                }
                lastInteractTime.put(uuid, currentTime);
                event.setCancelled(true);
                Fire.Shoot(event.getPlayer(), event.getPlayer().getInventory().getItemInMainHand());
            }
        }
    }

    @EventHandler
    public void onPlayerSwapHandItems(PlayerSwapHandItemsEvent event) {
        if (event.getOffHandItem() != null && event.getOffHandItem().getType() == Material.PURPLE_DYE &&
                (event.getOffHandItem().getItemMeta().getCustomModelData() == 1488 || event.getOffHandItem().getItemMeta().getCustomModelData() == 1489)) {
            event.setCancelled(true);
            UUID playerId = event.getPlayer().getUniqueId();
            if (!Reload.itemRegistry.containsKey(playerId)) {
                boolean hasammo = false;
                for (ItemStack item1 : event.getPlayer().getInventory().getContents()) {
                    if (item1 != null && item1.getType() == Material.PURPLE_DYE) {
                        ItemMeta meta = item1.getItemMeta();
                        if (meta != null && meta.hasCustomModelData() && meta.getCustomModelData() == 1) {
                            hasammo = true;
                            break;
                        }
                    }
                }
                if (hasammo) {
                    Integer value = Reload.getCustomMetadata(event.getPlayer().getInventory().getItemInMainHand());
                    if (value != null && value != 4) {
                        Reload.CheckAmmo(event.getPlayer(), event.getPlayer().getInventory().getItemInMainHand());
                        Reload.SetReloadItem(event.getPlayer());
                    } else if (value != null && value == 4) {
                        event.getPlayer().sendActionBar("Cartridges: 4/4");
                    } else {
                        event.getPlayer().sendActionBar("Error: Unable to determine the number of cartridges");
                    }
                } else {
                    event.getPlayer().sendActionBar("No cartridges");
                }
            }
        }
    }

    //Checking conditions | Проверка условий

    @EventHandler
    public void onPlayerPickupItem(PlayerPickupItemEvent event) {
        if (event.getItem().getItemStack().getType() == Material.PURPLE_DYE) {
            ItemMeta meta = event.getItem().getItemStack().getItemMeta();
            if (meta != null && meta.hasCustomModelData() && meta.getCustomModelData() == 1488) {
                if (hasPurpleDyeWithCustomModelData(event.getPlayer())) {
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getClickedInventory() == null || event.getClickedInventory().getType() != InventoryType.PLAYER) {
            Player player = (Player) event.getWhoClicked();
            ItemStack clickedItem = event.getCurrentItem();

            if (clickedItem != null && clickedItem.getType() == Material.PURPLE_DYE) {
                ItemMeta meta = clickedItem.getItemMeta();
                if (meta != null && meta.hasCustomModelData() && meta.getCustomModelData() == 1488) {
                    if (hasPurpleDyeWithCustomModelData(player)) {
                        event.setCancelled(true);
                        player.sendMessage("Only one rocketl per soldier!");
                    }
                }
            }
        }
    }

    public static boolean hasPurpleDyeWithCustomModelData(Player player) {
        for (ItemStack item : player.getInventory().getContents()) {
            if (item != null && item.getType() == Material.PURPLE_DYE) {
                ItemMeta meta = item.getItemMeta();
                if (meta != null && meta.hasCustomModelData() && meta.getCustomModelData() == 1488) {
                    return true;
                }
            }
        }
        return false;
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        if (event.getItemDrop().getItemStack().getItemMeta().getCustomModelData() == 1488 || event.getItemDrop().getItemStack().getItemMeta().getCustomModelData() == 1489) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerItemHeld(PlayerItemHeldEvent event) {
        if (event.getPlayer().getInventory().getItemInMainHand() != null && event.getPlayer().getInventory().getItemInMainHand().getType() == Material.PURPLE_DYE) {
            ItemMeta meta = event.getPlayer().getInventory().getItemInMainHand().getItemMeta();
            if (meta != null && meta.hasCustomModelData() && meta.getCustomModelData() == 1489) {
                event.setCancelled(true);
            }
        }
    }

    //Сancel use rocketl and ammo in craft | Запрещаем использовать ракетницу и заряды в крафте

    @EventHandler
    public void onCraftItem(CraftItemEvent event) {
        for (ItemStack ingredient : event.getInventory().getMatrix()) {
            if (isBanned(ingredient)) {
                event.setCancelled(true);
                break;
            }
        }
    }
    private boolean isBanned(ItemStack item) {
        if (item != null && item.getType() == Material.PURPLE_DYE) {
            ItemMeta meta = item.getItemMeta();
            if (meta != null && meta.hasCustomModelData()) {
                int customModelData = meta.getCustomModelData();
                return customModelData == 1488 || customModelData == 1489  || customModelData == 1;
            }
        }
        return false;
    }
}
