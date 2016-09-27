package org.siani.pandora.restful;

import org.siani.pandora.restful.exceptions.RestfulFailure;
import org.siani.pandora.restful.core.Resource;

import java.net.URL;
import java.util.Map;

public interface RestfulApi {
    Response get(URL url, String path) throws RestfulFailure;
    Response get(URL url, String path, Map<String, String> parameters) throws RestfulFailure;

    Resource resourceFrom(URL url, String path) throws RestfulFailure;

    Response post(URL url, String path) throws RestfulFailure;
    Response post(URL url, String path, Map<String, String> parameters) throws RestfulFailure;
    Response post(URL url, String path, Resource resource) throws RestfulFailure;

    Response put(URL url, String path) throws RestfulFailure;
    Response put(URL url, String path, Map<String, String> parameters) throws RestfulFailure;
    Response put(URL url, String path, Resource resource) throws RestfulFailure;

    Response delete(URL url, String path) throws RestfulFailure;
    Response delete(URL url, String path, Map<String, String> parameters) throws RestfulFailure;

    RestfulSecureConnection secure(URL url, URL certificate, String password);

    interface RestfulSecureConnection {
        Response get(String path) throws RestfulFailure;
        Response get(String path, Map<String, String> parameters) throws RestfulFailure;

        Resource getResource(String path) throws RestfulFailure;

        Response post(String path) throws RestfulFailure;
        Response post(String path, Map<String, String> parameters) throws RestfulFailure;
        Response post(String path, Resource resource) throws RestfulFailure;

        Response put(String path) throws RestfulFailure;
        Response put(String path, Map<String, String> parameters) throws RestfulFailure;

        Response delete(String path) throws RestfulFailure;
        Response delete(String path, Map<String, String> parameters) throws RestfulFailure;
    }

    interface Response {
        String content();
    }
}
