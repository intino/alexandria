package io.intino.konos.alexandria.activity.schemas;

public class ActivitySchemaAdapters {


	public static io.intino.konos.alexandria.activity.schemas.Bounds boundsFromLayer(io.intino.tara.magritte.Layer layer) {
		return boundsFromNode(layer.core$());
	}

	private static io.intino.konos.alexandria.activity.schemas.Bounds boundsFromNode(io.intino.tara.magritte.Node node) {
		io.intino.konos.alexandria.activity.schemas.Bounds schema = new io.intino.konos.alexandria.activity.schemas.Bounds();
		final java.util.Map<String, java.util.List<?>> variables = node.variables();
		variables.put("name", java.util.Collections.singletonList(node.name()));
		variables.put("id", java.util.Collections.singletonList(node.id()));

		for (io.intino.tara.magritte.Node component : node.componentList()) {
			if (component.name().equalsIgnoreCase(io.intino.konos.alexandria.activity.schemas.NorthEast.class.getSimpleName())) schema.northEast((northEastFromNode(component)));
			if (component.name().equalsIgnoreCase(io.intino.konos.alexandria.activity.schemas.SouthWest.class.getSimpleName())) schema.southWest((southWestFromNode(component)));
		}
		return schema;
	}


	public static io.intino.konos.alexandria.activity.schemas.NorthEast northEastFromLayer(io.intino.tara.magritte.Layer layer) {
		return northEastFromNode(layer.core$());
	}

	private static io.intino.konos.alexandria.activity.schemas.NorthEast northEastFromNode(io.intino.tara.magritte.Node node) {
		io.intino.konos.alexandria.activity.schemas.NorthEast schema = new io.intino.konos.alexandria.activity.schemas.NorthEast();
		final java.util.Map<String, java.util.List<?>> variables = node.variables();
		variables.put("name", java.util.Collections.singletonList(node.name()));
		variables.put("id", java.util.Collections.singletonList(node.id()));
		for (String variable : variables.keySet()) {
			if (variable.equals("longitude")) schema.longitude((Double) variables.get(variable).get(0));
			if (variable.equals("latitude")) schema.latitude((Double) variables.get(variable).get(0));
		}

		return schema;
	}


	public static io.intino.konos.alexandria.activity.schemas.SouthWest southWestFromLayer(io.intino.tara.magritte.Layer layer) {
		return southWestFromNode(layer.core$());
	}

	private static io.intino.konos.alexandria.activity.schemas.SouthWest southWestFromNode(io.intino.tara.magritte.Node node) {
		io.intino.konos.alexandria.activity.schemas.SouthWest schema = new io.intino.konos.alexandria.activity.schemas.SouthWest();
		final java.util.Map<String, java.util.List<?>> variables = node.variables();
		variables.put("name", java.util.Collections.singletonList(node.name()));
		variables.put("id", java.util.Collections.singletonList(node.id()));
		for (String variable : variables.keySet()) {
			if (variable.equals("longitude")) schema.longitude((Double) variables.get(variable).get(0));
			if (variable.equals("latitude")) schema.latitude((Double) variables.get(variable).get(0));
		}

		return schema;
	}


	public static io.intino.konos.alexandria.activity.schemas.Catalog catalogFromLayer(io.intino.tara.magritte.Layer layer) {
		return catalogFromNode(layer.core$());
	}

	private static io.intino.konos.alexandria.activity.schemas.Catalog catalogFromNode(io.intino.tara.magritte.Node node) {
		io.intino.konos.alexandria.activity.schemas.Catalog schema = new io.intino.konos.alexandria.activity.schemas.Catalog();
		final java.util.Map<String, java.util.List<?>> variables = node.variables();
		variables.put("name", java.util.Collections.singletonList(node.name()));
		variables.put("id", java.util.Collections.singletonList(node.id()));
		for (String variable : variables.keySet()) {
			if (variable.equals("name")) schema.name((String) variables.get(variable).get(0).toString());
			if (variable.equals("label")) schema.label((String) variables.get(variable).get(0).toString());
			if (variable.equals("embedded")) schema.embedded((Boolean) variables.get(variable).get(0));
			if (variable.equals("hideGroupings")) schema.hideGroupings((Boolean) variables.get(variable).get(0));
		}
		for (io.intino.tara.magritte.Node component : node.componentList()) {
			if (component.name().equalsIgnoreCase(io.intino.konos.alexandria.activity.schemas.Grouping.class.getSimpleName())) schema.groupingList().add(groupingFromNode(component));
			if (component.name().equalsIgnoreCase(io.intino.konos.alexandria.activity.schemas.Sorting.class.getSimpleName())) schema.sortingList().add(sortingFromNode(component));
		}
		return schema;
	}


	public static io.intino.konos.alexandria.activity.schemas.Grouping groupingFromLayer(io.intino.tara.magritte.Layer layer) {
		return groupingFromNode(layer.core$());
	}

	private static io.intino.konos.alexandria.activity.schemas.Grouping groupingFromNode(io.intino.tara.magritte.Node node) {
		io.intino.konos.alexandria.activity.schemas.Grouping schema = new io.intino.konos.alexandria.activity.schemas.Grouping();
		final java.util.Map<String, java.util.List<?>> variables = node.variables();
		variables.put("name", java.util.Collections.singletonList(node.name()));
		variables.put("id", java.util.Collections.singletonList(node.id()));
		for (String variable : variables.keySet()) {
			if (variable.equals("name")) schema.name((String) variables.get(variable).get(0).toString());
			if (variable.equals("type")) schema.type((String) variables.get(variable).get(0).toString());
			if (variable.equals("label")) schema.label((String) variables.get(variable).get(0).toString());
			if (variable.equals("countItems")) schema.countItems((Integer) variables.get(variable).get(0));
			if (variable.equals("histogram")) schema.histogram((String) variables.get(variable).get(0).toString());
		}
		for (io.intino.tara.magritte.Node component : node.componentList()) {
			if (component.name().equalsIgnoreCase(io.intino.konos.alexandria.activity.schemas.Group.class.getSimpleName())) schema.groupList().add(groupFromNode(component));
		}
		return schema;
	}


	public static io.intino.konos.alexandria.activity.schemas.Group groupFromLayer(io.intino.tara.magritte.Layer layer) {
		return groupFromNode(layer.core$());
	}

