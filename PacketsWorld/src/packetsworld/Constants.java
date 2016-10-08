/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package packetsworld;

/**
 *
 * @author JuanPablo
 */
public class Constants {
    private Constants() {
            // restrict instantiation
    }
    //Colors constants
    public static final Packet white = new Packet("white",1);
    public static final Packet yellow = new Packet("yellow",5);
    public static final Packet green = new Packet("green",10);
    public static final Packet blue = new Packet("blue",20);
    public static final Packet red = new Packet("red",50);
    public static final Packet black = new Packet("black",100);
    
    //World constants
    public static final int worldsize = 50;
    
    //Cell constants
    public static final int cell_empty = 0;
    public static final int cell_filled = 1;
    public static final int cell_obstacle = 2;
    public static final int cell_agent = 3;
    
    //ActionType constants
    public static final int actForward = 0;
    public static final int actTurnRight = 1;
    public static final int actTurnLeft = 2;
    public static final int actPickup = 3;
    public static final int actPutdown = 4;
    public static final int actRest = 5;
    
    //Agents constants
    public static final int delta = 4;
    public static final int stamina = 1000;
    public static final int restTime = 10000;
    
    //Direction
    public static final int North = 0;
    public static final int East = 1;
    public static final int South = 2;
    public static final int West = 3;
    
    //Backtracking depth
    public static final int limitBacktracking = 20;
    
}
