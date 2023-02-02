/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cs20models;

import java.awt.Color;
import java.awt.Container;
import javax.swing.BorderFactory;
import javax.swing.border.Border;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.general.Dataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

/**
 *
 * @author student
 */
public class SimulationRunnerModel extends AllModelsForView implements Runnable {

    public boolean StopRunning;
    public int mapSize;
    private Container container;
    public boolean paused;
    public XYSeries rabbitSeries;
    public XYSeries grassSeries;
    public XYSeries wolfSeries;
    public XYSeries rabbitGreenessSeries;
    public Dataset data;
    public int rabbitNum;
    public int grassNum;
    public int time;
    public int spaceNum;
    public int spacesTried;
    public int runSpeed;
    public int wolfNum;
    public ChartPanel chartPanel;
    public ChartPanel evolutionChartPanel;
    public int[] wolfSmootherArray;
    public int smootherCounter;
    public int averageRabbitGreeness;
    //public int totalDivisor;
    //public int totalGreen;

    public SimulationRunnerModel(Container container) {
        this.container = container;
        this.StopRunning = false;
        this.rabbitSeries = new XYSeries("Rabbits");
        this.grassSeries = new XYSeries("Grass");
        this.wolfSeries = new XYSeries("Wolves");
        this.rabbitSeries.setMaximumItemCount(75);
        this.grassSeries.setMaximumItemCount(75);
        this.wolfSeries.setMaximumItemCount(25);
        this.rabbitNum = 0;
        this.grassNum = 0;
        this.time = 0;
        this.spaceNum = 0;
        this.spacesTried = 0;
        this.runSpeed = 200;
        this.wolfNum = 0;
        this.smootherCounter = 0;
        this.wolfSmootherArray = new int[3];
        this.averageRabbitGreeness = 0;
        this.rabbitGreenessSeries = new XYSeries("Greeness (%)");
        //this.totalDivisor = 0;
        //this.totalGreen = 0;
    }

    @Override
    public void run() {
        this.drawChart();
        this.drawEvolutionChart();
        this.initMap();

        while (true) {
            if (this.StopRunning) {
                //this.rabbitSeries = new XYSeries("XYGraph");
                //this.grassSeries = new XYSeries("XYGraph");
                this.chartPanel.setVisible(false);
                this.evolutionChartPanel.setVisible(false);
                for (int x = 0; x < this.aGridModel.map.length; x++) {
                    for (int y = 0; y < this.aGridModel.map[0].length; y++) {
                        this.container.remove(this.aGridModel.getTile(x, y).panel);
                        this.container.repaint();
                    }
                }
                return;
            }
            if (this.paused == true) {
                try {
                    java.lang.Thread.sleep(200);
                } catch (InterruptedException ex) {
                    System.out.println(ex);
                }
            } else {
                for (int x = 0; x < this.aGridModel.map.length; x++) {
                    for (int y = 0; y < this.aGridModel.map[0].length; y++) {
                        if (this.aGridModel.map[x][y].rabbitOn != null && !this.aGridModel.map[x][y].rabbitOn.madeThisTurn) {
                            this.aGridModel.map[x][y].rabbitOn.rabbitSimStep();

                        } else if (this.aGridModel.map[x][y].wolfOn != null && !this.aGridModel.map[x][y].wolfOn.madeThisTurn) {
                            this.aGridModel.map[x][y].wolfOn.wolfSimStep();
                        }

                    }
                }
                for (int x = 0; x < this.aGridModel.map.length; x++) {
                    for (int y = 0; y < this.aGridModel.map[0].length; y++) {
                        if (this.aGridModel.map[x][y].rabbitOn != null && this.aGridModel.map[x][y].rabbitOn.madeThisTurn) {
                            this.aGridModel.map[x][y].rabbitOn.madeThisTurn = false;

                        }
                        if (this.aGridModel.map[x][y].wolfOn != null && this.aGridModel.map[x][y].wolfOn.madeThisTurn) {
                            this.aGridModel.map[x][y].wolfOn.madeThisTurn = false;
                        }
                    }
                }
                this.updateWorld(this.container);

                try {
                    java.lang.Thread.sleep(this.runSpeed);
                } catch (InterruptedException ex) {
                    System.out.println(ex);
                }
            }
        }
    }

