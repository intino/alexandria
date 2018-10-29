package io.intino.alexandria.eventstore.graph.natives.tank;



/**#D:\Users\jevora\repositories\ness\datalake-core\src\io\intino\ness\Model.tara#7#1**/
public class DropQN_0 implements io.intino.tara.magritte.Expression<String> {
	private io.intino.alexandria.eventstore.graph.Tank self;

	@Override
	public String value() {
		return "drop." + self.qualifiedName();
	}

	@Override
	public void self(io.intino.tara.magritte.Layer context) {
		self = (io.intino.alexandria.eventstore.graph.Tank) context;
	}

	@Override
	public Class<? extends io.intino.tara.magritte.Layer> selfClass() {
		return io.intino.alexandria.eventstore.graph.Tank.class;
	}
}