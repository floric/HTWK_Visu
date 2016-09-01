package org.htwkvisu.org.pois;

import javafx.geometry.Point2D;
import org.htwkvisu.domain.ScoreValue;
import org.htwkvisu.gui.MapCanvas;
import org.htwkvisu.model.ScoreTableModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public final class ScoringCalculator {

    public static List<ScoreValue> findAll() {
        return Arrays.stream(Category.values())
                .flatMap(category -> category.getTypes().stream()).filter(ScoreType::isEnabled)
                .flatMap(t -> t.findAll().stream())
                .collect(Collectors.toList());
    }

    public static List<BasicPOI> generateEnabled() {
        return Arrays.stream(Category.values())
                .flatMap(category -> category.getTypes().stream()).filter(ScoreType::isEnabled)
                .flatMap(t -> t.generateDrawable().stream())
                .collect(Collectors.toList());
    }

    public static List<ScoreTableModel> calcAllTableModels() {
        List<ScoreTableModel> tableModels = new ArrayList<>();
        for (Category category : Category.values()) {
            tableModels.addAll(category.getTypes().stream().map(t -> new ScoreTableModel(category, t))
                    .collect(Collectors.toList()));
        }
        tableModels.forEach(ScoreTableModel::onClick);
        tableModels.forEach(ScoreTableModel::onEnterCommit);
        return tableModels;
    }

    public static List<Boolean> enabledList() {
        return Arrays.stream(Category.values()).flatMap(category -> category.getTypes().stream())
                .map(ScoreType::isEnabled).collect(Collectors.toList());
    }

    public static double calculateScoreValue(Point2D pt) {
        return Arrays.stream(Category.values()).mapToDouble(t -> t.calculateScoreValue(pt)).sum();
    }

    public static double calculateScoreValueForCustom(Point2D pt) {
        return Arrays.stream(Category.values()).mapToDouble(t -> t.calculateScoreValueForCustom(pt)).sum();
    }

    public static double calculateEnabledScoreValue(Point2D pt) {
        return Arrays.stream(Category.values())
                .flatMap(category -> category.getTypes().stream()).filter(ScoreType::isEnabled)
                .mapToDouble(t -> t.calculateScoreValue(pt)).sum();
    }

    public static double calculateCustomEnabledScoreValue(Point2D pt) {
        return Arrays.stream(Category.values())
                .flatMap(category -> category.getTypes().stream()).filter(ScoreType::isEnabled)
                .mapToDouble(t -> t.calculateScoreValueForCustom(pt)).sum();
    }

    public static void updateMaxScoreValue(MapCanvas canvas) {
        Arrays.stream(Category.values()).forEach(c -> c.updateMaxScoreValue(canvas));
    }
}
