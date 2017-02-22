package com.earth2me.essentials;

import com.earth2me.essentials.utils.LocationUtil;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.HashMap;
import java.util.Map;

import static com.earth2me.essentials.I18n.tl;

public class TeleportSafety implements Listener {
    private static Map<Player, TeleportData> monitoredPlayers = new HashMap<>();

    public static void monitorPlayer(Player target, Player teleportee, Teleport teleport) {
        if (!monitoredPlayers.containsKey(target)) monitoredPlayers.put(target, new TeleportData(teleportee, teleport));
    }

    public static void stopMonitoringPlayer(Player player) {
        monitoredPlayers.remove(player);
    }

    public static boolean isPlayerMonitored(Player player) {
        return monitoredPlayers.containsKey(player);
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onPlayerMove(PlayerMoveEvent event) {
        if (!isPlayerMonitored(event.getPlayer())) return;
        TeleportData tpdata = monitoredPlayers.get(event.getPlayer());
        if (tpdata.getTeleport().getTimedTeleport() == null) {
            stopMonitoringPlayer(event.getPlayer());
            return;
        }
        Location location = event.getTo();
        if (LocationUtil.isBlockUnsafe(location.getWorld(), location.getBlockX(), location.getBlockY(), location.getBlockZ())) {
            Teleport teleport = tpdata.getTeleport();
            teleport.cancel(false);
            Player teleportee = tpdata.getTeleportee();
            teleportee.sendMessage(tl("tpCancelledTargetInUnsafeLocation"));
            stopMonitoringPlayer(event.getPlayer());
        }
    }

    private static class TeleportData {
        private final Player teleportee;
        private final Teleport teleport;

        public TeleportData(Player teleportee, Teleport teleport) {
            this.teleportee = teleportee;
            this.teleport = teleport;
        }

        public Player getTeleportee() {
            return teleportee;
        }

        public Teleport getTeleport() {
            return teleport;
        }
    }
}
