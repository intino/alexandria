package org.siani.pandora.consul.jmx;

public interface ConsulManagerMBean {

    void executeDeploy();

    void executeRetract();

    void executeLogServer();

    void executeLogProject();
}