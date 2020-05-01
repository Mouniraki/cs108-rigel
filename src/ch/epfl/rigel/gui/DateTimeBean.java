package ch.epfl.rigel.gui;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.time.*;

/**
 * The date time bean, a class with a similar behavior
 * to ZonedDateTime but with observable properties.
 *
 * @author Nicolas Szwajcok (315213)
 */
public final class DateTimeBean {
    private final ObjectProperty<LocalDate> date;
    private final ObjectProperty<LocalTime> time;
    private final ObjectProperty<ZoneId> zone;

    /**
     * Creates an instance of a time animator by initializing it's parameters.
     */
    public DateTimeBean(){
        this.date = new SimpleObjectProperty<>();
        this.time = new SimpleObjectProperty<>();
        this.zone = new SimpleObjectProperty<>();
    }

    /**
     * Returns the object property of a local date.
     *
     * @return The object property of a local date
     */
    public ObjectProperty<LocalDate> dateProperty(){
        return date;
    }

    /**
     * Returns the local date.
     *
     * @return The local date
     */
    public LocalDate getDate(){
        return date.get();
    }

    /**
     * Sets the value of the local date.
     *
     * @param date The local date to be set
     */
    public void setDate(LocalDate date){
        this.date.set(date);
    }

    /**
     * Returns the object property of a local time.
     *
     * @return The object property of a local time
     */
    public ObjectProperty<LocalTime> timeProperty(){
        return time;
    }

    /**
     * Returns the local time.
     *
     * @return The local time
     */
    public LocalTime getTime(){
        return time.get();
    }

    /**
     * Sets the value of the local time.
     *
     * @param time The local time to be set
     */
    public void setTime(LocalTime time){
        this.time.set(time);
    }

    /**
     * Returns the object property of a zone id.
     *
     * @return The object property of a zone id
     */
    public ObjectProperty<ZoneId> zoneProperty(){
        return zone;
    }

    /**
     * Returns the zone id.
     *
     * @return The zone id
     */
    public ZoneId getZone(){
        return zone.get();
    }

    /**
     * Sets the value of the zone id.
     *
     * @param zoneId The zone id value to be set
     */
    public void setZone(ZoneId zoneId){
        this.zone.set(zoneId);
    }

    /**
     * Returns all the information about date, time and zone id
     * contained in the data time bean in a format of zoned data time.
     *
     * @return Zoned date time containing all the information about the data time bean
     */
    public ZonedDateTime getZonedDateTime(){
        LocalDateTime ldt = LocalDateTime.of(getDate(), getTime());

        return ZonedDateTime.of(ldt, getZone());
    }

    /**
     * Modifies the parameters of the date time bean to change the date, time and zone id.
     *
     * @param zdt The zoned date time that will be used to change the internal parameters of the date time bean
     */
    public void setZonedDateTime(ZonedDateTime zdt){
        setDate(zdt.toLocalDate());
        setTime(zdt.toLocalTime());
        setZone(zdt.getZone());
    }
}
