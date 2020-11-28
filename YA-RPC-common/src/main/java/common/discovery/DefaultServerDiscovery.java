package common.discovery;

import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import common.protocol.DefaultProtocolTransformer;
import common.protocol.ProtocolTransformer;
import common.protocol.RemoteServer;
import common.server.RPCDispatcher;
import io.lettuce.core.Range;
import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;

public class DefaultServerDiscovery {

	private ScheduledExecutorService executorService;

	private ProtocolTransformer transformer;

	private RedisClient redis;

	private StatefulRedisConnection<String, String> connection;

	private RedisCommands<String, String> commands;

	public DefaultServerDiscovery(String registryCenterAddress, String serverAddress, RPCDispatcher dispatcher) {
		executorService = Executors.newSingleThreadScheduledExecutor();
		transformer = new DefaultProtocolTransformer();
		redis = RedisClient.create(registryCenterAddress);
		connection = redis.connect();
		commands = connection.sync();
		executorService.scheduleAtFixedRate(() -> {
			RemoteServer remoteServer = new RemoteServer();
			remoteServer.setServerAddress(serverAddress);
			remoteServer.setProcedures(dispatcher.allProcedures());
			commands.zadd(DiscoveryConstant.SERVER_REGISTRY_KEY, new Date().getTime() / 1000000,
					new String(transformer.remoteServer(remoteServer)));
		}, DiscoveryConstant.REGIST_INTERVAL_SECONDS,
				DiscoveryConstant.REGIST_INTERVAL_SECONDS, TimeUnit.SECONDS);
		executorService.scheduleAtFixedRate(() -> {
			double expireAt = new Date().getTime() / 1000000 - DiscoveryConstant.REGIST_KEEP_SECONDS;
			commands.zremrangebyscore(DiscoveryConstant.SERVER_REGISTRY_KEY, Range.create(0.0, expireAt));
		}, DiscoveryConstant.REGIST_INTERVAL_SECONDS,
				DiscoveryConstant.REGIST_INTERVAL_SECONDS, TimeUnit.SECONDS);
	}

}
