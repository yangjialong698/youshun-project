package com.ennova.pubinfotask.config;


import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class SocketChannelInitializer extends ChannelInitializer<SocketChannel> {

    @Autowired
    private WebSocketHandler webSocketHandler;

    @Override
    protected void initChannel(SocketChannel ch) {
        //设置log监听器
        ch.pipeline().addLast("logging",new LoggingHandler("DEBUG"));
        //设置解码器
        ch.pipeline().addLast("http-codec",new HttpServerCodec());
        //聚合器
        ch.pipeline().addLast("aggregator",new HttpObjectAggregator(65536));
        //用于大数据的分区传输
        ch.pipeline().addLast("http-chunked",new ChunkedWriteHandler());
        //自定义业务handler
        //ch.pipeline().addLast("handler",new WebSocketHandler());
        ch.pipeline().addLast("handler",webSocketHandler);
    }
}