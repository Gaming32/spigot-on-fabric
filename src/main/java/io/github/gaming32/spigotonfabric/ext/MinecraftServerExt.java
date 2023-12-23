package io.github.gaming32.spigotonfabric.ext;

import org.bukkit.command.ConsoleCommandSender;

public interface MinecraftServerExt {
    ConsoleCommandSender sof$getConsole();

    void sof$setConsole(ConsoleCommandSender console);

    boolean sof$hasStopped();
}
