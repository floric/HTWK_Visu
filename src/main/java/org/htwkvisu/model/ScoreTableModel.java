package org.htwkvisu.model;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import org.htwkvisu.org.pois.Category;
import org.htwkvisu.org.pois.ScoreType;
import org.htwkvisu.scoring.ConstantFallOf;
import org.htwkvisu.scoring.ExponentialFallOf;
import org.htwkvisu.scoring.IFallOf;
import org.htwkvisu.scoring.LinearFallOf;

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

    public void onClick() {
        enabledProperty().addListener((observable, oldValue, newValue) -> {
            type.setEnabled(newValue);
            Logger.getGlobal().info("Set enabled of type:" + type.name() + ", value: " + newValue);
        });

        fallOfProperty().addListener((observable, oldValue, newValue) -> {
            //TODO Switching Functions!
            Logger.getGlobal().info("switcher");
            double r = type.getFallOf().getRadius();
            double max = type.getFallOf().getMaximumValue();
            double exp = type.getFallOf().getExp();

            if(type.getFallOf() instanceof ExponentialFallOf){
                type.setFallOf(new ConstantFallOf(r,max));
            }else if ( type.getFallOf() instanceof ConstantFallOf){
                type.setFallOf(new LinearFallOf(r,max));
            }else{
                type.setFallOf(new ExponentialFallOf(r,max,exp));
            }
        });
    }

    public void onEnterCommit() {
        paramOneProperty().addListener((observable, oldValue, newValue) -> {
            double value = newValue.doubleValue();
            if (value >= 0) {
                type.getFallOf().setRadius(value);
                Logger.getGlobal().info("Set radius of type:" + type.name() + ", value: " + newValue);
            }else{
                Logger.getGlobal().warning("Set radius of type:" + type.name() + " not set");
            }
        });

        paramTwoProperty().addListener((observable, oldValue, newValue) -> {
            double value = newValue.doubleValue();
            if (value >= 0) {
                type.getFallOf().setMaxVal(value);
                Logger.getGlobal().info("Set maxval of type:" + type.name() + ", value: " + newValue);
            }else{
                Logger.getGlobal().warning("Set maxval of type:" + type.name() + " not set");
            }
        });

        paramThreeProperty().addListener((observable, oldValue, newValue) -> {
            double value = newValue.doubleValue();
            if (value >= 0) {
                type.getFallOf().setExp(value);
                Logger.getGlobal().info("Set exp of type:" + type.name() + ", value: " + newValue);
            }else{
                Logger.getGlobal().warning("Set exp of type:" + type.name() + " not set");
            }
        });

        weightProperty().addListener((observable, oldValue, newValue) -> {
            double value = newValue.doubleValue();
            if (value >= 1) {
                type.setWeight(value);
                Logger.getGlobal().info("Set weight of type:" + type.name() + ", value: " + newValue);
            }else{
                Logger.getGlobal().warning("Set weight of type:" + type.name() + " not set");
            }
        });
    }
}
