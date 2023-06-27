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
import io.intino.alexandria.ui.utils.DelayerUtil;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.*;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.time.ZoneOffset.UTC;

public class Eventline<DN extends EventlineNotifier, B extends Box> extends AbstractEventline<B> {
	private EventlineDatasource source;
	private int stepsCount = DefaultStepsCount;
	private Arrangement arrangement = Arrangement.Horizontal;
	private long page;
	private final Set<Long> loadedPages = new HashSet<>();

	private static final int DefaultStepsCount = 8;

	public enum Arrangement { Vertical, Horizontal }

	public Eventline(B box) {
		super(box);
	}

	public <DS extends EventlineDatasource> Eventline<DN, B> source(DS source) {
		this.source = source;
		return this;
	}

	public void select(Instant instant) {
		long page = pageOf(instant);
		Instant min = min(load(page));
		while (min != null && instant.isBefore(min) && page > 0) {
			page--;
			min = min(load(page));
		}
		page(page-1);
		DelayerUtil.execute(this, v -> notifier.scrollTo(ScaleFormatter.label(instant, selectedScale(), language())), 50);
	}

	public void page(long page) {
		this.page = page;
		if (page < 0) page = 0;
		if (page > countPages()) this.page = countPages()-1;
		resetPages();
		refreshToolbar();
		List<EventlineEventGroup> result = nextEvents();
		notifier.addEventsAfter(result);
		if (result.size() < stepsCount) notifier.addEventsBefore(previousEvents());
	}

	public void first() {
		page = -1;
		resetPages();
		refreshToolbar();
		notifier.addEventsAfter(nextEvents());
	}

	public void previous() {
		if (page <= 0) return;
		refreshToolbar();
		if (arrangement == Arrangement.Horizontal) notifier.addEventsBefore(previousEvents());
		else notifier.addEventsAfter(previousEvents());
	}

	public void next() {
		if (page >= countPages()-1) return;
		refreshToolbar();
		if (arrangement == Arrangement.Horizontal) notifier.addEventsAfter(nextEvents());
		else notifier.addEventsBefore(nextEvents());
	}

	public void last() {
		page = countPages();
		resetPages();
		refreshToolbar();
		notifier.addEventsAfter(previousEvents());
		notifier.scrollToEnd();
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
				.events(previousEvents()));
	}

	protected void _arrangement(Arrangement arrangement) {
		this.arrangement = arrangement;
	}

	private List<EventlineEventGroup> nextEvents() {
		List<EventlineEventGroup> result = new ArrayList<>();
		while (result.size() < stepsCount && page < countPages()) {
			page++;
			result.addAll(load(page));
		}
		return result.stream().sorted(eventsComparator()).collect(Collectors.toList());
	}

	private List<EventlineEventGroup> previousEvents() {
		List<EventlineEventGroup> result = new ArrayList<>();
		while (result.size() < stepsCount && page > 0) {
			page--;
			result.addAll(0, load(page));
		}
		return result.stream().sorted(eventsComparator()).collect(Collectors.toList());
	}

	private List<EventlineEventGroup> load(long page) {
		if (loadedPages.contains(page)) return Collections.emptyList();
		Scale scale = selectedScale();
		Instant from = from(page);
		Instant to = LocalDateTime.ofInstant(from, ZoneId.of("UTC")).plus(stepsCount, scale.temporalUnit()).toInstant(ZoneOffset.UTC);
		loadedPages.add(page);
		return source.events(from, to).entrySet().stream().map(this::schemaOf).collect(Collectors.toList());
	}

	private Comparator<EventlineEventGroup> eventsComparator() {
		if (arrangement == Arrangement.Vertical) return (o1, o2) -> o2.date().compareTo(o1.date());
		return Comparator.comparing(EventlineEventGroup::date);
	}

	private EventlineEventGroup schemaOf(Map.Entry<Instant, List<EventlineDatasource.Event>> entry) {
		EventlineEventGroup result = new EventlineEventGroup();
		result.date(entry.getKey());
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
		Instant date = from(page);
		EventlineToolbarInfo result = new EventlineToolbarInfo();
		result.label(ScaleFormatter.label(date, scale, language()));
		result.page(page);
		result.countPages(countPages());
		result.canPrevious(page >= 0);
		result.canNext(page < countPages());
		return result;
	}

	private long countPages() {
		Scale scale = selectedScale();
		long steps = scale.temporalUnit().between(source.from(), source.to());
		return pageOf(steps);
	}

	private int pageOf(long index) {
		return (int) (Math.floor(index / stepsCount) + (index % stepsCount > 0 ? 1 : 0));
	}

	private Instant from(long page) {
		return LocalDateTime.ofInstant(source.from(), UTC).plus(page*stepsCount, selectedScale().temporalUnit()).toInstant(UTC);
	}

	private long pageOf(Instant instant) {
		return pageOf(selectedScale().temporalUnit().between(source.from(), instant));
	}

	private long countSteps() {
		return selectedScale().temporalUnit().between(source.from(), source.to());
	}

	private long firstPage() {
		return countPages();
	}

	private void resetPages() {
		loadedPages.clear();
		notifier.resetEvents();
	}

	private Instant min(List<EventlineEventGroup> groups) {
		if (groups.size() <= 0) return null;
		return groups.get(0).date();
	}

	private Instant max(List<EventlineEventGroup> groups) {
		if (groups.size() <= 0) return null;
		return groups.get(groups.size()-1).date();
	}

}