	private static io.intino.konos.alexandria.activity.schemas.Group groupFromNode(io.intino.tara.magritte.Node node) {
		io.intino.konos.alexandria.activity.schemas.Group schema = new io.intino.konos.alexandria.activity.schemas.Group();
		final java.util.Map<String, java.util.List<?>> variables = node.variables();
		variables.put("name", java.util.Collections.singletonList(node.name()));
		variables.put("id", java.util.Collections.singletonList(node.id()));
		for (String variable : variables.keySet()) {
			if (variable.equals("name")) schema.name((String) variables.get(variable).get(0).toString());
			if (variable.equals("label")) schema.label((String) variables.get(variable).get(0).toString());
			if (variable.equals("selected")) schema.selected((Boolean) variables.get(variable).get(0));
			if (variable.equals("count")) schema.count((Integer) variables.get(variable).get(0));
		}

		return schema;
	}


	public static io.intino.konos.alexandria.activity.schemas.ClusterGroup clusterGroupFromLayer(io.intino.tara.magritte.Layer layer) {
		return clusterGroupFromNode(layer.core$());
	}

	private static io.intino.konos.alexandria.activity.schemas.ClusterGroup clusterGroupFromNode(io.intino.tara.magritte.Node node) {
		io.intino.konos.alexandria.activity.schemas.ClusterGroup schema = new io.intino.konos.alexandria.activity.schemas.ClusterGroup();
		final java.util.Map<String, java.util.List<?>> variables = node.variables();
		variables.put("name", java.util.Collections.singletonList(node.name()));
		variables.put("id", java.util.Collections.singletonList(node.id()));
		for (String variable : variables.keySet()) {
			if (variable.equals("name")) schema.name((String) variables.get(variable).get(0).toString());
			if (variable.equals("cluster")) schema.cluster((String) variables.get(variable).get(0).toString());
			if (variable.equals("items")) schema.items().addAll(((java.util.List<String>) variables.get(variable)));
		}

		return schema;
	}


	public static io.intino.konos.alexandria.activity.schemas.DateNavigatorState dateNavigatorStateFromLayer(io.intino.tara.magritte.Layer layer) {
		return dateNavigatorStateFromNode(layer.core$());
	}

	private static io.intino.konos.alexandria.activity.schemas.DateNavigatorState dateNavigatorStateFromNode(io.intino.tara.magritte.Node node) {
		io.intino.konos.alexandria.activity.schemas.DateNavigatorState schema = new io.intino.konos.alexandria.activity.schemas.DateNavigatorState();
		final java.util.Map<String, java.util.List<?>> variables = node.variables();
		variables.put("name", java.util.Collections.singletonList(node.name()));
		variables.put("id", java.util.Collections.singletonList(node.id()));
		for (String variable : variables.keySet()) {
			if (variable.equals("playing")) schema.playing((Boolean) variables.get(variable).get(0));
			if (variable.equals("canPlay")) schema.canPlay((Boolean) variables.get(variable).get(0));
			if (variable.equals("canPrevious")) schema.canPrevious((Boolean) variables.get(variable).get(0));
			if (variable.equals("canNext")) schema.canNext((Boolean) variables.get(variable).get(0));
		}

		return schema;
	}


	public static io.intino.konos.alexandria.activity.schemas.Dialog dialogFromLayer(io.intino.tara.magritte.Layer layer) {
		return dialogFromNode(layer.core$());
	}

	private static io.intino.konos.alexandria.activity.schemas.Dialog dialogFromNode(io.intino.tara.magritte.Node node) {
		io.intino.konos.alexandria.activity.schemas.Dialog schema = new io.intino.konos.alexandria.activity.schemas.Dialog();
		final java.util.Map<String, java.util.List<?>> variables = node.variables();
		variables.put("name", java.util.Collections.singletonList(node.name()));
		variables.put("id", java.util.Collections.singletonList(node.id()));
		for (String variable : variables.keySet()) {
			if (variable.equals("label")) schema.label((String) variables.get(variable).get(0).toString());
			if (variable.equals("description")) schema.description((String) variables.get(variable).get(0).toString());
			if (variable.equals("definition")) schema.definition((String) variables.get(variable).get(0).toString());
		}

		return schema;
	}


	public static io.intino.konos.alexandria.activity.schemas.DialogInput dialogInputFromLayer(io.intino.tara.magritte.Layer layer) {
		return dialogInputFromNode(layer.core$());
	}

	private static io.intino.konos.alexandria.activity.schemas.DialogInput dialogInputFromNode(io.intino.tara.magritte.Node node) {
		io.intino.konos.alexandria.activity.schemas.DialogInput schema = new io.intino.konos.alexandria.activity.schemas.DialogInput();
		final java.util.Map<String, java.util.List<?>> variables = node.variables();
		variables.put("name", java.util.Collections.singletonList(node.name()));
		variables.put("id", java.util.Collections.singletonList(node.id()));
		for (String variable : variables.keySet()) {
			if (variable.equals("path")) schema.path((String) variables.get(variable).get(0).toString());
			if (variable.equals("value")) schema.value((String) variables.get(variable).get(0).toString());
		}

		return schema;
	}


	public static io.intino.konos.alexandria.activity.schemas.DialogInputResource dialogInputResourceFromLayer(io.intino.tara.magritte.Layer layer) {
		return dialogInputResourceFromNode(layer.core$());
	}

	private static io.intino.konos.alexandria.activity.schemas.DialogInputResource dialogInputResourceFromNode(io.intino.tara.magritte.Node node) {
		io.intino.konos.alexandria.activity.schemas.DialogInputResource schema = new io.intino.konos.alexandria.activity.schemas.DialogInputResource();
		final java.util.Map<String, java.util.List<?>> variables = node.variables();
		variables.put("name", java.util.Collections.singletonList(node.name()));
		variables.put("id", java.util.Collections.singletonList(node.id()));
		for (String variable : variables.keySet()) {
			if (variable.equals("path")) schema.path((String) variables.get(variable).get(0).toString());
			if (variable.equals("file")) schema.file((io.intino.konos.Resource) variables.get(variable).get(0));
		}

		return schema;
	}


	public static io.intino.konos.alexandria.activity.schemas.DialogReference dialogReferenceFromLayer(io.intino.tara.magritte.Layer layer) {
		return dialogReferenceFromNode(layer.core$());
	}

