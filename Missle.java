import java.awt.*;
import java.util.*;

// A missle only travels to the left at varying speeds through its journey. 

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
   
   // Contruscts a missle given the screenWidth and screenHeight of the window that it will be in
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
   
   // Resets the position of the missle
   public void reset() {
      x = originX;
      y = originY;
      isOffScreen = false;
      frame = 0;
      if (new Random().nextBoolean() && new Random().nextBoolean()) {
         speed = new Random().nextInt(MAX_SPEED) + 1;
      }
   }
   
   // Draws this missle given a Graphics "g" pen
   public void draw(Graphics g) {
      g.fillRect(x, y, size, size);
   }
   
   // Updates the position of the missle
   // TODO change method name
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
   
   // Returns the size of this missle
   public int getSize() {
      return size;
   }
   
   // Returns the y position of this missle
   public int getY() {
      return y;
   }
   
   // Returns the x position of this missle
   public int getX() {
      return x;
   }
   
   // Returns true if the given victimX and victimY coordinates are in the same coordiantes 
   // this missle is in 
   public boolean isCrashing(int victimX, int victimY) {
      return victimX >= x && victimX <= x + size && victimY >= y && victimY <= y + size;
   }
   
   // Returns true if the missle has traveled off screen
   public boolean isOffScreen() {
      return isOffScreen;
   }
}
