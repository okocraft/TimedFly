package me.jackint0sh.timedfly.events;

import me.jackint0sh.timedfly.managers.PlayerManager;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class TimedFlyRunningEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private final PlayerManager playerManager;
    private final Player player;

    public TimedFlyRunningEvent(PlayerManager playerManager) {
        this.playerManager = playerManager;
        this.player = playerManager.getPlayer();
    }

    public PlayerManager getPlayerManager() {
        return playerManager;
    }

    public Player getPlayer() {
        return player;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
