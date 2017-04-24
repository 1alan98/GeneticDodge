import java.awt.*;
import java.util.*;

public class Missle {

   private int screenWidth;
   private int screenHeight;
   private int size;
   private int x;
   private int y;
   private int originX; 
   private int originY;
   private int frame;
   private int speed;
   private final int MAX_SPEED = 5;
   private boolean isOffScreen;

   public Missle(int screenWidth, int screenHeight) {
      this.screenWidth = screenWidth;
      this.screenHeight = screenHeight;
      Random r = new Random();
      size = r.nextInt(25) + 1;
      x = screenWidth;
      y = screenHeight / 2 - (size - 1) ;
      originX = x;
      originY = y;
      frame = 0;
      speed = r.nextInt(MAX_SPEED) + 1;
      isOffScreen = false;
   }
   
   public void reset() {
      x = originX;
      y = originY;
      isOffScreen = false;
      frame = 0;
      if (new Random().nextBoolean() && new Random().nextBoolean()) {
         speed = new Random().nextInt(MAX_SPEED) + 1;
      }
   }
   
   public void draw(Graphics g) {
      g.fillRect(x, y, size, size);
   }
   
   public void getMove() {
      if (new Random().nextInt(100) < 5) {
         speed = new Random().nextInt(MAX_SPEED) + 1;
      }
      x = x - speed;
      if (x < 0) {
         x = screenWidth + x;
         isOffScreen = true;
      }
   }
   
   public int getSize() {
      return size;
   }
   
   public int getY() {
      return y;
   }
   
   public int getX() {
      return x;
   }
   
   public boolean isCrashing(int victimX, int victimY) {
      return victimX >= x && victimX <= x + size && victimY >= y && victimY <= y + size;
   }
   
   public boolean isOffScreen() {
      return isOffScreen;
   }
}