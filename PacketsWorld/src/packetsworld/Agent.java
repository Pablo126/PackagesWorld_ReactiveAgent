/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package packetsworld;

import static java.lang.Math.random;
import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author JuanPablo
 */
public class Agent {
    private Cell[][] memory = new Cell[Constants.worldsize][Constants.worldsize];
    private Packet packet = null;
    private int stamina = Constants.stamina;
    private Coordinates position;
    private int Direction;
    Coordinates next_coor=null;
    Coordinates next_coor_dest=null;
    Packet packetpickup = null;
    private Cell[][] memory_potential = new Cell[Constants.worldsize][Constants.worldsize];
    ArrayList<Coordinates> visiblePackets = new ArrayList<Coordinates>();

    public Agent()
    {
        for (int i=0;i<Constants.worldsize;i++)
        {
            for (int j=0;j<Constants.worldsize;j++)
            {
                memory[i][j] = Cell.EmptyCell();
            }
        }
    }
    
    public void readVisionMatrix(Cell[][] matrix, Coordinates agentpos, int agentdir)
    {
        this.position = agentpos;
        this.Direction = agentdir;
        int g=0;
        int h=0;
        for(int i=agentpos.getX()-Constants.delta;i<=agentpos.getX()+Constants.delta;i++)
        {
            for(int j=agentpos.getY()-Constants.delta;j<=agentpos.getY()+Constants.delta;j++)
            {
                if(matrix[g][h]!=null)
                {
                    memory[i][j] = matrix[g][h];
                }
                h++;
            }
            h=0;
            g++;      
        }
    }
    //Potential-----------------------
    private void increasePotentialMemory()
    {
        for (int i=0;i<Constants.worldsize;i++)
        {
            for (int j=0;j<Constants.worldsize;j++)
            {
                if(memory[i][j].getValue() == Constants.cell_obstacle || memory[i][j].getValue() == Constants.cell_agent )
                    memory[i][j].resetPotential();
                else
                    memory[i][j].increasePotential();
            }
        }
    }
    
    private int ThinkByPotential()
    {
        int max=0;
        int new_direction=0;
        if(position.getX()+1<memory.length)
        {
            if(memory[position.getX()+1][position.getY()].getPotential()>max)//SOUTH
            {
                max = memory[position.getX()+1][position.getY()].getPotential();
                new_direction=Constants.South;    
            }
        }
        if(position.getY()+1<memory.length)
        {
            if(memory[position.getX()][position.getY()+1].getPotential()>max)//EAST
            {
                max =memory[position.getX()][position.getY()+1].getPotential();
                new_direction=Constants.East;
            }
        }
        if(position.getX()-1>=0)
        {
            if(memory[position.getX()-1][position.getY()].getPotential()>max)//NORTH
            {
                max = memory[position.getX()-1][position.getY()].getPotential();
                new_direction=Constants.North;
            }
        }
        if(position.getY()-1>=0)
        {
            if(memory[position.getX()][position.getY()-1].getPotential()>max)//WEST
            {
                max = memory[position.getX()][position.getY()-1].getPotential();
                new_direction=Constants.West;
            }
        }
        return getMovementByDirection(Direction,new_direction);
    }
    //-----------------------
    
    public int Think()
    {       
        int tamano=9000000;
        Coordinates n=null;
        searchPackets();
        int next=-1;
        int weight;
        increasePotentialMemory();
        if(packet != null && next_coor_dest != null && position.getX()==next_coor_dest.getX() && position.getY() == next_coor_dest.getY())
        {//PUTDOWN PACKET
            next = Constants.actPutdown;
            next_coor_dest = null;
            next_coor = null;
            packet = null;
        }
        else if(packet != null)
        {//Leading the packet
            Backtracking back_dest2 = new Backtracking(position,packet.getDestination(),memory);
            back_dest2.backtracking(Direction,new Coordinates(position.getX(),position.getY()),0);
            next_coor_dest = packet.getDestination();
            n = back_dest2.getNext();
            next = getNextMovement(n);
        }
        else if(packet == null && next_coor != null && position.getX()==next_coor.getX() && position.getY() == next_coor.getY())
        {//Above the packet for pick up
            packet = packetpickup;
            next = Constants.actPickup;
        }
        else if(visiblePackets.isEmpty())//No packets(RANDOM)
        {
            next = ThinkByPotential();
        }
        else//There are packets in the vision
        {
            for(Coordinates c : visiblePackets)
            {
                
                Backtracking back = new Backtracking(position,c,memory);
                back.backtracking(Direction,new Coordinates(position.getX(),position.getY()),0);
                Packet p = memory[c.getX()][c.getY()].getPacket();
                weight = p.getWeight();
                Backtracking back_dest = new Backtracking(c,p.getDestination(),memory);
                back_dest.backtracking(directionPath(position,c),new Coordinates(c.getX(),c.getY()),0);
                int x=back.getCamino()+(back_dest.getCamino()*weight);
                if(back.getCamino()==0)
                {
                    next = ThinkByPotential();
                }
                else if(x<tamano)//Path with less cost
                {
                    n = back.getNext();
                    tamano = x;
                    next_coor = c;
                    packetpickup = memory[c.getX()][c.getY()].getPacket();
                    next = getNextMovement(n);
                }
            }
        }
        
        //Stamina
        if(packet!=null)
            this.stamina-=packet.getWeight();
        else
            this.stamina--;
        System.out.println("Stamina="+this.stamina);
        if(this.stamina<=0)
        {
            next = Constants.actRest;
            this.stamina = Constants.stamina;
        }
        //--------------------
        
        System.out.println("Next="+next);
        return next;
    }
    
