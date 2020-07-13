package me.jackint0sh.timedfly.events;

import me.jackint0sh.timedfly.managers.PlayerManager;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class TimedFlyEndEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private final PlayerManager playerManager;
    private final Player player;

    public TimedFlyEndEvent(PlayerManager playerManager) {
        this.playerManager = playerManager;
        this.player = playerManager.getPlayer();
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public PlayerManager getPlayerManager() {
        return playerManager;
    }

    public Player getPlayer() {
        return player;
    }

    public String getItemKey() {
        return playerManager.getLastItemUsed();
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
}
