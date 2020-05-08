package me.jackscode.timedfly.api.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import me.jackscode.timedfly.api.Messenger;
import me.jackscode.timedfly.api.Permission;
import me.jackscode.timedfly.api.events.TimedFlyEndEvent;
import me.jackscode.timedfly.api.events.TimedFlyStartEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.security.InvalidParameterException;
import java.util.Arrays;
import java.util.UUID;

@Getter public class TFPlayer extends Messenger {

    @Getter(AccessLevel.NONE) @Setter private boolean hasTime;
    @Setter private int timeLeft;
    @Setter private int initialTime;
    @Setter private boolean timeRunning;
    @Setter private boolean timePaused;
    private final Player player;
    private final UUID uuid;

    public TFPlayer(Player player) {
        this.player = player;
        this.uuid = player.getUniqueId();
        this.tfPlayer = this;

        this.setPlaceholders();
    }

    public void startTimer() {
        if (!this.hasTime()) {
            this.sendMessage("You have no time left");
            return;
        }
        this.setTimeRunning(true);
        Bukkit.getPluginManager().callEvent(new TimedFlyStartEvent(this));
    }

    public void stopTimer() {
        if (!this.isTimeRunning()) {
            this.sendMessage("Timer is not running");
            return;
        }
        this.setTimeRunning(false);
        this.setHasTime(false);
        this.player.setAllowFlight(false);
        this.player.setFlying(false);
        Bukkit.getPluginManager().callEvent(new TimedFlyEndEvent(this));
    }

    public void decreaseTime() {
        this.timeLeft--;
    }


    public boolean hasTime() {
        boolean hasTime = this.timeLeft > 0;
        this.hasTime = hasTime;
        return hasTime;
    }

    public void addTime(int time) {
        this.timeLeft += time;
    }

    @Override
    public boolean sendMessage(String... messages) {
        Player player = this.player.getPlayer();
        if (player == null) return false;

        String[] msgs = Arrays.stream(messages)
                .map(message -> {
                    String colored = ChatColor.translateAlternateColorCodes('&', message);
                    return this.replacePlaceholders(colored);
                })
                .toArray(String[]::new);

        player.sendMessage(msgs);
        return true;
    }

    @Override
    public boolean hasPermission(String... permissions) {
        for (String permission : permissions) {
            Permission perm = Permission.get(permission);

            if (perm == null) {
                throw new InvalidParameterException("There is no permission with name: " + permission);
            }

            if (!getPlayer().hasPermission(perm.getNode())) {
                return false;
            }

        }
        return true;
    }

    private void setPlaceholders() {
        this.add("{player}", this.player.getName());
        this.add("{time_left}", " PARSED ");
        this.add("{time_left_seconds}", this.getTimeLeft() + "");
        this.add("{time_left_minutes}", this.getTimeLeft() * 60 + "");
        this.add("{initial_time}", " PARSED ");
        this.add("{initial_time_seconds}", this.getInitialTime() + "");
        this.add("{initial_time_minutes}", this.getInitialTime() * 60 + "");
    }
}