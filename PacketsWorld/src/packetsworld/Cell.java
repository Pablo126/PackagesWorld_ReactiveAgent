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
public class Cell {
    private int value = Constants.cell_empty;
    private Packet packet = null;
    private int potential = 0;
    
    private Cell()
    {

    }
    
    public void increasePotential()
    {
        this.potential++;
    }
    
    public void resetPotential()
    {
        this.potential = 0;
    }
    
    public int getPotential()
    {
        return this.potential;
    }
    
    static public Cell EmptyCell()
    {
        Cell c = new Cell();
        c.setValue(Constants.cell_empty);
        c.setPacket(null);
        return c;
    }
    
    static public Cell AgentCell()
    {
        Cell c = new Cell();
        c.setValue(Constants.cell_agent);
        c.setPacket(null);
        return c;
    }
    
    static public Cell NewCellWithPacket(Packet packet)
    {
        Cell c = new Cell();
        c.setValue(Constants.cell_filled);
        c.setPacket(packet);
        return c;
    }
    
    static public Cell NewCellWithObstacle()
    {
        Cell c = new Cell();
        c.setValue(Constants.cell_obstacle);
        return c;
    }
    
    private void setValue(int value)
    {
        this.value = value;
    }
    
    private void setPacket(Packet packet)
    {
        this.packet = packet;
    }
    
    public int getValue()
    {
        return this.value;
    }
    
    public Packet getPacket()
    {
        return this.packet;
    }
    
}
