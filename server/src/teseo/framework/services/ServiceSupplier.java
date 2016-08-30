package teseo.framework.services;

public interface ServiceSupplier {
    <S extends Service> S service(Class<S> service);
}
