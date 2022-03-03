package io.intino.fsm;

import io.intino.alexandria.logger.Logger;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import static java.util.Objects.requireNonNull;

/**
 * Represents a task that will be executed periodically, and provides full control over its execution flow.
 * */
public class StatefulScheduledService {

    private final TimePeriod period;
    private final ScheduledExecutorService executor;
    private final AtomicReference<State> state;
    private ScheduledFuture<?> future;

    public StatefulScheduledService(TimePeriod period) {
        this.period = requireNonNull(period);
        this.executor = Executors.newSingleThreadScheduledExecutor();
        this.state = new AtomicReference<>(State.Created);
    }

    public synchronized boolean start(Runnable task) {
        if(task == null) throw new NullPointerException("Task cannot be null");
        if(!state.compareAndSet(State.Created, State.Running)) return false;
        // scheduleWithFixedDelay waits from the end of the previous execution to the start of the next
        future = executor.scheduleWithFixedDelay(execute(task), 0, period.amount(), period.timeUnit());
        return true;
    }

    private Runnable execute(Runnable task) {
        return () -> {
            if(state.get().equals(State.Running))
                task.run();
        };
    }

    public void pause() {
        state.compareAndSet(State.Running, State.Paused);
    }

    public void resume() {
        state.compareAndSet(State.Paused, State.Running);
    }

    public synchronized void stop(long timeout, TimeUnit timeUnit) {
        if(state.get() == State.Terminated || state.get() == State.Cancelled) return;
        state.set(State.Terminated);
        executor.shutdown();
        waitFor(timeout, timeUnit);
    }

    public void cancel() {
        if(future == null) return;
        if(state.get() == State.Cancelled || state.get() == State.Terminated) return;
        future.cancel(true);
        state.set(State.Cancelled);
        executor.shutdownNow();
        waitFor(1, TimeUnit.SECONDS);
    }

    private void waitFor(long timeout, TimeUnit timeUnit) {
        try {
            executor.awaitTermination(timeout, timeUnit);
        } catch (InterruptedException e) {
            Logger.error(e);
        }
    }

    public ScheduledFuture<?> future() {
        return future;
    }

    public State state() {
        return state.get();
    }

    public TimePeriod period() {
        return period;
    }

    public enum State {
        Created,
        Running,
        Paused,
        Cancelled,
        Terminated
    }
}
