package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.*;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Set of celestial objects projected onto a plan
 * by a stereographic projection.
 *
 * @author Mounir Raki (310287)
 */
public class ObservedSky {
    private final StarCatalogue catalogue;

    private final Sun sun;
    private final Moon moon;
    private final List<Planet> planets;
    private final List<Star> stars;

    private final CartesianCoordinates projectedSun;
    private final CartesianCoordinates projectedMoon;
    private final double[] planetCoords;
    private final double[] starCoords;

    public ObservedSky(ZonedDateTime observationInstant, GeographicCoordinates observationPos, StereographicProjection projection, StarCatalogue catalogue) {
        this.catalogue = catalogue;

        double daysUntilJ2010 = Epoch.J2010.daysUntil(observationInstant);
        EclipticToEquatorialConversion eclToEqu = new EclipticToEquatorialConversion(observationInstant);
        EquatorialToHorizontalConversion equToHor = new EquatorialToHorizontalConversion(observationInstant, observationPos);

        sun = SunModel.SUN.at(daysUntilJ2010, eclToEqu);
        HorizontalCoordinates sunHorPor = equToHor.apply(sun.equatorialPos());
        projectedSun = projection.apply(sunHorPor);

        moon = MoonModel.MOON.at(daysUntilJ2010, eclToEqu);
        HorizontalCoordinates moonHorPor = equToHor.apply(moon.equatorialPos());
        projectedMoon = projection.apply(moonHorPor);

        planets = new ArrayList<>();
        stars = List.copyOf(catalogue.stars());

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

            planetCoords[i] = planetCartPos.x();
            planetCoords[i+1] = planetCartPos.y();
        }

        for (int i = 0; i < stars.size(); i++) {
            Star star = catalogue.stars().get(i);
            HorizontalCoordinates starHorPos = equToHor.apply(star.equatorialPos());
            CartesianCoordinates starCartPos = projection.apply(starHorPos);

            starCoords[i] = starCartPos.x();
            starCoords[i+1] = starCartPos.y();
        }

    }

    public CelestialObject objectClosestTo(CartesianCoordinates coords, double maxDistance){
        return null;
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
