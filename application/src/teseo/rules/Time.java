package teseo.rules;

import tara.lang.model.Metric;

public enum Time implements Metric<Integer> {
	day(v -> v * 3600 * 24),
	days(day.converter),
	d(day.converter),
	hour(v -> v * 3600),
	hours(hour.converter),
	h(hour.converter),
	minute(v -> v * 60),
	minutes(minute.converter),
	m(minute.converter),
	second(v -> v),
	seconds(second.converter),
	s(second.converter);


	private Converter<Integer> converter;

	Time(Converter<Integer> converter) {
		this.converter = converter;
	}

	@Override
	public Integer value(Integer t) {
		return this.converter.convert(t);
	}



}
