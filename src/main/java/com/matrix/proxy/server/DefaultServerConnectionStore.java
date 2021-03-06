/*
 * Copyright (C) 2019 Qunar, Inc.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.matrix.proxy.server;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.common.util.concurrent.MoreExecutors;
import io.netty.channel.Channel;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentMap;

/**
 * 存储应用连接
 * @author luqiang
 */
@Service
public class DefaultServerConnectionStore implements ServerConnectionStore {

    private final ConcurrentMap<String, ServerConnection> connections = Maps.newConcurrentMap();

    @Override
    public ServerConnection register(String instanceUuid, Channel channel) {
        DefaultServerConnection connection = new DefaultServerConnection(instanceUuid, channel);
        ServerConnection oldConnection = connections.putIfAbsent(instanceUuid, connection);
        if (oldConnection != null) {
            return oldConnection;
        }
        connection.init();
        connection.closeFuture().addListener(() -> connections.remove(instanceUuid, connection), MoreExecutors.directExecutor());
        return connection;
    }

    @Override
    public Optional<ServerConnection> getConnection(String agentId) {
        ServerConnection agentConnection = connections.get(agentId);
        if (agentConnection != null) {
            return Optional.of(agentConnection);
        } else {
            return Optional.empty();
        }
    }

    @Override
    public Map<String, ServerConnection> getAgentConnection() {
        return ImmutableMap.copyOf(connections);
    }

    @Override
    public Map<String, ServerConnection> searchConnection(String agentId) {
        Map<String, ServerConnection> connection = getAgentConnection();
        Map<String, ServerConnection> result = Maps.filterKeys(connection, key -> key.indexOf(agentId) >= 0);
        return result;
    }
}
