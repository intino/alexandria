package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.Scale;
import io.intino.alexandria.core.Box;
import io.intino.alexandria.schemas.EventlineEvent;
import io.intino.alexandria.schemas.EventlineEventGroup;
import io.intino.alexandria.schemas.EventlineSetup;
import io.intino.alexandria.schemas.EventlineToolbarInfo;
import io.intino.alexandria.ui.displays.notifiers.EventlineNotifier;
import io.intino.alexandria.ui.model.ScaleFormatter;
import io.intino.alexandria.ui.model.eventline.EventlineDatasource;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.*;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Eventline<DN extends EventlineNotifier, B extends Box> extends AbstractEventline<B> {
	private EventlineDatasource source;
	private int stepsCount = DefaultStepsCount;
	private Arrangement arrangement = Arrangement.Horizontal;
	private long page;
	private final Set<Long> loadedPages = new HashSet<>();

	private static final int DefaultStepsCount = 24;

	public enum Arrangement { Vertical, Horizontal }

	public Eventline(B box) {
		super(box);
	}

	public <DS extends EventlineDatasource> Eventline<DN, B> source(DS source) {
		this.source = source;
		return this;
	}

	public void select(Instant instant) {
		page(pageOf(instant));
	}

	public void page(long page) {
		this.page = page;
		if (page < 0) page = 0;
		if (page > countPages()) page = countPages()-1;
		resetPages();
		refreshToolbar();
		notifier.addEventsAfter(events());
	}

	public void first() {
		page = 0;
		resetPages();
		refreshToolbar();
		notifier.addEventsAfter(events());
	}

	public void previous() {
		if (page <= 0) return;
		while(page > 0 && loadedPages.contains(page)) page--;
		refreshToolbar();
		if (loadedPages.contains(page)) return;
		if (arrangement == Arrangement.Horizontal) notifier.addEventsBefore(events());
		else notifier.addEventsAfter(events());
	}

	public void next() {
		if (page >= countPages()-1) return;
		while(page < countPages() && loadedPages.contains(page)) page++;
		refreshToolbar();
		if (loadedPages.contains(page)) return;
		if (arrangement == Arrangement.Horizontal) notifier.addEventsAfter(events());
		else notifier.addEventsBefore(events());
	}

	public void last() {
		page = countPages()-1;
		resetPages();
		refreshToolbar();
		notifier.addEventsAfter(events());
	}

	@Override
	public void refresh() {
		super.refresh();
		if (source == null) throw new RuntimeException("Eventline source not defined");
		page = firstPage();
		notifier.setup(new EventlineSetup()
				.name(source.name())
				.label(label())
				.toolbar(toolbar())
				.events(events()));
	}

	protected void _arrangement(Arrangement arrangement) {
		this.arrangement = arrangement;
	}

	private List<EventlineEventGroup> events() {
		Scale scale = selectedScale();
		Instant from = from();
		Instant to = LocalDateTime.ofInstant(from, ZoneId.of("UTC")).plus(stepsCount, scale.temporalUnit()).toInstant(ZoneOffset.UTC);
		loadedPages.add(page);
		return source.events(from, to).entrySet().stream().sorted(eventsComparator()).map(this::schemaOf).collect(Collectors.toList());
	}

	private Comparator<Map.Entry<Instant, List<EventlineDatasource.Event>>> eventsComparator() {
		if (arrangement == Arrangement.Vertical) return (o1, o2) -> o2.getKey().compareTo(o1.getKey());
		return Map.Entry.comparingByKey();
	}

	private EventlineEventGroup schemaOf(Map.Entry<Instant, List<EventlineDatasource.Event>> entry) {
		EventlineEventGroup result = new EventlineEventGroup();
		result.shortDate(ScaleFormatter.shortLabel(entry.getKey(), selectedScale(), language()));
		result.longDate(ScaleFormatter.label(entry.getKey(), selectedScale(), language()));
		result.events(entry.getValue().stream().map(e -> schemaOf(entry.getKey(), e)).collect(Collectors.toList()));
		return result;
	}

	private EventlineEvent schemaOf(Instant date, EventlineDatasource.Event event) {
		EventlineEvent result = new EventlineEvent();
		result.shortDate(ScaleFormatter.shortLabel(date, selectedScale(), language()));
		result.longDate(ScaleFormatter.label(date, selectedScale(), language()));
		result.label(event.label());
		result.description(event.description());
		result.color(event.color());
		result.icon(event.icon());
		return result;
	}

	private Scale selectedScale() {
		return source.scale();
	}

	private void refreshToolbar() {
		notifier.refreshToolbar(toolbar());
	}

	private EventlineToolbarInfo toolbar() {
		Scale scale = selectedScale();
		Instant date = from();
		EventlineToolbarInfo result = new EventlineToolbarInfo();
		result.label(ScaleFormatter.label(date, scale, language()));
		result.page(page);
		result.countPages(countPages());
		result.canPrevious(page > 0);
		result.canNext(page < countPages()-1);
		return result;
	}

	private long countPages() {
		Scale scale = selectedScale();
		long steps = scale.temporalUnit().between(source.from(), source.to());
		return pageOf(steps)+1;
	}

	private int pageOf(long index) {
		return (int) (Math.floor(index / stepsCount) + (index % stepsCount > 0 ? 1 : 0));
	}

	private Instant from() {
		return source.next(source.from(), page*stepsCount);
	}

	private long pageOf(Instant instant) {
		return pageOf(selectedScale().temporalUnit().between(source.from(), instant));
	}

	private long countSteps() {
		return selectedScale().temporalUnit().between(source.from(), source.to());
	}

	private long firstPage() {
		return arrangement == Arrangement.Horizontal ? 0 : countPages()-1;
	}

	private void resetPages() {
		loadedPages.clear();
		notifier.resetEvents();
	}

}