package com.matrix.proxy.server.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.ImmutableMap;
import com.matrix.proxy.util.GzipUtils;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 * 用于集中管理处理器
 * @ClassName MessageHandler
 * @Author QIANGLU
 * @Date 2020/3/19 11:12 上午
 * @Version 1.0
 */
@ChannelHandler.Sharable
public class MessageHandler extends SimpleChannelInboundHandler<String> {

    private static final Logger logger = LoggerFactory.getLogger(MessageHandler.class);

    private final Map<Integer, ServerMessgaeProcess> processorMap;

    public MessageHandler(List<ServerMessgaeProcess> processors) {
        ImmutableMap.Builder<Integer, ServerMessgaeProcess> builder = new ImmutableMap.Builder<>();
        for (ServerMessgaeProcess processor : processors) {
            builder.put(processor.code(), processor);
        }
        processorMap = builder.build();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String message) throws Exception {

//        String data = GzipUtils.decompress(message);
        logger.info("revice message lenght {}",message.length());
        JSONObject obj = JSON.parseObject(message);
        int code = obj.getInteger("code").intValue();
        ServerMessgaeProcess messageProcessor = processorMap.get(code);
        if (messageProcessor == null) {
            logger.warn("can not process message code [{}], {}", ctx.channel());
            return;
        }

        messageProcessor.process(ctx, message);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        logger.warn("MessageHandler active [{}], {}", ctx.channel());
    }
}
