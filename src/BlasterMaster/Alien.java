package BlasterMaster;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

public class Alien extends Ship {
  private int moveType, enterY, permY;
  
  private final int MAX_HP;

  public Alien(int x,int y, int w, int h,int speed,int id) {
    super(x,y,w,h);
    this.setSpeed(speed);
    
    this.moveType = id;
    this.setDirX(1);
    if(this.moveType%2==0)
    this.setDirY(1);
    this.ammo = new ArrayList<Bullet>();
    
    
    this.setBulletBG(Color.magenta);
    enterY = 0- h*3;
    permY = y;
    
    if(id == 1) {
      this.setBg(Color.GREEN);
      this.setHp(10);
    }
    else if(id == 2) {
      this.setBg(Color.yellow);
      this.setHp(16);
    }
    else if(id == 3) {
      this.setBg(Color.red);
      this.setHp(20);
    }
    else if(id == 4) {
      this.setBg(Color.magenta);
      this.setHp(13);
    }
    else if(id == 5) {
      this.setBg(Color.orange);
      this.setHp(16);
    }
    else if(id == GameRunner.BOSS_ID) {
      this.setBg(Color.white);
      this.setHp(60 + 12*(id-6));
      this.setWidth(this.getWidth()*3/2);
      this.setHeight(this.getHeight()*3/2);
    }
      
    MAX_HP = this.getHp();
  }
  
