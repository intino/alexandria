package io.intino.alexandria.eventstore.graph;


public abstract class AbstractTank extends io.intino.tara.magritte.Layer implements io.intino.tara.magritte.tags.Terminal {
	protected String qualifiedName;
	protected io.intino.tara.magritte.Expression<String> feedQN;
	protected io.intino.tara.magritte.Expression<String> flowQN;
	protected io.intino.tara.magritte.Expression<String> dropQN;
	protected boolean active;

	public AbstractTank(io.intino.tara.magritte.Node node) {
		super(node);
	}

	public String qualifiedName() {
		return qualifiedName;
	}

	public String feedQN() {
		return feedQN.value();
	}

	public String flowQN() {
		return flowQN.value();
	}

	public String dropQN() {
		return dropQN.value();
	}

	public boolean active() {
		return active;
	}

	public Tank qualifiedName(String value) {
		this.qualifiedName = value;
		return (Tank) this;
	}

	public Tank feedQN(io.intino.tara.magritte.Expression<String> value) {
		this.feedQN = io.intino.tara.magritte.loaders.FunctionLoader.load(value, this, io.intino.tara.magritte.Expression.class);
		return (Tank) this;
	}

	public Tank flowQN(io.intino.tara.magritte.Expression<String> value) {
		this.flowQN = io.intino.tara.magritte.loaders.FunctionLoader.load(value, this, io.intino.tara.magritte.Expression.class);
		return (Tank) this;
	}

	public Tank dropQN(io.intino.tara.magritte.Expression<String> value) {
		this.dropQN = io.intino.tara.magritte.loaders.FunctionLoader.load(value, this, io.intino.tara.magritte.Expression.class);
		return (Tank) this;
	}

	public Tank active(boolean value) {
		this.active = value;
		return (Tank) this;
	}

	@Override
	protected java.util.Map<String, java.util.List<?>> variables$() {
		java.util.Map<String, java.util.List<?>> map = new java.util.LinkedHashMap<>();
		map.put("qualifiedName", new java.util.ArrayList(java.util.Collections.singletonList(this.qualifiedName)));
		map.put("feedQN", new java.util.ArrayList(java.util.Collections.singletonList(this.feedQN)));
		map.put("flowQN", new java.util.ArrayList(java.util.Collections.singletonList(this.flowQN)));
		map.put("dropQN", new java.util.ArrayList(java.util.Collections.singletonList(this.dropQN)));
		map.put("active", new java.util.ArrayList(java.util.Collections.singletonList(this.active)));
		return map;
	}

	@Override
	protected void load$(String name, java.util.List<?> values) {
		super.load$(name, values);
		if (name.equalsIgnoreCase("qualifiedName")) this.qualifiedName = io.intino.tara.magritte.loaders.StringLoader.load(values, this).get(0);
		else if (name.equalsIgnoreCase("feedQN")) this.feedQN = io.intino.tara.magritte.loaders.FunctionLoader.load(values, this, io.intino.tara.magritte.Expression.class).get(0);
		else if (name.equalsIgnoreCase("flowQN")) this.flowQN = io.intino.tara.magritte.loaders.FunctionLoader.load(values, this, io.intino.tara.magritte.Expression.class).get(0);
		else if (name.equalsIgnoreCase("dropQN")) this.dropQN = io.intino.tara.magritte.loaders.FunctionLoader.load(values, this, io.intino.tara.magritte.Expression.class).get(0);
		else if (name.equalsIgnoreCase("active")) this.active = io.intino.tara.magritte.loaders.BooleanLoader.load(values, this).get(0);
	}

	@Override
	protected void set$(String name, java.util.List<?> values) {
		super.set$(name, values);
		if (name.equalsIgnoreCase("qualifiedName")) this.qualifiedName = (String) values.get(0);
		else if (name.equalsIgnoreCase("feedQN")) this.feedQN = io.intino.tara.magritte.loaders.FunctionLoader.load(values.get(0), this, io.intino.tara.magritte.Expression.class);
		else if (name.equalsIgnoreCase("flowQN")) this.flowQN = io.intino.tara.magritte.loaders.FunctionLoader.load(values.get(0), this, io.intino.tara.magritte.Expression.class);
		else if (name.equalsIgnoreCase("dropQN")) this.dropQN = io.intino.tara.magritte.loaders.FunctionLoader.load(values.get(0), this, io.intino.tara.magritte.Expression.class);
		else if (name.equalsIgnoreCase("active")) this.active = (Boolean) values.get(0);
	}


	public io.intino.alexandria.eventstore.graph.DatalakeGraph graph() {
		return (io.intino.alexandria.eventstore.graph.DatalakeGraph) core$().graph().as(io.intino.alexandria.eventstore.graph.DatalakeGraph.class);
	}
}
