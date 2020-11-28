package common.client;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.UUID;

import common.protocol.RemoteRequest;
import common.protocol.RemoteResponse;
import common.protocol.SignHelper;
import common.server.RPCFunction.RPCFunctionHelper;

public class ServerHandler implements InvocationHandler {

	private RPCRegistry registry;

	public ServerHandler(RPCRegistry registry) {
		this.registry = registry;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		if (!RPCFunctionHelper.isRPCFunction(method)) {
			return method.invoke(proxy, args);
		}
		RemoteRequest request = new RemoteRequest();
		request.setRequestId(UUID.randomUUID().toString());
		request.setRemoteProcedure(SignHelper.toRemoteProcedure(method));
		request.setArgs(args);
		String err = null;
		for (int i = 0; i < 5; i++) {
			RemoteResponse response = registry.call(request);
			err = validate(request, response);
			if (err == null) {
				return response.getResult();
			}
		}
		throw new RuntimeException(err);
	}

	private String validate(RemoteRequest request, RemoteResponse response) {
		if (response.getError() != null && !response.getError().isEmpty()) {
			return response.getError();
		}
		if (!request.getRemoteProcedure().equals(response.getProcedure())) {
			return "Incorrect remote procedure";
		}
		if (!request.getRequestId().equals(response.getRequestId())) {
			return "Incorrect request id";
		}
		return null;
	}

}
