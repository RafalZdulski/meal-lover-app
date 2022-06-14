package org.zdulski.finalproject.view_auxs.loading;

import javafx.animation.FadeTransition;
import javafx.animation.FillTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.Timeline;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

import java.util.concurrent.TimeUnit;

public class LoadingCircles extends AnchorPane {
    private Circle[] circles;
    private int amountOfCircles;

    private double x;
    private double y;
    private double radius;
    private double circlesRadius;
    private double fadeTime = 1000;
    private double fillTime = 500;
    private Color baseColor = Color.DARKGRAY;

    private double angleStep;
    private FadeTransition[] fadeTransitions;
    private SequentialTransition[] rainbowTransitions;

    public LoadingCircles(double radius, int amountOfCircles, double circlesRadius, double fadeTime, double fillTime) {
        this.x = radius+circlesRadius/2;
        this.y = radius+circlesRadius/2;
        this.amountOfCircles = amountOfCircles;
        this.radius = radius;
        this.circlesRadius = circlesRadius;
        this.circles = initCircles(amountOfCircles, circlesRadius);
        this.fadeTime = fadeTime;
        this.fillTime = fillTime;

        fadeTransitions = initFadeTransitions(circles, fadeTime);
        rainbowTransitions = initRainbowTransitions(circles, fillTime);

        this.getChildren().addAll(circles);
    }

    public void play(){
        for (int i =0; i < amountOfCircles; i++) {
            rainbowTransitions[i].play();
        }
        for (int i =0; i < amountOfCircles; i++) {
            try {
                TimeUnit.MILLISECONDS.sleep((long) (fadeTime/amountOfCircles*2));
                fadeTransitions[i].play();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private Circle[] initCircles(int amountOfCircles, double circlesRadius) {
        Circle[] circles = new Circle[amountOfCircles];
        this.angleStep = 2*Math.PI/circles.length;
        for (int i =0; i < circles.length; i++){
            circles[i] = new Circle();
            circles[i].setRadius(circlesRadius);
            circles[i].setFill(baseColor);
            circles[i].setStrokeWidth(1);
            circles[i].setStroke(Color.DARKGRAY);
            circles[i].setCenterX(x+radius*Math.cos(angleStep*i));
            circles[i].setCenterY(y+radius*Math.sin(angleStep*i));
        }
        return circles;
    }

    private FadeTransition[] initFadeTransitions(Circle[] circles, double fadeTime){
        FadeTransition[] fadeTransitions = new FadeTransition[amountOfCircles];
        for (int i=0; i < amountOfCircles; i++){
            fadeTransitions[i] = getFadeTransition(circles[i],fadeTime);
        }
        return fadeTransitions;
    }

    private SequentialTransition[] initRainbowTransitions(Circle[] circles, double fillTime){
        SequentialTransition[] seqTransitions = new SequentialTransition[amountOfCircles];
        for (int i=0; i < amountOfCircles; i++){
            seqTransitions[i] = getRainbowTransition(circles[i],fillTime);
        }
        return seqTransitions;
    }

    private FadeTransition getFadeTransition(Node node, double fadeTime){
        FadeTransition fadeTrans = new FadeTransition(Duration.millis(fadeTime));
        fadeTrans.setNode(node);
        fadeTrans.setFromValue(1.0);
        fadeTrans.setToValue(0.1);
        fadeTrans.setCycleCount(Timeline.INDEFINITE);
        fadeTrans.setAutoReverse(true);
        return fadeTrans;
    }

    private FillTransition getColorTrans(Color from, Color to, double time){
        FillTransition fillTransition = new FillTransition();
        fillTransition.setFromValue(from.darker());
        fillTransition.setToValue(to.darker());
        fillTransition.setDuration(Duration.millis(time));
        return fillTransition;
    }

    private SequentialTransition getRainbowTransition(Node node, double fillTime){
        SequentialTransition seqTrans = new SequentialTransition();
        seqTrans.setNode(node);
        seqTrans.getChildren().addAll(
                getColorTrans(Color.RED, Color.ORANGE, fillTime),
                getColorTrans(Color.ORANGE, Color.YELLOW, fillTime),
                getColorTrans(Color.YELLOW, Color.GREEN, fillTime),
                getColorTrans(Color.GREEN, Color.BLUE, fillTime),
                getColorTrans(Color.BLUE, Color.PURPLE, fillTime),
                getColorTrans(Color.PURPLE, Color.RED, fillTime)
        );
        seqTrans.setCycleCount(Timeline.INDEFINITE);
        return seqTrans;
    }

}
