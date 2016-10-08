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
public class Backtracking {
    
    char[][] matrix = new char[Constants.worldsize][Constants.worldsize];
    boolean found;
    Coordinates start;
    Coordinates end;
    Cell[][] memory;
    int camino;
    Coordinates next;
    
    public Backtracking(Coordinates start, Coordinates end, Cell[][] memory)
    {
        this.start = start;
        this.end = end;
        this.memory = memory;
    }
    
    public char[][] getMatrix(){
        return matrix;
    }
    
    public Coordinates getNext(){
        return this.next;
    }
    
    public int getCamino()
    {
        return this.camino;
    }
    
    public void backtracking(int direction, Coordinates posicion, int total)
    {
        int turnCost;
        if(posicion.getX() == end.getX() && posicion.getY() == end.getY())
        {
            matrix[posicion.getX()][posicion.getY()] ='x';  
            found = true;
            this.camino = total;
            this.next = getNextX(matrix);
        }
        else
        {
            if(posicion.getX()>=0 && posicion.getX()<Constants.worldsize && posicion.getY()>=0 && posicion.getY()<Constants.worldsize)
            {
                if(memory[posicion.getX()][posicion.getY()].getValue() != Constants.cell_obstacle && matrix[posicion.getX()][posicion.getY()]!='x')
                {
                    matrix[posicion.getX()][posicion.getY()] ='x';   
                    total++;
                    //printMatrixChar(matrix);
                    if(total<Constants.limitBacktracking)
                    {
                    if(total<this.camino || !found)
                    {
                        //South
                     //   if(direction!=Constants.North)
            //            {
                            posicion.setX(posicion.getX()+1);
                            turnCost = turnCost(direction,Constants.South);
                            total+=turnCost;
                            backtracking(Constants.South,posicion,total);
                            total-=turnCost;
                            posicion.setX(posicion.getX()-1);
//                        }
                        //West
                        //if(direction!=Constants.East)
                        //{
                            posicion.setY(posicion.getY()-1);
                            turnCost = turnCost(direction,Constants.West);
                            total+=turnCost;
                            backtracking(Constants.West,posicion,total);
                            total-=turnCost;
                            posicion.setY(posicion.getY()+1);
                        //}
                        //North
                        //if(direction!=Constants.South)
                        //{
                            posicion.setX(posicion.getX()-1);
                            turnCost = turnCost(direction,Constants.North);
                            total+=turnCost;
                            backtracking(Constants.North,posicion,total);
                            total-=turnCost;
                            posicion.setX(posicion.getX()+1);
                       // }
                        //East
                       // if(direction!=Constants.West)
                        //{
                            posicion.setY(posicion.getY()+1);
                            turnCost = turnCost(direction,Constants.East);
                            total+=turnCost;
                            backtracking(Constants.East,posicion,total);
                            total-=turnCost;
                            posicion.setY(posicion.getY()-1);
                      //  }
                    }
                    }
                    total--;
                    matrix[posicion.getX()][posicion.getY()]=' ';
                }
            }
        }
    }
    
    public int turnCost(int old_direction, int new_direction)
    {
        switch(old_direction)
        {
            case Constants.North:
                switch(new_direction)
                {
                    case Constants.North:
                        return 0;
                    case Constants.East:
                        return 1;
                    case Constants.West:
                        return 1;
                    case Constants.South:
                        return 2;
                }
            case Constants.South:
                switch(new_direction)
                {
                    case Constants.North:
                        return 2;
                    case Constants.East:
                        return 1;
                    case Constants.West:
                        return 1;
                    case Constants.South:
                        return 0;
                }
            case Constants.East:
                switch(new_direction)
                {
                    case Constants.North:
                        return 1;
                    case Constants.East:
                        return 0;
                    case Constants.West:
                        return 2;
                    case Constants.South:
                        return 1;
                }
            case Constants.West:
                switch(new_direction)
                {
                    case Constants.North:
                        return 1;
                    case Constants.East:
                        return 2;
                    case Constants.West:
                        return 0;
                    case Constants.South:
                        return 1;
                }
        }
        return -1;
    }
    
    
    
    public Coordinates getNextX(char[][] matrix_aux)
    {
        //printMatrixChar(matrix_aux);
        if(start.getX()+1<matrix_aux.length)
        {
            if(matrix_aux[start.getX()+1][start.getY()]=='x')//SOUTH
                return new Coordinates(start.getX()+1,start.getY());
        }
        if(start.getY()+1<matrix_aux.length)
        {
            if(matrix_aux[start.getX()][start.getY()+1]=='x')//EAST
                return new Coordinates(start.getX(),start.getY()+1);
        }
        if(start.getX()-1>=0)
        {
            if(matrix_aux[start.getX()-1][start.getY()]=='x')//NORTH
                return new Coordinates(start.getX()-1,start.getY());
        }
        if(start.getY()-1>=0)
        {
            if(matrix_aux[start.getX()][start.getY()-1]=='x')//WEST
                return new Coordinates(start.getX(),start.getY()-1);
        }
        return null;
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
}
