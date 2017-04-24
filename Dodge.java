// A Dodge's only behaviors are traveling left and right, and jumping. The extend of those abilities depend on the
// specific Dodge.

import java.awt.*;
// import java.awt.event.*;
// import javax.swing.*;
import java.util.*;

public class Dodge /*implements MouseListener*/ {
   // TODO Reduce number of fields, maybe store some in a HashTable instead as DecisionFacotors and Movements?
   private int screenWidth;
   private int screenHeight;
   private int size;
   private boolean sex;
   private int jumpVelocity;
   private int x;
   private int y;
   private int originX;
   private int originY;
   private boolean jump;
   private int midJumpCount;
   private final double GRAVITY = 2;
   private int reactionTime;
   private int crashes;
   private double jumpFactor;
   private double jumpFactorVariability;
   private int prevMisslePosition;
   private int misslePosition;
   private int missleVelocity;
   private boolean jumped;
   private int speed;
   private int energy;
   private int maxEnergy;
   private int energyRecover;
   
   // Constructs a Dodge for an animation window with a given screenWidth and screenHeight
   public Dodge(int screenWidth, int screenHeight) {
      Random r = new Random();
      this.screenWidth = screenWidth;
      this.screenHeight = screenHeight;
      sex = r.nextBoolean();
      size = 1;
      // jumpVelocity = r.nextInt(16);// 1;
      //   jumpVelocity = r.nextInt();
      r.nextInt(4);
      x =  screenHeight / 15;
      y = screenWidth / 2;
      originX = x;
      originY = y;
      jump = false;
      if (r.nextBoolean()) {
         reactionTime = r.nextInt(screenWidth / 4 * 2);
      } else {
         reactionTime = r.nextInt(screenWidth / 4 * 2) + screenWidth / 2;
      }
      jumpFactorVariability = (r.nextInt(20) + 1) / 10.0;
      // reactionTime = r.nextInt(screenWidth / 4 * 2);// 1;
      crashes = 0;
      maxEnergy = r.nextInt(50) + 1;
      energy = maxEnergy;
      jumped = false;
      energyRecover = 0;
   }
   
   public Dodge(int screenWidth, int screenHeight, int jumpVelocity, int reactionTime, double jumpFactorVariability, int maxEnergy) {
      // TODO reduce redundancy between both constructors. First constructor can just call this constructor with the default settings
      Random r = new Random();
      this.screenWidth = screenWidth;
      this.screenHeight = screenHeight;
      sex = r.nextBoolean();
      size = 1;
      this.jumpVelocity = jumpVelocity;
      x =  screenHeight / 15;
      y = screenWidth / 2;
      originX = x;
      originY = y;
      jump = false;
      this.reactionTime = reactionTime;
      crashes = 0;
      this.jumpFactorVariability = jumpFactorVariability;
      jumped = false;
      this.maxEnergy = maxEnergy;
      energy = maxEnergy;
      energyRecover = 0;
      mutate();
   }
   
   // Mutates the reaction tiome, jump velocity, and maximum energy of a Dodge at random.  
   public void mutate() {
      Random r = new Random();
      int mutations = 0;
      if (r.nextInt(1000) < 1) {
         this.jumpVelocity = r.nextInt(16);
         mutations++;
         //System.out.println("mutation");
      }
      if (r.nextInt(1000) < 1) {
         this.reactionTime = r.nextInt(16);
         mutations++;
      }
      if (r.nextInt(1000) < 1) {
         this.jumpFactorVariability = r.nextInt(5) + 7;
         mutations++;
      }
      if (r.nextInt(1000) < 1) {
         this.maxEnergy += r.nextInt(11) - 5;
      }
      if (r.nextInt(2000) < 1 && mutations == 0) {
         this.jumpVelocity = r.nextInt(16);
         this.reactionTime = r.nextInt(16);
         this.jumpFactorVariability = r.nextInt(5) + 7;
         // System.out.println("Extreme mutation");
      }
   }
   
   // Dodge will begin jump if it isn't already jumping
   public void jump() {
      if (!jump) {
         midJumpCount = 0;
         jump = true;
      }
   }
   
