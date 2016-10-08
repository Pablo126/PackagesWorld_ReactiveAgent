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
public class Packet {
    
    private final String color;
    private final int weight;
    private Coordinates destination;
    private boolean moved;
    
    public Packet(String color, int weight){
        this.color = color;
        this.weight = weight;
        this.destination = null;
        this.moved = false;
    }
    
    public String getColor()
    {
        return this.color;
    }
    
    public int getWeight()
    {
        return this.weight;
    }
    
    public Coordinates getDestination()
    {
        return this.destination;
    }
    
    public void setDestination(int x, int y)
    {
        this.destination = new Coordinates(x,y);
    }
    
    public void setMoved(boolean value)
    {
        this.moved = value;
    }
    
    public boolean getMoved()
    {
        return this.moved;
    }
}
