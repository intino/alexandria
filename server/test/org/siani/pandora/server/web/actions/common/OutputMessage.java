package org.siani.pandora.server.web.actions.common;

import org.siani.pandora.server.actions.Action;

public interface OutputMessage extends Action.Output {
    void write(String message);
}
