package org.htwkvisu.gui.interpolate;

import javafx.scene.paint.Color;

public class InterpolateConfig {
    private Color[] cols;
    private int xSize;
    private int y;
    private int x;
    private float xNorm;
    private float yNorm;

    public InterpolateConfig(Color[] cols, int xSize, int y, int x, float xNorm, float yNorm) {
        this.cols = cols;
        this.xSize = xSize;
        this.y = y;
        this.x = x;
        this.xNorm = xNorm;
        this.yNorm = yNorm;
    }

    public Color[] getCols() {
        return cols;
    }

    public void setCols(Color[] cols) {
        this.cols = cols;
    }

    public int getxSize() {
        return xSize;
    }

    public void setxSize(int xSize) {
        this.xSize = xSize;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public float getxNorm() {
        return xNorm;
    }

    public void setxNorm(float xNorm) {
        this.xNorm = xNorm;
    }

    public float getyNorm() {
        return yNorm;
    }

    public void setyNorm(float yNorm) {
        this.yNorm = yNorm;
    }
}
