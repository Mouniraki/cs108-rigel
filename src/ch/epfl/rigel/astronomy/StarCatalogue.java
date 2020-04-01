package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.Preconditions;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public final class StarCatalogue {
    private final List<Star> stars;
    private final List<Asterism> asterisms;
    private final Map<Asterism, List<Integer>> map = new HashMap<>();


    public StarCatalogue(List<Star> stars, List<Asterism> asterisms){
        for (Asterism asterism : asterisms) {
            Preconditions.checkArgument(stars.contains(asterism));
        }
        this.stars = List.copyOf(stars);
        this.asterisms = List.copyOf(asterisms);

        for(Asterism a : asterisms){
            List<Integer> indexes = new ArrayList<>();
            for(Star s : stars){
                if(a.stars().contains(s))
                    indexes.add(stars.indexOf(s));
            }
            map.put(a, List.copyOf(indexes));
        }
    }

    public List<Star> stars(){
        return stars;
    }

    public Set<Asterism> asterisms(){
        return new HashSet<>(asterisms);
    }

    public List<Integer> asterismIndices(Asterism asterism) {
        Preconditions.checkArgument(asterisms.contains(asterism));
        List<Integer> indices = map.get(asterism);
        return indices;
    }


    public final static class Builder{
        private List<Star> stars;
        private List<Asterism> asterisms;

        public Builder(){
            this.stars = new ArrayList<>();
            this.asterisms = new ArrayList<>();
        }

        public Builder addStar(Star star){
            stars.add(star);
            return this;
        }

        public List<Star> stars(){
            return Collections.unmodifiableList(stars);
        }

        public Builder addAsterism(Asterism asterism){
            asterisms.add(asterism);
            return this;
        }

        public List<Asterism> asterisms(){
            return Collections.unmodifiableList(asterisms);
        }

        //TODO CHECK IF IT IS CORRECT
        public Builder loadFrom(InputStream inputStream, Loader loader) throws IOException {
            loader.load(inputStream, this);
            return this;
        }

        public StarCatalogue build(){
            return new StarCatalogue(stars, asterisms);
        }

    }

    public interface Loader {
        public abstract void load(InputStream inputStream, Builder builder) throws IOException;
    }
}
