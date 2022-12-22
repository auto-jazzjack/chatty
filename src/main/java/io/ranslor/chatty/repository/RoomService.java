package io.ranslor.chatty.repository;

import io.netty.channel.Channel;
import io.netty.util.internal.StringUtil;
import io.ranslor.chatty.model.Message;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RoomService {

    private final Map<Channel, String> id;
    private final Map<String, Message> messages;

    public RoomService() {
        id = new ConcurrentHashMap<>();
        messages = new ConcurrentHashMap<>();
    }

    public Channel getChannel(Message message) {
        return null;
    }

    public void save(Message message) {
        if (message != null && !StringUtil.isNullOrEmpty(message.getId())) {
            messages.put(message.getId(), message);
        }
    }

    public String getId(Channel channel) {
        return id.computeIfAbsent(channel, (ch) -> {
            return ch.remoteAddress().toString();

        });
    }
}
