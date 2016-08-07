package org.htwkvisu.org.pois;

import javafx.geometry.Point2D;
import org.htwkvisu.domain.ScoreValue;
import org.htwkvisu.domain.ScoreValueDAO;
import org.htwkvisu.scoring.ConstantFallOf;
import org.htwkvisu.scoring.ExponentialFallOf;
import org.htwkvisu.scoring.IFallOf;
import org.htwkvisu.scoring.LinearFallOf;
import org.htwkvisu.utils.MathUtils;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.htwkvisu.domain.NamedQueryConstants.*;

public enum ScoreType {
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

    private final String namedQuery;
    private IFallOf fallOf;

    ScoreType(String namedQuery, IFallOf fallOf) {
        this.namedQuery = namedQuery;
        this.fallOf = fallOf;
    }

    public String getNamedQuery() {
        return namedQuery;
    }

    public List<ScoreValue> findAll() {
        return ScoreValueDAO.exeNamedQuery(namedQuery);
    }

    public IFallOf getFallOf() {
        return fallOf;
    }

    public List<BasicPOI> generateDrawable() {
        return findAll().stream().map(scoreValue ->
                new BasicPOI(this, new Point2D(scoreValue.getPoint().getX(), scoreValue.getPoint().getY())))
                .collect(Collectors.toList());
    }

    public List<ScoreType> allScoreTypes(){
        return Stream.of(TERMINAL, HELIPAD, AERODROME, BUS, TRAIN, TRAM, HOSPITAL, PHARMACY, DENTIST, VETERINARY, DOCTORS
                , BLOOD_DONATION, SCHOOL, COLLEGE, LIBRARY, MUSEUM, RESEARCH_INSTITUTION, THEATRE)
                .collect(Collectors.toList());
    }

    public double calculateScoreValue(Point2D pt) {
        return generateDrawable().parallelStream()
                .filter(poi -> (pt.distance(poi.getCoordinates())) < fallOf.getRadius())
                .mapToDouble(poi -> fallOf.getValue(pt, poi.getCoordinates())).sum();
    }
}