  public void render(Graphics g) {
    //drawing the alien
    renderAmmo(g);
    g.setColor(getBg());
    //g.fillRect(getxPos(), getyPos(), getWidth(), getHeight()); //hitbox
    

    /*
     * To simulate the different types of aliens
     * There will be different movement patterns based upon moveType
     * 
     * NOTE: add these types:
     * *one that avoids you
     * *one that shoots diagonally
     * *one that aims at you (so diagonal bullets)
     * *one that sort of "hurls" its bullets at you (bullet horizontal speed = aliens horizontal speed)
     * *one that teleports
     * *one that approaches space invaders style
     * *Shadow Ship: inverse mirrors movement, super bulky, accurate aiming, different bullets speed
     * *Mimic: shoots when you shoot, free movement though
     * **mix and match behaviors
     */
    //this applies to all aliens
    if(enterY<permY) ///the aliens enter from the top of the screen
    {
      enterY+=getSpeed();
      setyPos(enterY);
    } 
    if(moveType == 1) //simple left to right
    {
      if(getxPos()<0) //keeps from leaving boundary
        setDirX(getDirX()*-1);
      else if(getxPos()+getWidth()>GameRunner.appletWidth) {
        setDirX(getDirX()*-1);
        setxPos(GameRunner.appletWidth-getWidth());
      }
      
      //firing periodically
      if(getxPos()%GameRunner.rndNum(37,71)==0)
        fireBullet(15,1);
    }
    else if(moveType == 2) //wave Movement
    {
      
      //fluctuating vertical movement smaller values mean more waves
      int vRange = GameRunner.rndNum(23,63);
      if(getyPos()%vRange == 0)
        setDirY(getDirY()*-1);
      
      if(getxPos()<0) //keeps from leaving boundary
        setDirX(getDirX()*-1);
      else if(getxPos()+getWidth()>GameRunner.appletWidth) {
        setDirX(getDirX()*-1);
        setxPos(GameRunner.appletWidth-getWidth());
      }
      
      if(getyPos()<0) //keeps from leaving boundary
        setDirY(getDirY()*-1);
      else if(getyPos()+getHeight()+200>GameRunner.appletHeight)
        setDirY(getDirY()*-1);
      
      //firing periodically
      if(getxPos()%GameRunner.rndNum(37,71)==0)
        fireBullet(15,1);
    }
    else if(moveType == 3) //aggressive Movement
    {
      
      //how fast does the alien take to follow you on average? smaller mean faster reaction times
      int reactTime = GameRunner.rndNum(23,63);
      int fireRange = GameRunner.rndNum(40,70);    ///How close the alien is to the player before it fires, larger values mean less accurate firing
      
      if(getxPos()<GameRunner.player.getxPos() && getxPos()%reactTime == 0 ) //follows the player
        setDirX(1);
      else if(getxPos()+getWidth()>GameRunner.player.getxPos() + GameRunner.player.getWidth() && getxPos()%reactTime == 0 ) //follows the player
        setDirX(-1);

      if(getxPos()<0) //keeps from leaving boundary
        setDirX(getDirX()*-1);
      else if(getxPos()+getWidth()>GameRunner.appletWidth) {
        setDirX(getDirX()*-1);
        setxPos(GameRunner.appletWidth-getWidth());
      }
      
      if(getxPos()>=GameRunner.player.getxPos()-fireRange && getxPos()+getWidth()<=GameRunner.player.getxPos() + GameRunner.player.getWidth()+fireRange  && getxPos()%7==0) { //when player is in sights FIRE!
        fireBullet(20,1);
      }
    }
    else if(moveType == 4) //aggressive vertical movement
    {
    //fluctuating vertical movement smaller values mean more waves
      int vRange = GameRunner.rndNum(23,63);
      if(getyPos()%vRange == 0)
        setDirY(getDirY()*-1);
      
    //how fast does the alien take to follow you on average? smaller values mean faster reaction times
      int reactTime = GameRunner.rndNum(23,63);
      int fireRange = GameRunner.rndNum(40,70);    ///How close the alien is to the player before it fires, larger values mean less accurate firing
      
      if(getxPos()<GameRunner.player.getxPos() && getxPos()%reactTime == 0 ) //follows the player
        setDirX(1);
      else if(getxPos()+getWidth()>GameRunner.player.getxPos() + GameRunner.player.getWidth() && getxPos()%reactTime == 0 ) //follows the player
        setDirX(-1);

      if(getxPos()<0) //keeps from leaving boundary
        setDirX(getDirX()*-1);
      else if(getxPos()+getWidth()>GameRunner.appletWidth) {
        setDirX(getDirX()*-1);
        setxPos(GameRunner.appletWidth-getWidth());
      }
      
      if(getyPos()<0) //keeps from leaving boundary
        setDirY(getDirY()*-1);
      else if(getyPos()+getHeight()+200>GameRunner.appletHeight)
        setDirY(getDirY()*-1);
      
      if(getxPos()>=GameRunner.player.getxPos()-fireRange && getxPos()+getWidth()<=GameRunner.player.getxPos() + GameRunner.player.getWidth()+fireRange && getxPos()%13==0) { //when player is in sights FIRE!
        fireBullet(20,1);
      }
    }
    else if(moveType == 5)//acceleration
    {
      int maxSpeed = 45;
      int accelerationRate = 37; //higher numbers mean lower acceleration 
      if(getSpeed()> maxSpeed)
        setSpeed(getSpeed()/GameRunner.rndNum(2, 7));
      if(getxPos()%accelerationRate == 0)
        setSpeed(getSpeed()+2);
      

      if(getxPos()<0) //keeps from leaving boundary
        setDirX(getDirX()*-1);
      else if(getxPos()+getWidth()>GameRunner.appletWidth) {
        setDirX(getDirX()*-1);
        setxPos(GameRunner.appletWidth-getWidth());
      }
      
      if(getxPos()%GameRunner.rndNum(30,71)==0)
        fireBullet(25,1);
      
    }
    else if(moveType == GameRunner.BOSS_ID) // BOSS
    {
    //fluctuating vertical movement smaller values mean more waves
      
      
      int chargeRange = 50; 
      ///How close the alien is to the player before it charges, larger values mean less accurate firing
        
      
      if(getxPos()<0) //keeps from leaving boundary
        setDirX(getDirX()*-1);
      else if(getxPos()+getWidth()>GameRunner.appletWidth) {
        setDirX(getDirX()*-1);
        setxPos(GameRunner.appletWidth-getWidth());
      }
      
      
      if(getyPos()<0) //keeps from leaving boundary
        setDirY(getDirY()*-1);
      else if(getyPos()+getHeight()>GameRunner.appletHeight)
        setDirY(getDirY()*-1);
      
      if(getxPos()>=GameRunner.player.getxPos()-chargeRange && getxPos()+getWidth()*2/3<=GameRunner.player.getxPos() + GameRunner.player.getWidth()+chargeRange && getDirY()<=0 ) { //when player is in sights Charge!
        setDirY(1);
        setDirX(0);
      }
      
      if(getyPos()+getHeight()>=GameRunner.player.getyPos()+GameRunner.player.getHeight() && getDirX()==0) {
        setDirY(getDirY()*-1);
        if((int)(Math.random()*(4))%2 == 0)
          setDirX(1);
        else
          setDirX(-1);
      }
      
      if(getxPos()>=GameRunner.player.getxPos()-50 && getxPos()+getWidth()<=GameRunner.player.getxPos() + GameRunner.player.getWidth()+50 && getxPos()%3==0) { //when player is in sights Charge!
        fireBullet(7,1);
      }
    }
    
    setxPos(getxPos()+getDirX()*getSpeed());
    setyPos(getyPos()+getDirY()*getSpeed());
    
  //Ship's Health Bar
    int barLength = 50;
    int barHeight = 4;

    if(getMoveType()<6) {
    g.setColor(Color.GRAY);
    g.fillRect(getxPos()+getWidth()-20, getyPos()-5, barLength, barHeight);
    }
    
    g.setColor(getBg().darker().darker());
    g.fillRect(getxPos()+getWidth()-20, getyPos()-5, barLength*getHp()/MAX_HP, barHeight);
    
    
    
  }
  
  public void renderAmmo(Graphics g) {
    
    for(Bullet b: ammo)
    {
      b.render(g);
      if(b.getDirY()>GameRunner.appletHeight) //once the bullet has crossed the bottom of the screen it should be wiped from memory
      {
        ammo.remove(0);
        ammo.trimToSize();
      }
        
    }
      
  }
  
 public void fireBullet(int speed, int dir) {
    
    ammo.add(new Bullet(getxPos()+getWidth()/2-5, getyPos()+getHeight(), 10, 10, speed*dir, getBulletBG()));
    
  }
 
 public int getMoveType() {
   return moveType;
 }

}