	private static io.intino.konos.alexandria.activity.schemas.DialogReference dialogReferenceFromNode(io.intino.tara.magritte.Node node) {
		io.intino.konos.alexandria.activity.schemas.DialogReference schema = new io.intino.konos.alexandria.activity.schemas.DialogReference();
		final java.util.Map<String, java.util.List<?>> variables = node.variables();
		variables.put("name", java.util.Collections.singletonList(node.name()));
		variables.put("id", java.util.Collections.singletonList(node.id()));
		for (String variable : variables.keySet()) {
			if (variable.equals("location")) schema.location((String) variables.get(variable).get(0).toString());
			if (variable.equals("width")) schema.width((Integer) variables.get(variable).get(0));
			if (variable.equals("height")) schema.height((Integer) variables.get(variable).get(0));
		}

		return schema;
	}


	public static io.intino.konos.alexandria.activity.schemas.DownloadItemParameters downloadItemParametersFromLayer(io.intino.tara.magritte.Layer layer) {
		return downloadItemParametersFromNode(layer.core$());
	}

	private static io.intino.konos.alexandria.activity.schemas.DownloadItemParameters downloadItemParametersFromNode(io.intino.tara.magritte.Node node) {
		io.intino.konos.alexandria.activity.schemas.DownloadItemParameters schema = new io.intino.konos.alexandria.activity.schemas.DownloadItemParameters();
		final java.util.Map<String, java.util.List<?>> variables = node.variables();
		variables.put("name", java.util.Collections.singletonList(node.name()));
		variables.put("id", java.util.Collections.singletonList(node.id()));
		for (String variable : variables.keySet()) {
			if (variable.equals("stamp")) schema.stamp((String) variables.get(variable).get(0).toString());
			if (variable.equals("option")) schema.option((String) variables.get(variable).get(0).toString());
		}

		return schema;
	}


	public static io.intino.konos.alexandria.activity.schemas.ElementOperationParameters elementOperationParametersFromLayer(io.intino.tara.magritte.Layer layer) {
		return elementOperationParametersFromNode(layer.core$());
	}

	private static io.intino.konos.alexandria.activity.schemas.ElementOperationParameters elementOperationParametersFromNode(io.intino.tara.magritte.Node node) {
		io.intino.konos.alexandria.activity.schemas.ElementOperationParameters schema = new io.intino.konos.alexandria.activity.schemas.ElementOperationParameters();
		final java.util.Map<String, java.util.List<?>> variables = node.variables();
		variables.put("name", java.util.Collections.singletonList(node.name()));
		variables.put("id", java.util.Collections.singletonList(node.id()));
		for (String variable : variables.keySet()) {
			if (variable.equals("operation")) schema.operation((String) variables.get(variable).get(0).toString());
			if (variable.equals("option")) schema.option((String) variables.get(variable).get(0).toString());
			if (variable.equals("from")) schema.from((java.time.Instant) variables.get(variable).get(0));
			if (variable.equals("to")) schema.to((java.time.Instant) variables.get(variable).get(0));
		}

		return schema;
	}


	public static io.intino.konos.alexandria.activity.schemas.ElementView elementViewFromLayer(io.intino.tara.magritte.Layer layer) {
		return elementViewFromNode(layer.core$());
	}

	private static io.intino.konos.alexandria.activity.schemas.ElementView elementViewFromNode(io.intino.tara.magritte.Node node) {
		io.intino.konos.alexandria.activity.schemas.ElementView schema = new io.intino.konos.alexandria.activity.schemas.ElementView();
		final java.util.Map<String, java.util.List<?>> variables = node.variables();
		variables.put("name", java.util.Collections.singletonList(node.name()));
		variables.put("id", java.util.Collections.singletonList(node.id()));
		for (String variable : variables.keySet()) {
			if (variable.equals("name")) schema.name((String) variables.get(variable).get(0).toString());
			if (variable.equals("label")) schema.label((String) variables.get(variable).get(0).toString());
			if (variable.equals("type")) schema.type((String) variables.get(variable).get(0).toString());
			if (variable.equals("embeddedElement")) schema.embeddedElement((Boolean) variables.get(variable).get(0));
			if (variable.equals("width")) schema.width((Integer) variables.get(variable).get(0));
			if (variable.equals("canSearch")) schema.canSearch((Boolean) variables.get(variable).get(0));
			if (variable.equals("canCreateClusters")) schema.canCreateClusters((Boolean) variables.get(variable).get(0));
			if (variable.equals("clusters")) schema.clusters().addAll(((java.util.List<String>) variables.get(variable)));
			if (variable.equals("emptyMessage")) schema.emptyMessage((String) variables.get(variable).get(0).toString());
		}
		for (io.intino.tara.magritte.Node component : node.componentList()) {
			if (component.name().equalsIgnoreCase(io.intino.konos.alexandria.activity.schemas.Toolbar.class.getSimpleName())) schema.toolbar((toolbarFromNode(component)));
			if (component.name().equalsIgnoreCase(io.intino.konos.alexandria.activity.schemas.Mold.class.getSimpleName())) schema.mold((moldFromNode(component)));
			if (component.name().equalsIgnoreCase(io.intino.konos.alexandria.activity.schemas.Center.class.getSimpleName())) schema.center((centerFromNode(component)));
			if (component.name().equalsIgnoreCase(io.intino.konos.alexandria.activity.schemas.Zoom.class.getSimpleName())) schema.zoom((zoomFromNode(component)));
		}
		return schema;
	}


	public static io.intino.konos.alexandria.activity.schemas.Toolbar toolbarFromLayer(io.intino.tara.magritte.Layer layer) {
		return toolbarFromNode(layer.core$());
	}

	private static io.intino.konos.alexandria.activity.schemas.Toolbar toolbarFromNode(io.intino.tara.magritte.Node node) {
		io.intino.konos.alexandria.activity.schemas.Toolbar schema = new io.intino.konos.alexandria.activity.schemas.Toolbar();
		final java.util.Map<String, java.util.List<?>> variables = node.variables();
		variables.put("name", java.util.Collections.singletonList(node.name()));
		variables.put("id", java.util.Collections.singletonList(node.id()));

		for (io.intino.tara.magritte.Node component : node.componentList()) {
			if (component.name().equalsIgnoreCase(io.intino.konos.alexandria.activity.schemas.Operation.class.getSimpleName())) schema.operationList().add(operationFromNode(component));
		}
		return schema;
	}


	public static io.intino.konos.alexandria.activity.schemas.Operation operationFromLayer(io.intino.tara.magritte.Layer layer) {
		return operationFromNode(layer.core$());
	}

