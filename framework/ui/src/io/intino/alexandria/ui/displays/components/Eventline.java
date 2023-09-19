package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.Scale;
import io.intino.alexandria.core.Box;
import io.intino.alexandria.schemas.*;
import io.intino.alexandria.ui.displays.events.SelectEvent;
import io.intino.alexandria.ui.displays.events.SelectListener;
import io.intino.alexandria.ui.displays.events.eventline.ExecuteEvent;
import io.intino.alexandria.ui.displays.events.eventline.ExecuteEventListener;
import io.intino.alexandria.ui.displays.events.eventline.SelectEventListener;
import io.intino.alexandria.ui.displays.notifiers.EventlineNotifier;
import io.intino.alexandria.ui.model.ScaleFormatter;
import io.intino.alexandria.ui.model.eventline.EventlineDatasource;
import io.intino.alexandria.ui.utils.DelayerUtil;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
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
	private List<EventlineEventGroup> pageGroups = new ArrayList<>();
	private final Map<String, List<EventlineDatasource.Event>> events = new HashMap<>();
	private int selectedPageGroupIndex = 0;
	private final Set<Long> loadedPages = new HashSet<>();
	private Instant selectedInstant;
	private SelectListener selectListener = null;
	private SelectEventListener selectEventListener = null;
	private ExecuteEventListener executeEventOperationListener = null;

	private static final int DefaultStepsCount = 8;

	public enum Arrangement { Vertical, Horizontal }

	public Eventline(B box) {
		super(box);
	}

	@Override
	public void didMount() {
		super.didMount();
		page(page);
	}

	public <DS extends EventlineDatasource> DS source() {
		return (DS) source;
	}

	public <DS extends EventlineDatasource> Eventline<DN, B> source(DS source) {
		this.source = source;
		return this;
	}

	public Eventline<DN, B> onSelect(SelectListener listener) {
		this.selectListener = listener;
		return this;
	}

	public Eventline<DN, B> onSelectEvent(SelectEventListener listener) {
		this.selectEventListener = listener;
		return this;
	}
	
	public Eventline<DN, B> onExecuteEventOperation(ExecuteEventListener listener) {
		this.executeEventOperationListener = listener;
		return this;
	}

	public void showLoading() {
		notifier.showLoading();
	}

	public void hideLoading() {
		notifier.hideLoading();
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
		DelayerUtil.execute(this, v -> notifier.scrollTo(ScaleFormatter.label(instant, timezoneOffset(), selectedScale(), language())), 50);
	}

	public void page(long page) {
		this.page = page;
		if (page < 0) page = 0;
		if (page > countPages()) this.page = countPages()-1;
		resetPages();
		List<EventlineEventGroup> result = nextEvents();
		notifier.addEventsAfter(result);
		if (result.size() < stepsCount) notifier.addEventsBefore(previousEvents());
		refreshToolbar(page);
		updateSelected(result);
	}

	public void first() {
		first(true);
	}

	public void first(boolean highlight) {
		notifier.showLoading();
		page = -1;
		resetPages();
		notifier.addEventsAfter(updateSelected(nextEvents()));
		refreshToolbar(0);
		notifier.hideLoading();
		DelayerUtil.execute(this, v -> notifier.scrollToStart(highlight), 50);
	}

	public void previous() {
		selectedPageGroupIndex--;
		if (selectedPageGroupIndex < 0) {
			previousPage();
			selectedPageGroupIndex = pageGroups.isEmpty() ? 0 : pageGroups.size()-1;
		}
		if (pageGroups.isEmpty()) return;
		update(pageGroups.get(selectedPageGroupIndex).date());
		DelayerUtil.execute(this, v -> notifier.scrollTo(ScaleFormatter.label(pageGroups.get(selectedPageGroupIndex).date(), timezoneOffset(), selectedScale(), language())), 50);
	}

	public void previousPage() {
		if (page <= 0) return;
		if (arrangement == Arrangement.Horizontal) notifier.addEventsBefore(updateSelected(previousEvents()));
		else notifier.addEventsAfter(updateSelected(previousEvents()));
		refreshToolbar(page);
	}

	public void next() {
		selectedPageGroupIndex++;
		if (selectedPageGroupIndex >= pageGroups.size()) {
			nextPage();
			selectedPageGroupIndex = 0;
		}
		if (pageGroups.isEmpty()) return;
		update(pageGroups.get(selectedPageGroupIndex).date());
		DelayerUtil.execute(this, v -> notifier.scrollTo(ScaleFormatter.label(pageGroups.get(selectedPageGroupIndex).date(), timezoneOffset(), selectedScale(), language())), 50);
	}

	public void nextPage() {
		if (page >= countPages()-1) return;
		if (arrangement == Arrangement.Horizontal) notifier.addEventsAfter(updateSelected(nextEvents()));
		else notifier.addEventsBefore(updateSelected(nextEvents()));
		refreshToolbar(page);
	}

	public void last() {
		last(true);
	}

	public void last(boolean highlight) {
		notifier.showLoading();
		page = countPages();
		resetPages();
		notifier.addEventsAfter(updateSelected(previousEvents()));
		refreshToolbar(countPages());
		notifier.hideLoading();
		DelayerUtil.execute(this, v -> notifier.scrollToEnd(highlight), 50);
	}

	public void update(Instant instant) {
		if (selectedInstant == instant) return;
		selectedInstant = instant;
		updateCurrentEventsIndex(instant);
		//notifier.scrollTo(ScaleFormatter.label(instant, selectedScale(), language()));
		if (selectListener != null) selectListener.accept(new SelectEvent(this, instant));
	}

	public void selectEvent(EventlineSelectEventParams params) {
		if (selectEventListener == null) return;
		EventlineDatasource.Event event = eventOf(params.date(), params.event());
		if (event == null) return;
		selectEventListener.accept(new io.intino.alexandria.ui.displays.events.eventline.SelectEvent(this, event));
	}

	public void executeEvent(EventlineExecuteEventParams params) {
		if (executeEventOperationListener == null) return;
		EventlineDatasource.Event event = eventOf(params.date(), params.event());
		if (event == null) return;
		executeEventOperationListener.accept(new ExecuteEvent(this, event, params.operation()));
	}

	public void refresh(EventlineDatasource.Event event) {
		notifier.refreshEvent(schemaOf(event));
	}

	@Override
	public void refresh() {
		super.refresh();
		if (source == null) throw new RuntimeException("Eventline source not defined");
		page = firstPage();
		notifier.setup(new EventlineSetup()
				.name(source.name())
				.label(label())
				.events(previousEvents())
				.toolbar(toolbar(countPages())));
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
		pageGroups = result.stream().sorted(eventsComparator()).collect(Collectors.toList());
		selectedPageGroupIndex = 0;
		if (!result.isEmpty()) selectedInstant = result.get(0).date();
		return pageGroups;
	}

	private List<EventlineEventGroup> previousEvents() {
		List<EventlineEventGroup> result = new ArrayList<>();
		while (result.size() < stepsCount && page > 0) {
			page--;
			result.addAll(0, load(page));
		}
		pageGroups = result.stream().sorted(eventsComparator()).collect(Collectors.toList());
		selectedPageGroupIndex = 0;
		if (!result.isEmpty()) selectedInstant = result.get(0).date();
		return pageGroups;
	}

	private List<EventlineEventGroup> load(long page) {
		if (loadedPages.contains(page)) return Collections.emptyList();
		Scale scale = selectedScale();
		Instant from = from(page);
		Instant to = LocalDateTime.ofInstant(from, ZoneId.of("UTC")).plus(stepsCount, scale.temporalUnit()).toInstant(ZoneOffset.UTC);
		loadedPages.add(page);
		return register(source.events(from, to)).entrySet().stream().map(e -> schemaOf(e, page)).collect(Collectors.toList());
	}

	private Comparator<EventlineEventGroup> eventsComparator() {
		if (arrangement == Arrangement.Vertical) return (o1, o2) -> o2.date().compareTo(o1.date());
		return Comparator.comparing(EventlineEventGroup::date);
	}

	private EventlineEventGroup schemaOf(Map.Entry<Instant, List<EventlineDatasource.Event>> entry, long page) {
		EventlineEventGroup result = new EventlineEventGroup();
		result.page(page);
		result.date(entry.getKey());
		result.shortDate(ScaleFormatter.shortLabel(entry.getKey(), timezoneOffset(), selectedScale(), language()));
		result.longDate(ScaleFormatter.label(entry.getKey(), timezoneOffset(), selectedScale(), language()));
		result.events(entry.getValue().stream().map(this::schemaOf).sorted(Comparator.comparing(EventlineEvent::label)).collect(Collectors.toList()));
		return result;
	}

	private EventlineEvent schemaOf(EventlineDatasource.Event event) {
		EventlineEvent result = new EventlineEvent();
		result.id(event.id());
		result.date(event.date());
		result.shortDate(ScaleFormatter.shortLabel(event.date(), timezoneOffset(), selectedScale(), language()));
		result.longDate(ScaleFormatter.label(event.date(), timezoneOffset(), selectedScale(), language()));
		result.label(event.label());
		result.category(event.category());
		result.color(event.color());
		result.icon(event.icon());
		result.iconTitle(event.iconTitle());
		result.comments(event.comments());
		result.operations(event.operations());
		return result;
	}

	private Scale selectedScale() {
		return source.scale();
	}

	private void refreshToolbar(long page) {
		notifier.refreshToolbar(toolbar(page));
	}

	private EventlineToolbarInfo toolbar(long page) {
		Scale scale = selectedScale();
		Instant date = from(page);
		EventlineToolbarInfo result = new EventlineToolbarInfo();
		result.label(ScaleFormatter.label(date, timezoneOffset(), scale, language()));
		result.page(page);
		result.countPages(countPages());
		result.loadedPages(new ArrayList<>(loadedPages));
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
		pageGroups.clear();
		events.clear();
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

	private void updateCurrentEventsIndex(Instant instant) {
		int index = pageGroupIndex(instant);
		if (index > 0) selectedPageGroupIndex = index;
	}

	private int pageGroupIndex(Instant instant) {
		for (int i = 0; i < pageGroups.size(); i++) {
			if (pageGroups.get(i).date().getEpochSecond() != instant.getEpochSecond()) continue;
			return i;
		}
		return -1;
	}

	private EventlineDatasource.Event eventOf(Instant instant, String id) {
		List<EventlineDatasource.Event> events = this.events.getOrDefault(label(instant), Collections.emptyList());
		if (events.isEmpty()) return null;
		return events.stream().filter(e -> e.id().equals(id)).findFirst().orElse(null);
	}

	private Map<Instant, List<EventlineDatasource.Event>> register(Map<Instant, List<EventlineDatasource.Event>> eventList) {
		eventList.forEach((key, value) -> {
			String date = label(key);
			if (!events.containsKey(date)) events.put(date, new ArrayList<>());
			events.get(date).addAll(value);
		});
		return eventList;
	}

	private String label(Instant instant) {
		return ScaleFormatter.label(instant, timezoneOffset(), selectedScale(), language());
	}

}