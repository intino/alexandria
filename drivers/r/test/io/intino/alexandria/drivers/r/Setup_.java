package io.intino.alexandria.drivers.r;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class Setup_ {
  Setup setup = new Setup("10.13.13.115", 4021);

  @Test
  public void isInstalled() {
    assertEquals(setup.isInstalled(), true);
  }

  @Test
  public void install() {
    setup.install();
  }

}