	private static io.intino.konos.alexandria.activity.schemas.Operation operationFromNode(io.intino.tara.magritte.Node node) {
		io.intino.konos.alexandria.activity.schemas.Operation schema = new io.intino.konos.alexandria.activity.schemas.Operation();
		final java.util.Map<String, java.util.List<?>> variables = node.variables();
		variables.put("name", java.util.Collections.singletonList(node.name()));
		variables.put("id", java.util.Collections.singletonList(node.id()));
		for (String variable : variables.keySet()) {
			if (variable.equals("name")) schema.name((String) variables.get(variable).get(0).toString());
			if (variable.equals("title")) schema.title((String) variables.get(variable).get(0).toString());
			if (variable.equals("type")) schema.type((String) variables.get(variable).get(0).toString());
			if (variable.equals("icon")) schema.icon((String) variables.get(variable).get(0).toString());
			if (variable.equals("when")) schema.when((String) variables.get(variable).get(0).toString());
		}
		for (io.intino.tara.magritte.Node component : node.componentList()) {
			if (component.name().equalsIgnoreCase(io.intino.konos.alexandria.activity.schemas.Property.class.getSimpleName())) schema.propertyList().add(propertyFromNode(component));
		}
		return schema;
	}


	public static io.intino.konos.alexandria.activity.schemas.Mold moldFromLayer(io.intino.tara.magritte.Layer layer) {
		return moldFromNode(layer.core$());
	}

	private static io.intino.konos.alexandria.activity.schemas.Mold moldFromNode(io.intino.tara.magritte.Node node) {
		io.intino.konos.alexandria.activity.schemas.Mold schema = new io.intino.konos.alexandria.activity.schemas.Mold();
		final java.util.Map<String, java.util.List<?>> variables = node.variables();
		variables.put("name", java.util.Collections.singletonList(node.name()));
		variables.put("id", java.util.Collections.singletonList(node.id()));

		for (io.intino.tara.magritte.Node component : node.componentList()) {
			if (component.name().equalsIgnoreCase(io.intino.konos.alexandria.activity.schemas.MoldBlock.class.getSimpleName())) schema.moldBlockList().add(moldBlockFromNode(component));
		}
		return schema;
	}


	public static io.intino.konos.alexandria.activity.schemas.Center centerFromLayer(io.intino.tara.magritte.Layer layer) {
		return centerFromNode(layer.core$());
	}

	private static io.intino.konos.alexandria.activity.schemas.Center centerFromNode(io.intino.tara.magritte.Node node) {
		io.intino.konos.alexandria.activity.schemas.Center schema = new io.intino.konos.alexandria.activity.schemas.Center();
		final java.util.Map<String, java.util.List<?>> variables = node.variables();
		variables.put("name", java.util.Collections.singletonList(node.name()));
		variables.put("id", java.util.Collections.singletonList(node.id()));
		for (String variable : variables.keySet()) {
			if (variable.equals("latitude")) schema.latitude((Double) variables.get(variable).get(0));
			if (variable.equals("longitude")) schema.longitude((Double) variables.get(variable).get(0));
		}

		return schema;
	}


	public static io.intino.konos.alexandria.activity.schemas.Zoom zoomFromLayer(io.intino.tara.magritte.Layer layer) {
		return zoomFromNode(layer.core$());
	}

	private static io.intino.konos.alexandria.activity.schemas.Zoom zoomFromNode(io.intino.tara.magritte.Node node) {
		io.intino.konos.alexandria.activity.schemas.Zoom schema = new io.intino.konos.alexandria.activity.schemas.Zoom();
		final java.util.Map<String, java.util.List<?>> variables = node.variables();
		variables.put("name", java.util.Collections.singletonList(node.name()));
		variables.put("id", java.util.Collections.singletonList(node.id()));
		for (String variable : variables.keySet()) {
			if (variable.equals("min")) schema.min((Integer) variables.get(variable).get(0));
			if (variable.equals("max")) schema.max((Integer) variables.get(variable).get(0));
			if (variable.equals("defaultValue")) schema.defaultValue((Integer) variables.get(variable).get(0));
		}

		return schema;
	}


	public static io.intino.konos.alexandria.activity.schemas.ExecuteItemTaskParameters executeItemTaskParametersFromLayer(io.intino.tara.magritte.Layer layer) {
		return executeItemTaskParametersFromNode(layer.core$());
	}

	private static io.intino.konos.alexandria.activity.schemas.ExecuteItemTaskParameters executeItemTaskParametersFromNode(io.intino.tara.magritte.Node node) {
		io.intino.konos.alexandria.activity.schemas.ExecuteItemTaskParameters schema = new io.intino.konos.alexandria.activity.schemas.ExecuteItemTaskParameters();
		final java.util.Map<String, java.util.List<?>> variables = node.variables();
		variables.put("name", java.util.Collections.singletonList(node.name()));
		variables.put("id", java.util.Collections.singletonList(node.id()));
		for (String variable : variables.keySet()) {
			if (variable.equals("item")) schema.item((String) variables.get(variable).get(0).toString());
			if (variable.equals("stamp")) schema.stamp((String) variables.get(variable).get(0).toString());
		}

		return schema;
	}


	public static io.intino.konos.alexandria.activity.schemas.ExportItemParameters exportItemParametersFromLayer(io.intino.tara.magritte.Layer layer) {
		return exportItemParametersFromNode(layer.core$());
	}

	private static io.intino.konos.alexandria.activity.schemas.ExportItemParameters exportItemParametersFromNode(io.intino.tara.magritte.Node node) {
		io.intino.konos.alexandria.activity.schemas.ExportItemParameters schema = new io.intino.konos.alexandria.activity.schemas.ExportItemParameters();
		final java.util.Map<String, java.util.List<?>> variables = node.variables();
		variables.put("name", java.util.Collections.singletonList(node.name()));
		variables.put("id", java.util.Collections.singletonList(node.id()));
		for (String variable : variables.keySet()) {
			if (variable.equals("stamp")) schema.stamp((String) variables.get(variable).get(0).toString());
			if (variable.equals("option")) schema.option((String) variables.get(variable).get(0).toString());
			if (variable.equals("from")) schema.from((java.time.Instant) variables.get(variable).get(0));
			if (variable.equals("to")) schema.to((java.time.Instant) variables.get(variable).get(0));
		}

		return schema;
	}


