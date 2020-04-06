package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.Preconditions;

import java.io.*;
import java.util.*;

/**
 * The class describing the behavior of a star catalogue.
 *
 * @author Nicolas Szwajcok (315213)
 * @author Mounir Raki (310287)
 */
public final class StarCatalogue {
    final private List<Star> stars;
    final private List<Asterism> asterisms;
    final private Map<Asterism, List<Integer>> map;

    /**
     * Creates an instance of a star catalogue.
     *
     * @param stars The list of stars that used to construct a star catalogue
     * @param asterisms The list of asterisms used to construct a star catalogue
     */
    public StarCatalogue(List<Star> stars, List<Asterism> asterisms) {
        Map<Asterism, List<Integer>> tempMap = new HashMap<>();
        for(Asterism asterism : asterisms) {
            Preconditions.checkArgument(stars.containsAll(asterism.stars()));
            List<Integer> indexes = new ArrayList<>();

            for(Star s : asterism.stars()) {
                indexes.add(stars.indexOf(s));
            }
            tempMap.put(asterism, List.copyOf(indexes));
        }
        this.map = Map.copyOf(tempMap);
        this.stars = List.copyOf(stars);
        this.asterisms = List.copyOf(asterisms);
    }

    /**
     * Returns the list of stars constituting the star catalogue
     * @return The list of stars constituting the star catalogue
     */
    public List<Star> stars() {
        return stars;
    }

    /**
     * Returns a collection of the asterisms constituting a star catalogue
     * @return A collection of the asterisms constituting a star catalogue
     */
    public Set<Asterism> asterisms() {
        return map.keySet();
    }

    /**
     * Returns the list of indices (in the star catalogue) of the stars constituting a given asterism
     * @param asterism The asterism used to obtain the indices
     * @return The list of indices in the star catalogue of the stars constituting a given asterism
     */
    public List<Integer> asterismIndices(Asterism asterism){
        Preconditions.checkArgument(asterisms.contains(asterism));
        return map.get(asterism);
    }

    /**
     * The class allowing to build a star catalogue.
     *
     * @author Nicolas Szwajcok (315213)
     */
    public final static class Builder {
        private List<Star> stars;
        private List<Asterism> asterisms;

        /**
         * Constructor of the builder that initializes its internal parameters.
         */
        public Builder(){
            this.stars = new ArrayList<>();
            this.asterisms = new ArrayList<>();
        }

        /**
         * Adds the given star to the catalogue under construction and returns the builder
         * @param star The star that will be included in the catalogue under construction
         * @return The builder
         */
        public Builder addStar(Star star){
            stars.add(star);
            return this;
        }

        /**
         * Allows to obtain a unmodifiable and not immutable view on the stars of the catalogue under construction
         * @return The unmodifiable and not immutable view on the stars of the catalogue under construction
         */
        public List<Star> stars(){
            return Collections.unmodifiableList(stars);
        }
        /**
         * Adds the given asterism to the catalogue under construction and returns the builder
         * @param asterism The asterism that will be included in the catalogue under construction
         * @return The builder
         */
        public Builder addAsterism(Asterism asterism){
            asterisms.add(asterism);
            return this;
        }

        /**
         * Allows to obtain a unmodifiable and not immutable view on the asterisms of the catalogue under construction
         * @return The unmodifiable and not immutable view on the asterisms of the catalogue under construction
         */
        public List<Asterism> asterisms() {
            return Collections.unmodifiableList(asterisms);
        }

        /**
         * Adds the stars and/or asterisms from the input stream to the catalogue and returns the builder
         * @param inputStream The input stream containing the stars and/or asterisms
         * @param loader The loader used to process the data from the input stream
         * @return The builder after adding to its content the input stream
         * @throws IOException In case of input or output error, the IOException will be thrown
         */
        public Builder loadFrom(InputStream inputStream, Loader loader) throws IOException {
            loader.load(inputStream, this);
            return this;
        }

        /**
         * Builds the catalogue containing the stars and asterisms added until now to the builder
         * @return The star catalogue containing the stars and asterisms added until now to the builder
         */
        public StarCatalogue build(){
            return new StarCatalogue(stars, asterisms);
        }
    }

    /**
     * The abstract interface representing a star and asterisms loader.
     *
     * @author Nicolas Szwajcok (315213)
     */
    public interface Loader {
        /**
         * Loads the stars and/or asterisms from the input stream to the catalogue under construction by the builder
         * @param inputStream The input stream containing the stars and/or asterisms
         * @param builder The loader used to process the data from the input stream
         * @throws IOException In case of input or output error, the IOException will be thrown
         */
        public abstract void load(InputStream inputStream, Builder builder) throws IOException;
    }
}
