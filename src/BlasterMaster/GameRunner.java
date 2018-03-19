package BlasterMaster;
/*
 * GO BOSTONHACKS! AND HERE"S TO MY FIRST GO-ROUND! OCT 27-28 2017
 */
import java.awt.Color; 
import java.awt.Graphics;
import java.awt.Image;
 
import java.awt.*; 
import java.applet.*;
import java.util.*;

import java.awt.event.*;

import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JApplet;


public class GameRunner extends JApplet implements KeyListener{
  
  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  /*
   * This simply is code that pertains to the applet window 
   * 
   */
  Image virtualMem;
  Graphics gBuffer;
  
  public static int appletWidth, appletHeight;
  Timer timer;
  
  public int paused;
  public boolean gameStarted;
  
  Star [] stars;
  Star [] planets;
  //Importing pictures 
  public  Image shuttle;
  public  Image alien1;
  public  Image alien2;
  public  Image alien3;
  public  Image alien4;
  public  Image alien5;
  public  Image boss;
  public  Image splode;
  
  public AudioClip shoot;
  public AudioClip bgm;
  public AudioClip death;
  public AudioClip lvlUp;
  public AudioClip eDeath;
  public AudioClip shipDMG;
  public AudioClip alienDMG;
  
  
  ///////////////////////////////////////////////////////////////////////
  
  //////////////////////////////////////////////////////////////////////////
  /*
   * (non-Javadoc)
   * This will be the code that declares the game Objects
   */
  
  public static Ship player;
  public static final int PLAYER_S = 60;
  public static final int ALIEN_S = 60;
  public static final int NUM_ALIEN_IDS = 5;
  public static final int BOSS_ID = NUM_ALIEN_IDS+1;
  
  boolean gameOver;
  int killCount, levelBar, level, score, hiScore; //determines when the next bosses appear
  boolean bossEncountered;
  boolean firing;
  
  //Enemy List
  ArrayList<Alien> enemies;
  
  
  
  /////////////////////////////////////////////////////////////////////
  
  /////////////////////////////////////
  /*
   *Non-essential testing variables
   */
  
  String str;
  //////////////////////////////////

  public void init()
  {
    //Applet Code
    appletWidth = getWidth();
    appletHeight = getHeight();
    virtualMem = createImage(appletWidth+1000,appletHeight+1000);
    gBuffer = virtualMem.getGraphics();
    addKeyListener(this);//enables the applet to listen for key input
    
    
    //game pictures and music 
    shuttle = getImage(getCodeBase(),"BlasterMaster.gif");
    boss = getImage(getDocumentBase(),"AlienBoss.gif");
    alien1 = getImage(getDocumentBase(),"Alien1.gif");
    alien2 = getImage(getDocumentBase(),"Alien2.gif");
    alien3 = getImage(getDocumentBase(),"Alien3.gif");
    alien4 = getImage(getDocumentBase(),"Alien4.gif");
    alien5 = getImage(getDocumentBase(),"Alien5.gif");
    splode = getImage(getDocumentBase(), "Splosion.gif");
    
    shoot = getAudioClip(getDocumentBase(), "jump.wav");
    bgm = getAudioClip(getDocumentBase(), "mixdown.wav");
    death = getAudioClip(getDocumentBase(), "xghost.mp3");
    lvlUp = getAudioClip(getDocumentBase(), "xpickup.mp3");
    eDeath = getAudioClip(getDocumentBase(), "shotgun.wav");
    shipDMG = getAudioClip(getDocumentBase(), "slam.wav");
    alienDMG = getAudioClip(getDocumentBase(), "thud.wav");
    
    //Instantiating Game Objects
    player = new Ship(appletWidth/2-PLAYER_S/2,appletHeight-PLAYER_S*2,PLAYER_S,PLAYER_S,150);
    enemies = new ArrayList<Alien>();
    enemies.add(new Alien(rndNum(0,appletWidth-50),rndNum(50,250),ALIEN_S,ALIEN_S,10,rndNum(1,NUM_ALIEN_IDS)));
    enemies.add(new Alien(rndNum(0,appletWidth-50),rndNum(50,150),ALIEN_S,ALIEN_S,10,rndNum(1,NUM_ALIEN_IDS)));
    enemies.add(new Alien(rndNum(0,appletWidth-50),rndNum(50,150),ALIEN_S,ALIEN_S,10,rndNum(1,NUM_ALIEN_IDS)));
    bossEncountered = false;
    
    //generates random star locations for the background
    stars = new Star[15];
    planets = new Star[15];
    for(int i = 0; i< stars.length;i++)
    {
      stars[i] = new Star(rndNum(0,appletWidth),-20);
      planets[i] = new Star(rndNum(0,appletWidth),-1000,50,100,new Color(rndNum(0,255),rndNum(0,255),rndNum(0,255)).darker().darker().darker().darker(),1);
    }
    
    
    
    //testing variables

    
    //Rendering
    timer = new Timer();
    timer.schedule(new TimerTask()
    {
       public void run()
       {
          repaint();
       }
    }, 0, 3);
    
    
    bgm.loop();
    //keylistening - without this the game would sometimes be unresponsive
    setFocusable(true);
    requestFocusInWindow();
    
    //game functionality
    gameOver = false;
    killCount = 0;
    levelBar = 15;
    level = 1;
    score = 0;
    bossEncountered = false;
    paused = 1;
    gameStarted = false;
    
  }
  
