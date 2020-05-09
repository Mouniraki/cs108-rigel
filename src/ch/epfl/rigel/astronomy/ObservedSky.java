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

    private final CartesianCoordinates sunPosition, moonPosition;
    private final double[] planetPositions, starPositions;

    private final StarCatalogue catalogue;
    private final Map<CelestialObject, CartesianCoordinates> map;

    /**
     * Builds a sky of celestial objects at a specific time and observation point.
     *
     * @param observationInstant
     *          the instant at which the sky is observed
     * @param observationPos
     *          the position of the observation point
     * @param projection
     *          the stereographic projection to use
     * @param catalogue
     *          the catalogue of stars and asterisms to project onto this sky
     */
    public ObservedSky(ZonedDateTime observationInstant,
                       GeographicCoordinates observationPos,
                       StereographicProjection projection,
                       StarCatalogue catalogue) {
        double daysUntilJ2010 = Epoch.J2010.daysUntil(observationInstant);
        EclipticToEquatorialConversion eclToEqu = new EclipticToEquatorialConversion(observationInstant);
        EquatorialToHorizontalConversion equToHor = new EquatorialToHorizontalConversion(observationInstant, observationPos);
        this.catalogue = catalogue;

        Map<CelestialObject, CartesianCoordinates> object_position = new HashMap<>();

        sun = SunModel.SUN.at(daysUntilJ2010, eclToEqu);
        moon = MoonModel.MOON.at(daysUntilJ2010, eclToEqu);
        planets = fillPlanets(daysUntilJ2010, eclToEqu);
        stars = List.copyOf(catalogue.stars());

        sunPosition = projectedObject(sun, equToHor, projection, object_position);
        moonPosition = projectedObject(moon, equToHor, projection, object_position);
        planetPositions = projectedCelestialObjects(planets, equToHor, projection, object_position);
        starPositions = projectedCelestialObjects(stars, equToHor, projection, object_position);

        map = Map.copyOf(object_position);
    }

    /**
     * Searches for the closest celestial object from a specific point and a maximal distance.
     *
     * @param c
     *          the point from which the closest celestial object is being searched
     * @param maxDistance
     *          the maximal distance to which the searching process will be conducted
     * @throws IllegalArgumentException
     *          if the maximal distance is negative or 0, or if the cartesian coordinates are null
     * @return the closest celestial object if there is one, or null if there are no close object
     *         with a distance to the point that is closer than the maximal distance
     */
    //TODO : Find out why the old implementation is incorrect (has surely to do with the intervals)
    public Optional<CelestialObject> objectClosestTo(CartesianCoordinates c, double maxDistance){
        Preconditions.checkArgument(c != null);
        double minDistance = maxDistance * maxDistance;
        CelestialObject closestObject = null;
        for(CelestialObject o : map.keySet()){
            double distance = c.squareDistanceTo(map.get(o));
            if(distance < minDistance){
                minDistance = distance;
                closestObject = o;
            }
        }

        if(closestObject != null)
            return Optional.of(closestObject);
        else
            return Optional.empty();
        /*
        ClosedInterval xInterval = ClosedInterval.of(c.x() - minDistance, c.x() + minDistance);
        ClosedInterval yInterval = ClosedInterval.of(c.y() - minDistance, c.y() + minDistance);

        for(CelestialObject o : map.keySet()){
            CartesianCoordinates cartesian = map.get(o);

            if (xInterval.contains(cartesian.x()) && yInterval.contains(cartesian.y())) {
                double distance = c.squareDistanceTo(cartesian);
                if (distance < minDistance) {
                    minDistance = distance;
                    if (minDistance > 0) {
                        xInterval = ClosedInterval.of(c.x() - minDistance, c.x() + minDistance);
                        yInterval = ClosedInterval.of(c.y() - minDistance, c.y() + minDistance);
                    }
                    closestObject = o;
                }
            }
        }
        */
    }

    /**
     * Getter for the generated Sun.
     *
     * @return the generated Sun in the constructor
     */
    public Sun sun(){
        return sun;
    }

    /**
     * Getter for the coordinates of the generated Sun.
     *
     * @return the coordinates of the generated Sun
     */
    public CartesianCoordinates sunPosition(){
        return sunPosition;
    }

    /**
     * Getter for the generated Moon.
     *
     * @return the generated Moon in the constructor
     */
    public Moon moon(){
        return moon;
    }

    /**
     * Getter for the coordinates of the generated Moon.
     *
     * @return the coordinates of the generated Moon
     */
    public CartesianCoordinates moonPosition(){
        return moonPosition;
    }

    /**
     * Getter for the list of the seven planets generated in the constructor.
     *
     * @return the list of seven planets (without the Earth)
     */
    public List<Planet> planets(){
        return planets;
    }

    /**
     * Getter for the array containing the positions of the seven planets.
     *
     * @return the array containing the positions of the seven planets
     */
    public double[] planetPositions(){
        return planetPositions;
    }

    /**
     * Getter for the list of stars.
     *
     * @return the list of stars
     */
    public List<Star> stars(){
        return stars;
    }

    /**
     * Getter for the array containing the positions of the stars.
     *
     * @return the array containing the positions of the stars
     */
    public double[] starPositions(){
        return starPositions;
    }

    /**
     * Getter for the list of asterisms (directly from StarCatalogue).
     *
     * @return the list of asterisms
     */
    public Set<Asterism> asterisms(){
        return catalogue.asterisms();
    }

    /**
     * Getter for the list of indices (from StarCatalogue) of the stars constituting a given asterism.
     *
     * @param a
     *          The asterism used to obtain the indices
     * @return The list of indices in the star catalogue of the stars constituting a given asterism
     */
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
        return List.copyOf(planets);
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

            int xIndex = 2 * i;
            int yIndex = 2*i + 1;
            objectCoords[xIndex] = objectCartPos.x();
            objectCoords[yIndex] = objectCartPos.y();
        }
        return objectCoords;
    }
}
