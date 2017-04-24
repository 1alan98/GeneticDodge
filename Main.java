import java.util.*;
import java.awt.*;
import java.io.*;

// Runs a simulation. Dodges are created, and they each are tested on their ability to jump 
// over the missle. The best dodges reprodue and survive to the next generation. The program
// creates and reads data from files for each generation

// TODO change dodge array into dodge ArrayList<Dodge> to vary population sizes by generation

public class Main {

   public static final int SCREEN_WIDTH = 500;
   public static final int SCREEN_HEIGHT = 500;
   public static final int GENERATIONS = 500;
   public static final boolean ANIMATE = true;
   public static final String FILE_PATH = "DodgesGenerations/";
   public static final String GENERATION_FILE_NAME = "_Generation";
   
   public static void main(String[] args) throws FileNotFoundException {
      // TODO allow to read from file when behaviors are complex enough to take
      // many many generations 10000 perhaps??
      
      //boolean load = input.next().equalsIgnoreCase("y");
      //if (load) {
         // String fileName = input.next();
         // generation = look through file
      //} else {
         //String file = input.next();
         //PrintStream output = new PrintStream(file);
         //Dodge first = new Dodge(SCREEN_WIDTH, SCREEN_HEIGHT);
      int generation = 0;
      //PrintStream output = new PrintStream("DODGES.txt");  
      DrawingPanel p = new DrawingPanel(GENERATIONS, GENERATIONS / 2);
      Graphics g = p.getGraphics();
      //String fileName = "DODGES.txt";
      for (int i = generation; i < GENERATIONS; i++) {
         // ArrayList<dodge> dodgeGeneration = generateGeneration(g, i);
         Dodge[] dodgeGeneration = generateGeneration(g, i);      
         testDodges(p, g, i, dodgeGeneration);
      }
      compileGenerations();
      System.out.println("DONE");
   }
   
   // Generates one generation of dodges. Also updates the graph of average Dodge fitness given the 
   // generation and a graphics g object. 
   public static Dodge[] generateGeneration(Graphics g, int generation) throws FileNotFoundException {
      Dodge[] dodge = new Dodge[100];
      if (generation == 0) {
         for (int i = 0; i < 100; i++) {
            //dodge.add()
            dodge[i] = new Dodge(SCREEN_WIDTH, SCREEN_HEIGHT);   
         }
      } 
      else {
         int[] topFourDodgers = reduceGenePool(g, dodge, generation);
         mate(dodge, topFourDodgers);
      }
      return dodge;
   }
   
   // Given a generation of Dodges, tests the fitness of each Dodge
   // If animation is true, it also shows an animation of the Dodges being tested
   // p represents the DrawingPanel for the animation
   // g represents the Graphics object to draw the animation
   // generation represents the generation being tested
   // dodge represents the array of the Dodges being tested
   public static void testDodges(DrawingPanel p, Graphics g, int generation, Dodge[] dodge) throws FileNotFoundException {
      PrintStream output = new PrintStream(new File(FILE_PATH + generation + GENERATION_FILE_NAME));
      Random r = new Random();
      output.println("Generation: " + generation);
      boolean animate = false;
      if (ANIMATE && generation == GENERATIONS - 1) {
         p = new DrawingPanel(SCREEN_WIDTH, SCREEN_HEIGHT);
         g = p.getGraphics();
         animate = true;
      }
      for (int i = 0; i < 100; i++) {
         Missle missle = new Missle(SCREEN_WIDTH, SCREEN_HEIGHT);
         for (int j = 0; j < 20000; j++) {
            missle.getMove();
            if (missle.isOffScreen()) {
               missle = new Missle(SCREEN_WIDTH, SCREEN_HEIGHT);          
            }
            int missleSize = missle.getSize();
            int missleX = missle.getX();
            dodge[i].jump(missleX, missleSize);
            dodge[i].getMove();
            if (missle.isCrashing(dodge[i].getX(), dodge[i].getY())) {
               missle = new Missle(SCREEN_WIDTH, SCREEN_HEIGHT);
               dodge[i].crash(missleX, missleSize);
            }
            if (animate && j > 1500) {
               missle.draw(g);
               dodge[i].draw(g);
               p.sleep(100);
               p.clear();
            }
         }
         output.println("Dodge #" + (i + 1));
         output.println("Fitness     Reaction Time     Jump Velocity         Jump Factor Variability          Max Energy");
         output.printf("%4d%13d%19d%28f%28d%n", dodge[i].getCrashes(), dodge[i].getReactionTime(), dodge[i].getJumpVelocity(), dodge[i].getJumpFactorVariability(), dodge[i].getMaxEnergy());
      }
   }
   
   
   // Given an array of Dodges, this kills the Dodges of the previous generation who have below average fitness
   // and saves the rest of the Dodges
   // MOVE TOP DODGERS IT DOES NOT BELONG HERE ????
   // It also finds four Dodges with the best fitness and returns the indexes of those Dodgers
   // It also 
   // g represents the 
   public static int[] reduceGenePool(Graphics g, Dodge[] dodge, int generation) throws FileNotFoundException {
      String fileName = FILE_PATH + (generation - 1) + GENERATION_FILE_NAME;
      Random r = new Random();
      Scanner dodgeFile = new Scanner(new File(fileName));
      // getToPreviousGeneration(dodgeFile, generation);
      int averageFitness = calculateAverageFitness(dodgeFile);
      g.drawRect(generation, GENERATIONS / 2 - averageFitness * (GENERATIONS / 100 * 75 / 200), 1, 1);
      dodgeFile = new Scanner(new File(fileName));
      // getToPreviousGeneration(dodgeFile, generation);
      int[] topFourDodgers = new int[4];
      int survivors = survivingDodgers(dodgeFile,averageFitness, dodge, topFourDodgers);  
      if (survivors < 50) {
         for (int i = survivors; i < 50; i++) {
            dodge[i] = new Dodge(SCREEN_WIDTH, SCREEN_HEIGHT);
         }
      }
      return topFourDodgers;
   }
    
