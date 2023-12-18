package io.github.gaming32.spigotonfabric.impl.util;

import com.mojang.logging.LogUtils;
import org.bukkit.Bukkit;
import org.slf4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Versioning {
    private static final Logger LOGGER = LogUtils.getLogger();

    public static String getBukkitVersion() {
        try (
            InputStream is = Bukkit.class.getClassLoader()
                .getResourceAsStream("META-INF/maven/org.spigotmc/spigot-api/pom.properties")
        ) {
            if (is != null) {
                final Properties properties = new Properties();
                properties.load(is);
                return properties.getProperty("version");
            }
        } catch (IOException e) {
            LOGGER.error("Could not get Bukkit version!", e);
        }
        return "Unknown-Version";
    }
}
