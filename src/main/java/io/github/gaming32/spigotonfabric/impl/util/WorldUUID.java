package io.github.gaming32.spigotonfabric.impl.util;

import com.mojang.logging.LogUtils;
import org.slf4j.Logger;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

public final class WorldUUID {
    private static final Logger LOGGER = LogUtils.getLogger();

    private WorldUUID() {
    }

    public static UUID getUUID(File baseDir) {
        final File file = new File(baseDir, "uid.dat");
        if (file.exists()) {
            try (DataInputStream dis = new DataInputStream(new FileInputStream(file))) {
                return new UUID(dis.readLong(), dis.readLong());
            } catch (IOException e) {
                LOGGER.warn("Failed to read {}, generating a new random UUID", file, e);
            }
        }

        final UUID uuid = UUID.randomUUID();
        try (DataOutputStream dos = new DataOutputStream(new FileOutputStream(file))) {
            dos.writeLong(uuid.getMostSignificantBits());
            dos.writeLong(uuid.getLeastSignificantBits());
        } catch (IOException e) {
            LOGGER.warn("Failed to write {}", file, e);
        }
        return uuid;
    }
}