  public void paint(Graphics g)
  {
    drawBackground(gBuffer);
    if(!gameStarted) {
      {
        gBuffer.setColor(Color.cyan);
        gBuffer.setFont(new Font("Sans-Serif", Font.PLAIN, 50)); 
        gBuffer.drawString("BlasterMaster", appletWidth/2-150, appletHeight/2-200);
        gBuffer.setColor(Color.white);
        gBuffer.setFont(new Font("Sans-Serif", Font.PLAIN, 30)); 
        gBuffer.drawString("Press any key to begin", appletWidth/2-150, appletHeight/2+30);
        gBuffer.drawImage(shuttle, player.getxPos()-20, player.getyPos()-25, this);
        
        gBuffer.setFont(new Font("Sans-Serif", Font.PLAIN, 20)); 
        gBuffer.drawString("'a' and 'd' are controls for left and right respectively, press spacebar to fire",250,appletHeight-200);
        
      }
    }
    else if(!gameOver && paused>0) {
    gBuffer.setFont(new Font("Sans-Serif", Font.PLAIN, 17)); 
    
    gBuffer.setColor(Color.WHITE); 
    gBuffer.drawString("Level: " + level, 0, 20);
    gBuffer.drawString("Score: " + score, 0, 40);
    gBuffer.drawString("HighScore: " + hiScore, 0, 60);
    
    player.render(gBuffer);
    renderEnemies(gBuffer);
    gBuffer.drawImage(shuttle, player.getxPos()-20, player.getyPos()-25, this);
    
    checkHitDetection();
    }
    if(paused<0)
    {
      gBuffer.setFont(new Font("Sans-Serif", Font.PLAIN, 30)); 
      gBuffer.drawString("Paused", appletWidth/2-100, appletHeight/2+200);
      gBuffer.drawString("Press p to Resume", appletWidth/2-150, appletHeight/2+230);
    }
    if(gameOver) {
      gBuffer.setFont(new Font("Sans-Serif", Font.PLAIN, 30)); 
      gBuffer.drawString("YOU LOST", appletWidth/2-100, appletHeight/2);
      gBuffer.drawString("Press r to Restart", appletWidth/2-150, appletHeight/2+30);
      gBuffer.drawImage(splode, player.getxPos()-20, player.getyPos()-25, this);
      player.setyPos(player.getyPos()+3);
      renderEnemies(gBuffer);
      
    }
    
    g.drawImage(virtualMem, 0, 0, this);
    
  }
  
  public void renderEnemies(Graphics g) {
    for(Alien e: enemies)
    {
      e.render(g);
      
      if(e.getMoveType()<6)
        gBuffer.drawImage(getImage(getDocumentBase(), "Alien"+ e.getMoveType()+ ".gif"), e.getxPos()-20, e.getyPos()-18, this);
      else
        gBuffer.drawImage(boss, e.getxPos()-20, e.getyPos()-18, this);
      
    }
  }
  
