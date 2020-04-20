package ch.epfl.rigel.gui;

import java.time.Duration;
import java.time.ZonedDateTime;

/**
 * A time accelerator.
 *
 * @author Mounir Raki (310287)
 */
@FunctionalInterface
public interface TimeAccelerator {
    /**
     * Calculates the actual simulated time from a initial simulated time and a duration.
     *
     * @param simulatedInitTime
     *          the time at the beginning of the simulation (ZonedDateTime)
     * @param realElapsedTime
     *          the real time elapsed from the beginning of the animation (in nanoseconds)
     *
     * @return the actual simulated time
     */
    ZonedDateTime adjust(ZonedDateTime simulatedInitTime, long realElapsedTime);

    /**
     * Returns a continuous time accelerator from a speedFactor.
     *
     * @param speedFactor
     *          the speed factor by which the simulation has to be sped up to
     * @return a continuous time accelerator
     */
    static TimeAccelerator continuous(int speedFactor) {
        return (simulatedInitTime, realElapsedTime) ->
            simulatedInitTime.plusNanos(speedFactor * realElapsedTime);
    }

    /**
     * Returns a discrete time accelerator from a running frequency and a time step.
     *
     * @param runningFrequency
     *          the frequency of of progression of the simulated time (in Hz)
     * @param step
     *          the discrete time step of the simulated time (Duration)
     * @return a discrete time accelerator
     */
    static TimeAccelerator discrete(int runningFrequency, Duration step) {
        double nanosInSeconds = 1e9;
        return (simulatedInitTime, realElapsedTime) -> simulatedInitTime.plusNanos(
                    step.toNanos() * (long) Math.floor(runningFrequency/nanosInSeconds * realElapsedTime)
            );
    }
}
