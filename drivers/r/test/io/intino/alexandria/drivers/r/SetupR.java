package io.intino.alexandria.drivers.r;

import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertEquals;


public class SetupR {
  Setup setup = new Setup("10.13.13.115", 4021);

  public SetupR() throws Exception {
  }

  @Test
  @Ignore
  public void isInstalledTrue() {
    assertEquals(setup.isInstalled(), true);
  }

  @Test
  @Ignore
  public void install() {
    setup.install();
  }

  @Test
  @Ignore
  public void uninstall() {
    setup.uninstall();
  }

}