  public void checkHitDetection() {
    
    if(firing) {
      shoot.play();
      player.fireBullet(17, -1);
    }
      
    
    //go through all of the player's bullets and for every enemy, check if they are touching
    ArrayList<Ship> removals = new ArrayList<Ship>();
    ArrayList<Alien> additions = new ArrayList<Alien>();
    boolean bossDefeated = false;
    outerloop:
    for(Bullet pb: player.getAmmo()) {
      for(Alien al: enemies) {
        if(hitDetect(pb,al) || hitDetect(al,pb))
        {
          removals.add(pb);
          alienDMG.play();
          al.setHp(al.getHp()-1);
          if(al.getHp()<=0)      //when an alien dies
          {
            gBuffer.drawImage(splode, al.getxPos(), al.getyPos(), this);
            eDeath.play();
            if(al.getMoveType()==BOSS_ID) //the level boss has been defeated
            {
              //enemies = new ArrayList<Alien>();\
              bossDefeated = true;
              additions.add(new Alien(rndNum(0,appletWidth-50),rndNum(50,150),ALIEN_S,ALIEN_S,10,rndNum(1,NUM_ALIEN_IDS)));
              additions.add(new Alien(rndNum(0,appletWidth-50),rndNum(50,150),ALIEN_S,ALIEN_S,10,rndNum(1,NUM_ALIEN_IDS)));
              additions.add(new Alien(rndNum(0,appletWidth-50),rndNum(50,150),ALIEN_S,ALIEN_S,10,rndNum(1,NUM_ALIEN_IDS)));
              bossEncountered =false;
              level ++;
              score+=500;
              if(score>hiScore)
                hiScore = score;
              player.setHp(player.getHp()+50);
              lvlUp.play();
              break outerloop;
            }
            
            if(!bossEncountered && !removals.contains(al) && al.getMoveType()<BOSS_ID)
            {
              additions.add(new Alien(rndNum(0,appletWidth-50),rndNum(50,150),ALIEN_S,ALIEN_S,10,rndNum(1,NUM_ALIEN_IDS)));
              killCount++;
              score+=200;
              if(score>hiScore)
                hiScore = score;
            }
            removals.add(al);
            
            
            
            if(killCount>=levelBar) { //introduces boss
              killCount = 0;
              levelBar+=5;
              additions.add(new Alien(rndNum(0,appletWidth-50),rndNum(50,150),ALIEN_S,ALIEN_S,10,BOSS_ID));
              bossEncountered = true;
            }
            
          }
        }
      }
    }
    
    if(bossDefeated)
      enemies = new ArrayList<Alien>();
    
    player.getAmmo().removeAll(removals);
    enemies.removeAll(removals);
    enemies.addAll(additions);
    
    
    
    //go through all the enemies and for each of their bullets, check if they are touching the player
    for(Alien al: enemies) {
      for(Bullet eb: al.getAmmo()) {
        if(hitDetect(eb,player) || hitDetect(player,eb))
        {
          removals.add(eb);
          shipDMG.play();
          player.setHp(player.getHp()-3);
        }
        
      }
      
      //player hits an alien
      
        if(hitDetect(al,player) || hitDetect(player,al))
        {
          if(al.getMoveType() == BOSS_ID)
            player.setHp(player.getHp()-2);
          else
            player.setHp(player.getHp()-1);
          shipDMG.play();
        }
      al.getAmmo().removeAll(removals);
    }
    
    
    //2 enemies knock into each other
    for(Alien al: enemies) {
      for(Alien al2: enemies) {
        if((hitDetect(al,al2) || hitDetect(al,al2)))
        {
          al.setDirX(al.getDirX()*-1);
          al2.setDirX(al2.getDirX()*-1);
        }
        
      }
    }
    
    
    if(player.getHp()<=0)
    {
      death.play();
      gameOver = true;
    }
  }
  
  
  public void drawBackground(Graphics g)
  {
    
    if(rndNum(0,5)==4)
      stars[rndNum(0,14)].enable();
    if(rndNum(0,60000) == 4)
      planets[rndNum(0,14)].enable();
    
    g.setColor(Color.BLACK);
    g.fillRect(0, 0, appletWidth+1000, appletHeight+1000);
    
    for(Star st: stars)
      st.render(g);
    for(Star pl: planets)
      pl.render(g);
    
  }
  
  
  
  public void update(Graphics g) {
    paint(g);
  }
  
  
  public boolean hitDetect(Ship a, Ship b) {
    
    if(isInside(a.getxPos(),a.getyPos(),b))
      return true;
    if(isInside(a.getxPos()+a.getWidth(),a.getyPos(),b))
      return true;
    if(isInside(a.getxPos(),a.getyPos()+a.getHeight(),b))
      return true;
    if(isInside(a.getxPos()+a.getWidth(),a.getyPos()+a.getHeight(),b))
      return true;
    
    return false;
    
  }
  
  
  public boolean isInside(int pointX,int pointY, Ship b) {
    if(pointX>=b.getxPos() && pointX<=b.getxPos() + b.getWidth() && pointY>=b.getyPos() && pointY<=b.getyPos()+b.getHeight())
        return true;
    return false;
  }
  
  /*
   * These are the methods for the keylistener
   */
  public void keyTyped(KeyEvent e)   
  {
    gameStarted = true;
    
    char ch = e.getKeyChar();
    if (ch == 'a') {
      //Right arrow key code'
      player.setDirX(-1);
    } else if (ch == 'd' ) {
          //Left arrow key code
      player.setDirX(1);
    } 
    
    //firing bullet
    if(ch == ' ') 
    {
      firing = true;
      //shoot.play();
      //player.fireBullet(17,-1);
    }
    if(ch == 'p' && !gameOver)
      paused*=-1;
    if(ch == 'r' && gameOver)
    {
      init();
      paused = 1;
      gameStarted = false;
    }
    
  }
  public void keyReleased(KeyEvent e)  
  {
    char ch = e.getKeyChar();
    if(ch == ' ')
      firing = false;
  }
  public void keyPressed(KeyEvent e){}
  
  //static methods
  public static int rndNum(int lowBound, int highBound)
  {
    int result = (int)(Math.random()*(highBound-lowBound+1))+lowBound;
    return result;
  }

}
