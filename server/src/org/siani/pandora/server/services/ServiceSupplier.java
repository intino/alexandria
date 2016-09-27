package org.siani.pandora.server.services;

public interface ServiceSupplier {
    <S extends Service> S service(Class<S> service);
}
