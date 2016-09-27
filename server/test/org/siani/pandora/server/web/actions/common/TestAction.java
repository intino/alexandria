package org.siani.pandora.server.web.actions.common;

import org.siani.pandora.server.actions.Action;

public class TestAction implements Action {

    private final Task<InputMessage, OutputMessage> task;

    public TestAction(Task<InputMessage, OutputMessage> task) {
        this.task = task;
    }

    public Task<InputMessage, OutputMessage> task() {
        return task;
    }
}
