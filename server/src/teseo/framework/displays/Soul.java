package teseo.framework.displays;

import teseo.framework.core.Client;

import java.util.HashMap;
import java.util.Map;

public abstract class Soul implements HistoryBoard, DisplayRepository, ClientProvider {
    private Client client;
    private final Map<String, Display> displays = new HashMap<>();

    @Override
    public Client client() {
        return client;
    }

    public void client(Client client) {
        this.client = client;
    }

    @Override
    public <T extends Display> T get(String id) {
        return (T) displays.get(id);
    }

    @Override
    public <T extends Display> void register(T display) {
        displays.put(display.id(), display);
    }

    @Override
    public void remove(String id) {
        displays.remove(id);
    }

}
