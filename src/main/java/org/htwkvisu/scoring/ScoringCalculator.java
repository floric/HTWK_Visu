package org.htwkvisu.scoring;

import javafx.geometry.Point2D;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * WIP
 */
public class ScoringCalculator {

    private List<String> categories = new LinkedList<>();
    private List<IScorable> pois = new LinkedList<>();

    public ScoringCalculator() {

    }

    public void addPOI(IScorable poi) {
        pois.add(poi);
    }

    public void addCategory(String cat) {
        categories.add(cat);
    }

    public void removeCategory(String cat) {
        categories.remove(cat);
    }

    public void resetCategories() {
        categories.clear();
    }

    public List<String> getCategories() {
        return categories;
    }

    public List<IScorable> getPois() {
        return pois;
    }

    public HashMap<String, Double> calculateValue(Point2D pt) {
        HashMap<String, Double> values = new HashMap<>();

        for (String catStr : categories) {
            double score = calculateCategoryValue(catStr, pt);
            values.put(catStr, score);
        }

        return values;
    }

    private double calculateCategoryValue(String catStr, Point2D pt) {
        double result = 0;

        for (IScorable poi : pois) {
            IFallOf fallOf = poi.getValues().get(catStr);
            double dist = pt.distance(poi.getPosition());

            // check if the point is relevant
            if (dist < fallOf.getRadius()) {
                result += fallOf.getValue(pt, poi.getPosition());
            }

        }

        return result;
    }
}
