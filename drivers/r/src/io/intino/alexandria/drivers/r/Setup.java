package io.intino.alexandria.drivers.r;

import io.intino.alexandria.drivers.utils.SSH;
import io.intino.alexandria.logger.Logger;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

public class Setup extends io.intino.alexandria.drivers.Setup {
  SSH ssh = null;

  Setup() {
    new Setup("localhost", 22);
  }

  Setup(String host, Integer port) {
    super();

    OutputStream output = new OutputStream()
    {
      private StringBuilder string = new StringBuilder();
      @Override
      public void write(int b) throws IOException {
        this.string.append((char) b );
      }

      public String toString(){
        return this.string.toString();
      }
    };

    PrintWriter console = new PrintWriter(System.out);
    try {
      ssh = new SSH(System.getProperty("user.home") + "/.ssh/id_rsa","root",host, port, console);
    } catch (com.jcraft.jsch.JSchException e) {
      Logger.error("Can't connect to server (ssh)");
    }
  }

  private void exec(String command) throws Exception {
    ssh.exec(command);
    if (ssh.getExitStatus() != 0) throw new Exception("Failed to execute command: "+ command);
  }

  @Override
  public boolean isInstalled() {
    return false;
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

  }
}
