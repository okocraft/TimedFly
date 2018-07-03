package com.timedfly.managers;

import com.timedfly.configurations.ConfigCache;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

import java.util.UUID;

public class BossBarManager {

    private long initialTime;
    private String title;
    private String color;
    private String style;
    private long currentTime;
    private boolean running;
    private Player player;
    private UUID uuid;
    private BossBar bar;

    public BossBarManager(UUID uuid, String title, String color, String style, long initialTime, long currentTime) {
        this.uuid = uuid;
        this.player = getPlayerFromUUID();
        this.title = title;
        this.color = color.toUpperCase();
        this.style = style.toUpperCase();
        this.initialTime = initialTime;
        this.currentTime = currentTime;
        this.bar = Bukkit.createBossBar(title, BarColor.valueOf(color.toUpperCase()), BarStyle.valueOf(style.toUpperCase()));
        this.running = false;
    }

    public BossBarManager(UUID uuid, long initialTime, long currentTime) {
        this.uuid = uuid;
        this.player = getPlayerFromUUID();
        this.color = "WHITE";
        this.style = "SEGMENTED_20";
        this.initialTime = initialTime;
        this.currentTime = currentTime;
        this.bar = Bukkit.createBossBar("", BarColor.WHITE, BarStyle.SEGMENTED_20);
        this.running = false;
    }

    public void show() {
        if (!ConfigCache.isBossBarTimerEnabled() || isRunning()) return;
        setRunning(true);

        setBarColor(getColor());
        setBarStyle(getStyle());
        setBarProgress(initialTime);

        addPlayer(player);
    }

    public void hide() {
        if (!ConfigCache.isBossBarTimerEnabled() || !isRunning()) return;
        setRunning(false);
        setBarProgress(1, 1);
        removeBar(player);
    }

    public String getTitle() {
        return title;
    }

    public BossBarManager setTitle(String title) {
        this.title = title;
        bar.setTitle(title);
        return this;
    }

    public String getColor() {
        return color;
    }

    public BossBarManager setColor(String color) {
        this.color = color;
        return this;
    }

    public String getStyle() {
        return style;
    }

    public BossBarManager setStyle(String style) {
        this.style = style;
        return this;
    }

    private long getInitialTime() {
        return initialTime;
    }

    public BossBarManager setInitialTime(long initialTime) {
        this.initialTime = initialTime;
        return this;
    }

    public long getCurrentTime() {
        return currentTime;
    }

    public BossBarManager setCurrentTime(long currentTime) {
        this.currentTime = currentTime;
        return this;
    }

    public boolean isRunning() {
        return running;
    }

    private void setRunning(boolean running) {
        this.running = running;
    }

    public BossBarManager createBar(String color, String style) {
        bar = Bukkit.createBossBar(ChatColor.translateAlternateColorCodes('&', title),
                BarColor.valueOf(color.toUpperCase()), BarStyle.valueOf(style.toUpperCase()));
        return this;
    }

    public BossBarManager addPlayer(Player player) {
        if (!bar.getPlayers().contains(player) && bar != null) {
            bar.addPlayer(player);
        }
        return this;
    }

    public BossBarManager setBarProgress(double progress, double initialTime) {
        if (!ConfigCache.isBossBarTimerEnabled()) return this;
        double barProgress = initialTime / progress;

        if (Double.isInfinite(barProgress) || Double.isNaN(barProgress) || progress <= 0 || barProgress <= 0) {
            removeBar(player);
            return this;
        }

        bar.setProgress(1 / barProgress);
        return this;
    }

    public BossBarManager setBarProgress(double progress) {
        if (!ConfigCache.isBossBarTimerEnabled()) return this;
        double barProgress = getInitialTime() / progress;

        if (Double.isInfinite(barProgress) || Double.isNaN(barProgress) || progress <= 0 || barProgress <= 0) {
            removeBar(player);
            return this;
        }

        bar.setProgress(1 / barProgress);
        return this;
    }

    public boolean containsBar(Player player) {
        return bar.getPlayers().contains(player);
    }

    public void removeBar(Player player) {
        if (containsBar(player)) bar.removePlayer(player);
    }

    public String getBarColor() {
        return bar.getColor().name();
    }

    public BossBarManager setBarColor(String color) {
        bar.setColor(BarColor.valueOf(color.toUpperCase()));
        return this;
    }

    public String getBarStyle() {
        return bar.getStyle().name().replace("_", "");
    }

    public BossBarManager setBarStyle(String style) {
        bar.setStyle(BarStyle.valueOf(style.toUpperCase()));
        return this;
    }

    public String getBarName() {
        return bar.getTitle();
    }

    public BossBarManager setBarName(String text) {
        bar.setTitle(ChatColor.translateAlternateColorCodes('&', text));
        return this;
    }

    public Player getPlayerFromUUID() {
        return Bukkit.getPlayer(uuid);
    }

    public Player getPlayer() {
        return player;
    }

    public BossBarManager setPlayer(Player player) {
        this.player = player;
        return this;
    }
}