    public int getNextMovement(Coordinates nextCell)
    {
        //printMatrixChar(matrix_aux);
        if(position.getX()+1<memory.length)
        {
            if(position.getX()+1==nextCell.getX() && position.getY()==nextCell.getY())//SOUTH
                return getMovementByDirection(Direction,Constants.South);        
        }
        if(position.getY()+1<memory.length)
        {
            if(position.getX()==nextCell.getX() && position.getY()+1==nextCell.getY())//EAST
                return getMovementByDirection(Direction,Constants.East);
        }
        if(position.getX()-1>=0)
        {
            if(position.getX()-1==nextCell.getX() && position.getY()==nextCell.getY())//NORTH
                return getMovementByDirection(Direction,Constants.North);
        }
        if(position.getY()-1>=0)
        {
            if(position.getX()==nextCell.getX() && position.getY()-1==nextCell.getY())//WEST
                return getMovementByDirection(Direction,Constants.West);
        }
        return -1;
    }
    
    public int getMovementByDirection(int old_direction,int new_direction)
    {
        switch(old_direction)
        {
            case Constants.North:
                switch(new_direction)
                {
                    case Constants.North:
                        return Constants.actForward;
                    case Constants.East:
                        return Constants.actTurnRight;
                    case Constants.West:
                        return Constants.actTurnLeft;
                    case Constants.South:
                        return Constants.actTurnRight;
                }
            case Constants.South:
                switch(new_direction)
                {
                    case Constants.North:
                        return Constants.actTurnRight;
                    case Constants.East:
                        return Constants.actTurnLeft;
                    case Constants.West:
                        return Constants.actTurnRight;
                    case Constants.South:
                        return Constants.actForward;
                }
            case Constants.East:
                switch(new_direction)
                {
                    case Constants.North:
                        return Constants.actTurnLeft;
                    case Constants.East:
                        return Constants.actForward;
                    case Constants.West:
                        return Constants.actTurnRight;
                    case Constants.South:
                        return Constants.actTurnRight;
                }
            case Constants.West:
                switch(new_direction)
                {
                    case Constants.North:
                        return Constants.actTurnRight;
                    case Constants.East:
                        return Constants.actTurnRight;
                    case Constants.West:
                        return Constants.actForward;
                    case Constants.South:
                        return Constants.actTurnLeft;
                }
        }
        return -1;
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
    
    public Packet getPacket()
    {
        return packet;
    }
    
    public void setPacket(Packet packet)
    {
        this.packet = packet;
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
    
    public Cell[][] getMemory()
    {
        return memory;
    }
    
    public void setPosition(Coordinates position)
    {
        this.position = position;
    }
    
    public void setDirection(int direction)
    {
        this.Direction = direction;
    }
    
    public void searchPackets()
    {
        visiblePackets.clear();
        for(int i=0;i<memory.length;i++)
        {
            for(int j=0;j<memory.length;j++)
            {
                if(memory[i][j].getValue()==Constants.cell_filled && memory[i][j].getPacket().getMoved()==false)
                    visiblePackets.add(new Coordinates(i,j));
            }
        }
    }
    
    
    //Return the action to go to a cell depending of the agent direction
    public int NextStep(int direction_start,int next_cell_position)
    {
        switch(direction_start)
        {
            case Constants.North:
                switch(next_cell_position)
                {
                    case Constants.North: return Constants.actForward;
                    case Constants.East: return Constants.actTurnRight;
                    case Constants.West: return Constants.actTurnLeft;
                    case Constants.South: return Constants.actTurnRight;
                }
            case Constants.East:
                switch(next_cell_position)
                {
                    case Constants.North: return Constants.actTurnLeft;
                    case Constants.East: return Constants.actForward;
                    case Constants.West: return Constants.actTurnRight;
                    case Constants.South: return Constants.actTurnRight;
                }
            case Constants.West:
                switch(next_cell_position)
                {
                    case Constants.North: return Constants.actTurnRight;
                    case Constants.East: return Constants.actTurnRight;
                    case Constants.West: return Constants.actForward;
                    case Constants.South: return Constants.actTurnLeft;
                }
            case Constants.South:
                switch(next_cell_position)
                {
                    case Constants.North: return Constants.actTurnRight;
                    case Constants.East: return Constants.actTurnLeft;
                    case Constants.West: return Constants.actTurnRight;
                    case Constants.South: return Constants.actForward;
                }
        }
        return Constants.actForward;
    }
    
    //This function supose a direction when the agent pickup the packet
    private int directionPath(Coordinates start,Coordinates end)
    {
        int direction=-1;
        Random rand = new Random();
        Coordinates result = new Coordinates(start.getX()-end.getX(),start.getY()-end.getY());
        if(result.getX()<0 && result.getY()<0)//South or East
            direction = randInt(1,2);
        else if(result.getX()<0 && result.getY()>0)//South or West
            direction = randInt(2,3);
        else if(result.getX()>0 && result.getY()>0)//North or West
        {
            while(direction==1 || direction==2 || direction==-1){
                direction = randInt(0,3);}
        }
        else if(result.getX()>0 && result.getY()<0)
            direction = randInt(0,1);
        else if(result.getX()>0)
            direction = Constants.North;
        else if(result.getX()<0)
            direction = Constants.South;
        else if(result.getY()>0)
            direction = Constants.West;
        else if(result.getY()<0)
            direction = Constants.East;
        else
            direction = randInt(0,3);
        return direction;
    }
    
    public static int randInt(int min, int max) {
        Random rand = new Random();
        int randomNum = rand.nextInt((max - min) + 1) + min;

        return randomNum;
    }
    
    
}