	public static io.intino.konos.alexandria.activity.schemas.GroupingGroup groupingGroupFromLayer(io.intino.tara.magritte.Layer layer) {
		return groupingGroupFromNode(layer.core$());
	}

	private static io.intino.konos.alexandria.activity.schemas.GroupingGroup groupingGroupFromNode(io.intino.tara.magritte.Node node) {
		io.intino.konos.alexandria.activity.schemas.GroupingGroup schema = new io.intino.konos.alexandria.activity.schemas.GroupingGroup();
		final java.util.Map<String, java.util.List<?>> variables = node.variables();
		variables.put("name", java.util.Collections.singletonList(node.name()));
		variables.put("id", java.util.Collections.singletonList(node.id()));
		for (String variable : variables.keySet()) {
			if (variable.equals("grouping")) schema.grouping((String) variables.get(variable).get(0).toString());
			if (variable.equals("name")) schema.name((String) variables.get(variable).get(0).toString());
		}

		return schema;
	}


	public static io.intino.konos.alexandria.activity.schemas.GroupingSelection groupingSelectionFromLayer(io.intino.tara.magritte.Layer layer) {
		return groupingSelectionFromNode(layer.core$());
	}

	private static io.intino.konos.alexandria.activity.schemas.GroupingSelection groupingSelectionFromNode(io.intino.tara.magritte.Node node) {
		io.intino.konos.alexandria.activity.schemas.GroupingSelection schema = new io.intino.konos.alexandria.activity.schemas.GroupingSelection();
		final java.util.Map<String, java.util.List<?>> variables = node.variables();
		variables.put("name", java.util.Collections.singletonList(node.name()));
		variables.put("id", java.util.Collections.singletonList(node.id()));
		for (String variable : variables.keySet()) {
			if (variable.equals("name")) schema.name((String) variables.get(variable).get(0).toString());
			if (variable.equals("groups")) schema.groups().addAll(((java.util.List<String>) variables.get(variable)));
		}

		return schema;
	}


	public static io.intino.konos.alexandria.activity.schemas.Item itemFromLayer(io.intino.tara.magritte.Layer layer) {
		return itemFromNode(layer.core$());
	}

	private static io.intino.konos.alexandria.activity.schemas.Item itemFromNode(io.intino.tara.magritte.Node node) {
		io.intino.konos.alexandria.activity.schemas.Item schema = new io.intino.konos.alexandria.activity.schemas.Item();
		final java.util.Map<String, java.util.List<?>> variables = node.variables();
		variables.put("name", java.util.Collections.singletonList(node.name()));
		variables.put("id", java.util.Collections.singletonList(node.id()));
		for (String variable : variables.keySet()) {
			if (variable.equals("name")) schema.name((String) variables.get(variable).get(0).toString());
			if (variable.equals("label")) schema.label((String) variables.get(variable).get(0).toString());
			if (variable.equals("group")) schema.group((java.time.Instant) variables.get(variable).get(0));
		}
		for (io.intino.tara.magritte.Node component : node.componentList()) {
			if (component.name().equalsIgnoreCase(io.intino.konos.alexandria.activity.schemas.ItemStamp.class.getSimpleName())) schema.itemStampList().add(itemStampFromNode(component));
			if (component.name().equalsIgnoreCase(io.intino.konos.alexandria.activity.schemas.ItemBlock.class.getSimpleName())) schema.itemBlockList().add(itemBlockFromNode(component));
		}
		return schema;
	}


	public static io.intino.konos.alexandria.activity.schemas.ItemStamp itemStampFromLayer(io.intino.tara.magritte.Layer layer) {
		return itemStampFromNode(layer.core$());
	}

	private static io.intino.konos.alexandria.activity.schemas.ItemStamp itemStampFromNode(io.intino.tara.magritte.Node node) {
		io.intino.konos.alexandria.activity.schemas.ItemStamp schema = new io.intino.konos.alexandria.activity.schemas.ItemStamp();
		final java.util.Map<String, java.util.List<?>> variables = node.variables();
		variables.put("name", java.util.Collections.singletonList(node.name()));
		variables.put("id", java.util.Collections.singletonList(node.id()));
		for (String variable : variables.keySet()) {
			if (variable.equals("name")) schema.name((String) variables.get(variable).get(0).toString());
			if (variable.equals("values")) schema.values().addAll(((java.util.List<String>) variables.get(variable)));
		}
		for (io.intino.tara.magritte.Node component : node.componentList()) {
			if (component.name().equalsIgnoreCase(io.intino.konos.alexandria.activity.schemas.Property.class.getSimpleName())) schema.propertyList().add(propertyFromNode(component));
		}
		return schema;
	}


	public static io.intino.konos.alexandria.activity.schemas.ItemBlock itemBlockFromLayer(io.intino.tara.magritte.Layer layer) {
		return itemBlockFromNode(layer.core$());
	}

	private static io.intino.konos.alexandria.activity.schemas.ItemBlock itemBlockFromNode(io.intino.tara.magritte.Node node) {
		io.intino.konos.alexandria.activity.schemas.ItemBlock schema = new io.intino.konos.alexandria.activity.schemas.ItemBlock();
		final java.util.Map<String, java.util.List<?>> variables = node.variables();
		variables.put("name", java.util.Collections.singletonList(node.name()));
		variables.put("id", java.util.Collections.singletonList(node.id()));
		for (String variable : variables.keySet()) {
			if (variable.equals("name")) schema.name((String) variables.get(variable).get(0).toString());
			if (variable.equals("hidden")) schema.hidden((Boolean) variables.get(variable).get(0));
		}

		return schema;
	}


	public static io.intino.konos.alexandria.activity.schemas.ItemRefreshInfo itemRefreshInfoFromLayer(io.intino.tara.magritte.Layer layer) {
		return itemRefreshInfoFromNode(layer.core$());
	}

	private static io.intino.konos.alexandria.activity.schemas.ItemRefreshInfo itemRefreshInfoFromNode(io.intino.tara.magritte.Node node) {
		io.intino.konos.alexandria.activity.schemas.ItemRefreshInfo schema = new io.intino.konos.alexandria.activity.schemas.ItemRefreshInfo();
		final java.util.Map<String, java.util.List<?>> variables = node.variables();
		variables.put("name", java.util.Collections.singletonList(node.name()));
		variables.put("id", java.util.Collections.singletonList(node.id()));

		for (io.intino.tara.magritte.Node component : node.componentList()) {
			if (component.name().equalsIgnoreCase(io.intino.konos.alexandria.activity.schemas.Mold.class.getSimpleName())) schema.mold((moldFromNode(component)));
			if (component.name().equalsIgnoreCase(io.intino.konos.alexandria.activity.schemas.Item.class.getSimpleName())) schema.item((itemFromNode(component)));
		}
		return schema;
	}


