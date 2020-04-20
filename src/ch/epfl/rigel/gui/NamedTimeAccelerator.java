package ch.epfl.rigel.gui;

import java.time.Duration;

/**
 * A named Time Accelerator.
 *
 * @author Mounir Raki (310287)
 */
public enum NamedTimeAccelerator {
    TIMES_1("1×", TimeAccelerator.continuous(1)),
    TIMES_30("30×", TimeAccelerator.continuous(30)),
    TIMES_300("300×", TimeAccelerator.continuous(300)),
    TIMES_3000("3000×", TimeAccelerator.continuous(3000)),
    DAY("jour", TimeAccelerator.discrete(60, Duration.ofHours(24))),
    SIDEREAL_DAY("jour sidéral",
            TimeAccelerator.discrete(60, Duration
            .ofHours(23)
            .plusMinutes(56)
            .plusSeconds(4)));

    private final String name;
    private final TimeAccelerator accelerator;

    NamedTimeAccelerator(String name, TimeAccelerator accelerator){
        this.name = name;
        this.accelerator = accelerator;
    }

    /**
     * Getter for the name of the accelerator.
     *
     * @return the name of the accelerator
     */
    public String getName(){
        return name;
    }

    /**
     * Getter for the accelerator.
     *
     * @return the accelerator
     */
    public TimeAccelerator getAccelerator(){
        return accelerator;
    }

    /**
     * Redefines the toString method in java.lang.Object to return the name of the accelerator.
     *
     * @return the name of the accelerator
     */
    @Override
    public String toString() {
        return name;
    }
}
