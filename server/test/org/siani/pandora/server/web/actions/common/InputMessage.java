package org.siani.pandora.server.web.actions.common;


import org.siani.pandora.server.actions.Action;

public interface InputMessage extends Action.Input {
    String sessionId();
    String message();
}
