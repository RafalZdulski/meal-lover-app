package org.zdulski.finalproject.view_auxs.loading;

import javafx.animation.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class LoadingSlider extends AnchorPane {
    private double height;
    private double width;

    private Rectangle slider;
    private Rectangle track;
    private double sliderWidth = 50;

    private double pathDuration = 2000;
    private double bounceDuration = 2000;
    private SequentialTransition transition;

    public LoadingSlider(double width, double height, double sliderWidth, double pathDuration, double bounceDuration) {
        super();
        this.height = height;
        this.width = width;
        this.sliderWidth = sliderWidth;
        this.pathDuration = pathDuration;
        this.bounceDuration = bounceDuration;
        track = new Rectangle(width, height, Color.LIGHTGREY);
        track.setId("track");
        slider = new Rectangle(this.sliderWidth, height, Color.DARKGRAY);
        slider.setId("slider");
        this.getChildren().addAll(track, slider);
        transition = initTransition();
    }

    private SequentialTransition initTransition(){
        SequentialTransition sq = new SequentialTransition();
        sq.getChildren().addAll(
                rightTransition(),
                rightBounce(),
                leftTransition(),
                leftBounce()
        );
        sq.setCycleCount(Timeline.INDEFINITE);
        return sq;
    }

    public void play(){
        transition.play();
    }

    private PathTransition rightTransition(){
        Line path = new Line(sliderWidth /2,height/2, width- sliderWidth /2, height/2 );
        return getPathTransition(path);
    }

    private PathTransition leftTransition(){
        Line path = new Line(width- sliderWidth /2,height/2, sliderWidth /2, height/2 );
        return getPathTransition(path);
    }

    private PathTransition getPathTransition(Line path) {
        PathTransition pathTransition = new PathTransition();
        pathTransition.setDuration(Duration.millis(pathDuration));
        pathTransition.setPath(path);
        pathTransition.setNode(slider);
        pathTransition.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);
        pathTransition.setInterpolator(Interpolator.LINEAR);
        return pathTransition;
    }

    private Timeline rightBounce(){
        KeyValue widthValue = new KeyValue(slider.widthProperty(), 0);
        KeyFrame widthFrame = new KeyFrame(Duration.millis(bounceDuration /2), widthValue);
        KeyValue xValue = new KeyValue(slider.xProperty(), slider.getWidth());
        KeyFrame xFrame = new KeyFrame(Duration.millis(bounceDuration /2), xValue);
        Timeline timeline = new Timeline(widthFrame, xFrame);
        timeline.setAutoReverse(true);
        timeline.setCycleCount(2);
        return timeline;
    }

    private Timeline leftBounce(){
        KeyValue widthValue = new KeyValue(slider.widthProperty(), 0);
        KeyFrame keyFrame = new KeyFrame(Duration.millis(bounceDuration /2), widthValue);
        Timeline timeline = new Timeline(keyFrame);
        timeline.setAutoReverse(true);
        timeline.setCycleCount(2);
        return timeline;
    }

    /* GETTERS AND SETTERS */

    public double getTrackHeight() {
        return height;
    }

    public void setTrackHeight(double height) {
        this.height = height;
    }

    public double getTrackWidth() {
        return width;
    }

    public void setTrackWidth(double width) {
        this.width = width;
    }

    public Rectangle getSlider() {
        return slider;
    }

    public void setSlider(Rectangle slider) {
        this.slider = slider;
    }

    public Rectangle getTrack() {
        return track;
    }

    public void setTrack(Rectangle track) {
        this.track = track;
    }

    public double getSliderWidth() {
        return sliderWidth;
    }

    public void setSliderWidth(double innerWidth) {
        this.sliderWidth = innerWidth;
    }

    public double getPathDuration() {
        return pathDuration;
    }

    public void setPathDuration(double pathDuration) {
        this.pathDuration = pathDuration;
    }

    public double getBounceDuration() {
        return bounceDuration;
    }

    public void setBounceDuration(double bounceDuration) {
        this.bounceDuration = bounceDuration;
    }
}
