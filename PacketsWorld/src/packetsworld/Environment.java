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
public class Environment {
    
    private Cell[][] world = new Cell[Constants.worldsize][Constants.worldsize];
    private Coordinates agent_coordinates;
    private int agent_orientation;
    private Packet p_aux = null;
    
    public Environment()
    {
        //Filled all the matrix with no packets and 0 as value of cell
        for (int i=0;i<Constants.worldsize;i++)
        {
            for (int j=0;j<Constants.worldsize;j++)
            {
                world[i][j] = Cell.EmptyCell();
            }
        }
        //Set agent coordinates
        agent_coordinates = new Coordinates(0,0);
        //Set agent orientation
        agent_orientation = Constants.East;
    }
    
    public void addPacketToEnvironment(Packet p, Coordinates origin, Coordinates destination)
    {
        p.setDestination(destination.getX(), destination.getY());
        this.world[origin.getX()][origin.getY()] = Cell.NewCellWithPacket(p);
        System.out.println("Added packet with color:"+p.getColor()+" in Position:"+origin.print()+" and Destination:"+destination.print());
    }
    
    public void addObstacleToEnvironment(Coordinates coordinates)
    {
        this.world[coordinates.getX()][coordinates.getY()] = Cell.NewCellWithObstacle();
    }
    
    public Cell[][] getWorld()
    {
        return world;
    }
    
   public Cell[][] getVision(Coordinates agent_coordinates)
   {
       int grade = Constants.delta;
       int x = agent_coordinates.getX();
       int y = agent_coordinates.getY();
       Cell[][] vision_grid = new Cell[grade*2+1][grade*2+1];
       int h=0;
       int g=0;
       for(int i=x-grade;i<=x+grade;i++)
       {
           for(int j=y-grade;j<=y+grade;j++)
           {
               if(i>=0 && i<Constants.worldsize && j>=0 && j<Constants.worldsize)
               {
                   vision_grid[g][h] = world[i][j];
               }
               else
               {
                   vision_grid[g][h] = null;
               }
               h++;
           }
           h=0;
           g++;  
       }
       return vision_grid;
   }
   
   public void setAgentCoordinates(Coordinates coordinates)
   {
       this.agent_coordinates = coordinates;
       world[coordinates.getX()][coordinates.getY()] = Cell.AgentCell();
       System.out.println("Agent move to "+coordinates.print());
   }
   
   public void setAgentOrientation(int orientation)
   {
       this.agent_orientation = orientation;
   }
   
   public int getAgentOrientation()
   {
       return this.agent_orientation;
   }
   
   public void printMatrix(Cell[][] matriz)
   {
        for (int x=0; x < matriz.length; x++) 
        {
            System.out.print("| ");
            for (int y=0; y < matriz[x].length; y++) 
            {
              if(matriz[x][y]!=null)
              {
                System.out.print (this.getSymbolCellValue(matriz[x][y].getValue()));
                if (y!=matriz[x].length-1) System.out.print(" | ");
              }
              else
              {
                  System.out.print ("N");
                if (y!=matriz[x].length-1) System.out.print(" | ");
              }
            }
            System.out.println(" |");
        }
        System.out.println(" ");
   }
   
   public Coordinates getAgentCoordinates()
   {
       return agent_coordinates;
   }
   
   public char getSymbolCellValue(int value)
   {
       switch (value) {  
            case(0): 
                return ' ';
            case(1): 
                return 'P';
            case(2): 
                return 'X';
           default: 
                return 'O';
       }
   }
   public void setPacketPickup(Packet packet)
   {
       p_aux = packet;
   }
   
   public void readAction(int action) throws InterruptedException
   {
       switch(action)
       {
           case Constants.actRest:
               System.out.println("REST: 10 seconds; wait");
               Thread.sleep(Constants.restTime);
               break;
           case Constants.actPutdown:
               world[this.agent_coordinates.getX()][this.agent_coordinates.getY()] = Cell.AgentCell();
               p_aux = null;
               System.out.println("Action: PUT DOWN");
               break;
           case Constants.actPickup:
               //The agent pickup the packet
               world[this.agent_coordinates.getX()][this.agent_coordinates.getY()] = Cell.AgentCell();
               p_aux=null;
               System.out.println("Action: PICK UP");
               break;
           case Constants.actForward:
               switch(this.agent_orientation){
                case Constants.North: // Orientacion north
                        if (world[this.agent_coordinates.getX()-1][this.agent_coordinates.getY()].getValue() != Constants.cell_obstacle) 
                            moveAgent(new Coordinates(this.agent_coordinates.getX()-1,this.agent_coordinates.getY()));
                        break;
                case Constants.South: // Orientacion South
                        if (world[this.agent_coordinates.getX()+1][this.agent_coordinates.getY()].getValue() != Constants.cell_obstacle) 
                            moveAgent(new Coordinates(this.agent_coordinates.getX()+1,this.agent_coordinates.getY()));
                        break;
                case Constants.East: // Orientacion east
                        if (world[this.agent_coordinates.getX()][this.agent_coordinates.getY()+1].getValue() != Constants.cell_obstacle) 
                            moveAgent(new Coordinates(this.agent_coordinates.getX(),this.agent_coordinates.getY()+1));
                        break;
                case 3: // Orientacion Oeste
			if (world[this.agent_coordinates.getX()][this.agent_coordinates.getY()-1].getValue() != Constants.cell_obstacle) 
                            moveAgent(new Coordinates(this.agent_coordinates.getX(),this.agent_coordinates.getY()-1));
                        break;
                }
               System.out.println("Action: FORWARD");
                break;
           case Constants.actTurnLeft:
               this.agent_orientation = (this.agent_orientation+3)%4;
               System.out.println("Action: TURN LEFT");
               break;
           case Constants.actTurnRight:
               this.agent_orientation = (this.agent_orientation+1)%4;
               System.out.println("Action: TURN RIGHT");
               break;
        }
   }
   
    public void moveAgent(Coordinates coordinates)
    {
        for (int x=0; x < world.length; x++) 
         {
             for (int y=0; y < world[x].length; y++) 
             {
                if(x==this.agent_coordinates.getX() && y==this.agent_coordinates.getY())
                    if(p_aux == null)
                        world[x][y]=Cell.EmptyCell();      
                    else
                    {
                        world[x][y]=Cell.NewCellWithPacket(p_aux);
                        p_aux = null;
                    }
             }
         }
        if(world[coordinates.getX()][coordinates.getY()].getValue()==Constants.cell_filled)
        {
            p_aux = world[coordinates.getX()][coordinates.getY()].getPacket();
        }
        world[coordinates.getX()][coordinates.getY()] = Cell.AgentCell();
        this.agent_coordinates = coordinates;
    }

}

