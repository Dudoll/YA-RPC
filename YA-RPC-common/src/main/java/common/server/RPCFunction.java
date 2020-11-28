package common.server;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;

import common.util.ReflectUtil;

@Inherited
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface RPCFunction {

	public static class RPCFunctionHelper {

		public static boolean isRPCFunction(Method method) {
			return ReflectUtil.isAnnotationPresent(method, RPCFunction.class);
		}

	}

}
