package com.aguadelamiseria.customtab;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.List;

public class CommandListener implements Listener {

    private CustomTab customTab;

    public CommandListener(CustomTab customTab){
        this.customTab = customTab;
    }

    @EventHandler
    public void onCommandPreprocessed(PlayerCommandPreprocessEvent event){
        Player player = event.getPlayer();

        if (player.hasPermission("customtab.bypass")) return;

        List<String> allowedCommands = customTab.getCommandList(player);

        String command = event.getMessage().split("\\s")[0].substring(1);

        if (customTab.getConfig().getString("type").equalsIgnoreCase("whitelist")){
            if (!allowedCommands.contains(command)){
                event.setCancelled(true);
                String message = customTab.getConfig().getString("block-command-execution-message");

                if (!message.isEmpty() || message != null){
                    player.sendMessage(customTab.format(message));
                }
            }
        } else {
            if (allowedCommands.contains(command)){
                event.setCancelled(true);
                String message = customTab.getConfig().getString("block-command-execution-message");

                if (!message.isEmpty() || message != null){
                    player.sendMessage(customTab.format(message));
                }
            }
        }
    }
}
