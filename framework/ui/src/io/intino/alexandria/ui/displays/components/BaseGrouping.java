package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.displays.events.SelectionEvent;
import io.intino.alexandria.ui.displays.events.SelectionListener;
import io.intino.alexandria.ui.displays.notifiers.BaseGroupingNotifier;
import io.intino.alexandria.ui.model.datasource.Group;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static java.util.stream.Collectors.toList;

public class BaseGrouping<DN extends BaseGroupingNotifier, B extends Box> extends AbstractBaseGrouping<DN, B> {
	private List<Collection> collections = new ArrayList<>();
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
		_groups(groups);
		refresh();
		return this;
	}

	public void select(List<String> groups) {
		this.selection = new ArrayList<>(groups);
		notifySelection();
		collections.forEach(c -> c.filter(key(), selection));
	}

	public BaseGrouping<DN, B> bindTo(Collection... collections) {
		this.collections = Arrays.stream(collections).filter(Objects::nonNull).collect(toList());
		if (this.collections.size() > 0) {
			Collection collection = this.collections.get(0);
			if (collection.ready()) loadGroups();
			else collection.onReady((event) -> loadGroups());
		}
		return this;
	}

	@Override
	public void refresh() {
		super.refresh();
		if (groups.size() > 0) refreshGroups();
	}

	protected BaseGrouping _groups(List<Group> groups) {
		this.groups = groups;
		return this;
	}

	private String key() {
		return label() != null && !label().isEmpty() ? label() : name();
	}

	private void loadGroups() {
		if (collections.size() <= 0) return;
		_groups(collections.get(0).source().groups(key()));
		refreshGroups();
	}

	private void refreshGroups() {
		notifier.refreshGroups(groups.stream().map(this::groupOf).collect(toList()));
	}

	private io.intino.alexandria.schemas.Group groupOf(Group group) {
		return new io.intino.alexandria.schemas.Group().label(group.label()).count(group.count()).color(group.color());
	}

	private void notifySelection() {
		notifier.refreshSelection(selection);
		if (selectionListener == null) return;
		selectionListener.accept(new SelectionEvent(this, selection));
	}

}