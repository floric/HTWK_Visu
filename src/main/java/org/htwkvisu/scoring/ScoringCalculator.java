package org.htwkvisu.scoring;

import javafx.geometry.Point2D;

import java.util.HashMap;
import java.util.LinkedList;

/**
 * WIP
 */
public class ScoringCalculator {

    private LinkedList<String> categories = new LinkedList<>();

    public ScoringCalculator() {

    }

    public void addDataValue(IScorable val) {

    }

    public void addCategory(String cat) {
        categories.add(cat);
    }

    public HashMap<String, Double> calculateValue(Point2D pt) {
        HashMap<String, Double> values = new HashMap<>();

        return values;
    }
}
