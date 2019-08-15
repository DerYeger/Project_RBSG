package de.uniks.se19.team_g.project_rbsg.component;

import javafx.beans.property.*;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

public class ZoomableScrollPane extends ScrollPane {
    private SimpleDoubleProperty scaleValue;
    //private double scaleValue = 0.7;
    private SimpleDoubleProperty zoomIntensity;
    //private double zoomIntensity = 0.02;
    private Node target;
    private Node zoomNode;

    private SimpleBooleanProperty disablePlusZoom = new SimpleBooleanProperty(false);
    private SimpleBooleanProperty disableMinusZoom = new SimpleBooleanProperty(false);

    public ZoomableScrollPane(Node target) {
        super();

        scaleValue = new SimpleDoubleProperty(0.7);
        zoomIntensity = new SimpleDoubleProperty(0.02);

        getStylesheets().add("/ui/darkTheme.css");
        getStyleClass().add("invisible");
        this.target = target;
        this.zoomNode = new Group(target);
        setContent(outerNode(zoomNode));

        setPannable(true);
        setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        setFitToHeight(true); //center
        setFitToWidth(true); //center

        updateScale();
    }

    private Node outerNode(Node node) {
        Node outerNode = centeredNode(node);
        outerNode.setOnScroll(e -> {
            e.consume();
            onScroll(e.getDeltaY(), new Point2D(e.getX(), e.getY()));
        });
        return outerNode;
    }

    private Node centeredNode(Node node) {
        VBox vBox = new VBox(node);
        vBox.setAlignment(Pos.CENTER);
        return vBox;
    }

    private void updateScale() {
        target.setScaleX(getScaleValue());
        target.setScaleY(getScaleValue());
    }

    public void onScroll(double wheelDelta, Point2D mousePoint) {
        if(wheelDelta > 5) wheelDelta = 5;
        if(wheelDelta < -5) wheelDelta = -5;
        double zoomFactor = Math.exp(wheelDelta * zoomIntensity.get());
        Bounds innerBounds = zoomNode.getLayoutBounds();
        Bounds viewportBounds = getViewportBounds();

        // calculate pixel offsets from [0, 1] range
        double valX = this.getHvalue() * (innerBounds.getWidth() - viewportBounds.getWidth());
        double valY = this.getVvalue() * (innerBounds.getHeight() - viewportBounds.getHeight());
        disableZoomButtons(wheelDelta);
        if(target.getScaleX() > 1.4 && wheelDelta > 0) return;
        if(target.getScaleX() < 0.8 && wheelDelta < 0) return;
        setScaleValue(scaleValue.get() * zoomFactor);
        updateScale();
        disableZoomButtons(wheelDelta);
        this.layout(); // refresh ScrollPane scroll positions & target bounds

        // convert target coordinates to zoomTarget coordinates
        Point2D posInZoomTarget = target.parentToLocal(zoomNode.parentToLocal(mousePoint));

        // calculate adjustment of scroll position (pixels)
        Point2D adjustment = target.getLocalToParentTransform().deltaTransform(posInZoomTarget.multiply(zoomFactor - 1));

        // convert back to [0, 1] range
        // (too large/small values are automatically corrected by ScrollPane)
        Bounds updatedInnerBounds = zoomNode.getBoundsInLocal();
        this.setHvalue((valX + adjustment.getX()) / (updatedInnerBounds.getWidth() - viewportBounds.getWidth()));
        this.setVvalue((valY + adjustment.getY()) / (updatedInnerBounds.getHeight() - viewportBounds.getHeight()));
    }

    private void disableZoomButtons(double wheelDelta) {
        if(target.getScaleX() > 1.4 && wheelDelta > 0) {
            disablePlusZoom.set(true);
        } else {
            disablePlusZoom.set(false);
        }
        if(target.getScaleX() < 0.8 && wheelDelta < 0) {
            disableMinusZoom.set(true);
        } else {
            disableMinusZoom.set(false);
        }
    }

    private double getScaleValue()
    {
        return scaleValue.get();
    }

    public SimpleDoubleProperty scaleValueProperty()
    {
        return scaleValue;
    }

    public void setScaleValue(double scaleValue)
    {
        this.scaleValue.set(scaleValue);
    }

    public double getZoomIntensity()
    {
        return zoomIntensity.get();
    }

    public SimpleDoubleProperty zoomIntensityProperty()
    {
        return zoomIntensity;
    }

    public void setZoomIntensity(double zoomIntensity)
    {
        this.zoomIntensity.set(zoomIntensity);
    }

    public SimpleBooleanProperty getDisablePlusZoom(){
        return disablePlusZoom;
    }

    public SimpleBooleanProperty getDisableMinusZoom(){
        return disableMinusZoom;
    }

}


