package org.siani.pandora.server.pushservice;

public interface SessionProvider {
    <S extends Session> S session(String id);
    <C extends Client> C client(String id);
    <C extends Client> C currentClient();
}
