package teseo.framework.web.actions;

import teseo.framework.actions.Action;
import teseo.framework.actions.AdapterProxy;
import spark.Request;
import spark.Response;

import java.lang.reflect.InvocationHandler;

import static java.lang.reflect.Proxy.newProxyInstance;

public abstract class SparkWrapper {

	protected AdapterProxy adapters;

	public static RequestWrapper wrap(Request request) {
		return new RequestWrapper(request);
	}

	public static ResponseWrapper wrap(Response response) {
		return new ResponseWrapper(response);
	}

	public SparkWrapper with(AdapterProxy adapters) {
		this.adapters = adapters;
		return this;
	}

	@SuppressWarnings("unchecked")
	public <T extends Action.Message> T as(Class<T> messageClass) {
		return (T) newProxyInstance(messageClass.getClassLoader(), new Class[]{messageClass}, handler());
	}

	protected abstract InvocationHandler handler();

}
