package net.politwineteamrocketl.cmd;

import net.politwineteamrocketl.PolitWineTeamRocketl;
import net.politwineteamrocketl.listener.PWTRListener;
import net.politwineteamrocketl.process.Reload;
import net.politwineteamrocketl.process.Switch;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import java.util.ArrayList;
import java.util.List;

public class MainCmd implements CommandExecutor {
    private final PolitWineTeamRocketl plugin;
    public MainCmd(PolitWineTeamRocketl plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;
        if (!sender.hasPermission("pwtr.commands")) {
            player.sendMessage(ChatColor.DARK_RED + "You don't have a permission");
            return true;
        }
        if (args[0].equals("give")) {
            if (args.length != 3) {
                player.sendMessage("Use: /pwtr give {nick} rocketl/ammo");
                return true;
            }
            String playerName = args[1];
            Player targetPlayer = Bukkit.getPlayer(playerName);
            if (targetPlayer == null) {
                sender.sendMessage(ChatColor.RED + "Player " + playerName + " not found");
                return true;
            }

            if (args[2].equals("rocketl")) {
                if (PWTRListener.hasPurpleDyeWithCustomModelData(targetPlayer)) {
                    player.sendMessage(ChatColor.DARK_RED + "The player already has a rocketl");
                    return true;
                }
                ItemStack purpleDye = new ItemStack(Material.PURPLE_DYE);
                Reload.setCustomMetadata(purpleDye, 0);
                ItemMeta meta = purpleDye.getItemMeta();
                meta.setCustomModelData(1488);
                meta.setDisplayName(ChatColor.WHITE + "Rocketl");
                List<String> lore = new ArrayList<>();
                lore.add(ChatColor.WHITE + "Granted: " + ChatColor.GOLD + targetPlayer.getName());
                meta.setLore(lore);
                purpleDye.setItemMeta(meta);
                targetPlayer.getInventory().addItem(purpleDye);
                targetPlayer.sendMessage(ChatColor.DARK_GREEN + "You got a rocketl");
                sender.sendMessage(ChatColor.DARK_GREEN + "You give rocketl player " + ChatColor.GREEN + playerName);
            } else if (args[2].equals("ammo")) {
                ItemStack purpleDye = new ItemStack(Material.PURPLE_DYE, 20);
                ItemMeta meta = purpleDye.getItemMeta();
                meta.setCustomModelData(1);
                meta.setDisplayName(ChatColor.WHITE + "Cartridge");
                purpleDye.setItemMeta(meta);
                targetPlayer.getInventory().addItem(purpleDye);
                targetPlayer.sendMessage(ChatColor.DARK_GREEN + "You got 20 cartridges");
                sender.sendMessage(ChatColor.DARK_GREEN + "You give 20 cartridges player " + ChatColor.GREEN + playerName);
            }
        } else if (args[0].equals("reload")) {
            reloadPlugin();
            sender.sendMessage(ChatColor.DARK_GREEN + "Plugin has been successfully reloaded!");
        } else if (args[0].equals("switch")) {
            if (args.length != 2) {
                player.sendMessage("Use: /pwtr switch {ник}");
                return true;
            }
            String playerName = args[1];
            Player target = Bukkit.getPlayer(playerName);
            if (target == null) {
                sender.sendMessage(ChatColor.RED + "Player " + playerName + " not found");
                return true;
            }
            Switch.SwitchPlayerFire(target, ((Player) sender).getPlayer());
        } else if (args[0].equals("help")) {
            sender.sendMessage("┍ Все комманды ┓");
            sender.sendMessage("/pwtr help - hint output");
            sender.sendMessage("/pwtr give [ник] rocketl/ammo - give the player rocketl ro cartridges");
            sender.sendMessage("/pwtr reload - full reboot of the plugin");
            sender.sendMessage("/pwtr switch [ник] - switches the player's explosion mode");
        } else if (args[0].equals("test")) {
            player.sendMessage("d1");
        } else {
            player.sendMessage(ChatColor.RED + "Not enough arguments");
        }
        return true;
    }

    //Reload call | Вызов перезагрузки

    private void reloadPlugin() {
        Bukkit.getPluginManager().disablePlugin(plugin);
        Bukkit.getPluginManager().enablePlugin(plugin);
    }
}
