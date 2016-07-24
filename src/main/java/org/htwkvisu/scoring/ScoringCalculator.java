package org.htwkvisu.scoring;

import javafx.geometry.Point2D;

import java.util.*;

/**
 * ScoringCalculator calculates a scoring values of a given point.
 * Every category can get a specific weight value for the relative importance in the score value.
 */
public class ScoringCalculator {

    public static final String CATEGORY_HEALTH = "Health";
    public static final String CATEGORY_INFRASTRUCTURE = "Infrastructure";
    public static final String CATEGORY_EDUCATION = "Education";

    private Set<String> categories = new HashSet<>();
    private Map<String, Double> weights = new HashMap<>();
    private List<IScorable> pois = new LinkedList<>();

    public ScoringCalculator() {

    }

    public void addPOI(IScorable poi) {
        pois.add(poi);
    }

    public void addCategory(String cat, double weight) {
        categories.add(cat);
        weights.put(cat, weight);
    }

    public void setCategoryWeight(String cat, double weight) {
        if (categories.contains(cat) && weights.containsKey(cat)) {
            weights.put(cat, weight);
        } else {
            throw new IllegalArgumentException("Category not known!");
        }
    }

    public double getCategoryWeight(String cat) {
        if (!weights.containsKey(cat)) {
            throw new IllegalArgumentException("Weight not known!");
        }

        return weights.get(cat);
    }

    public void removeCategory(String cat) {
        if (!categories.contains(cat) || !weights.containsKey(cat)) {
            throw new IllegalArgumentException("Category not known!");
        }

        categories.remove(cat);
        weights.remove(cat);
    }

    public void resetCategories() {
        categories.clear();
        weights.clear();
    }

    public List<String> getCategories() {
        return new LinkedList<>(categories);
    }

    public List<IScorable> getPOIs() {
        return pois;
    }

    public HashMap<String, Double> calculateValue(Point2D pt) {
        HashMap<String, Double> values = new HashMap<>();

        // calculate total weights sum
        double totalWeightSum = weights.values().parallelStream().reduce((a, b) -> a + b).get();

        // calculate category scores
        categories.parallelStream().forEach(catStr -> {
            double score = calculateCategoryValue(catStr, pt) * weights.get(catStr) / totalWeightSum;
            values.put(catStr, score);
        });

        return values;
    }

    private double calculateCategoryValue(String catStr, Point2D pt) {

        // filter unused points and sum calculated values to category value
        double result = pois.parallelStream().filter(poi -> {
            IFallOf fallOf = poi.getCategoryFallOfs().get(catStr);
            return (pt.distance(poi.getCoordinates())) < fallOf.getRadius();
        }).mapToDouble(poi -> poi.getCategoryFallOfs().get(catStr).getValue(pt, poi.getCoordinates())).sum();

        return result;
    }
}
