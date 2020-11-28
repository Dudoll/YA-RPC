package common.server;

import java.util.List;

import common.protocol.RemoteProcedure;
import common.protocol.RemoteRequest;

public interface RPCDispatcher {

	void registService(Object service);

	void unregistService(Object service);

	List<RemoteProcedure> allProcedures();

	Object process(RemoteRequest request);

}