	public static io.intino.konos.alexandria.activity.schemas.MoldBlock moldBlockFromLayer(io.intino.tara.magritte.Layer layer) {
		return moldBlockFromNode(layer.core$());
	}

	private static io.intino.konos.alexandria.activity.schemas.MoldBlock moldBlockFromNode(io.intino.tara.magritte.Node node) {
		io.intino.konos.alexandria.activity.schemas.MoldBlock schema = new io.intino.konos.alexandria.activity.schemas.MoldBlock();
		final java.util.Map<String, java.util.List<?>> variables = node.variables();
		variables.put("name", java.util.Collections.singletonList(node.name()));
		variables.put("id", java.util.Collections.singletonList(node.id()));
		for (String variable : variables.keySet()) {
			if (variable.equals("name")) schema.name((String) variables.get(variable).get(0).toString());
			if (variable.equals("expanded")) schema.expanded((Boolean) variables.get(variable).get(0));
			if (variable.equals("layout")) schema.layout((String) variables.get(variable).get(0).toString());
			if (variable.equals("style")) schema.style((String) variables.get(variable).get(0).toString());
			if (variable.equals("width")) schema.width((Integer) variables.get(variable).get(0));
			if (variable.equals("height")) schema.height((Integer) variables.get(variable).get(0));
			if (variable.equals("hidden")) schema.hidden((Boolean) variables.get(variable).get(0));
			if (variable.equals("hiddenIfMobile")) schema.hiddenIfMobile((Boolean) variables.get(variable).get(0));
		}
		for (io.intino.tara.magritte.Node component : node.componentList()) {
			if (component.name().equalsIgnoreCase(io.intino.konos.alexandria.activity.schemas.Stamp.class.getSimpleName())) schema.stampList().add(stampFromNode(component));
			if (component.name().equalsIgnoreCase(io.intino.konos.alexandria.activity.schemas.MoldBlock.class.getSimpleName())) schema.moldBlockList().add(moldBlockFromNode(component));
		}
		return schema;
	}


	public static io.intino.konos.alexandria.activity.schemas.Stamp stampFromLayer(io.intino.tara.magritte.Layer layer) {
		return stampFromNode(layer.core$());
	}

	private static io.intino.konos.alexandria.activity.schemas.Stamp stampFromNode(io.intino.tara.magritte.Node node) {
		io.intino.konos.alexandria.activity.schemas.Stamp schema = new io.intino.konos.alexandria.activity.schemas.Stamp();
		final java.util.Map<String, java.util.List<?>> variables = node.variables();
		variables.put("name", java.util.Collections.singletonList(node.name()));
		variables.put("id", java.util.Collections.singletonList(node.id()));
		for (String variable : variables.keySet()) {
			if (variable.equals("name")) schema.name((String) variables.get(variable).get(0).toString());
			if (variable.equals("label")) schema.label((String) variables.get(variable).get(0).toString());
			if (variable.equals("editable")) schema.editable((Boolean) variables.get(variable).get(0));
			if (variable.equals("shape")) schema.shape((String) variables.get(variable).get(0).toString());
			if (variable.equals("layout")) schema.layout((String) variables.get(variable).get(0).toString());
			if (variable.equals("height")) schema.height((Integer) variables.get(variable).get(0));
		}
		for (io.intino.tara.magritte.Node component : node.componentList()) {
			if (component.name().equalsIgnoreCase(io.intino.konos.alexandria.activity.schemas.Property.class.getSimpleName())) schema.propertyList().add(propertyFromNode(component));
		}
		return schema;
	}


	public static io.intino.konos.alexandria.activity.schemas.OpenItemDialogParameters openItemDialogParametersFromLayer(io.intino.tara.magritte.Layer layer) {
		return openItemDialogParametersFromNode(layer.core$());
	}

	private static io.intino.konos.alexandria.activity.schemas.OpenItemDialogParameters openItemDialogParametersFromNode(io.intino.tara.magritte.Node node) {
		io.intino.konos.alexandria.activity.schemas.OpenItemDialogParameters schema = new io.intino.konos.alexandria.activity.schemas.OpenItemDialogParameters();
		final java.util.Map<String, java.util.List<?>> variables = node.variables();
		variables.put("name", java.util.Collections.singletonList(node.name()));
		variables.put("id", java.util.Collections.singletonList(node.id()));
		for (String variable : variables.keySet()) {
			if (variable.equals("item")) schema.item((String) variables.get(variable).get(0).toString());
			if (variable.equals("stamp")) schema.stamp((String) variables.get(variable).get(0).toString());
		}

		return schema;
	}


	public static io.intino.konos.alexandria.activity.schemas.PageLocation pageLocationFromLayer(io.intino.tara.magritte.Layer layer) {
		return pageLocationFromNode(layer.core$());
	}

	private static io.intino.konos.alexandria.activity.schemas.PageLocation pageLocationFromNode(io.intino.tara.magritte.Node node) {
		io.intino.konos.alexandria.activity.schemas.PageLocation schema = new io.intino.konos.alexandria.activity.schemas.PageLocation();
		final java.util.Map<String, java.util.List<?>> variables = node.variables();
		variables.put("name", java.util.Collections.singletonList(node.name()));
		variables.put("id", java.util.Collections.singletonList(node.id()));
		for (String variable : variables.keySet()) {
			if (variable.equals("value")) schema.value((String) variables.get(variable).get(0).toString());
			if (variable.equals("internal")) schema.internal((Boolean) variables.get(variable).get(0));
		}

		return schema;
	}


	public static io.intino.konos.alexandria.activity.schemas.PictureData pictureDataFromLayer(io.intino.tara.magritte.Layer layer) {
		return pictureDataFromNode(layer.core$());
	}

