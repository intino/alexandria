package teseo.framework.web.actions.common;


import teseo.framework.actions.Action;

public interface InputMessage extends Action.Input {
    String sessionId();
    String message();
}
