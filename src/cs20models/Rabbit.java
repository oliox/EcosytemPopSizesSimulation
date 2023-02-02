/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cs20models;

import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

/**
 *
 * @author 5kullduggery
 */
public class Rabbit {

    public Tile tileOn;
    public JLabel label;
    public ImageIcon rabbitPic;// = new ImageIcon ("/media/VirtualHome/student/Downloads/EcosystemSimulationPrototype/pictures/rabbit.png");
    public int consumptionRate;
    public int reproductionRate;
    public int timeSinceReproduction;
    public GridWorldModel aGridModel;
    public boolean madeThisTurn;
    public int greenLevel;
    public BufferedImage img;
    public File imgFile;
    public static int i = 0;

    public Rabbit(Tile tileOn, GridWorldModel aGridModel, int consumptionRate, int greenLevel) {
        this.tileOn = tileOn;
        this.consumptionRate = consumptionRate;//23;
        this.reproductionRate = 1;
        this.timeSinceReproduction = 1;
        this.aGridModel = aGridModel;
        this.madeThisTurn = true;
        if (greenLevel >= 100) {
            this.greenLevel = 100;
        } else {
            this.greenLevel = greenLevel;
        }
        
        
        InputStream inStr = getClass().getResourceAsStream("rabbit.png");
        try {
            this.img =  ImageIO.read(inStr);
        } catch (IOException ex) {
            System.out.print(ex);
        }

        if (this.aGridModel.evolutionOn == true) {
            int width = img.getWidth();
            int height = img.getHeight();
            Color color;
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    color = new Color(img.getRGB(x, y));
                    //System.out.println(img.getRGB(x, y));
                    if (color.getRed() == 0 && color.getBlue() == 0) {
                        if (this.greenLevel >= 0) {
                            color = new Color(0, (int) (this.greenLevel * 2.55), 0);
                            img.setRGB(x, y, color.getRGB());
                        } else {
                            color = new Color(0, 1, 0);
                            img.setRGB(x, y, color.getRGB());
                        }
                    }
                }
            }
        }

        this.rabbitPic = new ImageIcon(this.img);
    }

    public void die() {
        this.getTileOn().label.setVisible(false);
        this.getTileOn().setRabbitOn(null);
        //this.aGridModel.rabbitsList.remove(this);//.remove(aGridModel.rabbitsList.get(this.indx));
    }

    public Tile getTileOn() {
        return this.tileOn;
    }

    public void setTileOn(Tile t) {
        this.tileOn = t;
    }

    public void rabbitSimStep() {
        //eat, reproduce, die

        if (this.getTileOn().getGrassVal() > this.consumptionRate) {
            this.getTileOn().setGrassVal(this.getTileOn().getGrassVal() - this.consumptionRate);

            if (this.timeSinceReproduction == this.reproductionRate) {
                this.reproduction();
            } else {
                this.timeSinceReproduction += 1;
            }
        } else {
            this.die();

        }
    }

    public void reproduction() {
        this.timeSinceReproduction = 0;
        int spaceNum = (int) (Math.random() * 8 + 1);
        int spacesTried = 0;

        int xDest = 0;
        int yDest = 0;
        while (spacesTried <= 8) {
            switch (spaceNum) {
                case 1:
                    xDest = this.getTileOn().x - 1;
                    yDest = this.getTileOn().y - 1;
                    break;
                case 2:
                    xDest = this.getTileOn().x;
                    yDest = this.getTileOn().y - 1;
                    break;
                case 3:
                    xDest = this.getTileOn().x + 1;
                    yDest = this.getTileOn().y - 1;
                    break;
                case 4:
                    xDest = this.getTileOn().x - 1;
                    yDest = this.getTileOn().y;
                    break;
                case 5:
                    xDest = this.getTileOn().x + 1;
                    yDest = this.getTileOn().y;
                    break;
                case 6:
                    xDest = this.getTileOn().x - 1;
                    yDest = this.getTileOn().y + 1;
                    break;
                case 7:
                    xDest = this.getTileOn().x;
                    yDest = this.getTileOn().y + 1;
                    break;
                case 8:
                    xDest = this.getTileOn().x + 1;
                    yDest = this.getTileOn().y + 1;
                    break;
            }
            spacesTried++;
            if (xDest >= 0 && xDest < this.aGridModel.map.length && yDest >= 0 && yDest < this.aGridModel.map[0].length && this.aGridModel.getTile(xDest, yDest).rabbitOn == null && this.aGridModel.getTile(xDest, yDest).grassVal >= 30) {
                if (this.aGridModel.evolutionOn) {
                    this.aGridModel.makeRabbit(this.aGridModel.getTile(xDest, yDest), this.greenLevel + ((int) (Math.random() * 3) - 1) * 2);// or 4
                } else {
                    this.aGridModel.makeRabbit(this.aGridModel.getTile(xDest, yDest), 0);
                }
                this.timeSinceReproduction = 0;
                break;
            } else {
                spaceNum++;
                if (spaceNum > 8) {
                    spaceNum = 1;
                }
            }
        }
    }
}
