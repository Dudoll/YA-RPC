package common.client;

import java.util.List;

import common.protocol.RemoteRequest;
import common.protocol.RemoteResponse;
import common.protocol.RemoteServer;

public interface RPCRegistry {

	void refresh(List<RemoteServer> remoteServers);

	RemoteResponse call(RemoteRequest remoteRequest);

}
