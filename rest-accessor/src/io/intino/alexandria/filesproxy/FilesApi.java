package io.intino.alexandria.filesproxy;

import io.intino.alexandria.filesproxy.exceptions.FilesApiFailure;
import io.intino.konos.alexandria.schema.Resource;

import java.io.InputStream;
import java.net.URL;
import java.util.Map;

public interface FilesApi {
    Connection connect(String url);
    Connection connect(String url, URL certificate, String password);

    interface Connection {
        String upload(Resource resource) throws FilesApiFailure;
        String upload(String path, Resource resource) throws FilesApiFailure;
        String upload(InputStream content, String contentType) throws FilesApiFailure;
        String upload(String path, InputStream content, String contentType) throws FilesApiFailure;
        String upload(InputStream content, String contentType, Map<String, String> parameters) throws FilesApiFailure;
        String upload(String path, InputStream content, String contentType, Map<String, String> parameters) throws FilesApiFailure;
        Resource download(String id) throws FilesApiFailure;
        Resource download(String path, String id) throws FilesApiFailure;
    }
}
