package common.discovery;

import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import common.client.RPCRegistry;
import common.protocol.DefaultProtocolTransformer;
import common.protocol.ProtocolTransformer;
import io.lettuce.core.Range;
import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;

public class DefaultClientDiscovery {

	private ScheduledExecutorService executorService;

	private ProtocolTransformer transformer;

	private RedisClient redis;

	private StatefulRedisConnection<String, String> connection;

	private RedisCommands<String, String> commands;

	public DefaultClientDiscovery(String registryCenterAddress, RPCRegistry registry) {
		executorService = Executors.newSingleThreadScheduledExecutor();
		transformer = new DefaultProtocolTransformer();
		redis = RedisClient.create(registryCenterAddress);
		connection = redis.connect();
		commands = connection.sync();
		executorService.scheduleAtFixedRate(() -> {
			double now = new Date().getTime() / 1000000;
			double expireAt = now - DiscoveryConstant.REGIST_KEEP_SECONDS;
			List<String> results = commands.zrangebyscore(DiscoveryConstant.SERVER_REGISTRY_KEY,
					Range.create(expireAt, now));
			registry.refresh(results.stream().map(result -> transformer.remoteServer(result.getBytes()))
					.collect(Collectors.toList()));
		}, DiscoveryConstant.REGIST_INTERVAL_SECONDS,
				DiscoveryConstant.REGIST_INTERVAL_SECONDS, TimeUnit.SECONDS);
	}

}
