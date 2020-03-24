package com.matrix.proxy.service;

import com.matrix.proxy.module.Command;
import com.matrix.proxy.server.ServerConnectionStore;
import com.matrix.proxy.util.ResponseCode;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * @ClassName JdkCommandServiceImpl
 * @Author QIANGLU
 * @Date 2020/3/23 5:07 下午
 * @Version 1.0
 */
@Service("jdkCommandService")
public class JdkCommandServiceImpl extends AbstractCommandService implements JdkCommandService {


    public JdkCommandServiceImpl(ServerConnectionStore serverConnectionStore) {
        super(serverConnectionStore);
    }


    /**
     * 获取应用当前参数信息
     * @param instanceUuid
     * @return
     */
    @Override
    public String getMetric(String instanceUuid,Integer type) {
        Command.CommandBuilder commandBuilder = Command.builder();
        commandBuilder.id(UUID.randomUUID().toString()).code(type).command("info").instanceUuid(instanceUuid);
        return dispose(commandBuilder.build());
    }


}
