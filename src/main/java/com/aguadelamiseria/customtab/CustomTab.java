package com.aguadelamiseria.customtab;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerCommandSendEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CustomTab extends JavaPlugin implements Listener, CommandExecutor {

    private CommandListener commandListener;
    private boolean commandListenerEnabled;

    @Override
    public void onEnable(){
        saveDefaultConfig();
        getServer().getPluginManager().registerEvents(this,this);
        getCommand("customtab").setExecutor(this);

        this.commandListener = new CommandListener(this);

        if (getConfig().getBoolean("block-command-execution")){
            this.commandListenerEnabled = true;
            getServer().getPluginManager().registerEvents(commandListener,this);
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {

        if (sender instanceof Player && !sender.hasPermission("customtab.reload")) {
            return false;
        }

        if (args.length >= 1 && args[0].equalsIgnoreCase("reload")){
            reloadConfig();

            if (!getConfig().getBoolean("block-command-execution") && commandListenerEnabled){
                PlayerCommandPreprocessEvent.getHandlerList().unregister(commandListener);
                this.commandListenerEnabled = false;
            } else if (getConfig().getBoolean("block-command-execution") && !commandListenerEnabled){
                getServer().getPluginManager().registerEvents(commandListener,this);
                this.commandListenerEnabled = true;
            }

            sender.sendMessage(format("&b&lCustomTab >> &aConfiguration file reloaded successfully!"));
        } else {
            sender.sendMessage(format("&b&lCustomTab >> &fUsage: &e/customtab reload"));
        }
        return false;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onCommandSent(PlayerCommandSendEvent event){
        Player player = event.getPlayer();

        if (player.hasPermission("customtab.bypass")) return;

        List<String> completions = getCommandList(player);
        Collection<String> commands = event.getCommands();

        if (getConfig().getString("type").equalsIgnoreCase("whitelist")){
            commands.clear();
            commands.addAll(completions);
        } else {
            commands.removeAll(completions);
        }
    }

    public List<String> getCommandList(Player player){
        List<String> completions = new ArrayList<>();

        for (String keyGroup : getConfig().getConfigurationSection("groups").getKeys(false)){
            if (player.hasPermission("customtab.group."+keyGroup) || keyGroup.equalsIgnoreCase("default")){
                completions.addAll(getConfig().getStringList("groups."+keyGroup+".list"));
            }
        }
        return completions;
    }

    public String format(String message){
        return ChatColor.translateAlternateColorCodes('&',message);
    }

}
