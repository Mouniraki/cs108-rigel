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

/**
 * The time animator that uses the time elapsed in real world
 * to change the time in the simulation.
 *
 * @author Nicolas Szwajcok (315213)
 */
public final class TimeAnimator extends AnimationTimer {
    private DateTimeBean dateTimeBean;
    private ObjectProperty<TimeAccelerator> accelerator;
    private SimpleBooleanProperty running;
    private int counter = 0;
    private List<Long> times = new ArrayList<>(Arrays.asList(0L, 0L));

    /**
     * Creates an instance of a time animator by initializing some of it's parameters.
     *
     * @param dateTimeBean The date time bean at the moment of creation of the time animator
     */
    public TimeAnimator(DateTimeBean dateTimeBean){
        this.dateTimeBean = dateTimeBean;
        accelerator = null;
    }

    /**
     * Updates the date time bean by a real world time. The real world time can be accelerated by the class' animator.
     *
     * @param l The number of nanoseconds since an unspecified starting instance
     */
    @Override
    public void handle(long l) {
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

    /**
     * Starts the time animator.
     */
    @Override
    public void start(){
        super.start();
        setRunning(true);

    }

    /**
     * Stops the time animator.
     */
    @Override
    public void stop(){
        super.stop();
        setRunning(false);
    }

    /**
     * Returns the object property of a time accelerator.
     *
     * @return The object property of a time accelerator
     */
    public ObjectProperty<TimeAccelerator> acceleratorProperty(){
        return accelerator;
    }

    /**
     * Returns the time accelerator.
     *
     * @return The time accelerator
     */
    public TimeAccelerator getAccelerator(){
        return accelerator.get();
    }

    /**
     * Sets the value of a time accelerator.
     */
    public void setAccelerator(TimeAccelerator accelerator){
        if(this.accelerator == null) {
            this.accelerator = new SimpleObjectProperty<>(accelerator);
        }
        else{
            this.accelerator.setValue(accelerator);
        }
    }

    /**
     * Returns the read-only boolean property telling if the time animator is currently running.
     *
     * @return The read-only boolean object telling if the time animator is currently running
     */
    public ReadOnlyBooleanProperty runningProperty(){
        return running;
    }

    /**
     * Returns the read-only boolean property telling if the time animator is currently running.
     *
     * @return The read-only boolean object telling if the time animator is currently running
     */
    public ReadOnlyBooleanProperty getRunning(){//not sure if correct, but avoids of being modified
        return running;
    }

    /**
     * Sets the read-only boolean property telling if the time animator is currently running.
     */
    public void setRunning(Boolean running){
        if(this.running == null) {
            this.running = new SimpleBooleanProperty(running);
        }
        else {
            this.running.setValue(running);
        }
    }
}
