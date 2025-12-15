package com.mines.server.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import lombok.extern.slf4j.Slf4j;
import java.nio.charset.StandardCharsets;

/**
 * TCP数据包日志拦截器：记录所有接收和发送的数据包
 */
@Slf4j
public class TcpLoggingHandler extends ChannelDuplexHandler {

    // 最大日志内容长度（避免过长）
    private static final int MAX_CONTENT_LENGTH = 1000;

    /**
     * 接收客户端数据时记录日志
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof String) {
            String request = (String) msg;
            log.info("[TCP接收] 客户端IP:{} 数据:{}",
                    ctx.channel().remoteAddress(),
                    truncateContent(request));
        }
        super.channelRead(ctx, msg); // 继续传递数据
    }

    /**
     * 向客户端发送数据时记录日志
     */
    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        if (msg instanceof String) {
            String response = (String) msg;
            log.info("[TCP发送] 客户端IP:{} 数据:{}",
                    ctx.channel().remoteAddress(),
                    truncateContent(response));
        }
        super.write(ctx, msg, promise); // 继续发送数据
    }

    /**
     * 截断过长的内容，避免日志冗余
     */
    private String truncateContent(String content) {
        if (content == null) return "null";
        String processed = content.replaceAll("\\r|\\n", " ");
        return processed.length() <= MAX_CONTENT_LENGTH
                ? processed
                : processed.substring(0, MAX_CONTENT_LENGTH) + "...[截断]";
    }
}
