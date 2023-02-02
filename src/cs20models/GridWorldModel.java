package cs20models;

import java.util.ArrayList;

/**
 * A class to model the problem or situation your program solves
 *
 * @author cheng
 */
public class GridWorldModel {

    //the tiles
    public int startingGrass;
    public int rabbitConsumptionRate;
    public int dimension;
    public Tile[][] map;
    public boolean wolvesOn;
    public boolean evolutionOn;

    public GridWorldModel() {
        this.rabbitConsumptionRate = 12;
        this.dimension = 20;
        this.startingGrass = 100;
        this.map = new Tile[this.dimension][this.dimension];
        this.wolvesOn = true;

        for (int x = 0; x < this.map.length; x++) {
            for (int y = 0; y < this.map[0].length; y++) {
                this.map[x][y] = new Tile(this.startingGrass, x, y);
                if (x % 3 == 0 && y % 3 == 0) {
                    this.makeRabbit(this.map[x][y], 0);
                }
                 if (x % 6 == 0 && y % 6 == 0) {
                    this.makeWolf(this.map[x][y]);
                }
            }
        }
    }

    //the rabbits
    // public ArrayList<Rabbit> rabbitsList = new ArrayList<Rabbit>();
    public void makeRabbit(Tile tileOn, int greenLevel) {
        //this.rabbitsList.add(new Rabbit(tileOn, rabbitsList.size(), this));
        tileOn.rabbitOn = new Rabbit(tileOn, this, this.rabbitConsumptionRate, greenLevel);
        //System.out.print(greenLevel + " ");
        //tileOn.setWolfOn(new Rabbit (tileOn, this));
    }

    //wolves
    public void makeWolf(Tile tileOn) {
        tileOn.setWolfOn(new Wolf(tileOn, this));
        if (tileOn.rabbitOn != null) {
            tileOn.rabbitOn.die();
        }
    }

    public Tile getTile(int x, int y) {
        return this.map[x][y];
    }

    public void setWolvesOn(boolean b) {
        this.wolvesOn = b;
        if (b) {
            this.rabbitConsumptionRate = 12;
        } else {
            this.rabbitConsumptionRate = 23;
        }
        for (int x = 0; x < this.map.length; x++) {
            for (int y = 0; y < this.map[0].length; y++) {
                if (b) {
                    this.map[x][y].regrowthRate = 6;
                    if (this.map[x][y].getRabbitOn() != null) {
                        this.map[x][y].getRabbitOn().consumptionRate = 12;
                    }
                } else {
                    this.map[x][y].regrowthRate = 4;
                    if (this.map[x][y].getRabbitOn() != null) {
                        this.map[x][y].getRabbitOn().consumptionRate = 23;
                    }
                }
            }
        }

    }

    public void rabbitDisease() {
        int alternateKill = 0;

        for (int x = 0; x < this.map.length; x++) {
            for (int y = 0; y < this.map[0].length; y++) {
                if (this.map[x][y].getRabbitOn() != null) {
                    if (alternateKill >= 4) {
                        alternateKill = 0;
                    } else {
                        alternateKill++;
                        this.map[x][y].getRabbitOn().die();
                    }
                }
            }
        }

    }

    public void wolvesDisease() {
        int alternateKill = 0;

        for (int x = 0; x < this.map.length; x++) {
            for (int y = 0; y < this.map[0].length; y++) {
                if (this.map[x][y].getWolfOn() != null) {
                    if (alternateKill >= 4) {
                        alternateKill = 0;
                    } else {
                        alternateKill++;
                        this.map[x][y].getWolfOn().die();
                    }
                }
            }
        }

    }

    public void grassDisease() {
        for (int x = 0; x < this.map.length; x++) {
            for (int y = 0; y < this.map[0].length; y++) {
                this.map[x][y].setGrassVal(this.map[x][y].getGrassVal() / 2);
            }
        }

    }
    
    public void setEvolutionOn(boolean b) {
        this.evolutionOn = b;
    }
}
