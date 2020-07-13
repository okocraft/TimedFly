package me.jackint0sh.timedfly.commands;

public class Arguments {

    public enum Type {
        TIMEDFLY, TFLY
    }

    enum TimedFly {
        HELP("help", "Show this page."),
        RELOAD("reload", "Reload the plugin's files."),
        PERMISSIONS("permissions", "Show the list of available permissions."),
        EDITOR("editor", "Open the fly item creator.");

        private final String usage;
        private final String description;

        TimedFly(String usage, String description) {
            this.usage = usage;
            this.description = description;
        }

        public String getUsage() {
            return usage;
        }

        public String getDescription() {
            return description;
        }

        @Override
        public String toString() {
            return this.usage.split(" ")[0];
        }
    }

    enum TFly {
        HELP("help", "Show the help page for /tfly."),
        SET("set <time> [target]", "Set the flight time of a player"),
        ADD("add <time> [target]", "Add flight time to a player"),
        PAUSE("pause [target]", "Pause the timer of a player."),
        RESUME("resume [target]", "Resume the timer of a player."),
        TOGGLE("toggle [target]", "Toggles stop & resume of a player."),
        TIMELEFT("timeleft [target]", "Get the time left of a player.");

        private final String usage;
        private final String description;

        TFly(String usage, String description) {
            this.usage = usage;
            this.description = description;
        }

        public String getUsage() {
            return usage;
        }

        public String getDescription() {
            return description;
        }

        @Override
        public String toString() {
            return this.usage.split(" ")[0];
        }
    }
}
