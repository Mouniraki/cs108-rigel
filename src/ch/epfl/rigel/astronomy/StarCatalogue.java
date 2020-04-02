package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.Preconditions;

import java.io.*;
import java.util.*;

public final class StarCatalogue {
    final private List<Star> stars;
    final private List<Asterism> asterisms;
    private final Map<Asterism, List<Integer>> map = new HashMap<>();

    public StarCatalogue(List<Star> stars, List<Asterism> asterisms) {

        for (Asterism asterism : asterisms) {
            for (int b = 0; b < asterism.stars().size(); ++b) {
                Preconditions.checkArgument(stars.contains(asterism.stars().get(b)));
            }
        }

        for(Asterism a : asterisms){
            List<Integer> indexes = new ArrayList<>();
            for(Star s : stars){
                if(a.stars().contains(s))
                    indexes.add(stars.indexOf(s));
            }
            map.put(a, List.copyOf(indexes));
        }

        this.stars = List.copyOf(stars);
        this.asterisms = List.copyOf(asterisms);
    }

//        InputStream is = this.getClass().getResourceAsStream("/asterisms.txt");
//        Reader isReader = new InputStreamReader(is, StandardCharsets.UTF_8);
//        int asterismIndex = 0;
//        String str = "";
//        BufferedReader reader = new BufferedReader(isReader);
//        Character readerObj;
//        boolean loopControler = true;
//        while (loopControler) {
//            try {
//                readerObj = (char) reader.read();
//                if (readerObj == Character.MAX_VALUE) {
//                    loopControler = false;
//                }
//
//                if (readerObj == ',' || readerObj == '\n') {
//                    map.put(Integer.parseInt(str), asterismIndex);
//                    System.out.println(Integer.parseInt(str) + " : " + asterismIndex);
//                    str = "";
//                    asterismIndex += 1;
//                } else {
//                    str += readerObj;
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }

    public List<Star> stars(){
        return stars;
    }

    public Set<Asterism> asterisms(){ //TODO to check
        return (Set<Asterism>) asterisms;
    }

    public List<Integer> asterismIndices(Asterism asterism){//in test check the lengths of two arrays
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

        public List<Star> stars(){ //TODO Verify non modifiable pas immuable|it's ok as intended
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