    public void initMap() {
        int panelSize = this.mapSize / this.aGridModel.map.length;
        for (int x = 0; x < this.aGridModel.map.length; x++) {
            for (int y = 0; y < this.aGridModel.map[0].length; y++) {
                this.container.add(this.aGridModel.getTile(x, y).panel);
                this.aGridModel.getTile(x, y).panel.add(this.aGridModel.getTile(x, y).label);
                this.aGridModel.getTile(x, y).size = panelSize;
                this.aGridModel.getTile(x, y).panel.setSize(this.aGridModel.getTile(x, y).size, this.aGridModel.getTile(x, y).size);
                this.aGridModel.getTile(x, y).panel.setLocation(this.aGridModel.getTile(x, y).x * panelSize, this.aGridModel.getTile(x, y).y * panelSize);
                Border blackLine = BorderFactory.createLineBorder(Color.BLACK);
                this.aGridModel.getTile(x, y).panel.setBorder(blackLine);
            }
        }
    }

    //the drawing necessary to change the world each step
    public void updateWorld(Container cont) {
        int panelSize = this.mapSize / this.aGridModel.map.length;
        for (int x = 0; x < this.aGridModel.map.length; x++) {
            for (int y = 0; y < this.aGridModel.map[0].length; y++) {
                //colouring panels (bc it could change)
                if (this.aGridModel.getTile(x, y).grassVal <= 90) {
                    this.aGridModel.getTile(x, y).grassVal += this.aGridModel.getTile(x, y).regrowthRate;
                }
                this.aGridModel.getTile(x, y).color = new Color((int) (200 - (200 * ((double) (this.aGridModel.getTile(x, y).getGrassVal() + 0.01) / 100))), 140, 0);
                this.aGridModel.getTile(x, y).panel.setBackground(this.aGridModel.getTile(x, y).color);

                if (this.aGridModel.getTile(x, y).getRabbitOn() != null) {
                    this.rabbitNum++;

                    //this.totalGreen +=this.aGridModel.getTile(x, y).rabbitOn.greenLevel;
                    this.averageRabbitGreeness += this.aGridModel.getTile(x, y).rabbitOn.greenLevel; //remove

                    //Image image = this.aGridModel.map[x][y].getRabbitOn().rabbitPic.getImage();// transform it 
                    //Image newimg = image.getScaledInstance(panelSize, panelSize, java.awt.Image.SCALE_SMOOTH); // scale it the smooth way  
                    //this.aGridModel.getTile(x, y).getRabbitOn().rabbitPic = new ImageIcon(newimg);
                    this.aGridModel.getTile(x, y).label.setIcon(this.aGridModel.getTile(x, y).getRabbitOn().rabbitPic);
                    this.aGridModel.getTile(x, y).label.setSize(panelSize, panelSize);
                    this.aGridModel.getTile(x, y).label.setVisible(true);
                }
                if (this.aGridModel.getTile(x, y).getWolfOn() != null) {
                    this.wolfNum++;
                    //System.out.print(this.aGridModel.getTile(x, y).getWolfOn().wolfPic);
                    this.aGridModel.getTile(x, y).label.setIcon(this.aGridModel.getTile(x, y).getWolfOn().wolfPic);
                    this.aGridModel.getTile(x, y).label.setSize(panelSize, panelSize);
                    this.aGridModel.getTile(x, y).label.setVisible(true);
                }

                this.grassNum += this.aGridModel.getTile(x, y).grassVal;

                this.aGridModel.getTile(x, y).panel.setVisible(true);
            }
        }

        this.grassSeries.add(this.time, this.grassNum / 120);
        this.rabbitSeries.add(this.time, this.rabbitNum);

        if (this.smootherCounter >= 2) {
            this.smootherCounter = 0;
            int average = 0;
            for (int i = 0; i < this.wolfSmootherArray.length; i++) {
                average += this.wolfSmootherArray[i];
            }
            average = average / this.wolfSmootherArray.length;
            this.wolfSeries.add(this.time, average * 12);//45);
        } else {
            this.wolfSmootherArray[this.smootherCounter] = this.wolfNum;
            this.smootherCounter++;
        }
        if (this.aGridModel.evolutionOn) {
            if (this.rabbitNum > 0) {
                this.averageRabbitGreeness = this.averageRabbitGreeness / this.rabbitNum; //testing only
            }
            this.rabbitGreenessSeries.add(this.time, this.averageRabbitGreeness);
        }

        this.checkIfAllDead();
        this.wolfNum = 0;
        this.rabbitNum = 0;
        this.grassNum = 0;
        this.time++;
    }

