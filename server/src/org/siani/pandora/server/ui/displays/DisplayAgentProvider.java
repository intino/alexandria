package org.siani.pandora.server.ui.displays;

public interface DisplayAgentProvider {
    DisplayAgent agent(Display display, MessageCarrier carrier);
}
