package common.protocol;

public interface ProtocolDeserializer {

	RemoteServer remoteServer(byte[] bytes);

	RemoteProcedure remoteProcedure(byte[] bytes);

	RemoteRequest remoteRequest(byte[] bytes);

	RemoteResponse remoteResponse(byte[] bytes);

}
