package io.github.gaming32.spigotonfabric.impl.help;

import lombok.Getter;

@Getter
@SuppressWarnings("ClassCanBeRecord")
public class HelpTopicAmendment {
    private final String topicName;
    private final String shortText;
    private final String fullText;
    private final String permission;

    public HelpTopicAmendment(String topicName, String shortText, String fullText, String permission) {
        this.fullText = fullText;
        this.shortText = shortText;
        this.topicName = topicName;
        this.permission = permission;
    }
}
