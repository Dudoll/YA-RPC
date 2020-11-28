package common.client;

import java.lang.reflect.Proxy;
import java.util.stream.Stream;

import common.server.RPCFunction.RPCFunctionHelper;
import common.server.RPCService;
import common.util.ReflectUtil;

public class ServerProxy {

	private RPCRegistry registry;

	public ServerProxy(RPCRegistry registry) {
		this.registry = registry;
	}

	@SuppressWarnings("unchecked")
	public <S> S serviceInstance(Class<S> serviceClass) {
		if (!ReflectUtil.isAnnotationPresent(serviceClass, RPCService.class)) {
			throw new IllegalArgumentException(
					RPCService.class.getName() + " must be present in class " + serviceClass.getName());
		}
		if (!serviceClass.isInterface()) {
			throw new IllegalArgumentException(
					"class " + serviceClass.getName() + " must be an interface at the client side");
		}
		boolean hasRPCFunction = Stream.of(serviceClass.getMethods()).anyMatch(RPCFunctionHelper::isRPCFunction);
		if (!hasRPCFunction) {
			throw new IllegalArgumentException(
					"class " + serviceClass.getName() + " must have at least one remote procedure");
		}
		return (S) Proxy.newProxyInstance(serviceClass.getClassLoader(), new Class[] { serviceClass },
				new ServerHandler(registry));
	}

}
