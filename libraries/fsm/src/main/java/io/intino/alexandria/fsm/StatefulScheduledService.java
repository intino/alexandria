package io.intino.alexandria.fsm;

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
class StatefulScheduledService {

    private final String name;
    private final TimePeriod period;
    private final ScheduledExecutorService executor;
    private final AtomicReference<State> state;
    private ScheduledFuture<?> execution;

    public StatefulScheduledService(String name, TimePeriod period) {
        this.name = name;
        this.period = requireNonNull(period);
        this.executor = Executors.newSingleThreadScheduledExecutor(r -> new Thread(r, "FSM-" + name + "-Thread"));
        this.state = new AtomicReference<>(State.Created);
    }

    public synchronized boolean start(Task task) {
        if(task == null) throw new NullPointerException("Task cannot be null");
        if(!state.compareAndSet(State.Created, State.Running)) return false;
        // scheduleWithFixedDelay waits from the end of the previous execution to the start of the next
        execution = executor.scheduleWithFixedDelay(execute(task), 0, period.amount(), period.timeUnit());
        return true;
    }

    private Runnable execute(Task task) {
        return () -> {
            try {
                if(state.get().equals(State.Running)) task.onUpdate();
                task.onFinally();
            } catch (Throwable e) {
                Logger.error(e);
            }
        };
    }

    public boolean pause() {
        return state.compareAndSet(State.Running, State.Paused);
    }

    public boolean resume() {
        return state.compareAndSet(State.Paused, State.Running);
    }

    public synchronized void stop(long timeout, TimeUnit timeUnit) {
        if(state.get() == State.Terminated || state.get() == State.Cancelled) return;
        state.set(State.Terminated);
        executor.shutdown();
        waitFor(timeout, timeUnit);
    }

    public synchronized void cancel() {
        if(execution == null) return;
        if(state.get() == State.Cancelled || state.get() == State.Terminated) return;
        state.set(State.Cancelled);
        execution.cancel(true);
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

    public ScheduledFuture<?> execution() {
        return execution;
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

    static abstract class Task {

        /**
         * Called every update cycle of the executor if the service's state == Running
         * */
        abstract void onUpdate();

        /**
         * Called every update cycle of the executor if the service's state == Paused
         * */
        void onPaused() {}

        /**
         * Called every update cycle of the executor, even if the service is paused
         * */
        void onFinally() {}
    }
}
