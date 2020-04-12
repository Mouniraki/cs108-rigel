package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.*;

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

    private final CartesianCoordinates projectedSun, projectedMoon;
    private final double[] planetCoords, starCoords;

    private final StarCatalogue catalogue;
    private final Map<CelestialObject, CartesianCoordinates> map;

    public ObservedSky(ZonedDateTime observationInstant, GeographicCoordinates observationPos, StereographicProjection projection, StarCatalogue catalogue) {
        double daysUntilJ2010 = Epoch.J2010.daysUntil(observationInstant);
        EclipticToEquatorialConversion eclToEqu = new EclipticToEquatorialConversion(observationInstant);
        EquatorialToHorizontalConversion equToHor = new EquatorialToHorizontalConversion(observationInstant, observationPos);
        this.catalogue = catalogue;

        planets = new ArrayList<>();
        stars = List.copyOf(catalogue.stars());
        map = new HashMap<>();

        sun = SunModel.SUN.at(daysUntilJ2010, eclToEqu);
        HorizontalCoordinates sunHorPor = equToHor.apply(sun.equatorialPos());
        projectedSun = projection.apply(sunHorPor);
        map.put(sun, projectedSun);

        moon = MoonModel.MOON.at(daysUntilJ2010, eclToEqu);
        HorizontalCoordinates moonHorPor = equToHor.apply(moon.equatorialPos());
        projectedMoon = projection.apply(moonHorPor);
        map.put(moon, projectedMoon);

        for(PlanetModel pm : PlanetModel.ALL){
            if(pm != PlanetModel.EARTH) {
                Planet planet = pm.at(daysUntilJ2010, eclToEqu);
                planets.add(planet);
            }
        }

        planetCoords = new double[planets.size() * 2];
        starCoords = new double[stars.size() * 2];

        for (int i = 0; i < planets.size(); i++) {
            Planet planet = planets.get(i);
            HorizontalCoordinates planetHorPos = equToHor.apply(planet.equatorialPos());
            CartesianCoordinates planetCartPos = projection.apply(planetHorPos);

            map.put(planet, planetCartPos);
            planetCoords[i] = planetCartPos.x();
            planetCoords[i+1] = planetCartPos.y();
        }

        for (int i = 0; i < stars.size(); i++) {
            Star star = catalogue.stars().get(i);
            HorizontalCoordinates starHorPos = equToHor.apply(star.equatorialPos());
            CartesianCoordinates starCartPos = projection.apply(starHorPos);

            map.put(star, starCartPos);
            starCoords[i] = starCartPos.x();
            starCoords[i+1] = starCartPos.y();
        }

    }

    public Optional<CelestialObject> objectClosestTo(CartesianCoordinates coords, double maxDistance){
        CelestialObject closestObject = null;
        double minDistance = maxDistance;

        for(Map.Entry<CelestialObject, CartesianCoordinates> e : map.entrySet()) {
            if(coords.distanceTo(e.getValue()) < minDistance) {
                minDistance = coords.distanceTo(e.getValue());
                closestObject = e.getKey();
            }
        }

        if(closestObject != null)
            return Optional.of(closestObject);
        else
            return Optional.empty();
    }

    //TRYING TO MODULARISE THE CODE, BUT ISSUES WITH THE "List<CelestialObject> objects" NOT REPRESENTING CORRECTLY
    //THE TYPE OF OBJECT I WANT TO MANIPULATE
    private double[] celestialObjectsAdder(List<CelestialObject> objects, EquatorialToHorizontalConversion equToHor, StereographicProjection projection){
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

    public Sun sun(){
        return sun;
    }
    public CartesianCoordinates sunPosition(){
        return projectedSun;
    }

    public Moon moon(){
        return moon;
    }
    public CartesianCoordinates moonPosition(){
        return projectedMoon;
    }

    public List<Planet> planets(){
        return List.copyOf(planets);
    }
    public double[] planetPositions(){
        return planetCoords;
    }

    public List<Star> stars(){
        return stars;
    }
    public double[] starPositions(){
        return starCoords;
    }

    public Set<Asterism> asterisms(){
        return catalogue.asterisms();
    }
    public List<Integer> asterismsIndices(Asterism a){
        return catalogue.asterismIndices(a);
    }
}
