package io.github.gaming32.spigotonfabric.impl.scoreboard;

abstract class FabricScoreboardComponent {
    private FabricScoreboard scoreboard;

    FabricScoreboardComponent(FabricScoreboard scoreboard) {
        this.scoreboard = scoreboard;
    }

    abstract FabricScoreboard checkState();

    public FabricScoreboard getScoreboard() {
        return scoreboard;
    }

    abstract void unregister();
}
