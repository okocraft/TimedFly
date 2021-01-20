package me.jackint0sh.timedfly.listeners;

import me.jackint0sh.timedfly.events.TimedFlyEndEvent;
import me.jackint0sh.timedfly.events.TimedFlyRunningEvent;
import me.jackint0sh.timedfly.events.TimedFlyStartEvent;
import me.jackint0sh.timedfly.flygui.FlyItem;
import me.jackint0sh.timedfly.managers.PlayerManager;
import me.jackint0sh.timedfly.utilities.Config;
import me.jackint0sh.timedfly.utilities.Languages;
import me.jackint0sh.timedfly.utilities.MessageUtil;
import me.jackint0sh.timedfly.utilities.TimeParser;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.List;
import java.util.stream.Collectors;

public class TimedFlyListener implements Listener {

    @EventHandler
    private void onFlyStart(TimedFlyStartEvent event) {
        event.getPlayerManager().setFromPlugin(true);
    }

    @EventHandler
    private void onFlyRunning(TimedFlyRunningEvent event) {
        PlayerManager playerManager = event.getPlayerManager();
        Player player = event.getPlayer();
        if (player == null || !player.isOnline()) return;

        String timeLeft = TimeParser.toReadableString(playerManager.getTimeLeft());
        String translate = Languages.getString("fly.time.time_left.self");

        if (Config.getConfig("config").get().getBoolean("Messages.ActionBar") && playerManager.getTimeLeft() > 0) {
            if (translate != null) {
                MessageUtil.sendActionBar(player, translate.replace("[time_left]", timeLeft));
            }
        }

        List<Long> times = Config.getConfig("config").get().getStringList("Messages.Announcer.Times")
                .stream().map(TimeParser::parseNoException).collect(Collectors.toList());

        if (times.stream().anyMatch(time -> time == playerManager.getTimeLeft())) {
            if (Config.getConfig("config").get().getBoolean("Messages.Announcer.Titles")) {
                String title = Languages.getString("fly.time.announcer.title");
                String subtitle = Languages.getString("fly.time.announcer.subtitle");

                if (title == null) {
                    if (subtitle != null) title = "";
                    else return;
                }
                if (subtitle == null) subtitle = "";

                MessageUtil.sendTitle(player,
                        title.replace("[time_left]", timeLeft),
                        subtitle.replace("[time_left]", timeLeft),
                        0, 45, 0
                );
            }
            if (Config.getConfig("config").get().getBoolean("Messages.Announcer.Chat")) {
                MessageUtil.sendTranslation(player, "fly.time.announcer.chat", new String[][]{{
                        "[time_left]", timeLeft
                }});
            }
        }

    }

    @EventHandler
    private void onFlyEnd(TimedFlyEndEvent event) {
        PlayerManager playerManager = event.getPlayerManager();
        Player player = event.getPlayer();
        if (player == null || !player.isOnline()) return;

        String title, subtitle, chat, action_bar;
        String timeLeft = TimeParser.toReadableString(playerManager.getTimeLeft());

        if (playerManager.isInBlacklistedWorld()) return;

        if (!playerManager.hasTime()) {
            title = Languages.getString("fly.time.flight_end.title").replace("[time_left]", timeLeft);
            subtitle = Languages.getString("fly.time.flight_end.subtitle").replace("[time_left]", timeLeft);
            chat = Languages.getString("fly.time.flight_end.chat").replace("[time_left]", timeLeft);
            action_bar = Languages.getString("fly.time.flight_end.action_bar").replace("[time_left]", timeLeft);
        } else {
            title = Languages.getString("fly.time.flight_paused.title").replace("[time_left]", timeLeft);
            subtitle = Languages.getString("fly.time.flight_paused.subtitle").replace("[time_left]", timeLeft);
            chat = Languages.getString("fly.time.flight_paused.chat").replace("[time_left]", timeLeft);
            action_bar = Languages.getString("fly.time.flight_paused.action_bar").replace("[time_left]", timeLeft);
        }

        if (Config.getConfig("config").get().getBoolean("Messages.ActionBar")) {
            MessageUtil.sendActionBar(player, action_bar);
        }

        if (Config.getConfig("config").get().getBoolean("Messages.Title")) {
            MessageUtil.sendTitle(player, title, subtitle);
        }

        if (Config.getConfig("config").get().getBoolean("Messages.Chat")) {
            MessageUtil.sendMessage(player, chat);
        }

        InventoryListener.runCommands(FlyItem.getConfigItem(event.getItemKey()), "onFlyDisable", player);

        PlayerListener.handlePlayerQuery(playerManager, true);
        playerManager.setFromPlugin(false);
    }

}
