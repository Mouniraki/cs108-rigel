package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.Preconditions;
import ch.epfl.rigel.coordinates.*;
import ch.epfl.rigel.math.ClosedInterval;

import java.time.ZonedDateTime;
import java.util.*;

/**
 * Set of celestial objects projected onto a plan
 * by a stereographic projection.
 *
 * @author Mounir Raki (310287)
 */
public class ObservedSky {
    private final Sun sun;
    private final Moon moon;
    private final List<Planet> planets;
    private final List<Star> stars;
    private final ZonedDateTime observationInstant;
    private final GeographicCoordinates observationPos;

    private final CartesianCoordinates sunPosition, moonPosition;
    private final double[] planetPositions, starPositions;

    private final StarCatalogue catalogue;
    private final Map<CelestialObject, CartesianCoordinates> map;

    public ObservedSky(ZonedDateTime observationInstant,
                       GeographicCoordinates observationPos,
                       StereographicProjection projection,
                       StarCatalogue catalogue) {
        double daysUntilJ2010 = Epoch.J2010.daysUntil(observationInstant);
        EclipticToEquatorialConversion eclToEqu = new EclipticToEquatorialConversion(observationInstant);
        EquatorialToHorizontalConversion equToHor = new EquatorialToHorizontalConversion(observationInstant, observationPos);
        this.observationInstant = observationInstant;
        this.observationPos = observationPos;
        this.catalogue = catalogue;

        Map<CelestialObject, CartesianCoordinates> object_position = new HashMap<>();

        sun = SunModel.SUN.at(daysUntilJ2010, eclToEqu);
        moon = MoonModel.MOON.at(daysUntilJ2010, eclToEqu);
        planets = List.copyOf(
                fillPlanets(daysUntilJ2010, eclToEqu)
        );
        stars = List.copyOf(catalogue.stars());

        sunPosition = projectedObject(sun, equToHor, projection, object_position);
        moonPosition = projectedObject(moon, equToHor, projection, object_position);
        planetPositions = projectedCelestialObjects(planets, equToHor, projection, object_position);
        starPositions = projectedCelestialObjects(stars, equToHor, projection, object_position);

        map = Map.copyOf(object_position);
    }

    public Optional<CelestialObject> objectClosestTo(CartesianCoordinates c, double maxDistance){
        Preconditions.checkArgument(maxDistance > 0);
        double minDistance = maxDistance;
        ClosedInterval xInterval = ClosedInterval.of(c.x() - minDistance, c.x() + minDistance);
        ClosedInterval yInterval = ClosedInterval.of(c.y() - minDistance, c.y() + minDistance);

        CelestialObject closestObject = null;

        for(CelestialObject o : map.keySet()){
            CartesianCoordinates cartesian = map.get(o);
            if(xInterval.contains(cartesian.x()) && yInterval.contains(cartesian.y())){
                double distance = c.distanceTo(cartesian);
                if(distance < minDistance){
                    minDistance = distance;
                    if(minDistance != 0) {
                        xInterval = ClosedInterval.of(c.x() - minDistance, c.x() + minDistance);
                        yInterval = ClosedInterval.of(c.y() - minDistance, c.y() + minDistance);
                    }
                    closestObject = o;
                }
            }
        }

        if(closestObject != null)
            return Optional.of(closestObject);
        else
            return Optional.empty();
    }

    public Sun sun(){
        return sun;
    }
    public CartesianCoordinates sunPosition(){
        return sunPosition;
    }

    public Moon moon(){
        return moon;
    }
    public CartesianCoordinates moonPosition(){
        return moonPosition;
    }

    public ZonedDateTime observationInstant(){
        return observationInstant;
    }
    public GeographicCoordinates observationPos(){
        return observationPos;
    }

    public List<Planet> planets(){
        return planets;
    }
    public double[] planetPositions(){
        return planetPositions;
    }

    public List<Star> stars(){
        return stars;
    }
    public double[] starPositions(){
        return starPositions;
    }

    public Set<Asterism> asterisms(){
        return catalogue.asterisms();
    }
    public List<Integer> asterismsIndices(Asterism a){
        return catalogue.asterismIndices(a);
    }

    private List<Planet> fillPlanets(double daysUntilJ2010, EclipticToEquatorialConversion eclToEqu) {
        List<Planet> planets = new ArrayList<>();
        for (PlanetModel pm : PlanetModel.ALL) {
            if (pm != PlanetModel.EARTH) {
                Planet planet = pm.at(daysUntilJ2010, eclToEqu);
                planets.add(planet);
            }
        }
        return planets;
    }

    private CartesianCoordinates projectedObject(CelestialObject object,
                                                 EquatorialToHorizontalConversion equToHor,
                                                 StereographicProjection projection,
                                                 Map<CelestialObject, CartesianCoordinates> map){
        HorizontalCoordinates objectHorPor = equToHor.apply(object.equatorialPos());
        CartesianCoordinates projectedCoordinates = projection.apply(objectHorPor);
        map.put(object, projectedCoordinates);

        return projectedCoordinates;
    }

    private <T> double[] projectedCelestialObjects(List<T> objects,
                                                   EquatorialToHorizontalConversion equToHor,
                                                   StereographicProjection projection,
                                                   Map<CelestialObject, CartesianCoordinates> map){
        double[] objectCoords = new double[objects.size() * 2];
        for (int i = 0; i < objects.size(); i++) {
            T object = objects.get(i);
            HorizontalCoordinates objectHorPos = equToHor.apply(((CelestialObject) object).equatorialPos());
            CartesianCoordinates objectCartPos = projection.apply(objectHorPos);

            map.put((CelestialObject) object, objectCartPos);
            objectCoords[i] = objectCartPos.x();
            objectCoords[i+1] = objectCartPos.y();
        }
        return objectCoords;
    }
}
