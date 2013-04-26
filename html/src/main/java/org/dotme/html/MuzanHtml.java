package org.dotme.html;

import playn.core.PlayN;
import playn.html.HtmlGame;
import playn.html.HtmlPlatform;

import org.dotme.core.Muzan;

public class MuzanHtml extends HtmlGame {

  @Override
  public void start() {
    HtmlPlatform platform = HtmlPlatform.register();
    platform.assets().setPathPrefix("muzan/");
    PlayN.run(new Muzan());
  }
}
