package net.politwineteamrocketl;

import net.politwineteamrocketl.cmd.MainCmd;
import net.politwineteamrocketl.listener.PWTRListener;
import net.politwineteamrocketl.process.Fire;
import net.politwineteamrocketl.process.Reload;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public final class PolitWineTeamRocketl extends JavaPlugin {

    @Override
    public void onEnable() {
        getLogger().info(ChatColor.DARK_GREEN + "PolitWineTeamRocketl began its work auspiciously");
        Bukkit.getPluginManager().registerEvents(new PWTRListener(this), this);
        Bukkit.getPluginManager().registerEvents(new Fire(this), this);
        Bukkit.getPluginManager().registerEvents(new Reload(this), this);
        this.getCommand("pwtr").setExecutor(new MainCmd(this));
    }

    @Override
    public void onDisable() {
        getLogger().info(ChatColor.DARK_RED + "PolitWineTeamRocketl is now disabled");
    }
}
