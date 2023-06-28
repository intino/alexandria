package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.Scale;
import io.intino.alexandria.core.Box;
import io.intino.alexandria.schemas.EventlineEvent;
import io.intino.alexandria.schemas.EventlineEventGroup;
import io.intino.alexandria.schemas.EventlineSetup;
import io.intino.alexandria.schemas.EventlineToolbarInfo;
import io.intino.alexandria.ui.displays.events.SelectEvent;
import io.intino.alexandria.ui.displays.events.SelectListener;
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
	private Instant selectedInstant;
	private SelectListener selectListener = null;
	private List<EventlineEventGroup> currentEvents = new ArrayList<>();
	private int currentEventsIndex = 0;

	private static final int DefaultStepsCount = 8;

	public enum Arrangement { Vertical, Horizontal }

	public Eventline(B box) {
		super(box);
	}

	public <DS extends EventlineDatasource> Eventline<DN, B> source(DS source) {
		this.source = source;
		return this;
	}

	public Eventline<DN, B> onSelect(SelectListener listener) {
		this.selectListener = listener;
		return this;
	}
	
	public void select(Instant instant) {
		if (source == null) return;
		if (selectedInstant != null && selectedInstant.equals(instant)) return;
		long page = pageOf(instant);
		if (page < 0) { first(false); return; }
		if (page > countPages()) { last(false); return;}
		Instant min = min(load(page));
		while (min != null && instant.isBefore(min) && page > 0) {
			page--;
			min = min(load(page));
		}
		page(page-1);
		update(instant);
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
		updateSelected(result);
	}

	public void first() {
		first(true);
	}

	public void first(boolean highlight) {
		page = -1;
		resetPages();
		refreshToolbar();
		notifier.addEventsAfter(updateSelected(nextEvents()));
		DelayerUtil.execute(this, v -> notifier.scrollToStart(highlight), 50);
	}

	public void previous() {
		currentEventsIndex--;
		if (currentEventsIndex < 0) {
			previousPage();
			currentEventsIndex = currentEvents.isEmpty() ? 0 : currentEvents.size()-1;
		}
		if (currentEvents.isEmpty()) return;
		update(currentEvents.get(currentEventsIndex).date());
		DelayerUtil.execute(this, v -> notifier.scrollTo(ScaleFormatter.label(currentEvents.get(currentEventsIndex).date(), selectedScale(), language())), 50);
	}

	public void previousPage() {
		if (page <= 0) return;
		refreshToolbar();
		if (arrangement == Arrangement.Horizontal) notifier.addEventsBefore(updateSelected(previousEvents()));
		else notifier.addEventsAfter(updateSelected(previousEvents()));
	}

	public void next() {
		currentEventsIndex++;
		if (currentEventsIndex >= currentEvents.size()) {
			nextPage();
			currentEventsIndex = 0;
		}
		if (currentEvents.isEmpty()) return;
		update(currentEvents.get(currentEventsIndex).date());
		DelayerUtil.execute(this, v -> notifier.scrollTo(ScaleFormatter.label(currentEvents.get(currentEventsIndex).date(), selectedScale(), language())), 50);
	}

	public void nextPage() {
		if (page >= countPages()-1) return;
		refreshToolbar();
		if (arrangement == Arrangement.Horizontal) notifier.addEventsAfter(updateSelected(nextEvents()));
		else notifier.addEventsBefore(updateSelected(nextEvents()));
	}

	public void last() {
		last(true);
	}

	public void last(boolean highlight) {
		page = countPages();
		resetPages();
		refreshToolbar();
		notifier.addEventsAfter(updateSelected(previousEvents()));
		DelayerUtil.execute(this, v -> notifier.scrollToEnd(highlight), 50);
	}

	public void update(Instant instant) {
		if (selectedInstant == instant) return;
		selectedInstant = instant;
		updateCurrentEventsIndex(instant);
		//notifier.scrollTo(ScaleFormatter.label(instant, selectedScale(), language()));
		if (selectListener != null) selectListener.accept(new SelectEvent(this, instant));
	}

	private void updateCurrentEventsIndex(Instant instant) {
		for (int i = 0; i < currentEvents.size(); i++) {
			if (currentEvents.get(i).date().getEpochSecond() != instant.getEpochSecond()) continue;
			currentEventsIndex = i;
			break;
		}
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
		currentEvents = result.stream().sorted(eventsComparator()).collect(Collectors.toList());
		currentEventsIndex = 0;
		if (!result.isEmpty()) selectedInstant = result.get(0).date();
		return currentEvents;
	}

	private List<EventlineEventGroup> previousEvents() {
		List<EventlineEventGroup> result = new ArrayList<>();
		while (result.size() < stepsCount && page > 0) {
			page--;
			result.addAll(0, load(page));
		}
		currentEvents = result.stream().sorted(eventsComparator()).collect(Collectors.toList());
		currentEventsIndex = 0;
		if (!result.isEmpty()) selectedInstant = result.get(0).date();
		return currentEvents;
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
		result.category(event.category());
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
		result.canPrevious(page > 0);
		result.canNext(page < countPages()-1);
		return result;
	}

	private long countPages() {
		Scale scale = selectedScale();
		long steps = scale.temporalUnit().between(source.from(), source.to())+1;
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

	private List<EventlineEventGroup> updateSelected(List<EventlineEventGroup> events) {
		if (events.isEmpty()) return events;
		update(events.get(0).date());
		return events;
	}

}