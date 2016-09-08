package teseo.framework.channel;

public abstract class MessageReader {

	protected Message prototype;

	public MessageReader() {
	}

	protected abstract void createPrototype();
	public abstract Message read();
}
