package client;

import common.client.DefaultRPCRegistry;
import common.client.RPCRegistry;
import common.client.ServerProxy;
import common.discovery.DefaultClientDiscovery;
import facade.TestService;

public class ClientBootstrap {

	private String registryAddress = "";

	private RPCRegistry registry;

	@SuppressWarnings("unused")
	private DefaultClientDiscovery clientDiscovery;

	public ServerProxy proxy;

	public ClientBootstrap() {
		registry = new DefaultRPCRegistry();
		clientDiscovery = new DefaultClientDiscovery(registryAddress, registry);
		proxy = new ServerProxy(registry);
		proxy.serviceInstance(TestService.class).uppercase("aaa");
	}

}
