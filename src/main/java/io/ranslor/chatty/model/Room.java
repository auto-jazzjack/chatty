package io.ranslor.chatty.model;

import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class Room {
    private String id;
    private ChannelGroup channels;
    private List<Message> messages;

    public Room(String id) {
        channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
        this.id = id;
    }

    public void addMessage(Message message) {
        synchronized (this) {
            if (messages == null) {
                messages = new ArrayList<>();
            }
            messages.add(message);
        }
    }
}
