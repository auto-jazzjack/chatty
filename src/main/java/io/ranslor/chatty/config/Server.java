package io.ranslor.chatty.config;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;
import io.ranslor.chatty.repository.RoomService;
import org.springframework.stereotype.Component;

@Component
public class Server {


    public Server(RoomService roomService) throws Exception {
        ServerBootstrap serverBootstrap = new ServerBootstrap();

        serverBootstrap.group(new NioEventLoopGroup(1), new NioEventLoopGroup(2))
                .channel(NioServerSocketChannel.class)
                //.handler(userService) // [3]
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 3000)
                .option(ChannelOption.SO_BACKLOG, 500) // [4]
                .childOption(ChannelOption.TCP_NODELAY, true) // [5]
                .childOption(ChannelOption.SO_LINGER, 0) // [6]
                .childOption(ChannelOption.SO_KEEPALIVE, true) // [7]
                .childOption(ChannelOption.SO_REUSEADDR, true) // [8]
                .childHandler(new ChannelInitializer<>() {
                    @Override
                    protected void initChannel(Channel ch) throws Exception {
                        ch.pipeline().addLast(new HttpServerCodec());
                        ch.pipeline().addLast(new HttpServerHandler(roomService));
                    }
                }); // [9]
        ;
        serverBootstrap.bind(9090).sync();
        ;
    }
}