	private static io.intino.konos.alexandria.activity.schemas.PictureData pictureDataFromNode(io.intino.tara.magritte.Node node) {
		io.intino.konos.alexandria.activity.schemas.PictureData schema = new io.intino.konos.alexandria.activity.schemas.PictureData();
		final java.util.Map<String, java.util.List<?>> variables = node.variables();
		variables.put("name", java.util.Collections.singletonList(node.name()));
		variables.put("id", java.util.Collections.singletonList(node.id()));
		for (String variable : variables.keySet()) {
			if (variable.equals("item")) schema.item((String) variables.get(variable).get(0).toString());
			if (variable.equals("stamp")) schema.stamp((String) variables.get(variable).get(0).toString());
			if (variable.equals("value")) schema.value((String) variables.get(variable).get(0).toString());
		}

		return schema;
	}


	public static io.intino.konos.alexandria.activity.schemas.PlatformInfo platformInfoFromLayer(io.intino.tara.magritte.Layer layer) {
		return platformInfoFromNode(layer.core$());
	}

	private static io.intino.konos.alexandria.activity.schemas.PlatformInfo platformInfoFromNode(io.intino.tara.magritte.Node node) {
		io.intino.konos.alexandria.activity.schemas.PlatformInfo schema = new io.intino.konos.alexandria.activity.schemas.PlatformInfo();
		final java.util.Map<String, java.util.List<?>> variables = node.variables();
		variables.put("name", java.util.Collections.singletonList(node.name()));
		variables.put("id", java.util.Collections.singletonList(node.id()));
		for (String variable : variables.keySet()) {
			if (variable.equals("title")) schema.title((String) variables.get(variable).get(0).toString());
			if (variable.equals("subtitle")) schema.subtitle((String) variables.get(variable).get(0).toString());
			if (variable.equals("logo")) schema.logo((String) variables.get(variable).get(0).toString());
			if (variable.equals("favicon")) schema.favicon((String) variables.get(variable).get(0).toString());
			if (variable.equals("authServiceUrl")) schema.authServiceUrl((String) variables.get(variable).get(0).toString());
		}

		return schema;
	}


	public static io.intino.konos.alexandria.activity.schemas.Property propertyFromLayer(io.intino.tara.magritte.Layer layer) {
		return propertyFromNode(layer.core$());
	}

	private static io.intino.konos.alexandria.activity.schemas.Property propertyFromNode(io.intino.tara.magritte.Node node) {
		io.intino.konos.alexandria.activity.schemas.Property schema = new io.intino.konos.alexandria.activity.schemas.Property();
		final java.util.Map<String, java.util.List<?>> variables = node.variables();
		variables.put("name", java.util.Collections.singletonList(node.name()));
		variables.put("id", java.util.Collections.singletonList(node.id()));
		for (String variable : variables.keySet()) {
			if (variable.equals("name")) schema.name((String) variables.get(variable).get(0).toString());
			if (variable.equals("value")) schema.value((String) variables.get(variable).get(0).toString());
		}

		return schema;
	}


	public static io.intino.konos.alexandria.activity.schemas.Range rangeFromLayer(io.intino.tara.magritte.Layer layer) {
		return rangeFromNode(layer.core$());
	}

	private static io.intino.konos.alexandria.activity.schemas.Range rangeFromNode(io.intino.tara.magritte.Node node) {
		io.intino.konos.alexandria.activity.schemas.Range schema = new io.intino.konos.alexandria.activity.schemas.Range();
		final java.util.Map<String, java.util.List<?>> variables = node.variables();
		variables.put("name", java.util.Collections.singletonList(node.name()));
		variables.put("id", java.util.Collections.singletonList(node.id()));
		for (String variable : variables.keySet()) {
			if (variable.equals("min")) schema.min((Double) variables.get(variable).get(0));
			if (variable.equals("max")) schema.max((Double) variables.get(variable).get(0));
			if (variable.equals("scale")) schema.scale((String) variables.get(variable).get(0).toString());
		}

		return schema;
	}


	public static io.intino.konos.alexandria.activity.schemas.Reference referenceFromLayer(io.intino.tara.magritte.Layer layer) {
		return referenceFromNode(layer.core$());
	}

	private static io.intino.konos.alexandria.activity.schemas.Reference referenceFromNode(io.intino.tara.magritte.Node node) {
		io.intino.konos.alexandria.activity.schemas.Reference schema = new io.intino.konos.alexandria.activity.schemas.Reference();
		final java.util.Map<String, java.util.List<?>> variables = node.variables();
		variables.put("name", java.util.Collections.singletonList(node.name()));
		variables.put("id", java.util.Collections.singletonList(node.id()));
		for (String variable : variables.keySet()) {
			if (variable.equals("name")) schema.name((String) variables.get(variable).get(0).toString());
			if (variable.equals("label")) schema.label((String) variables.get(variable).get(0).toString());
		}
		for (io.intino.tara.magritte.Node component : node.componentList()) {
			if (component.name().equalsIgnoreCase(io.intino.konos.alexandria.activity.schemas.ReferenceProperty.class.getSimpleName())) schema.referencePropertyList().add(referencePropertyFromNode(component));
		}
		return schema;
	}


	public static io.intino.konos.alexandria.activity.schemas.ReferenceProperty referencePropertyFromLayer(io.intino.tara.magritte.Layer layer) {
		return referencePropertyFromNode(layer.core$());
	}

	private static io.intino.konos.alexandria.activity.schemas.ReferenceProperty referencePropertyFromNode(io.intino.tara.magritte.Node node) {
		io.intino.konos.alexandria.activity.schemas.ReferenceProperty schema = new io.intino.konos.alexandria.activity.schemas.ReferenceProperty();
		final java.util.Map<String, java.util.List<?>> variables = node.variables();
		variables.put("name", java.util.Collections.singletonList(node.name()));
		variables.put("id", java.util.Collections.singletonList(node.id()));
		for (String variable : variables.keySet()) {
			if (variable.equals("name")) schema.name((String) variables.get(variable).get(0).toString());
			if (variable.equals("value")) schema.value((String) variables.get(variable).get(0).toString());
		}

		return schema;
	}


	public static io.intino.konos.alexandria.activity.schemas.RequestRange requestRangeFromLayer(io.intino.tara.magritte.Layer layer) {
		return requestRangeFromNode(layer.core$());
	}

	private static io.intino.konos.alexandria.activity.schemas.RequestRange requestRangeFromNode(io.intino.tara.magritte.Node node) {
		io.intino.konos.alexandria.activity.schemas.RequestRange schema = new io.intino.konos.alexandria.activity.schemas.RequestRange();
		final java.util.Map<String, java.util.List<?>> variables = node.variables();
		variables.put("name", java.util.Collections.singletonList(node.name()));
		variables.put("id", java.util.Collections.singletonList(node.id()));
		for (String variable : variables.keySet()) {
			if (variable.equals("from")) schema.from((java.time.Instant) variables.get(variable).get(0));
			if (variable.equals("to")) schema.to((java.time.Instant) variables.get(variable).get(0));
		}

		return schema;
	}


