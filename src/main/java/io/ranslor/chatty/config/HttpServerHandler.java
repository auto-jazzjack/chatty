package io.ranslor.chatty.config;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshakerFactory;
import io.ranslor.chatty.repository.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class HttpServerHandler extends ChannelInboundHandlerAdapter {

    private final RoomService roomService;
    private static final String ROOMID = "ROOM-ID";

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        if (msg instanceof HttpRequest) {
            HttpRequest msg1 = (HttpRequest) msg;
            HttpHeaders headers = msg1.headers();
            if (headers.get(HttpHeaderNames.CONNECTION).equalsIgnoreCase("UPGRADE") &&
                    headers.get(HttpHeaderNames.UPGRADE).equalsIgnoreCase("WebSocket")) {

                String s = headers.get(ROOMID);

                ctx.channel().pipeline()
                        .replace(this, "websocketHandler", new WebSocketHandler(roomService, s));
                roomService.getChannelGroup(s).add(ctx.channel());

                handleHandshake(ctx, msg1);
            }
        }
    }

    protected void handleHandshake(ChannelHandlerContext ctx, HttpRequest req) {
        WebSocketServerHandshakerFactory wsFactory =
                new WebSocketServerHandshakerFactory(getWebSocketURL(req), null, true);
        WebSocketServerHandshaker handshaker = wsFactory.newHandshaker(req);

        if (handshaker == null) {
            WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(ctx.channel());
        } else {
            handshaker.handshake(ctx.channel(), req);
        }
    }

    protected String getWebSocketURL(HttpRequest req) {
        return "ws://" + req.headers().get("Host") + req.getUri();
    }

}
