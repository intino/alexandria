package teseo.framework.web.actions.common;

import teseo.framework.actions.Action;

public interface OutputMessage extends Action.Output {
    void write(String message);
}
