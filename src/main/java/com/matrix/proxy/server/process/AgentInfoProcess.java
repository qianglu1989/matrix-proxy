package com.matrix.proxy.server.process;

import com.matrix.proxy.server.handler.ServerMessgaeProcess;
import com.matrix.proxy.util.ResponseCode;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @ClassName TestMessgaeProcess
 * @Author QIANGLU
 * @Date 2020/3/19 4:12 下午
 * @Version 1.0
 */
@Slf4j
@Service
public class AgentInfoProcess implements ServerMessgaeProcess {
    @Override
    public Integer code() {
        return ResponseCode.APP_INFO.getCode();
    }

    @Override
    public void process(ChannelHandlerContext ctx, String datagram) {
        System.out.println("server receive message :"+ datagram);
    }

}
