/*
 * The controller classes (like the ViewUserActions class) provides actions
 * that the user can trigger through the view classes.  Those actions are 
 * written in this class as private inner classes (i.e. classes 
 * declared inside another class).
 *
 * You can use all the public instance variables you defined in AllModelsForView, 
 * DrawnView, and ViewOutputs as though they were part of this class! This is 
 * due to the magic of subclassing (i.e. using the extends keyword).
 */
package cs20viewcontroller;

import cs20models.SimulationRunnerModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * ViewUserActions is a class that contains actions users can trigger.
 *
 * User actions are written as private inner classes that implements the
 * ActionListener interface. These actions must be "wired" into the DrawnView in
 * the ViewUserActions constructor, or else they won't be triggered by the user.
 *
 * Actions that the user can trigger are meant to manipulate some model classes
 * by sending messages to them to tell them to update their data members.
 *
 * Actions that the user can trigger can also be used to manipulate the GUI by
 * sending messages to the view classes (e.g. the DrawnView class) to tell them
 * to update themselves (e.g. to redraw themselves on the screen).
 *
 * @author cheng
 */
public class ViewUserActions extends ViewOutputs {

    /*
     * Step 1 of 2 for writing user actions.
     * -------------------------------------
     *
     * User actions are written as private inner classes that implement
     * ActionListener, and override the actionPerformed method.
     *
     * Use the following as a template for writting more user actions.
     */
    private class PlayStopBtn implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent ae) {
            if (playStopBtn.isSelected()) {
                theRunner = new SimulationRunnerModel(getContentPane());
                theRunner.mapSize = getContentPane().getWidth() - 150;
                theThreadedTimer = new Thread(theRunner);
                theThreadedTimer.start();
                playStopBtn.setText("Stop");
                pauseUnpause.setVisible(true);
                speedSlider.setVisible(true);
                rabbitKillBtn.setVisible(true);
                wolvesKillBtn.setVisible(true);
                grassKillBtn.setVisible(true);
                wolvesCheck.setVisible(true);
                speedLabel.setVisible(true);
                if (wolvesCheck.isSelected()) {
                    evolutionCheckBox.setVisible(true);
                }

            } else {
                theRunner.StopRunning = true;
                playStopBtn.setText("Play");
                pauseUnpause.setVisible(false);
                speedSlider.setVisible(false);
                rabbitKillBtn.setVisible(false);
                wolvesKillBtn.setVisible(false);
                grassKillBtn.setVisible(false);
                wolvesCheck.setVisible(false);
                evolutionCheckBox.setVisible(false);
                speedLabel.setVisible(false);
                evolutionCheckBox.setSelected(false);
                wolvesCheck.setSelected(true);
            }
        }
    }

    private class PauseButton implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent ae) {
            if (pauseUnpause.isSelected()) {
                theRunner.paused = true;
                pauseUnpause.setText("Unpause");
            } else {
                theRunner.paused = false;
                pauseUnpause.setText("Pause");
            }
        }
    }

    private class SpeedSlider implements ChangeListener {

        @Override
        public void stateChanged(ChangeEvent ae) {
            theRunner.runSpeed = 10000/speedSlider.getValue()+1;
        }
    }

    private class WolvesCheck implements ActionListener {

        // wolf reproduction movement worng NE - SW (reversed maybe?)
        // need to figure out switching, weird stuff with using method instead of direct access
        @Override
        public void actionPerformed(ActionEvent ae) {
            //theRunner.aGridModel.wolvesOn = wolvesCheck.isSelected(); // aGridModel.setWolvesOn(wolvesCheck.isSelected());
            theRunner.aGridModel.setWolvesOn(wolvesCheck.isSelected());
            if (!wolvesCheck.isSelected()) {
                evolutionCheckBox.setVisible(false);
                evolutionCheckBox.setSelected(false);
                for (int x = 0; x < aGridModel.map.length; x++) {
                    for (int y = 0; y < aGridModel.map[0].length; y++) {
                        if (theRunner.aGridModel.map[x][y].getWolfOn() != null) {
                            theRunner.aGridModel.map[x][y].getWolfOn().die();
                        }
                    }
                }
            } else {
                evolutionCheckBox.setVisible(true);
            }
        }
    }

    private class RabbitKill implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent ae) {
            theRunner.aGridModel.rabbitDisease();
        }
    }

    private class WolvesKill implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent ae) {
            theRunner.aGridModel.wolvesDisease();
        }
    }

    private class GrassKill implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent ae) {
            theRunner.aGridModel.grassDisease();
        }
    }

    private class EvolutionCheck implements ActionListener {

        // wolf reproduction movement worng NE - SW (reversed maybe?)
        // need to figure out switching, weird stuff with using method instead of direct access
        @Override
        public void actionPerformed(ActionEvent ae) {
            theRunner.aGridModel.setEvolutionOn(evolutionCheckBox.isSelected());
            theRunner.evolutionChartPanel.setVisible(evolutionCheckBox.isSelected());
            theRunner.rabbitGreenessSeries.clear();
            theRunner.averageRabbitGreeness = 0;
        }
    }

    /*
     * ViewUserActions constructor used for wiring user actions to GUI elements
     */
    public ViewUserActions() {
        /*
         * Step 2 of 2 for writing user actions.
         * -------------------------------------
         *
         * Once a private inner class has been written for a user action, you
         * can wire it to a GUI element (i.e. one of GUI elements you drew in
         * the DrawnView class and which you gave a memorable public variable
         * name!).
         *
         * Use the following as a template for wiring more user actions.
         */
        pauseUnpause.setVisible(false);
        speedSlider.setVisible(false);
        rabbitKillBtn.setVisible(false);
        wolvesKillBtn.setVisible(false);
        grassKillBtn.setVisible(false);
        wolvesCheck.setVisible(false);
        evolutionCheckBox.setVisible(false);
        speedLabel.setVisible(false);
        playStopBtn.addActionListener(new PlayStopBtn());
        pauseUnpause.addActionListener(new PauseButton());
        speedSlider.addChangeListener(new SpeedSlider());
        wolvesCheck.addActionListener(new WolvesCheck());
        rabbitKillBtn.addActionListener(new RabbitKill());
        wolvesKillBtn.addActionListener(new WolvesKill());
        grassKillBtn.addActionListener(new GrassKill());
        evolutionCheckBox.addActionListener(new EvolutionCheck());
    }
}
