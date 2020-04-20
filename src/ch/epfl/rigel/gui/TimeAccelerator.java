package ch.epfl.rigel.gui;

import java.time.Duration;
import java.time.ZonedDateTime;

/**
 * A time accelerator.
 *
 * @author Mounir Raki (310287)
 */
@FunctionalInterface
public interface TimeAccelerator { //NEED TO BE SURE THAT IT IS CORRECT
    ZonedDateTime adjust(ZonedDateTime simulatedInitTime, long realElapsedTime);

    static TimeAccelerator continuous(int speedFactor) {
        return (simulatedInitTime, realElapsedTime) ->
            simulatedInitTime.plusNanos(speedFactor * realElapsedTime);
    }

    static TimeAccelerator discrete(int runningFrequency, Duration step) {
        double nanosInSeconds = 1e9;
        return (simulatedInitTime, realElapsedTime) -> simulatedInitTime.plusNanos(
                    step.toNanos() * (long) Math.floor(runningFrequency/nanosInSeconds * realElapsedTime)
            );
    }
}
