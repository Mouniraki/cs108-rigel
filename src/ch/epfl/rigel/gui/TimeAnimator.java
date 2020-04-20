package ch.epfl.rigel.gui;

import javafx.animation.AnimationTimer;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class TimeAnimator extends AnimationTimer {
    private DateTimeBean dateTimeBean;
    private ObjectProperty<TimeAccelerator> accelerator;
    private SimpleBooleanProperty running;
    private int counter = 0;
    private List<Long> times = new ArrayList<>(Arrays.asList(0L, 0L));


    public TimeAnimator(DateTimeBean dateTimeBean){
        this.dateTimeBean = dateTimeBean;
        accelerator = null;
    }

    @Override
    public void handle(long l) {
        if(counter == 0){
            long initialValue = l;
        }

        int cValue = counter % 2;

        long deltaTime = 0;
        times.set(cValue, l);

        if(counter != 0){
            if (cValue == 0) {
                deltaTime = l - times.get(1);
            } else {
                deltaTime = l - times.get(0);
            }
        }

        ZonedDateTime newZonedDateTime = getAccelerator().adjust(dateTimeBean.getZonedDateTime(), deltaTime);
        dateTimeBean.setZonedDateTime(newZonedDateTime);

        ++counter;
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
        if(this.accelerator == null) {
            this.accelerator = new SimpleObjectProperty<>(accelerator);
        }
        else{
            this.accelerator.setValue(accelerator);
        }    }

    public ReadOnlyBooleanProperty runningProperty(){
        return running;
    }

    public ReadOnlyBooleanProperty getRunning(){//not sure if correct, but avoids of being modified
        return running;
    }

    public void setRunning(Boolean running){
        if(this.running == null) {
            this.running = new SimpleBooleanProperty(running);
        }
        else {
            this.running.setValue(running);
        }
    }
}
