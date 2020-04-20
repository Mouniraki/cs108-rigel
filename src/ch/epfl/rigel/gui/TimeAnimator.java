package ch.epfl.rigel.gui;

import javafx.animation.AnimationTimer;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;

public final class TimeAnimator extends AnimationTimer {
    private DateTimeBean dateTimeBean;
    private ObjectProperty<TimeAccelerator> accelerator;
    private SimpleBooleanProperty running;

    TimeAnimator(DateTimeBean dateTimeBean){
        this.dateTimeBean = dateTimeBean;
        accelerator = null;
    }








    @Override
    public void handle(long l) {
        long copyOfL = l;

        if (copyOfL  >= l){
            long firstL = l;
        }

        long deltaTime;


    }

    public void start(){
        super.start();
        setRunning(true);

    }

    public void stop(){
        super.stop();
        setRunning(false);
    }

    public ObjectProperty<TimeAccelerator> acceleratorProperty(){
        return accelerator;
    }

    public TimeAccelerator getAccelerator(){
        return accelerator.get();
    }

    public void setAccelerator(TimeAccelerator accelerator){
        this.accelerator = new SimpleObjectProperty<>(accelerator);
    }

    public ReadOnlyBooleanProperty runningProperty(){
        return running;
    }

    public ReadOnlyBooleanProperty getRunning(){//not sure if correct, but avoids of being modified
        return running;
    }

    public void setRunning(Boolean running){
        this.running = new SimpleBooleanProperty(running);
    }
}
