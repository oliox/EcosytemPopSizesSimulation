/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cs20models;

import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author student
 */
public class Tile {
    public int grassVal;
    public JPanel panel;
    public JLabel label;
    public Rabbit rabbitOn;
    public int x;
    public int y;
    public int size;
    public Color color;
    public int regrowthRate;
    public Wolf wolfOn;
    
    public Tile (int startingGrassVal, int x, int y) {
        this.grassVal = startingGrassVal;
        this.panel = new JPanel();
        this.label = new JLabel();
        this.x = x;
        this.y = y;
        this.color = Color.green;
        this.regrowthRate = 6;
        
    }
    
    public int getGrassVal() {
        return this.grassVal;
    }
    
    public void setGrassVal(int newVal) {
        this.grassVal = newVal;
    }
    
    public Rabbit getRabbitOn() {
        return this.rabbitOn;
    }
    
    public void setRabbitOn(Rabbit r) {
        this.rabbitOn = r;
    }
    
    public Wolf getWolfOn() {
        return this.wolfOn;
    }
    
    public void setWolfOn(Wolf w) {
        this.wolfOn = w;
    }
}
