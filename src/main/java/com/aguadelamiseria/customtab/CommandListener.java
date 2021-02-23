package com.aguadelamiseria.customtab;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommandListener implements Listener {

    private final CustomTab customTab;
    private final Pattern pattern = Pattern.compile("/([\\w-]+)");

    public CommandListener(CustomTab customTab){
        this.customTab = customTab;
    }

    @EventHandler
    public void onCommandPreprocessed(PlayerCommandPreprocessEvent event){
        Player player = event.getPlayer();

        if (player.hasPermission(CustomTab.BYPASS_PERMISSION)) return;

        List<String> allowedCommands = customTab.getCommandList(player);

        Matcher matcher = pattern.matcher(event.getMessage());
        if (!matcher.find()) return;
        String command = matcher.group(1);

        if (customTab.isWhitelist() && allowedCommands.contains(command)
                || !customTab.isWhitelist() && !allowedCommands.contains(command)){
            return;
        }
        event.setCancelled(true);
        customTab.sendMessage(player, "block-command-execution-message");
    }
}