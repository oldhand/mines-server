package com.mines.server.config;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.mines.server.handler.RealTimeDataHandler;
import com.mines.server.handler.TcpLoggingHandler;
import java.nio.charset.StandardCharsets;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * Netty配置类，处理TCP连接与粘包拆包（按文档要求用@@作为分隔符）
 */
@Component
public class NettyServerConfig {
    @Value("${netty.tcp.port:8888}")
    private int tcpPort;

    @Value("${netty.tcp.boss-threads:2}")
    private int bossThreads;

    @Value("${netty.tcp.worker-threads:8}")
    private int workerThreads;

    @Autowired
    private RealTimeDataHandler realTimeDataHandler;

    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;

    /** 启动Netty服务（项目启动时执行） */
    @PostConstruct
    public void start() {
        bossGroup = new NioEventLoopGroup(bossThreads);
        workerGroup = new NioEventLoopGroup(workerThreads);

        try {
            ServerBootstrap bootstrap = new ServerBootstrap()
                    .group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) {
                            // 分隔符解码器（处理粘包拆包）
                            byte[] delimiterBytes = "@@".getBytes(StandardCharsets.UTF_8);
                            io.netty.buffer.ByteBuf delimiter = Unpooled.wrappedBuffer(delimiterBytes);
                            ch.pipeline().addLast(new DelimiterBasedFrameDecoder(10240, delimiter));

                            // 字符串编解码器
                            ch.pipeline().addLast(new StringDecoder(StandardCharsets.UTF_8));
                            ch.pipeline().addLast(new StringEncoder(StandardCharsets.UTF_8));

                            // 添加TCP日志拦截器
                            ch.pipeline().addLast(new TcpLoggingHandler());

                            // 核心业务处理器
                            ch.pipeline().addLast(realTimeDataHandler);
                        }
                    });

            bootstrap.bind(tcpPort).sync();
            System.out.println("Netty TCP服务启动成功，端口：" + tcpPort);
        } catch (InterruptedException e) {
            throw new RuntimeException("Netty服务启动失败", e);
        }
    }

    /** 关闭Netty服务（项目停止时执行） */
    @PreDestroy
    public void stop() {
        if (bossGroup != null) bossGroup.shutdownGracefully();
        if (workerGroup != null) workerGroup.shutdownGracefully();
        System.out.println("Netty TCP服务已关闭");
    }
}