    public void checkIfAllDead() {
        if (this.rabbitNum == 0) {
            this.aGridModel.makeRabbit(this.aGridModel.map[(int) (Math.random() * 20)][(int) (Math.random() * 20)], 0);
        }
        //System.out.println(this.aGridModel.wolvesOn);
        for (int i = 0; i < 2; i++) {
            if (this.wolfNum == 0 && this.aGridModel.wolvesOn) {
                int randX = (int) (Math.random() * 20);
                int randY = (int) (Math.random() * 20);
                this.aGridModel.makeWolf(this.aGridModel.map[randX][randY]);
                this.aGridModel.map[randX][randY].getWolfOn().timeSinceReproduction = 1;
            }
        }
    }

    public void drawChart() {
        // Create a simple XY chart

        // Add the series to your data set
        //XYSeriesCollection dataset = new XYSeriesCollection();
        //dataset.addSeries(this.createDataset());
        // Generate the graph
        JFreeChart chart = ChartFactory.createXYLineChart(
                "Organism Population Sizes", // Title
                "Time", // x-axis Label
                "Population Size", // y-axis Label
                this.createDataset(), // Dataset
                PlotOrientation.VERTICAL, // Plot Orientation
                true, // Show Legend
                false, // Use tooltips
                false // Configure chart to generate URLs?
        );

        this.chartPanel = new ChartPanel(chart);
        container.add(chartPanel);
        XYPlot plot = chart.getXYPlot();
        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setRange(0.0, 450.0);

        ValueAxis range = plot.getRangeAxis(); //remove if he wants name, but will show wrong numbers
        //range.setVisible(false);

        this.chartPanel.setVisible(true);
        int height = (container.getHeight() - (this.mapSize));
        this.chartPanel.setSize((int) (height * 1.75), height);
        this.chartPanel.setLocation(0, container.getHeight() - this.chartPanel.getHeight());
    }

    private XYDataset createDataset() {
        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(this.rabbitSeries);
        dataset.addSeries(this.grassSeries);
        dataset.addSeries(this.wolfSeries);
        return dataset;
    }

    public void drawEvolutionChart() {
        JFreeChart chart = ChartFactory.createXYLineChart(
                "Rabbit Colour Over Time", // Title
                "Time", // x-axis Label
                "Greeness", // y-axis Label
                this.evolutionDataset(), // Dataset
                PlotOrientation.VERTICAL, // Plot Orientation
                false, // Show Legend
                false, // Use tooltips
                false // Configure chart to generate URLs?
        );

        this.evolutionChartPanel = new ChartPanel(chart);
        container.add(evolutionChartPanel);
        XYPlot plot = chart.getXYPlot();
        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setRange(0.0, 100.0);

        this.evolutionChartPanel.setVisible(false);
        int height = (container.getHeight() - (this.mapSize));
        this.evolutionChartPanel.setSize(this.container.getWidth() - this.chartPanel.getWidth(), height);
        this.evolutionChartPanel.setLocation(this.chartPanel.getWidth(), container.getHeight() - this.evolutionChartPanel.getHeight());
    }

    private XYDataset evolutionDataset() {
        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(this.rabbitGreenessSeries);
        return dataset;
    }
}
