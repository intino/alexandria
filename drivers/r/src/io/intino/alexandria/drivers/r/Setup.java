package io.intino.alexandria.drivers.r;

import io.intino.alexandria.logger.Logger;

public class Setup extends io.intino.alexandria.drivers.Setup {

  private static String[] SOCompatibles = {"ubuntu 18.04"};

  public Setup() throws Exception {
    new Setup("localhost", 22);
  }

  public Setup(String host, Integer port) throws Exception {
    super(SOCompatibles,host, port);
  }

  @Override
  public boolean isInstalled() {
    try {
      exec("apt -qq list r-base");
    } catch (Exception e) {
      Logger.error("Can't exec command in server (ssh). Last command: " + e.getMessage());
    }

    return getLastMessage().contains("[installed]");
  }

  @Override
  public void install() {
    try {
      exec("apt update");
      exec("apt -y upgrade");
      exec("apt -y install r-base");

    } catch (Exception e) {
      Logger.error("Can't exec command in server (ssh). Last command: " + e.getMessage());
    }
  }

  @Override
  public void uninstall() {
    try {
      exec("apt -y purge r-base");
      exec("apt -y autoremove");
    } catch (Exception e) {
      Logger.error("Can't exec command in server (ssh). Last command: " + e.getMessage());
    }
  }
}
