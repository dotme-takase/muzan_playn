package org.dotme.java;

import playn.core.PlayN;
import playn.java.JavaPlatform;

import org.dotme.core.Muzan;

public class MuzanJava {

  public static void main(String[] args) {
    JavaPlatform platform = JavaPlatform.register();
    platform.assets().setPathPrefix("org/dotme/resources");
    PlayN.run(new Muzan());
  }
}
