package com.matrix.proxy.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.cache.*;
import com.matrix.proxy.module.Command;
import com.matrix.proxy.server.ServerConnection;
import com.matrix.proxy.server.ServerConnectionStore;
import com.matrix.proxy.server.SyncFuture;
import com.matrix.proxy.util.ResponseCode;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName AbstractCommunicateCommandService
 * @Author QIANGLU
 * @Date 2020/3/23 4:45 下午
 * @Version 1.0
 */
@Slf4j
public abstract class AbstractCommandService implements CommandService {

    private final LoadingCache<String, SyncFuture> futureCache;

    private ServerConnectionStore serverConnectionStore;

    public AbstractCommandService(ServerConnectionStore serverConnectionStore) {
        this.serverConnectionStore = serverConnectionStore;
        futureCache = CacheBuilder.newBuilder()
                .initialCapacity(100)
                .maximumSize(10000)
                .concurrencyLevel(20)
                .expireAfterWrite(8, TimeUnit.SECONDS)
                .removalListener(new RemovalListener<Object, Object>() {
                    @Override
                    public void onRemoval(RemovalNotification<Object, Object> notification) {
                        log.info("LoadingCache: {} was removed, cause is {}", notification.getKey(), notification.getCause());
                    }
                })
                .build(new CacheLoader<String, SyncFuture>() {
                    @Override
                    public SyncFuture load(String key) throws Exception {
                        // 当获取key的缓存不存在时，不需要自动添加
                        return null;
                    }
                });
    }


    @Override
    public String dispose(Command command) {
        Optional<ServerConnection> connection = this.serverConnectionStore.getConnection(command.getInstanceUuid());
        SyncFuture syncFuture = new SyncFuture();

        if (connection.isPresent() && connection.get().isActive()) {
            try {
                futureCache.put(command.getId(), syncFuture);

                return connection.get().write(JSON.toJSONString(command), syncFuture);
            } catch (Exception e) {
                log.error("CommandService dispose error:{}", e);
            }
        }
        return "";
    }

    @Override
    public void ackSync(String msg) {

        log.info("接收到服务返回数据 length: {}", msg.length());

        try {
            JSONObject object = JSON.parseObject(msg);
            String id = object.getString("id");

            SyncFuture syncFuture = futureCache.getIfPresent(id);

            if (syncFuture != null) {
                syncFuture.setResponse(msg);
                futureCache.invalidate(id);
            }
        } catch (Exception e) {
            log.error("ackSync data error", e);
        }

    }

}
