package de.timilul.dSGVOManager;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashSet;
import java.util.Set;

public class DSGVOManager extends JavaPlugin implements Listener {

    private Set<Player> unacceptedPlayers = new HashSet<>();

    @Override
    public void onEnable() {
        // Plugin startup logic
        Bukkit.getPluginManager().registerEvents(this, this);
        getLogger().info("DSGVOPlugin wurde aktiviert.");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getLogger().info("DSGVOPlugin wurde deaktiviert.");
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        unacceptedPlayers.add(player);
        sendDSGVOMessage(player);
    }

    private void sendDSGVOMessage(Player player) {
        player.sendMessage(ChatColor.RED + "Bitte akzeptiere die DSGVO, um auf dem Server zu spielen.");
        player.sendMessage(ChatColor.GREEN + "/dsgvo akzeptieren - Um die DSGVO zu akzeptieren.");
        player.sendMessage(ChatColor.RED + "/dsgvo ablehnen - Um die DSGVO abzulehnen.");
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (unacceptedPlayers.contains(player)) {
            event.setCancelled(true); // Bewegung verhindern
            player.sendMessage(ChatColor.RED + "Du musst die DSGVO akzeptieren, um dich zu bewegen.");
        }
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        if (unacceptedPlayers.contains(player)) {
            event.setCancelled(true); // Chat verhindern
            player.sendMessage(ChatColor.RED + "Du musst die DSGVO akzeptieren, um den Chat zu verwenden.");
        }
    }

    // Command to accept or decline DSGVO
    @Override
    public boolean onCommand(org.bukkit.command.CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (label.equalsIgnoreCase("dsgvo")) {
                if (args.length > 0) {
                    if (args[0].equalsIgnoreCase("akzeptieren")) {
                        if (unacceptedPlayers.contains(player)) {
                            unacceptedPlayers.remove(player);
                            player.sendMessage(ChatColor.GREEN + "Du hast die DSGVO akzeptiert. Viel Spaß auf dem Server!");
                        } else {
                            player.sendMessage(ChatColor.YELLOW + "Du hast die DSGVO bereits akzeptiert.");
                        }
                        return true;
                    } else if (args[0].equalsIgnoreCase("ablehnen")) {
                        player.kickPlayer(ChatColor.RED + "Du hast die DSGVO abgelehnt und wurdest vom Server entfernt.");
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
