package io.intino.alexandria.drivers.utils;


import com.jcraft.jsch.*;

import java.io.*;

public class SSH {

  private Session session;
  private PrintWriter console;
  private static String error;
  private Integer exitStatus = 0;
  JSch jsch;
  String user;
  String host;
  Integer port;

  public static class SSHException extends Exception {
    SSHException(Throwable cause) {
      super(cause);
    }
  }

  public SSH(String privateKey, String user, String host, Integer port, PrintWriter console) throws JSchException {
    this.user = user;
    this.host = host;
    this.port = port;

    this.console = console;
    jsch = new JSch();
    jsch.addIdentity(privateKey);

    session = jsch.getSession(user, host, port);
    java.util.Properties config = new java.util.Properties();
    config.put("StrictHostKeyChecking", "no");
    session.setConfig(config);

    session.connect();
  }


  public void close() {
    session.disconnect();
  }

  public void exec(String command) throws SSHException, JSchException {
    exitStatus = 0;
    error = "";
    System.setErr(new PrintStream(new OutputStream() {
      private StringBuilder line = new StringBuilder();

      @Override
      public void write(int b) throws IOException {
        if (b == '\n') {
          String s = line.toString();
          line.setLength(0);
          SSH.error += s + '\n';
        } else if (b != '\r') {
          line.append((char) b);
        }
      }
    }));

    try {
      Channel channel = session.openChannel("exec");
      ((ChannelExec) channel).setCommand(command);
      ((ChannelExec) channel).setErrStream(System.err);

      InputStream in = channel.getInputStream();
      channel.connect();

      byte[] tmp = new byte[10];
      while (true) {
        while (in.available() > 0) {
          int i = in.read(tmp, 0, 10);
          if (i < 0) break;

          console.print(new String(tmp, 0, i));
          console.flush();
        }
        if (channel.isClosed() || (channel.getExitStatus() > -1)) {
          if (in.available() > 0) continue;
          exitStatus = channel.getExitStatus();
          break;
        }

        waitASecond();
      }
      channel.disconnect();
    } catch (Exception e) {
      throw new SSHException(e);
    }
  }

  private void waitASecond() {
    try {
      Thread.sleep(1000);
    } catch (Exception e) {
    }
  }

  public String getError() {
    return error;
  }

  public Integer getExitStatus() {
    return exitStatus;
  }
}
