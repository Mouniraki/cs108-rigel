package ch.epfl.rigel.gui;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.time.*;

public final class DateTimeBean {
    private ObjectProperty<LocalDate> date;
    private ObjectProperty<LocalTime> time;
    private ObjectProperty<ZoneId> zone;


    public DateTimeBean(){
        this.date = null;
        this.time = null;
        this.zone = null;
    }

    public ObjectProperty<LocalDate> dateProperty(){
        return date;
    }

    public LocalDate getDate(){
        return date.get();
    }

    public void setDate(LocalDate date){
        this.date = new SimpleObjectProperty<>(date);
    }

    public ObjectProperty<LocalTime> timeProperty(){
        return time;
    }

    public LocalTime getTime(){
        return time.get();
    }

    public void setTime(LocalTime time){
        this.time = new SimpleObjectProperty<>(time);
    }

    public ObjectProperty<ZoneId> zoneProperty(){
        return zone;
    }

    public ZoneId getZone(){
        return zone.get();
    }

    public void setZone(ZoneId zone){
        this.zone = new SimpleObjectProperty<>(zone);
    }

    public ZonedDateTime getZonedDateTime(){
        LocalDateTime ldt = LocalDateTime.of(date.get(), time.get());

        return ZonedDateTime.of(ldt, zone.get());
    }

    public void setZonedDateTime(ZonedDateTime zdt){
        setDate(zdt.toLocalDate());
        setTime(zdt.toLocalTime());
        setZone(zdt.getZone());
    }
}
