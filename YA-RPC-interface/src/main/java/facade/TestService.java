package facade;

import common.server.RPCFunction;
import common.server.RPCService;

@RPCService
public interface TestService {

	@RPCFunction
	Float sum(Float a, Float b);

	@RPCFunction
	String uppercase(String str);

}
