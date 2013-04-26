package org.dotme.android;

import playn.android.GameActivity;
import playn.core.PlayN;

import org.dotme.core.Muzan;

public class MuzanActivity extends GameActivity {

  @Override
  public void main(){
    platform().assets().setPathPrefix("org/dotme/resources");
    PlayN.run(new Muzan());
  }
}
