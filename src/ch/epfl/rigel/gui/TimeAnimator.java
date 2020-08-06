package ch.epfl.rigel.gui;

import javafx.animation.AnimationTimer;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import java.time.ZonedDateTime;

/**
 * The time animator that uses the elapsed time in the real world
 * to change the time in the simulation.
 *
 * @author Nicolas Szwajcok (315213)
 */
public final class TimeAnimator extends AnimationTimer {
    private final DateTimeBean dateTimeBean;
    private final ObjectProperty<TimeAccelerator> accelerator;
    private final SimpleBooleanProperty running;
    /*
    private int counter;
    private long time;

     */

    //CORRECTION
    private long realStart;
    private ZonedDateTime simulatedStart;

    /**
     * Creates an instance of a time animator.
     *
     * @param dateTimeBean The date time bean at the moment of creation of the time animator
     */
    public TimeAnimator(DateTimeBean dateTimeBean){
        this.dateTimeBean = dateTimeBean;
        this.accelerator = new SimpleObjectProperty<>();
        this.running = new SimpleBooleanProperty(false);
        //counter = 0;
    }

    /**
     * Updates the DateTime bean by the real world time. The real world time can be accelerated by the class' animator.
     *
     * @param realNow The number of nanoseconds that have elapsed since an unspecified starting instance
     */
    @Override
    public void handle(long realNow) { //APPELEE 60 FOIS/SECONDE
        if(simulatedStart == null){ //SI LE TEMPS SIMULE EST NUL => ON L'INITIALISE AVEC LA VALEUR DU TEMPS ACTUEL
            realStart = realNow;
            simulatedStart = dateTimeBean.getZonedDateTime();
        } else { //SINON, ON CALCULE LA DIFFERENCE DU TEMPS REEL ET DU TEMPS AU DEMARRAGE
                // (MESURE LE TEMPS DEPUIS LE PREMIER APPEL A HANDLE)
            long elapsedRealNs = realNow - realStart;
            //ET ON SET LE ZONEDDATETIME DE SORTE A LUI RAJOUTER PETIT A PETIT LA DIFFERENCE DE TEMPS
            //POUR FAIRE PROGRESSER L'ANIMATION
            dateTimeBean.setZonedDateTime(accelerator.get().adjust(simulatedStart, elapsedRealNs));
        }
        /*
        long deltaTime = counter == 0 ? 0 : realNow - time;
        ZonedDateTime newZonedDateTime = getAccelerator().adjust(dateTimeBean.getZonedDateTime(), deltaTime);

        dateTimeBean.setZonedDateTime(newZonedDateTime);
        time = realNow;
        counter += 1;

         */
    }

    /**
     * Starts the time animator.
     */
    @Override
    public void start(){
        realStart = 0;
        simulatedStart = null;
        setRunning(true);
        super.start();
        /*
        super.start();
        setRunning(true);
         */
    }

    /**
     * Stops the time animator.
     */
    @Override
    public void stop(){
        super.stop();
        setRunning(false);
        //counter = 0;
    }

    /**
     * Returns the object property of the time accelerator.
     *
     * @return The object property of the time accelerator
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
     * Sets the value of the time accelerator.
     */
    public void setAccelerator(TimeAccelerator accelerator){
        this.accelerator.setValue(accelerator);
    }

    /**
     * Returns the read-only boolean property informing if the time animator is currently running.
     *
     * @return The read-only boolean object informing if the time animator is currently running
     */
    public ReadOnlyBooleanProperty runningProperty(){
        return running;
    }

    /**
     * Returns the read-only boolean property informing if the time animator is currently running.
     *
     * @return The read-only boolean object informing if the time animator is currently running
     */
    public boolean getRunning(){
        return running.get();
    }

    /**
     * Sets the read-only boolean property informing if the time animator is currently running.
     */
    private void setRunning(Boolean running){
        this.running.setValue(running);
    }
}
