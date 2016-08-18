package org.htwkvisu.org.pois;

import javafx.geometry.Point2D;
import org.htwkvisu.domain.ScoreValue;
import org.htwkvisu.domain.ScoreValueDAO;
import org.htwkvisu.scoring.ConstantFallOf;
import org.htwkvisu.scoring.ExponentialFallOf;
import org.htwkvisu.scoring.IFallOf;
import org.htwkvisu.scoring.LinearFallOf;
import org.htwkvisu.utils.MathUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.htwkvisu.domain.NamedQueryConstants.*;

public enum ScoreType implements IScorable{
    TERMINAL(FIND_TERMINAL, new ExponentialFallOf(MathUtils.convertKilometresToUnits(100), 100, 3))
    , HELIPAD(FIND_HELIPAD, new ExponentialFallOf(MathUtils.convertKilometresToUnits(100), 100, 3))
    , AERODROME(FIND_AERODROME, new ExponentialFallOf(MathUtils.convertKilometresToUnits(100), 100, 3))
    , BUS(FIND_BUS, new LinearFallOf(MathUtils.convertKilometresToUnits(50), 50))
    , TRAIN(FIND_TRAIN, new LinearFallOf(MathUtils.convertKilometresToUnits(100), 100))
    , TRAM(FIND_TRAM, new ConstantFallOf(MathUtils.convertKilometresToUnits(50), 50))
    , HOSPITAL(FIND_HOSPITAL, new ExponentialFallOf(MathUtils.convertKilometresToUnits(50), 50, 3))
    , PHARMACY(FIND_PHARMACY, new LinearFallOf(MathUtils.convertKilometresToUnits(50), 50))
    , DENTIST(FIND_DENTIST, new LinearFallOf(MathUtils.convertKilometresToUnits(70), 70))
    , VETERINARY(FIND_VETERINARY, new LinearFallOf(MathUtils.convertKilometresToUnits(70), 70))
    , DOCTORS(FIND_DOCTORS, new LinearFallOf(MathUtils.convertKilometresToUnits(70), 70))
    , BLOOD_DONATION(FIND_BLOOD_DONATION, new LinearFallOf(MathUtils.convertKilometresToUnits(100), 100))
    , SCHOOL(FIND_SCHOOL, new ConstantFallOf(MathUtils.convertKilometresToUnits(100), 100))
    , COLLEGE(FIND_COLLEGE, new LinearFallOf(MathUtils.convertKilometresToUnits(100), 100))
    , LIBRARY(FIND_LIBRARY, new LinearFallOf(MathUtils.convertKilometresToUnits(100), 100))
    , MUSEUM(FIND_MUSEUM, new LinearFallOf(MathUtils.convertKilometresToUnits(50), 50))
    , RESEARCH_INSTITUTION(FIND_RESEARCH_INSTITUTION, new ExponentialFallOf(MathUtils.convertKilometresToUnits(100), 100, 3))
    , THEATRE(FIND_THEATRE, new LinearFallOf(MathUtils.convertKilometresToUnits(50), 50));

    private static final double NEUTRAL_WEIGHT = 1.0;
    private final String namedQuery;
    private IFallOf fallOf;
    private List<BasicPOI> customPOIs = new ArrayList<>();
    private double weight = NEUTRAL_WEIGHT;
    private boolean enabled = false;
    private List<BasicPOI> drawable = new ArrayList<>();

    ScoreType(String namedQuery, IFallOf fallOf) {
        this.namedQuery = namedQuery;
        this.fallOf = fallOf;
    }

    public List<ScoreValue> findAll() {
        return ScoreValueDAO.exeNamedQuery(namedQuery);
    }

    public List<BasicPOI> generateDrawable() {
        if(drawable.isEmpty()){
            drawable.addAll(findAll().stream().map(scoreValue ->
                    new BasicPOI(this, new Point2D(scoreValue.getPoint().getX(), scoreValue.getPoint().getY())))
                    .collect(Collectors.toList()));
        }
        return drawable;
    }

    @Override
    public double calculateScoreValue(Point2D pt) {
        return calScoreValueForInput(pt,this.generateDrawable());
    }

    @Override
    public double calculateScoreValueForCustom(Point2D pt) {
        return calScoreValueForInput(pt, this.getCustomPOIs());
    }

    private double calScoreValueForInput(Point2D pt, List<BasicPOI> inputPOIs) {
        double totalWeightSum = calcTotalWeight();
        double weightQuotient = weight / totalWeightSum;
        return inputPOIs.parallelStream()
                .filter(poi -> (pt.distance(poi.getCoordinates())) < fallOf.getRadius())
                .mapToDouble(poi -> fallOf.getValue(pt, poi.getCoordinates())).sum() * weightQuotient;
    }

    /**
     * Ignores the weight if neutral element is set for all
     * @return totalweight - the weight of all scoretypes in all categories
     */
    private double calcTotalWeight() {
        List<Double> weights = allScoreTypes().parallelStream().map(ScoreType::getWeight).collect(Collectors.toList());
        return weights.parallelStream().allMatch(x -> x == NEUTRAL_WEIGHT)
                ? NEUTRAL_WEIGHT
                : weights.stream().reduce((a, b) -> a + b).orElse(NEUTRAL_WEIGHT);
    }

    public List<ScoreType> allScoreTypes() {
        return Stream.of(TERMINAL, HELIPAD, AERODROME, BUS, TRAIN, TRAM, HOSPITAL, PHARMACY, DENTIST, VETERINARY, DOCTORS
                , BLOOD_DONATION, SCHOOL, COLLEGE, LIBRARY, MUSEUM, RESEARCH_INSTITUTION, THEATRE)
                .collect(Collectors.toList());
    }

    public String getNamedQuery() {
        return namedQuery;
    }

    public IFallOf getFallOf() {
        return fallOf;
    }

    public void setFallOf(IFallOf fallOf) {
        this.fallOf = fallOf;
    }

    public List<BasicPOI> getCustomPOIs() {
        Objects.requireNonNull(customPOIs);
        return customPOIs;
    }

    public void setCustomPOIs(Point2D... customPoints) {
        Objects.requireNonNull(customPoints);
        this.customPOIs = Arrays.asList(customPoints).stream().map(p -> new BasicPOI(this, p)).collect(Collectors.toList());
    }

    public void setCustomPOIs(List<Point2D> customPoints) {
        Objects.requireNonNull(customPoints);
        this.customPOIs = customPoints.stream().map(p -> new BasicPOI(this, p)).collect(Collectors.toList());
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        if(weight == 0.0){
            throw new IllegalArgumentException("Weight shouldn't be 0.0 .");
        }
        this.weight = weight;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
