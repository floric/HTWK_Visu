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

    /**
     * Add new POI (IScorable).
     *
     * @param poi POI
     */
    public void addPOI(IScorable poi) {
        pois.add(poi);
    }

    /**
     * Add category.
     *
     * @param cat    Category
     * @param weight Weight
     */
    public void addCategory(String cat, double weight) {
        categories.add(cat);
        weights.put(cat, weight);
    }

    /**
     * Set weight of specific category.
     *
     * @param cat Category
     * @param weight Weight value
     */
    public void setCategoryWeight(String cat, double weight) {
        if (categories.contains(cat) && weights.containsKey(cat)) {
            weights.put(cat, weight);
        } else {
            throw new IllegalArgumentException("Category not known!");
        }
    }

    /**
     * Get weight of specific category.
     *
     * @param cat Category
     * @return Weight value
     */
    public double getCategoryWeight(String cat) {
        if (!weights.containsKey(cat)) {
            throw new IllegalArgumentException("Weight not known!");
        }

        return weights.get(cat);
    }

    /**
     * Remove specific category.
     *
     * @param cat Category
     */
    public void removeCategory(String cat) {
        if (!categories.contains(cat) || !weights.containsKey(cat)) {
            throw new IllegalArgumentException("Category not known!");
        }

        categories.remove(cat);
        weights.remove(cat);
    }

    /**
     * Removes all categories
     */
    public void resetCategories() {
        categories.clear();
        weights.clear();
    }

    /**
     * Get all categories as list.
     *
     * @return List of category identifiers
     */
    public List<String> getCategories() {
        return new LinkedList<>(categories);
    }

    /**
     * Get all POIs as list.
     *
     * @return List of POIs
     */
    public List<IScorable> getPOIs() {
        return pois;
    }


    /**
     * Calculate scores for all added categories.
     *
     * @param pt Point
     * @return Score values in map with categories as keys.
     */
    public Map<String, Double> calculateValue(Point2D pt) {
        Map<String, Double> values = new HashMap<>();

        // calculate total weights sum
        double totalWeightSum = weights.values().parallelStream().reduce((a, b) -> a + b).orElse(0.0);

        // calculate category scores
        categories.parallelStream().forEach(catStr -> {
            double score = calculateCategoryValue(catStr, pt) * weights.get(catStr) / totalWeightSum;
            if (score < 0) {
                score = 0;
            }

            values.put(catStr, score);
        });

        return values;
    }


    /**
     * Calculate total score from values.
     * The scores of the categories are added in a logarithmic scale.
     *
     * @param values Values from categories
     * @return Total Score of point
     */
    public double calculateScoreFromCategorys(Map<String, Double> values) {
        // use logarithmic for categories to soften high differences between categories
        // the total score is the sum of each used category
        return values.values().parallelStream().mapToDouble(Math::log).sum();
    }

    /**
     * Calculate score for specific category.
     *
     * @param catStr Category identifier
     * @param pt Point
     * @return Score value
     */
    private double calculateCategoryValue(String catStr, Point2D pt) {

        // filter unused points and sum calculated values to category value
        return pois.parallelStream().filter(poi -> {
            IFallOf fallOf = poi.getCategoryFallOfs().get(catStr);
            return (pt.distance(poi.getCoordinates())) < fallOf.getRadius();
        }).mapToDouble(poi -> poi.getCategoryFallOfs().get(catStr).getValue(pt, poi.getCoordinates())).sum();
    }
}
