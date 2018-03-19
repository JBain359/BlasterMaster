package BlasterMaster;

import java.awt.Color; 
import java.awt.*; 
import java.util.*;


public class Ship {
  

  private int xPos, yPos , width , height , hp , speed , dirX , dirY, shotsFired;
  private Color bg, bulletBG;
  private final int MAX_HP, MAX_SHOTS;
  ArrayList<Bullet> ammo;
  
  

  public Ship(int x, int y, int w, int h, int health)
  {
    MAX_HP = health;
    MAX_SHOTS = 20;
    xPos = x;
    yPos = y;
    width = w;
    height = h;
    bg = Color.RED;
    dirX = 0;
    dirY = 0;
    hp = health;
    speed = 8;
    ammo = new ArrayList<Bullet>();
    bulletBG = Color.cyan;
    shotsFired = 0;
  }
  
  public Ship(int x, int y, int w, int h) {
    MAX_HP = 0;
    MAX_SHOTS = 0;
    xPos = x;
    yPos = y;
    width = w;
    height = h;
    bg = Color.RED;
    dirX = 0;
    dirY = 0;
    hp = 0;
    speed = 5;
  }

  public void render(Graphics g) {
    
  //drawing the ships bullets
    renderAmmo(g);
    
    //Drawing the ship
    g.setColor(bg);
    //g.fillRect(xPos, yPos, width, height); //hitbox

     
    //Moving the ship
    xPos += dirX*speed;
    
    //ensuring the ship stays on screen
    if(xPos<0)
      xPos = 0;
    if(xPos+width>GameRunner.appletWidth)
      xPos = GameRunner.appletWidth-width;
    
    //Ship's Health Bar
    int barLength = 200;
    int barHeight = 25;
    
    
    double rd = 51;
    double gn = 0;       //acheives a purple color @ low hp
    double bl = 255;
    
    double rd2 = 0;
    double gn2 = 255;       //acheives a color when the hp is max
    double bl2 = 0;
    
    g.setColor(Color.gray);
    g.fillRect(0, GameRunner.appletHeight-barHeight, barLength, barHeight);
    g.drawString("HEALTH", 0, GameRunner.appletHeight-barHeight);
    
    
    for(int i = 0; i< barLength*hp/MAX_HP;i++)
    {
      g.setColor(new Color((int)rd,(int)gn,(int)bl));
      g.drawLine(i, GameRunner.appletHeight, i, GameRunner.appletHeight-barHeight);
      
      rd+=(rd2-rd)/barLength;
      gn+=(gn2-gn)/barLength;
      bl+=(bl2-bl)/barLength;
    }
    
    //engine gauge: the more repeatedly you fire the hotter your engine gets! if it gets too hot then you lose hp!
    barLength = 170;
    barHeight = 25;
    
    
    
    rd = 255;
    gn = 255;       //acheives a color when there is little firing
    bl = 0;
    
    rd2 = 255;
    gn2 = 0;       //acheives a color when their is max firing
    bl2 = 0;
    
    g.setColor(Color.gray);
    g.fillRect(GameRunner.appletWidth-barLength, GameRunner.appletHeight-barHeight, barLength, barHeight);
    g.drawString("BLASTER OVERHEAT", GameRunner.appletWidth-barLength, GameRunner.appletHeight-barHeight);
    
    for(int i = GameRunner.appletWidth-barLength; i< barLength*(shotsFired+GameRunner.appletWidth/10)/MAX_SHOTS;i++)
    {
      g.setColor(new Color((int)rd,(int)gn,(int)bl));
      g.drawLine(i, GameRunner.appletHeight, i, GameRunner.appletHeight-barHeight);
      
      rd+=(rd2-rd)/barLength;
      gn+=(gn2-gn)/barLength;
      bl+=(bl2-bl)/barLength;
    }
    
    if(shotsFired/MAX_SHOTS>=1)
    {
      hp--;
      shotsFired = MAX_SHOTS;
    }
    
    shotsFired--;
    if(shotsFired < 0)
      shotsFired = 0;
    
  }
  
  

  public void renderAmmo(Graphics g) {
    
    for(Bullet b: ammo)
    {
      b.render(g);
      if(b.getDirY()+b.getHeight()<0) //once the bullet has crossed the top of the screen it should be wiped from memory
      {
        ammo.remove(0);
        ammo.trimToSize();
      }
        
    }
      
  }/*
  
  public void resizeAmmoArray() {
    //in order to keep the ammo array from growing massive, this function will remove any null pointers from ammo
    //by vetting the elements in to a temporary array and then setting ammo to that array
    ArrayList<Bullet> tempAmmo = new ArrayList<Bullet>();
    for(int i = 0; i< ammo.size();i++)
    {
      Bullet b = ammo.get(i);
      
      if(b != null)
        tempAmmo.add(b);
      
    }
    
    ammo = tempAmmo;
  }
  */
  public void fireBullet(int speed, int dir) {
    
    shotsFired+=2;
    ammo.add(new Bullet(xPos+width/2-5, yPos, 10, 15, speed*dir, bulletBG));
    
  }
  
  

  
  
  
  /*
   * Getters and Setters
   */

  public int getxPos() {
    return xPos;
  }

  public void setxPos(int xPos) {
    this.xPos = xPos;
  }

  public int getyPos() {
    return yPos;
  }

  public void setyPos(int yPos) {
    this.yPos = yPos;
  }

  public int getWidth() {
    return width;
  }

  public void setWidth(int width) {
    this.width = width;
  }

  public int getHeight() {
    return height;
  }

  public void setHeight(int height) {
    this.height = height;
  }

  public int getHp() {
    return hp;
  }

  public void setHp(int hp) {
    this.hp = hp;
  }

  public int getSpeed() {
    return speed;
  }

  public void setSpeed(int speed) {
    this.speed = speed;
  }

  public int getDirX() {
    return dirX;
  }

  public void setDirX(int dirX) {
    this.dirX = dirX;
  }

  public int getDirY() {
    return dirY;
  }

  public void setDirY(int dirY) {
    this.dirY = dirY;
  }

  public Color getBg() {
    return bg;
  }

  public void setBg(Color bg) {
    this.bg = bg;
  }
  
  public Color getBulletBG() {
    return bulletBG;
  }

  public void setBulletBG(Color bulletBG) {
    this.bulletBG = bulletBG;
  }
  
  public ArrayList<Bullet> getAmmo() {
    return ammo;
  }

  public int getShotsFired() {
    return shotsFired;
  }

  public void setShotsFired(int shotsFired) {
    this.shotsFired = shotsFired;
  }
  
  public int getMAX_HP() {
    return MAX_HP;
  }
}
