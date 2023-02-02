package cs20viewcontroller;

import cs20models.Tile;

/**
 * Write methods in aGridModel.rabbitsList.get(i) class for displaying data in
 * the DrawnView.
 *
 * You can use all the public instance variables you defined in AllModelsForView
 * and DrawnView as though they were part of aGridModel.rabbitsList.get(i)
 * class! This is due to the magic of subclassing (i.e. using the extends
 * keyword).
 *
 * The methods for displaying data in the DrawnView are written as methods in
 * aGridModel.rabbitsList.get(i) class.
 *
 * Make sure to use these methods in the ViewUserActions class though, or else
 * they will be defined but never used!
 *
 * @author cheng
 */
public class ViewOutputs extends DrawnView {

//    public void doSimStep() {
//        //eat, reproduce, die
//        for (int i = 0; i < aGridModel.rabbitsList.size(); i++) {
//            if (aGridModel.rabbitsList.get(i).getTileOn().getGrassVal() > aGridModel.rabbitsList.get(i).consumptionRate) {
//                aGridModel.rabbitsList.get(i).getTileOn().setGrassVal(aGridModel.rabbitsList.get(i).getTileOn().getGrassVal() - aGridModel.rabbitsList.get(i).consumptionRate);
//                if (aGridModel.rabbitsList.get(i).timeSinceReproduction == aGridModel.rabbitsList.get(i).reproductionRate) {
//                    aGridModel.rabbitsList.get(i).timeSinceReproduction = 0;
//                    Tile destTile = aGridModel.rabbitsList.get(i).tileOn;
//                    do {
//                        destTile = aGridModel.getTile(aGridModel.rabbitsList.get(i).getTileOn().x + (int) (Math.random() * 3) - 1, aGridModel.rabbitsList.get(i).getTileOn().y + (int) (Math.random() * 3) - 1);
//                    } while (destTile.rabbitOn != null);
//                    aGridModel.makeRabbit(destTile);
//                } else {
//                    aGridModel.rabbitsList.get(i).timeSinceReproduction++;
//                }
//            } else {
//                //die
//            }
//        }
//    }

}
