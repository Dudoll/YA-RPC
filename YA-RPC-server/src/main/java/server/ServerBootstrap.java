package server;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.time.Duration;

import common.protocol.DefaultProtocolTransformer;
import common.protocol.ProtocolTransformer;
import common.protocol.RemoteRequest;
import common.protocol.RemoteResponse;
import common.protocol.SignHelper;
import common.server.DefaultRPCDispatcher;
import common.server.DefaultRPCServer;
import common.server.RPCDispatcher;
import facade.TestService;
import server.impl.TestServiceImpl;

public class ServerBootstrap {

	private String registryAddress = "http://127.0.0.1:8080";

	private RPCDispatcher dispatcher;

	@SuppressWarnings("unused")
	private DefaultRPCServer server;

	public ServerBootstrap() {
		dispatcher = new DefaultRPCDispatcher();
		server = new DefaultRPCServer(registryAddress, dispatcher);
	}

	public static void main(String[] args) {
		ServerBootstrap bootstrap = new ServerBootstrap();
		bootstrap.dispatcher.registService(new TestServiceImpl());
		HttpClient client = HttpClient.newBuilder().connectTimeout(Duration.ofMillis(5000))
				.followRedirects(HttpClient.Redirect.NORMAL).build();
		ProtocolTransformer transformer = new DefaultProtocolTransformer();
		try {
			RemoteRequest remoteRequest = new RemoteRequest();
			remoteRequest.setRequestId("");
			remoteRequest.setRemoteProcedure(
					SignHelper.toRemoteProcedure(TestService.class.getMethod("uppercase", String.class)));
			remoteRequest.setArgs(new Object[] { "aaa" });

			Object result = bootstrap.dispatcher.process(remoteRequest);
			System.out.println(result);

			HttpRequest request = HttpRequest.newBuilder().uri(URI.create(bootstrap.registryAddress))
					.POST(BodyPublishers.ofByteArray(transformer.remoteRequest(remoteRequest))).build();
			byte[] bytes = client.send(request, HttpResponse.BodyHandlers.ofByteArray()).body();
			RemoteResponse remoteResponse = transformer.remoteResponse(bytes);
			System.out.println(remoteResponse.getResult());
			System.exit(0);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
