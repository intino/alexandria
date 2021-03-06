package io.intino.alexandria.restaccessor;

import io.intino.alexandria.Resource;
import io.intino.alexandria.restaccessor.exceptions.RestfulFailure;

import java.net.URL;
import java.util.List;
import java.util.Map;

public interface RestAccessor {
	Response get(URL url, String path) throws RestfulFailure;

	Response get(URL url, String path, Map<String, String> parameters) throws RestfulFailure;

	Resource getResource(URL url, String path) throws RestfulFailure;

	Resource getResource(URL url, String path, Map<String, String> parameters) throws RestfulFailure;

	Response post(URL url, String path) throws RestfulFailure;

	Response post(URL url, String path, Map<String, String> parameters) throws RestfulFailure;

	Response post(URL url, String path, String body) throws RestfulFailure;

	Response post(URL url, String path, Resource resource) throws RestfulFailure;

	Response post(URL url, String path, List<Resource> resourceList) throws RestfulFailure;

	Response post(URL url, String path, Map<String, String> parameters, List<Resource> resourceList) throws RestfulFailure;

	Response put(URL url, String path) throws RestfulFailure;

	Response put(URL url, String path, String body) throws RestfulFailure;

	Response put(URL url, String path, Map<String, String> parameters) throws RestfulFailure;

	Response put(URL url, String path, Resource resource) throws RestfulFailure;

	Response put(URL url, String path, List<Resource> resourceList) throws RestfulFailure;

	Response put(URL url, String path, Map<String, String> parameters, List<Resource> resourceList) throws RestfulFailure;

	Response delete(URL url, String path) throws RestfulFailure;

	Response delete(URL url, String path, Map<String, String> parameters) throws RestfulFailure;

	RestfulSecureConnection secure(URL url, URL certificate, String password);

	RestfulSecureConnection secure(URL url, String token);

	RestfulSecureConnection secure(URL url, String authHeaderName, String token);

	interface RestfulSecureConnection {
		Response get(String path) throws RestfulFailure;

		Response get(String path, Map<String, String> parameters) throws RestfulFailure;

		Resource getResource(String path) throws RestfulFailure;

		Resource getResource(String path, Map<String, String> parameters) throws RestfulFailure;

		Response post(String path) throws RestfulFailure;

		Response post(String path, String body) throws RestfulFailure;

		Response post(String path, Map<String, String> parameters) throws RestfulFailure;

		Response post(String path, Resource resource) throws RestfulFailure;

		Response post(String path, List<Resource> resourceList) throws RestfulFailure;

		Response post(String path, Map<String, String> parameters, List<Resource> resourceList) throws RestfulFailure;

		Response put(String path) throws RestfulFailure;

		Response put(String path, String body) throws RestfulFailure;

		Response put(String path, Map<String, String> parameters) throws RestfulFailure;

		Response put(String path, List<Resource> resourceList) throws RestfulFailure;

		Response put(String path, Map<String, String> parameters, List<Resource> resourceList) throws RestfulFailure;

		Response delete(String path) throws RestfulFailure;

		Response delete(String path, Map<String, String> parameters) throws RestfulFailure;
	}

}
