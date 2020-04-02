package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.Preconditions;

import java.io.*;
import java.util.*;

public final class StarCatalogue {
    final private List<Star> stars;
    final private List<Asterism> asterisms;
    final private Map<Asterism, List<Integer>> map = new HashMap<>();

    public StarCatalogue(List<Star> stars, List<Asterism> asterisms) {

        for (Asterism asterism : asterisms) {
            for (int i = 0; i < asterism.stars().size(); ++i) {
                Preconditions.checkArgument(stars.contains(asterism.stars().get(i)));
            }
        }

        for(Asterism a : asterisms) {
            List<Integer> indexes = new ArrayList<>();
            for(Star s : a.stars()) {
                if(a.stars().contains(s)) {
                    indexes.add(stars.indexOf(s));
                }
            }
            map.put(a, List.copyOf(indexes));
        }

        this.stars = List.copyOf(stars);
        this.asterisms = List.copyOf(asterisms);
    }

    public List<Star> stars(){
        return stars;
    }

    public Set<Asterism> asterisms(){
        return new HashSet<>(asterisms);
    }

    public List<Integer> asterismIndices(Asterism asterism){
        Preconditions.checkArgument(asterisms.contains(asterism));
        return map.get(asterism);
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
