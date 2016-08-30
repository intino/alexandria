package teseo.framework.displays;

public interface SoulRepository<Client, Soul> {
    Soul get(Client client);
    void register(Client client, Soul soul);
    void remove(Client client);
}
