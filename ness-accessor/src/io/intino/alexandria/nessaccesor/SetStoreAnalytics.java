package io.intino.alexandria.nessaccesor;

import io.intino.alexandria.nessaccesor.NessAccessor.SetStore.SetFilter;
import io.intino.alexandria.nessaccesor.NessAccessor.SetStore.Tank;
import io.intino.alexandria.nessaccesor.NessAccessor.SetStore.Tank.Tub;
import io.intino.alexandria.nessaccesor.NessAccessor.SetStore.Tank.Tub.Set;
import io.intino.alexandria.nessaccesor.NessAccessor.SetStore.Timetag;
import io.intino.alexandria.nessaccesor.SetStoreAnalytics.TankHistogram.Axis;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;


public class SetStoreAnalytics {
	private NessAccessor.SetStore setStore;

	public SetStoreAnalytics(NessAccessor.SetStore setStore) {
		this.setStore = setStore;
	}

	public TankEvolution evolutionOf(String tank, int count) {
		return evolutionOf(setStore.tank(tank), count);
	}

	public TankHistogram histogramOf(String tank, Axis axis) {
		return histogramOf(setStore.tank(tank), axis);
	}

	private TankEvolution evolutionOf(Tank tank, int count) {
		return new TankEvolution(tank.tubs(count));
	}

	private TankHistogram histogramOf(Tank tank, Axis axis) {
		return new TankHistogram(axis, tank.last());
	}

	public interface Point<T> {
		T item();

		int size();
	}

	public static class TankHistogram<T> {
		private final Axis<T> axis;
		private final Map<T, Integer> data;

		public TankHistogram(Axis axis, Tub tub) {
			this.axis = axis;
			this.data = new HashMap<>();
			this.initData(tub);
		}

		public Stream<Point> points() {
			return data.keySet().stream().sorted(axis.sorting()).map(this::pointOf);
		}

		private void initData(Tub tub) {
			tub.sets().forEach(this::put);
		}

		private Point<T> pointOf(T item) {
			return new Point<T>() {
				@Override
				public T item() {
					return item;
				}

				@Override
				public int size() {
					return sizeOf(item);
				}
			};
		}

		public void put(Set set) {
			T item = axis.itemOf(set);
			data.put(item, sizeOf(item) + sizeOf(set));
		}

		private Integer sizeOf(T item) {
			return data.getOrDefault(item, 0);
		}

		private int sizeOf(Set set) {
			return set.size();
		}

		public interface Axis<T> {
			T itemOf(Set set);

			Comparator<? super T> sorting();
		}

	}

	public static class TankEvolution {
		private Stream<Tub> tubs;

		public TankEvolution(Stream<Tub> tubs) {
			this.tubs = tubs;
		}

		public Stream<Point> points() {
			return points(s -> true);
		}

		public Stream<Point> points(SetFilter filter) {
			return tubs.map(t -> point(t, filter));
		}

		private Point<Timetag> point(Tub tub, SetFilter filter) {
			return new Point<Timetag>() {
				@Override
				public Timetag item() {
					return tub.timetag();
				}

				@Override
				public int size() {
					return tub.sets(filter).stream().mapToInt(Set::size).sum();
				}
			};
		}

	}


}
