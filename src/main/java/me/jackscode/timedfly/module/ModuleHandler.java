package me.jackscode.timedfly.module;

import me.jackscode.timedfly.api.TimedFlyModule;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.*;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ModuleHandler {

    public TimedFlyModule enableModules(File module) {
        try {
            System.out.println("Attempting to load module: " + module.getName());
            // Prepare to load class
            URLClassLoader classLoader = new URLClassLoader(
                    new URL[]{module.toURI().toURL()},
                    this.getClass().getClassLoader()
            );

            // Get module.yml file
            InputStream inputStream = classLoader.getResourceAsStream("module.yml");

            // module.yml must exist
            if (inputStream == null) {
                throw new ModuleException("There is no module.yml file on the module " + module.getName());
            }

            // Read contents of module.yml file
            InputStreamReader streamReader = new InputStreamReader(inputStream);
            BufferedReader reader = new BufferedReader(streamReader);

            // Load yml into a YamlConfiguration
            YamlConfiguration moduleConfig = new YamlConfiguration();
            moduleConfig.load(reader);

            String main = moduleConfig.getString("main");

            // Main class must be inside the modules.yml file
            if (main == null) {
                throw new ModuleException("There is no main section on modules.yml of " + module.getName());
            }

            // Get class to load
            Class<?> clazz = Class.forName(main, true, classLoader);

            // Load class constructor
            Constructor<?> constructor = clazz.getConstructor();

            // Make new instance of the module
            Object instance = constructor.newInstance();

            // Class must extend Module abstract class
            if (!(instance instanceof TimedFlyModule)) {
                throw new ModuleException(main + " must implement Module");
            }

            System.out.println("Module " + module.getName() + " has been loaded");
            // Return the instance as a Module
            return (TimedFlyModule) instance;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<TimedFlyModule> loadModules(Path path) {
        try {
            return Files.list(path)
                    .map(Path::toFile)
                    .map(this::enableModules)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


}