package common.protocol;

public interface ProtocolSerializer {

	byte[] remoteServer(RemoteServer remoteServer);

	byte[] remoteProcedure(RemoteProcedure remoteProcedure);

	byte[] remoteRequest(RemoteRequest remoteRequest);

	byte[] remoteResponse(RemoteResponse remoteResponse);

}
