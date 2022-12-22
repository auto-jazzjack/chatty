package io.ranslor.chatty.repository;

import io.netty.channel.group.ChannelGroup;
import io.netty.util.internal.StringUtil;
import io.ranslor.chatty.model.Message;
import io.ranslor.chatty.model.Room;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RoomService {

    private final Map<String, Room> messages;

    public RoomService() {
        messages = new ConcurrentHashMap<>();
    }


    public void save(Message message) {
        if (message != null && !StringUtil.isNullOrEmpty(message.getId())) {
            messages.computeIfAbsent(message.getId(), (k) -> new Room(message.getId()))
                    .addMessage(message);
        }
    }

    public ChannelGroup getChannelGroup(String roomId) {
        return messages.computeIfAbsent(roomId, (k) -> new Room(roomId))
                .getChannels();
    }
}
