/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package packetsworld;

import java.awt.event.KeyEvent;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author JuanPablo
 */
/*
Por hacer:
    -Cuando mueve aleatorio, que compruebe que esta dentro de los limites de la matriz el movimient
    -En caso de no ver paquetes que mueva por potencial
    -Graficas
*/
public class PacketsWorld {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
            //Charts c = new Charts();
            
            Environment env = new Environment();
            env.addPacketToEnvironment(Constants.black, new Coordinates(3,1), new Coordinates(3,3));
            env.addObstacleToEnvironment(new Coordinates(2,1));
            env.addObstacleToEnvironment(new Coordinates(2,3));
            env.addPacketToEnvironment(Constants.white, new Coordinates(14,4), new Coordinates(8,9));
            env.addPacketToEnvironment(Constants.blue, new Coordinates(6,9), new Coordinates(7,32));
            env.addPacketToEnvironment(Constants.red, new Coordinates(13,15), new Coordinates(14,17));
            env.addPacketToEnvironment(Constants.black, new Coordinates(6,17), new Coordinates(4,8));
            //env.addPacketToEnvironment(Constants.white, new Coordinates(0,0), new Coordinates(4,4));
            //env.addPacketToEnvironment(Constants.black, new Coordinates(1,3), new Coordinates(2,2));
            env.setAgentCoordinates(new Coordinates(1,1));
            env.setAgentOrientation(Constants.North);
            
            Agent ag = new Agent();
            ag.setPosition(env.getAgentCoordinates());
            ag.setDirection(Constants.North);
            int next;
            Scanner in = new Scanner(System.in);
            while(true)
            {
                try 
                {
                    int done = 0;
                    Cell[][] vision = env.getVision(env.getAgentCoordinates());
                    //System.out.println("Vision");
                    //env.printMatrix(vision);
                    //Beliefs
                    ag.readVisionMatrix(vision,env.getAgentCoordinates(),env.getAgentOrientation());
                    System.out.println("Ag.Memory");
                    env.printMatrix(ag.getMemory());
                    /*System.out.println("Memory.Potential");
                    printMatrixPotential(ag.getMemory());*/
                    //Desire
                    next = ag.Think();
                    //Intention
                    env.readAction(next);
                    while(done!=1) 
                    {
                            done = in.nextInt( );
                    }
                } 
                catch (InterruptedException ex) {
                    Logger.getLogger(PacketsWorld.class.getName()).log(Level.SEVERE, null, ex);
                }
                System.out.println("World");
                env.printMatrix(env.getWorld());
            }
    }
    
    static public void printMatrixBooleans(boolean[][] matriz)
   {
        for (int x=0; x < matriz.length; x++) 
        {
            System.out.print("| ");
            for (int y=0; y < matriz[x].length; y++) 
            {
              if(matriz[x][y]==true)
              {
                System.out.print ("X");
                if (y!=matriz[x].length-1) System.out.print(" | ");
              }
              else
              {
                  System.out.print (" ");
                if (y!=matriz[x].length-1) System.out.print(" | ");
              }
            }
            System.out.println(" |");
        }
        System.out.println(" ");
   }
    
   static public void printMatrixChar(char[][] matriz)
   {
        for (int x=0; x < matriz.length; x++) 
        {
            System.out.print("| ");
            for (int y=0; y < matriz[x].length; y++) 
            {
              System.out.print (matriz[x][y]);
                if (y!=matriz[x].length-1) System.out.print(" | ");
            }
            System.out.println(" |");
        }
        System.out.println(" ");
   }
   
   static public void printMatrixPotential(Cell[][] matriz)
   {
        for (int x=0; x < matriz.length; x++) 
        {
            System.out.print("| ");
            for (int y=0; y < matriz[x].length; y++) 
            {
              System.out.print (matriz[x][y].getPotential());
                if (y!=matriz[x].length-1) System.out.print(" | ");
            }
            System.out.println(" |");
        }
        System.out.println(" ");
   }
}
