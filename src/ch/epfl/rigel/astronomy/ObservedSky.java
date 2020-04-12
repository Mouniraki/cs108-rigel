package ch.epfl.rigel.astronomy;

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
    private final List<CelestialObject> planets, stars;

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
        this.catalogue = catalogue;

        Map<CelestialObject, CartesianCoordinates> object_position = new HashMap<>();

        List<CelestialObject> tempPlanets = new ArrayList<>();
        for(PlanetModel pm : PlanetModel.ALL){
            if(pm != PlanetModel.EARTH) {
                Planet planet = pm.at(daysUntilJ2010, eclToEqu);
                tempPlanets.add(planet);
            }
        }

        sun = SunModel.SUN.at(daysUntilJ2010, eclToEqu);
        moon = MoonModel.MOON.at(daysUntilJ2010, eclToEqu);
        planets = List.copyOf(tempPlanets);
        stars = List.copyOf(catalogue.stars());

        sunPosition = projectedObject(sun, equToHor, projection, object_position);
        moonPosition = projectedObject(moon, equToHor, projection, object_position);
        planetPositions = projectedObjects(planets, equToHor, projection, object_position);
        starPositions = projectedObjects(stars, equToHor, projection, object_position);
        map = Map.copyOf(object_position);
    }

    public Optional<CelestialObject> objectClosestTo(CartesianCoordinates c, double maxDistance){
        CelestialObject closestObject = null;
        double minDistance = maxDistance;

        ClosedInterval xInterval = ClosedInterval.of(c.x() - minDistance, c.x() + minDistance);
        ClosedInterval yInterval = ClosedInterval.of(c.y() - minDistance, c.y() + minDistance);

        for(CelestialObject o : map.keySet()){
            CartesianCoordinates cartesian = map.get(o);
            double x = cartesian.x();
            double y = cartesian.y();
            if(xInterval.contains(x) && yInterval.contains(y)){
                minDistance = c.distanceTo(cartesian);
                xInterval = ClosedInterval.of(c.x() - minDistance, c.x() + minDistance);
                yInterval = ClosedInterval.of(c.y() - minDistance, c.y() + minDistance);
                closestObject = o;
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

    public List<CelestialObject> planets(){
        return planets;
    }
    public double[] planetPositions(){
        return planetPositions;
    }

    public List<CelestialObject> stars(){
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

    private CartesianCoordinates projectedObject(CelestialObject object,
                                                 EquatorialToHorizontalConversion equToHor,
                                                 StereographicProjection projection,
                                                 Map<CelestialObject, CartesianCoordinates> map){
        HorizontalCoordinates objectHorPor = equToHor.apply(object.equatorialPos());
        CartesianCoordinates projectedCoordinates = projection.apply(objectHorPor);
        map.put(object, projectedCoordinates);

        return projectedCoordinates;
    }

    private double[] projectedObjects(List<CelestialObject> objects,
                                      EquatorialToHorizontalConversion equToHor,
                                      StereographicProjection projection,
                                      Map<CelestialObject, CartesianCoordinates> map){
        double[] objectCoords = new double[objects.size() * 2];
        for (int i = 0; i < objects.size(); i++) {
            CelestialObject object = objects.get(i);
            HorizontalCoordinates objectHorPos = equToHor.apply(object.equatorialPos());
            CartesianCoordinates objectCartPos = projection.apply(objectHorPos);

            map.put(object, objectCartPos);
            objectCoords[i] = objectCartPos.x();
            objectCoords[i+1] = objectCartPos.y();
        }
        return objectCoords;
    }
}
