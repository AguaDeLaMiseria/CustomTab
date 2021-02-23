package com.aguadelamiseria.customtab;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
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
import java.util.Objects;

public class CustomTab extends JavaPlugin implements Listener, CommandExecutor {

    public final static String BYPASS_PERMISSION = "customtab.bypass";
    public final static String RELOAD_PERMISSION = "customtab.reload";

    private final CommandListener commandListener = new CommandListener(this);
    private boolean commandListenerEnabled, whitelist;

    @Override
    public void onEnable(){
        saveDefaultConfig();
        getServer().getPluginManager().registerEvents(this,this);
        Objects.requireNonNull(getCommand("customtab")).setExecutor(this);
        setup();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (sender instanceof Player && !sender.hasPermission(RELOAD_PERMISSION)) return false;

        if (args.length >= 1 && args[0].equalsIgnoreCase("reload")){
            reloadConfig();
            setup();
            sender.sendMessage(format("&b&lCustomTab >> &aConfiguration file reloaded successfully!"));
            Bukkit.getOnlinePlayers().forEach(Player::updateCommands);
        } else {
            sender.sendMessage(format("&b&lCustomTab >> &fUsage: &e/customtab reload"));
        }
        return false;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onCommandSent(PlayerCommandSendEvent event){
        Player player = event.getPlayer();

        if (player.hasPermission(BYPASS_PERMISSION)) return;

        List<String> completions = getCommandList(player);
        Collection<String> commands = event.getCommands();

        if (whitelist){
            commands.clear();
            commands.addAll(completions);
        } else {
            commands.removeAll(completions);
        }
    }

    public List<String> getCommandList(Player player){
        List<String> completions = new ArrayList<>();
        ConfigurationSection groupSection = getConfig().getConfigurationSection("groups");
        Objects.requireNonNull(groupSection);

        for (String keyGroup : groupSection.getKeys(false)){
            if (player.hasPermission("customtab.group."+keyGroup)
                    || keyGroup.equalsIgnoreCase("default")){
                completions.addAll(groupSection.getStringList(keyGroup+".list"));
            }
        }
        return completions;
    }

    public String format(String message){
        return ChatColor.translateAlternateColorCodes('&',message);
    }

    public void sendMessage(CommandSender target, String path){
        path = getConfig().getString(path);
        if (path == null || path.isEmpty()) return;
        target.sendMessage(format(path));
    }

    private void setup(){
        String type = getConfig().getString("type");
        Objects.requireNonNull(type);
        whitelist = type.equalsIgnoreCase("whitelist");
        if (!getConfig().getBoolean("block-command-execution") && commandListenerEnabled){
            PlayerCommandPreprocessEvent.getHandlerList().unregister(commandListener);
            this.commandListenerEnabled = false;
        } else if (getConfig().getBoolean("block-command-execution") && !commandListenerEnabled) {
            getServer().getPluginManager().registerEvents(commandListener, this);
            this.commandListenerEnabled = true;
        }
    }

    public boolean isWhitelist() {
        return whitelist;
    }
}