   // Mates the given Dodges. In some of the generations half of the offspring will be the
   //          children of the top four dogers. In others, the offspring will be evenly spread
   //          out between all the parents
   // dodge represents the array of the Dodges who will mate
   // topFourDodgers represents an array with the indexes of the top four dodges
   public static void mate(Dodge[] dodge, int[] topFourDodgers) {
      Random r = new Random();
      if (r.nextBoolean()) {
         for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 25; j++) {
               //System.out.println(50 + i * 25 +  j);
               if (r.nextInt(100) < 75) {
                  dodgeBirth(dodge, j, 25 - 1 - j, 50 + i * 25 +  j);
               }
            }
         }
         
         for (int i = 0; i < 100; i++) {
            if (dodge[i] == null) {
               dodge[i] = new Dodge(SCREEN_WIDTH, SCREEN_HEIGHT);
            }
         }
      } 
      else {       
         /* 
         ORIGINAL CODE, MAKES DODGES EVOLVE EXTREMELY QUICKLY
          -----------------------------------------------------
         */
         for (int i = 0; i < 25; i++) {
            dodgeBirth(dodge, i, 25 - 1 - i, 50 + i);
         }
         int eliteOffspring = 0;
         for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
               for (int k = j + 1; k < 4; k++) {
                  dodgeBirth(dodge, topFourDodgers[j], topFourDodgers[k], 75 + eliteOffspring);
                  eliteOffspring++;
               }
            }
         }
      }
      
      dodgeBirth(dodge, topFourDodgers[0], topFourDodgers[1], 99);
      //dodge[99] =  new Dodge(SCREEN_WIDTH, SCREEN_HEIGHT, dodge[topFourDodgers[0]].getJumpVelocity(), dodge[topFourDodgers[1]].getReactionTime(), dodge[topFourDodgers[0]].getJumpFactorVariability(),dodge[topFourDodgers[1]].getMaxEnergy());
      if (r.nextInt(1000) == 0) {
         apocolypse(dodge);
      }
   }
   
   // Initiates an apocolypse on the Dodges, with mortality rate of 98%
   // dodge represents the array of Dodges who will go through the apocolypse
   public static void apocolypse(Dodge[] dodge) {
      for (int i = 0; i < 100; i++) {
         if (new Random().nextInt(50) != 0) {
            dodge[i] = new Dodge(SCREEN_WIDTH, SCREEN_HEIGHT);
         }
      }
   }
   
   // Reaches the beginning of the given generation
   // dodgeFile represents the Scanner object of a sorted 
   //       text file of the stats of multiple Dodge generations
   public static void getToPreviousGeneration(Scanner dodgeFile, int generation) {
      dodgeFile.nextLine(); // generation line     
      for (int i = 0; i < (100 * 3 + 1) * (generation - 1); i++) {
         dodgeFile.nextLine();
      }
   }
   
   // Calculates the average fitness of a Dodge generation
   // dodgeFile reprensents the Scanner object of the file with the stats of the Dodge generation
   public static int calculateAverageFitness(Scanner dodgeFile) {
      int averageFitness = 0;
      dodgeFile.nextLine();   // generation
      for (int i = 0; i < 100; i++) {
         dodgeFile.nextLine();   // id
         dodgeFile.nextLine();  // labels
         int fitness = dodgeFile.nextInt();
         averageFitness += fitness; // fitness       
         dodgeFile.nextLine(); // reaction time, jump velocity , max energy
      }
      return averageFitness / 100;
   }
   
   // Given a file with the stats of a Dodge generation, this finds and saves the Dodges with above average fitness
   // dogeFile represents the Scanner object for the file of a Dodge generation
   // returns the number of Dodgers who had above average fitness
   // averageFitness represents the average fitness of the generation
   // dodge represents the array of the Dodges
   // topFourDodges represents the array of the index of the top four Dodges
   public static int survivingDodgers(Scanner dodgeFile, int averageFitness, Dodge[] dodge, int[] topFourDodgers) {
      int survivors = 0;
      Random r = new Random();
      int bestFitness = 100;
      int secondBestFitness = 100;
      int thirdBestFitness = 100;
      int fourthBestFitness = 100;
      int checked = 0;
      dodgeFile.nextLine(); // generation
      while (survivors < 50 && checked < 100 && dodgeFile.hasNext()) {
         checked++;
         dodgeFile.nextLine(); // id
         dodgeFile.nextLine(); // labels//
         int fitness = dodgeFile.nextInt();
         if (fitness <= averageFitness && r.nextInt(100) < 95) {
            int reactionTime = dodgeFile.nextInt();
            int jumpVelocity = dodgeFile.nextInt();
            double jumpFactorVariability = dodgeFile.nextDouble();
            int maxEnergy = dodgeFile.nextInt();
            dodge[survivors] = new Dodge(SCREEN_WIDTH, SCREEN_HEIGHT, jumpVelocity, reactionTime, jumpFactorVariability, maxEnergy);
            if (fitness < bestFitness) {
               bestFitness = fitness;
               topFourDodgers[0] = survivors;
            } 
            else if (fitness < secondBestFitness) {
               secondBestFitness = fitness;
               topFourDodgers[1] = survivors;
            } 
            else if (fitness < thirdBestFitness) {
               thirdBestFitness = fitness;
               topFourDodgers[2] = survivors;
            } 
            else if (fitness < fourthBestFitness) {
               fourthBestFitness = fitness;
               topFourDodgers[3] = survivors;
            } 
            survivors++;
            dodgeFile.nextLine(); // go to next line
         } 
         else {
            dodgeFile.nextLine(); // discared reaction time and jump velocity
         }
      }
      return survivors;
   }
   
   // Compiles all the text files with the stats of each generation into one large file
   public static void compileGenerations() throws FileNotFoundException {
      PrintStream output = new PrintStream(new File("DodgesGenerations/Generations_Compiled"));
      for (int i = 0; i < 100; i++) {
         Scanner generation = new Scanner(new File("DodgesGenerations/" + i + "_Generation"));
         for (int j = 0; j < 301; j++) {
            output.println(generation.nextLine());
         }
      }
   }
   
   // Given the indexes of the mom and dad Dodge, creates a new dodge
   // dodge represents the array with all the dodges
   // dadIndex represents the index of the dad in the dodge array
   // momIndex represents the index of the mom in the dodge array
   // birthIndex represents the index of the Dodge that will be born
   public static void dodgeBirth(Dodge[] dodge, int dadIndex, int momIndex,int birthIndex) {
      // use a hash table for this
      Random r = new Random();
      double[] dadGenes = dodge[dadIndex].getGenes();
      double[] momGenes = dodge[momIndex].getGenes();
      int offspringReactionTime;
      int offspringJumpVelocity;
      int offspringMaxEnergy;
      double offspringJumpFactorVariability = ((dadGenes[3] + momGenes[3]) / 2.0) * ((r.nextInt(6)+ 7.5) / 10.0);      
      if (r.nextBoolean()) {
         offspringReactionTime = (int)dadGenes[0];
      } 
      else {
         offspringReactionTime = (int)momGenes[0];
      }
      if (r.nextBoolean()) {
         offspringJumpVelocity = (int)dadGenes[1];
      } 
      else {
         offspringJumpVelocity = (int)momGenes[1];
      }
      if (r.nextBoolean()) {
         offspringMaxEnergy = (int)dadGenes[2];
      } 
      else {
         offspringMaxEnergy = (int)momGenes[2];
      }
      dodge[birthIndex] = new Dodge(SCREEN_WIDTH, SCREEN_HEIGHT, offspringJumpVelocity, offspringReactionTime, offspringJumpFactorVariability, offspringMaxEnergy);
   }
   
   //public static double[] getDodgeStats() {
      // get stats for a single dodge in a file
      // probably helpful if you want to load dodges from a file
   //}
}
