package io.intino.alexandria.drivers.shiny;

import io.intino.alexandria.logger.Logger;

public class Setup extends io.intino.alexandria.drivers.Setup {

  private static String[] SOCompatibles = {"ubuntu 18.04"};
  private io.intino.alexandria.drivers.r.Setup setupR;

  public Setup() throws Exception {
     new Setup("localhost", 22);
  }

  protected Setup(String host, Integer port) throws Exception {
    super(SOCompatibles, host, port);
    setupR = new io.intino.alexandria.drivers.r.Setup(host, port);
  }

  @Override
  public boolean isInstalled() {
    return setupR.isInstalled();
  }

  @Override
  public void install() {
    setupR.install();

    try {
      exec("su - -c \"R -e \\\"install.packages('shiny', repos='https://cran.rstudio.com/')\\\"\"");

      exec("apt -y install gdebi-core");

      exec("apt -qq list shiny-server");
      if (! getLastMessage().contains("installed"))
        exec("wget https://download3.rstudio.org/ubuntu-14.04/x86_64/shiny-server-1.5.9.923-amd64.deb -O shiny-server-1.5.9.923-amd64.deb; gdebi -n shiny-server-1.5.9.923-amd64.deb; rm -f shiny-server-1.5.9.923-amd64.deb");

      exec("sed -i \"/disable_protocols/d\" \"/etc/shiny-server/shiny-server.conf\"");
      exec("echo \"disable_protocols websocket xdr-streaming xhr-streaming iframe-eventsource iframe-htmlfile xdr-polling xhr-polling iframe-xhr-polling;\" >> \"/etc/shiny-server/shiny-server.conf\"");
      exec("echo \"preserve_logs true;\" >> \"/etc/shiny-server/shiny-server.conf\"");

      exec("/bin/systemctl restart shiny-server");

      exec("apt -y install libcurl4-openssl-dev libssl-dev libxml2-dev libudunits2-dev libgdal-dev");

      installPackage("dplyr");
      installPackage("fst");
      installPackage("ggplot2");
      installPackage("plotly");
      installPackage("ggmap");
      installPackage("sf");
      installPackage("tidyverse");
      installPackage("classInt");
      installPackage("gapminder");
      installPackage("gtable");
      installPackage("shinydashboard");
      installPackage("lubridate");
      installPackage("shinyjs");
      installPackage("sitools");

    } catch (Exception e) {
      Logger.error("Can't exec command in server (ssh). Last command: " + e.getMessage());
    }
  }

  @Override
  public void uninstall() {
    try {
      uninstallPackage("sitools");
      uninstallPackage("shinyjs");
      uninstallPackage("lubridate");
      uninstallPackage("shinydashboard");
      uninstallPackage("gtable");
      uninstallPackage("gapminder");
      uninstallPackage("classInt");
      uninstallPackage("tidyverse");
      uninstallPackage("sf");
      uninstallPackage("ggmap");
      uninstallPackage("plotly");
      uninstallPackage("ggplot2");
      uninstallPackage("fst");
      uninstallPackage("dplyr");

      exec("apt -y purge libcurl4-openssl-dev libssl-dev libxml2-dev libudunits2-dev libgdal-dev");
      exec("apt -y purge shiny-server gdebi-core");
      uninstallPackage("shiny");

      exec("apt -y autoremove");

    } catch (Exception e) {
      Logger.error("Can't exec command in server (ssh). Last command: " + e.getMessage());
    }

  }

  private void installPackage(String name) throws Exception {
    if (! isInstalledPackage(name))
      exec("su - -c \"R -e \\\"install.packages('"+name+"')\\\"\"");
  }

  private void uninstallPackage(String name) throws Exception {
    if (isInstalledPackage(name))
    exec("su - -c \"R -e \\\"remove.packages('"+name+"')\\\"\"");
  }

  private boolean isInstalledPackage(String name) throws Exception {
    exec("su - -c \"R -e \\\"rownames(installed.packages())\\\"\"");
    return getLastMessage().contains("\""+name +"\"");
  }
}
