package io.ranslor.chatty.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.group.ChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.ranslor.chatty.model.Message;
import io.ranslor.chatty.repository.RoomService;

public class WebSocketHandler extends ChannelInboundHandlerAdapter {

    private final ObjectMapper mapper = new ObjectMapper();
    private final RoomService roomService;
    private final String roomId;

    public WebSocketHandler(RoomService roomService, String roomId) {
        this.roomService = roomService;
        this.roomId = roomId;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof TextWebSocketFrame) {
            TextWebSocketFrame text = (TextWebSocketFrame) msg;
            Message message;

            try {
                message = mapper.readValue(text.text(), Message.class);
            } catch (Exception e) {
                super.channelRead(ctx, msg);
                return;
            }

            roomService.save(message);
            ChannelGroup channelGroup = roomService.getChannelGroup(this.roomId);
            ((TextWebSocketFrame) msg).retain(1);
            channelGroup.writeAndFlush(msg);
        }
        super.channelRead(ctx, msg);
    }
}
