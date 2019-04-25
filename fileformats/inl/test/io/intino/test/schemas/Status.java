package io.intino.test.schemas;

import java.time.Instant;

public class Status {
	public double battery;
	public double cpuUsage;
	public boolean isPlugged;
	public boolean isScreenOn;
	public double temperature;
	public Instant created;

	public Status temperature(double temperature) {
		this.temperature = temperature;
		return this;
	}

	public Status battery(double battery) {
		this.battery = battery;
		return this;
	}

	public Status isPlugged(boolean isPlugged) {
		this.isPlugged = isPlugged;
		return this;
	}

	public Status isScreenOn(boolean isScreenOn) {
		this.isScreenOn = isScreenOn;
		return this;
	}

	public Status cpuUsage(double cpuUsage) {
		this.cpuUsage = cpuUsage;
		return this;
	}

	public boolean isScreenOn() {
		return isScreenOn;
	}

	public Instant created() {
		return created;
	}

	public Status created(String date) {
		created = Instant.parse(date);
		return this;
	}

}
