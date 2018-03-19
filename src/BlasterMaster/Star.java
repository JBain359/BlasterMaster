package BlasterMaster;

import java.awt.Color; 
import java.awt.Graphics;

public class Star {
  public int x,y,w,h,size;
  public boolean enabled;
  public int speed;
  public Color bg;
  
  
  public Star(int x,int y) {
    this.x = x;
    this.y = y;
    enabled = false;
    this.speed = 10;
    size = GameRunner.rndNum(2, 5);
    this.bg = Color.WHITE;
  }
  
  public Star(int x,int y, int sizeLowBound, int sizeHighBound, Color c, int speed) {
    this.x = x;
    this.y = y;
    enabled = false;
    this.speed = speed;
    size = GameRunner.rndNum(sizeLowBound, sizeHighBound);
    this.bg = c;
  }
  
  public void render(Graphics g) {
    
    g.setColor(bg);
    g.fillOval(x, y, this.size, this.size);
    if(enabled)
    y+=speed;
    
    if(y>=GameRunner.appletHeight)
    {
      y=-20;
      if(speed<5)
        y=-1000;
      enabled = false;
    }
    
  }
  
  public void enable() {
    enabled = true;
  }

}
