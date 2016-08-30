package teseo.framework.actions;

public interface AdapterProxy {
    RequestAdapter requestAdapterOf(String name, Class clazz);
    ResponseAdapter responseAdapterOf(String name);
}