   // Dodge will decide if it'll jump given the opponentX and opponentSize. 
   public void jump(int opponentX, int opponentSize) {
      jumped = jumped && misslePosition > opponentX;
      misslePosition = opponentX;
      missleVelocity = misslePosition - prevMisslePosition;
      jumpFactor = (opponentSize / 15.) * jumpFactorVariability;
      int energyNeeded = (int)Math.ceil(2 * jumpVelocity * jumpFactor / GRAVITY);
      if (opponentX - x < reactionTime && !jump && !jumped && energyNeeded <= energy && energy >= maxEnergy / 2) {
         midJumpCount = 0;
         jump = true;
         jumped = true;
         energyRecover = 0;
         energy -= energyNeeded;
      }
      if (missleVelocity < 10 && !jump) {
         speed = 4 + missleVelocity;
      }
      prevMisslePosition = misslePosition;
   }
   

   // Draws the dodge with the given Graphics pen "g"
   public void draw(Graphics g) {
      g.drawRect(x, y, size, size);
   }
   
   // Updates the position of the Dodge.
   // TODO change method name
   public void getMove() {
      // calculate missel speed
      // jump factor missle height based
      x = x + speed;
      if (x > screenWidth) {
         jumped = false;
      }
      x = x % screenWidth;
      if (x < 0) {
         x = screenWidth + x;
      }
      if (jump) {
         midJumpCount++;
         y = (int)(originY - (jumpFactor * jumpVelocity * midJumpCount - 0.5 * GRAVITY * midJumpCount * midJumpCount));
         if ( y < 0) {
            y = 0;
            midJumpCount = (int)((jumpFactor*jumpVelocity + Math.sqrt (-2*GRAVITY*originY + Math.pow (jumpFactor*jumpVelocity, 2)))/GRAVITY);
         }
         if (y > originY) {
            y = originY;
            jump = false;
         }   
      }
      if (!jump && energy < maxEnergy && energyRecover % 4 == 0) { //&& !jump/*&& midJumpCount % recoverRate == 0*/) {
         energy++;
      }
      energyRecover++;
   }
   
   // Returns the result of the quadratic formula given a, b, and c. 
   public double quadFormula(double a, double b, double c) {
      // System.out.println(a);
      // System.out.println("ERROR" + ( -b + Math.sqrt(b * b - 4 * a * c) / (2 * a) ));
      // System.out.println(b * b - 4 * a * c);
      return (-b + Math.sqrt(b * b - 4 * a * c) ) / (2 * a);
   }
   
   // Returns the x position of the Dodge
   public int getX() {
      return x;
   }
   
   // Returns the y position of the Dodge
   public int getY() {
      return y;
   }
   
   // Resets the position of the Dodge and increases the jump velocity. 
   public void reset() {
      jumpVelocity++;
      x = originX;
      y = originY;
      jump = false;
   }
   
   //  Resets the Dodge's position when it crashes, given the opponentX and opponentSize.
   public void crash(int opponentX, int opponentSize) {
      // System.out.println(x - opponentX);
      /*if (x - opponentX < 3) {
         reactionTime++;
         jumpVelocity++;
      } else if (x - opponentX < opponentSize / 2) {
         jumpVelocity++;
      } else {
         reactionTime--;
      }
      //System.out.println("jumpnow : " + reactionTime + "  jumpv : " + jumpVelocity);*/
      x = originX;
      y = originY;
      energy -= maxEnergy / 10;
      jump = false;
      crashes++;
   }
   
   // Returns the jump velocity of this Dodge
   public int getJumpVelocity() {
      return jumpVelocity;
   }
   
   // Returns the reaction time of this Dodge
   public int getReactionTime() {
      return reactionTime;
   }
   
   // Returns the number of crashes this Dodge has had. 
   public int getCrashes() {
      return crashes;
   }
   
   // Returns the jump factor variability of this Dodge
   public double getJumpFactorVariability() {
      return jumpFactorVariability; 
   }
   
   // Returns the max energy of this Dodge
   public int getMaxEnergy() {
      return maxEnergy;
   }
   
   // Returns the Dodges reaction time, jump velocity, max energy, and jump factor variability
   // as an array. 
   // TODO Use HashMap for this instead
   public double[] getGenes() {
      double[] genes = {reactionTime, jumpVelocity, maxEnergy, jumpFactorVariability};
      return genes;
   }
}
