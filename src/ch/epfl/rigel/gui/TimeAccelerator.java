package ch.epfl.rigel.gui;

import java.time.Duration;
import java.time.ZonedDateTime;

/**
 * CLASSDESCRIPTION
 *
 * @author Mounir Raki (310287)
 */
@FunctionalInterface
public interface TimeAccelerator { //MUST IMPLEMENT WITH LAMBDAS
    ZonedDateTime adjust(ZonedDateTime simulatedInitTime, long realElapsedTime);

    static TimeAccelerator continuous(int speedFactor) {
        return null;
    }
    static TimeAccelerator discrete(int runningFrequency, Duration step) {
        return null;
    }
}
