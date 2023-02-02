package cs20models;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;


/*Programmer: Connor Mills
Date created: Oct 23, 2018
School: Ernest Manning Highschool, Calgary, AB*/
public class Wolf {

    public Tile tileOn;
    public ImageIcon wolfPic;// = new ImageIcon ("/media/VirtualHome/student/Downloads/EcosystemSimulationPrototype/pictures/rabbit.png");
    public int eatingRatio;
    public int reproductionRate;
    public int timeSinceReproduction;
    public int timeSinceEating;
    public int rabbitsRequired;
    public int range;
    public GridWorldModel aGridModel;
    public boolean madeThisTurn;
    public int energy;
    public int energyLossRate;

    public Wolf(Tile tileOn, GridWorldModel aGridModel) {
        this.tileOn = tileOn;
        URL url = getClass().getResource("wolf.png");

        this.wolfPic = new ImageIcon(url);
        this.eatingRatio = 3;
        this.reproductionRate = 1;
        this.timeSinceReproduction = 0;
        this.timeSinceEating = 0;
        this.rabbitsRequired = 1;
        this.range = 2;
        this.aGridModel = aGridModel;
        this.madeThisTurn = true;
        this.energy = 7;
        this.energyLossRate = 4;
    }

    public void die() {
        this.getTileOn().label.setVisible(false);
        this.getTileOn().setWolfOn(null);
        //this.aGridModel.wolvesList.remove(this);
    }

    public Tile getTileOn() {
        return this.tileOn;
    }

    public void setTileOn(Tile t) {
        this.tileOn = t;
    }

    //using aGridWorld as an argument means that I cannot edit it from within the code, extends makes it unable to run (too much work?)
    //passing as argument seems best option, however i then need to make a function within grid model that changes variable
    
    
    public void wolfSimStep() {

        //int amountEaten = 0;
        int xPos = this.tileOn.x;
        int yPos = this.tileOn.y;

        for (int x = xPos - this.range; x < xPos + this.range; x++) { //eat certain amount of rabbits in the 4x4 area
            for (int y = yPos - this.range; y < yPos + this.range; y++) {
                if (y < 20 && y >= 0 && x < 20 && x >= 0 && this.aGridModel.map[x][y].rabbitOn != null) {
                    if (this.timeSinceEating >= this.eatingRatio) {
                        if (Math.random() * 100 >= this.aGridModel.map[x][y].rabbitOn.greenLevel) {
                            this.aGridModel.map[x][y].rabbitOn.die();
                            this.energy++;
                        }
                    } else {
                        this.timeSinceEating++;
                    }

                }
            }
        }
        
        this.energy -= energyLossRate;

        if (this.energy <= 0) {//check if got enough food
            this.die();
        } else {
            this.directionalReproduction(xPos, yPos);
        }
    }

    public void reproduction(int xPos, int yPos, int direction) {
        if (this.timeSinceReproduction >= this.reproductionRate) {
            //make new wolf
            //direction = (int) (Math.random() * 4) + 1;
            int tries = 0;
            int xDest = 0;
            int yDest = 0;
            while (tries < 4) {
                switch (direction) {
                    case 0:
                        xDest = xPos - 2;
                        yDest = yPos - 2;
                        break;
                    case 1:
                        xDest = xPos - 2;
                        yDest = yPos + 2;
                        break;
                    case 2:
                        xDest = xPos + 2;
                        yDest = yPos - 2;
                        break;
                    case 3:
                        xDest = xPos + 2;
                        yDest = yPos + 2;
                        break;
                }
                if (xDest >= 0 && xDest < 20 && yDest >= 0 && yDest < 20 && this.aGridModel.map[xDest][yDest].wolfOn == null) {
                    this.aGridModel.makeWolf(this.aGridModel.map[xDest][yDest]);
                    this.timeSinceReproduction = 0;
                    break;
                } else {
                    tries++;
                    if (direction > 4) {
                        direction = 1;
                    } else {
                        direction++;
                    }
                }
            }
            this.timeSinceReproduction = 0;
        } else {
            this.timeSinceReproduction++;
        }
    }

    public void directionalReproduction(int xPos, int yPos) {
        int[] rabbitsAtLoc = new int[4];
        int indx = 0;
        for (int xDest = xPos - 2; xDest <= xPos + 2; xDest += 4) {
            for (int yDest = yPos - 2; yDest <= yPos + 2; yDest += 4) {
                for (int x = xDest - 3; x < xDest + 3; x++) {
                    for (int y = yDest - 3; y < yDest + 3; y++) {
                        if (x >= 0 && x < 20 && y >= 0 && y < 20 && this.aGridModel.map[x][y].getRabbitOn() != null) {
                            rabbitsAtLoc[indx]++;
                        }
                    }
                }
                indx++;
            }
        }
        int biggestLoc = 0;
        int biggestNum = 0;
        for (int i = 0; i < rabbitsAtLoc.length; i++) {
            if (rabbitsAtLoc[i] > biggestNum) {
                biggestLoc = i;
                biggestNum = rabbitsAtLoc[i];
            }
        }
        this.reproduction(xPos, yPos, biggestLoc);
    }

}
