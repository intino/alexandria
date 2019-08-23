package io.intino.alexandria.drivers;

import io.intino.alexandria.drivers.utils.SSH;
import io.intino.alexandria.drivers.utils.Shell;
import io.intino.alexandria.logger.Logger;

import java.io.File;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.file.Files;

public class Setup implements ISetup {

  private String outputLast = "";
  private SSH ssh = null;

  private String soID;
  private String soVersionID;

  protected Setup(String[] SOCompatibles, String host, Integer port) throws Exception {

    try {
      String privateKeyFile = System.getProperty("user.home") + "/.ssh/id_rsa";
      if (! new File(privateKeyFile).isFile()) {
        System.out.println("Private key is missing. Generate new...");

        String command = "/usr/bin/ssh-keygen -t rsa -b 4096 -q -N '' -f " + privateKeyFile;
        System.out.println("Command: " + command);
        new Shell().executeCommand(command, new File(System.getProperty("user.home")));

        String authorized_keys = System.getProperty("user.home") + "/.ssh/authorized_keys";
        Files.copy(new File(privateKeyFile).toPath(), new File(authorized_keys).toPath());
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    OutputStream output = new OutputStream()
    {
      private StringBuilder string = new StringBuilder();
      @Override
      public void write(int b) {
        this.string.append((char) b );
        System.out.print((char) b);
        outputLast = outputLast + (char) b;
      }

      public String toString(){
        return this.string.toString();
      }
    };

    PrintWriter console = new PrintWriter(output);
    try {
      ssh = new SSH(System.getProperty("user.home") + "/.ssh/id_rsa","root",host, port, console);

      exec("source /etc/os-release; echo -n $ID");
      soID = outputLast;
      exec("source /etc/os-release; echo -n $VERSION_ID");
      soVersionID = outputLast;

    } catch (Exception e) {
      Logger.error("Can't connect to server (ssh)");
    }

    checkRequisites(SOCompatibles);
  }

  public Setup() {
  }

  private String getSOCurrentVersion() {
    return soID + " " + soVersionID;
  }

  private void checkRequisites(String[] SOCompatibles) throws Exception {
    boolean compatible = false;
    for (String so: SOCompatibles) {
      if (so.equals(getSOCurrentVersion())) {
        compatible = true;
        break;
      }
    }

    if (! compatible) {
      String message = "Operation system is not compatible. Current version: " + getSOCurrentVersion();
      Logger.error(message);
      throw new Exception(message);
    }
  }

  protected String getSOID() {
    return soID;
  }

  protected String getSOVERSION_ID() {
    return soVersionID;
  }

  protected String getLastMessage() {
    return outputLast;
  }

  protected void exec(String command) throws Exception {
    outputLast = "";
    Logger.info("Command: " + command);
    ssh.exec(command);
    if (ssh.getExitStatus() != 0) throw new Exception("Failed to execute command: "+ command);
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
