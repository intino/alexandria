package io.intino.alexandria.ness.eventstore.graph.natives.tank;


import io.intino.alexandria.ness.eventstore.graph.Tank;

/**#D:\Users\jevora\repositories\ness\datalake-core\src\io\intino\ness\Model.tara#5#1**/
public class FeedQN_0 implements io.intino.tara.magritte.Expression<String> {
	private Tank self;

	@Override
	public String value() {
		return "feed." + self.qualifiedName();
	}

	@Override
	public void self(io.intino.tara.magritte.Layer context) {
		self = (Tank) context;
	}

	@Override
	public Class<? extends io.intino.tara.magritte.Layer> selfClass() {
		return Tank.class;
	}
}