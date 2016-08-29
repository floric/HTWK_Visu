package org.htwkvisu.model;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import org.htwkvisu.org.pois.Category;
import org.htwkvisu.org.pois.ScoreType;
import org.htwkvisu.scoring.IFallOf;

import java.util.logging.Logger;

public class ScoreTableModel {

    private final SimpleStringProperty category;
    private final SimpleStringProperty typeProp;
    private SimpleObjectProperty<IFallOf> fallOf;
    private final ScoreType type;
    private SimpleDoubleProperty paramOne;
    private SimpleDoubleProperty paramTwo;
    private SimpleDoubleProperty paramThree;
    private SimpleDoubleProperty weight;
    private SimpleBooleanProperty enabled;

    public ScoreTableModel(Category category, ScoreType type) {
        this.enabled = new SimpleBooleanProperty(type.isEnabled());
        this.category = new SimpleStringProperty(category.toString());
        this.typeProp = new SimpleStringProperty(type.toString());
        this.fallOf = new SimpleObjectProperty<>(type.getFallOf());
        this.type = type;
        this.paramOne = new SimpleDoubleProperty(fallOf.getValue().getRadius());
        this.paramTwo = new SimpleDoubleProperty(fallOf.getValue().getMaximumValue());
        this.paramThree = new SimpleDoubleProperty(fallOf.getValue().getExp());
        this.weight = new SimpleDoubleProperty(type.getWeight());
    }

    public void onClick() {
        enabledProperty().addListener((observable, oldValue, newValue) -> {
            type.setEnabled(newValue);
            Logger.getGlobal().info("Set enabled of type:" + type.name() + ", value: " + newValue);
        });
    }

    public IFallOf switchAndGetFallOf() {
        IFallOf nextFallOf = fallOf.getValue().switchToNext();
        type.setFallOf(nextFallOf);
        return nextFallOf;
    }

    public void onEnterCommit() {
        paramOneProperty().addListener((observable, oldValue, newValue) ->
                type.getFallOf().setRadius(newValue.doubleValue()));

        paramTwoProperty().addListener((observable, oldValue, newValue) ->
                type.getFallOf().setMaxVal(newValue.doubleValue()));

        paramThreeProperty().addListener((observable, oldValue, newValue) ->
                type.getFallOf().setExp(newValue.doubleValue()));

        weightProperty().addListener((observable, oldValue, newValue) ->
                type.setWeight(newValue.doubleValue()));
    }

    public boolean getEnabled() {
        return enabled.get();
    }

    public SimpleBooleanProperty enabledProperty() {
        return enabled;
    }

    public String getCategory() {
        return category.get();
    }

    public SimpleStringProperty categoryProperty() {
        return category;
    }

    public void setCategory(String category) {
        this.category.set(category);
    }

    public String getTypeProp() {
        return typeProp.get();
    }

    public SimpleStringProperty typePropProperty() {
        return typeProp;
    }

    public void setTypeProp(String typeProp) {
        this.typeProp.set(typeProp);
    }

    public IFallOf getFallOf() {
        return fallOf.get();
    }

    public SimpleObjectProperty<IFallOf> fallOfProperty() {
        return fallOf;
    }

    public void setFallOf(IFallOf fallOf) {
        this.fallOf.set(fallOf);
    }

    public ScoreType getType() {
        return type;
    }

    public double getParamOne() {
        return paramOne.get();
    }

    public SimpleDoubleProperty paramOneProperty() {
        return paramOne;
    }

    public void setParamOne(double paramOne) {
        this.paramOne.set(paramOne);
    }

    public double getParamTwo() {
        return paramTwo.get();
    }

    public SimpleDoubleProperty paramTwoProperty() {
        return paramTwo;
    }

    public void setParamTwo(double paramTwo) {
        this.paramTwo.set(paramTwo);
    }

    public double getParamThree() {
        return paramThree.get();
    }

    public SimpleDoubleProperty paramThreeProperty() {
        return paramThree;
    }

    public void setParamThree(double paramThree) {
        this.paramThree.set(paramThree);
    }

    public double getWeight() {
        return weight.get();
    }

    public SimpleDoubleProperty weightProperty() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight.set(weight);
    }

    public void setEnabled(boolean enabled) {
        this.enabled.set(enabled);
    }
}
