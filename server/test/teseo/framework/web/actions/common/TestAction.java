package teseo.framework.web.actions.common;

import teseo.framework.actions.Action;

public class TestAction implements Action {

    private final Task<InputMessage, OutputMessage> task;

    public TestAction(Task<InputMessage, OutputMessage> task) {
        this.task = task;
    }

    public Task<InputMessage, OutputMessage> task() {
        return task;
    }
}
