package io.ranslor.chatty.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.ranslor.chatty.model.Message;
import io.ranslor.chatty.repository.RoomService;

public class WebSocketHandler extends ChannelInboundHandlerAdapter {

    private final ObjectMapper mapper = new ObjectMapper();
    private final RoomService roomService;

    public WebSocketHandler(RoomService roomService) {
        this.roomService = roomService;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof TextWebSocketFrame) {
            TextWebSocketFrame text = (TextWebSocketFrame) msg;
            Message message;

            try {
                message = mapper.readValue(text.text(), Message.class);
            } catch (Exception e) {
                message = null;
            }

            roomService.save(message);
            System.out.println(text.text());
        }
        super.channelRead(ctx, msg);
    }
}
