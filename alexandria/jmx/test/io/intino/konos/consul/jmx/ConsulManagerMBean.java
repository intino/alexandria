package io.intino.konos.consul.jmx;

public interface ConsulManagerMBean {

    void executeDeploy();

    void executeRetract();

    void executeLogServer();

    void executeLogProject();
}