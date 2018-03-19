package BlasterMaster;

import java.awt.Color;
import java.awt.Graphics;

public class Bullet extends Ship {
  
  
  
  public Bullet(int x,int y, int w, int h,int speed,Color c) {
    super(x,y,w,h);
    this.setSpeed(speed);
    this.setBg(c);
    
    
  }
  
  public void render(Graphics g) {
    
  //Drawing the bullet
    g.setColor(getBg());
    g.fillOval(getxPos(), getyPos(), getWidth(), getHeight());
    
    //Moving the bullet
    setyPos(getyPos()+getSpeed());
    
  }
  
  

}
