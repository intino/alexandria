package io.intino.fsm;

import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static java.util.Objects.requireNonNull;

public class TimePeriod {

    private final long amount;
    private final TimeUnit timeUnit;

    public TimePeriod(long amount, TimeUnit timeUnit) {
        this.amount = requirePositive(amount);
        this.timeUnit = requireNonNull(timeUnit);
    }

    public long amount() {
        return amount;
    }

    public TimeUnit timeUnit() {
        return timeUnit;
    }

    public TemporalUnit temporalUnit() {
        return ChronoUnit.valueOf(timeUnit.name());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TimePeriod that = (TimePeriod) o;
        return amount == that.amount && timeUnit == that.timeUnit;
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount, timeUnit);
    }

    @Override
    public String toString() {
        return amount + " " + timeUnit.name().toLowerCase();
    }

    private long requirePositive(long period) {
        if(period <= 0) throw new IllegalArgumentException("Period amount must be > 0");
        return period;
    }
}
