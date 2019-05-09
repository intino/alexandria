package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.displays.events.SelectionEvent;
import io.intino.alexandria.ui.displays.events.SelectionListener;
import io.intino.alexandria.ui.displays.notifiers.BaseGroupingNotifier;
import io.intino.alexandria.ui.model.datasource.Group;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class BaseGrouping<DN extends BaseGroupingNotifier, B extends Box> extends AbstractBaseGrouping<B> {
	private Collection collection;
	private List<String> selection;
	private SelectionListener selectionListener;
	private List<Group> groups = new ArrayList<>();

	public BaseGrouping(B box) {
        super(box);
    }

    public BaseGrouping onSelect(SelectionListener listener) {
    	this.selectionListener = listener;
    	return this;
	}

	public BaseGrouping groups(List<Group> groups) {
    	this.groups = groups;
    	return this;
	}

	public BaseGrouping<DN, B> bindTo(Collection collection) {
		this.collection = collection;
		this.collection.onReady((event) -> loadGroups());
		return this;
	}

	@Override
	public void init() {
		super.init();
		if (groups.size() > 0) refreshGroups();
	}

	public void select(String[] groups) {
		this.selection = Arrays.asList(groups);
		notifySelection();
		if (collection != null) collection.filter(key(), selection);
	}

	private String key() {
		return label() != null ? label() : name();
	}

	private void loadGroups() {
		groups(collection.source().groups(key()));
		refreshGroups();
	}

	private void refreshGroups() {
		notifier.refreshGroups(groups.stream().map(this::groupOf).collect(toList()));
	}

	private io.intino.alexandria.schemas.Group groupOf(Group group) {
		return new io.intino.alexandria.schemas.Group().label(group.label()).count(group.count());
	}

	private void notifySelection() {
		if (selectionListener == null) return;
		selectionListener.accept(new SelectionEvent(this, selection));
	}

}