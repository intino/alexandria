package teseo.consul.jmx;

public interface ConsulManagerMBean {

    void executeDeploy();

    void executeRetract();

    void executeLogServer();

    void executeLogProject();
}