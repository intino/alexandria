package io.intino.alexandria.drivers;

import java.io.File;

public class Setup implements ISetup {

  protected String privateKeyFile=System.getProperty("user.home") + "/.ssh/id_rsa";

  protected Setup() {
    try {
      if (! new File(privateKeyFile).isFile()) {
        System.out.println("Private key is missing. Generate new...");

        //ssh-keygen -t rsa -b 4096 -q -N "" -f
//        String[] command = {"/usr/bin/ssh-keygen", "-t rsa", "-b 4096", "-q", "-N \"\"", "-f " + privateKeyFile};
//        new ProcessBuilder(command).start();



      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public boolean isInstalled() {
    return false;
  }

  @Override
  public void install() {

  }

  @Override
  public void uninstall() {

  }
}