	public static io.intino.konos.alexandria.activity.schemas.Resource resourceFromLayer(io.intino.tara.magritte.Layer layer) {
		return resourceFromNode(layer.core$());
	}

	private static io.intino.konos.alexandria.activity.schemas.Resource resourceFromNode(io.intino.tara.magritte.Node node) {
		io.intino.konos.alexandria.activity.schemas.Resource schema = new io.intino.konos.alexandria.activity.schemas.Resource();
		final java.util.Map<String, java.util.List<?>> variables = node.variables();
		variables.put("name", java.util.Collections.singletonList(node.name()));
		variables.put("id", java.util.Collections.singletonList(node.id()));
		for (String variable : variables.keySet()) {
			if (variable.equals("name")) schema.name((String) variables.get(variable).get(0).toString());
			if (variable.equals("value")) schema.value((String) variables.get(variable).get(0).toString());
		}

		return schema;
	}


	public static io.intino.konos.alexandria.activity.schemas.SaveItemParameters saveItemParametersFromLayer(io.intino.tara.magritte.Layer layer) {
		return saveItemParametersFromNode(layer.core$());
	}

	private static io.intino.konos.alexandria.activity.schemas.SaveItemParameters saveItemParametersFromNode(io.intino.tara.magritte.Node node) {
		io.intino.konos.alexandria.activity.schemas.SaveItemParameters schema = new io.intino.konos.alexandria.activity.schemas.SaveItemParameters();
		final java.util.Map<String, java.util.List<?>> variables = node.variables();
		variables.put("name", java.util.Collections.singletonList(node.name()));
		variables.put("id", java.util.Collections.singletonList(node.id()));
		for (String variable : variables.keySet()) {
			if (variable.equals("stamp")) schema.stamp((String) variables.get(variable).get(0).toString());
			if (variable.equals("item")) schema.item((String) variables.get(variable).get(0).toString());
			if (variable.equals("value")) schema.value((String) variables.get(variable).get(0).toString());
		}

		return schema;
	}


	public static io.intino.konos.alexandria.activity.schemas.Scale scaleFromLayer(io.intino.tara.magritte.Layer layer) {
		return scaleFromNode(layer.core$());
	}

	private static io.intino.konos.alexandria.activity.schemas.Scale scaleFromNode(io.intino.tara.magritte.Node node) {
		io.intino.konos.alexandria.activity.schemas.Scale schema = new io.intino.konos.alexandria.activity.schemas.Scale();
		final java.util.Map<String, java.util.List<?>> variables = node.variables();
		variables.put("name", java.util.Collections.singletonList(node.name()));
		variables.put("id", java.util.Collections.singletonList(node.id()));
		for (String variable : variables.keySet()) {
			if (variable.equals("name")) schema.name((String) variables.get(variable).get(0).toString());
			if (variable.equals("label")) schema.label((String) variables.get(variable).get(0).toString());
			if (variable.equals("symbol")) schema.symbol((String) variables.get(variable).get(0).toString());
		}

		return schema;
	}


	public static io.intino.konos.alexandria.activity.schemas.Sorting sortingFromLayer(io.intino.tara.magritte.Layer layer) {
		return sortingFromNode(layer.core$());
	}

	private static io.intino.konos.alexandria.activity.schemas.Sorting sortingFromNode(io.intino.tara.magritte.Node node) {
		io.intino.konos.alexandria.activity.schemas.Sorting schema = new io.intino.konos.alexandria.activity.schemas.Sorting();
		final java.util.Map<String, java.util.List<?>> variables = node.variables();
		variables.put("name", java.util.Collections.singletonList(node.name()));
		variables.put("id", java.util.Collections.singletonList(node.id()));
		for (String variable : variables.keySet()) {
			if (variable.equals("name")) schema.name((String) variables.get(variable).get(0).toString());
			if (variable.equals("label")) schema.label((String) variables.get(variable).get(0).toString());
			if (variable.equals("mode")) schema.mode((String) variables.get(variable).get(0).toString());
		}

		return schema;
	}


	public static io.intino.konos.alexandria.activity.schemas.UserInfo userInfoFromLayer(io.intino.tara.magritte.Layer layer) {
		return userInfoFromNode(layer.core$());
	}

	private static io.intino.konos.alexandria.activity.schemas.UserInfo userInfoFromNode(io.intino.tara.magritte.Node node) {
		io.intino.konos.alexandria.activity.schemas.UserInfo schema = new io.intino.konos.alexandria.activity.schemas.UserInfo();
		final java.util.Map<String, java.util.List<?>> variables = node.variables();
		variables.put("name", java.util.Collections.singletonList(node.name()));
		variables.put("id", java.util.Collections.singletonList(node.id()));
		for (String variable : variables.keySet()) {
			if (variable.equals("fullName")) schema.fullName((String) variables.get(variable).get(0).toString());
			if (variable.equals("photo")) schema.photo((String) variables.get(variable).get(0).toString());
		}

		return schema;
	}


	public static io.intino.konos.alexandria.activity.schemas.Validation validationFromLayer(io.intino.tara.magritte.Layer layer) {
		return validationFromNode(layer.core$());
	}

	private static io.intino.konos.alexandria.activity.schemas.Validation validationFromNode(io.intino.tara.magritte.Node node) {
		io.intino.konos.alexandria.activity.schemas.Validation schema = new io.intino.konos.alexandria.activity.schemas.Validation();
		final java.util.Map<String, java.util.List<?>> variables = node.variables();
		variables.put("name", java.util.Collections.singletonList(node.name()));
		variables.put("id", java.util.Collections.singletonList(node.id()));
		for (String variable : variables.keySet()) {
			if (variable.equals("input")) schema.input((String) variables.get(variable).get(0).toString());
			if (variable.equals("status")) schema.status((Boolean) variables.get(variable).get(0));
			if (variable.equals("message")) schema.message((String) variables.get(variable).get(0).toString());
			if (variable.equals("modifiedInputs")) schema.modifiedInputs((String) variables.get(variable).get(0).toString());
		}

		return schema;
	}

}

