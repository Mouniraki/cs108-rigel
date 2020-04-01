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
        /*
        InputStream is = this.getClass().getResourceAsStream("/asterisms.txt");
        Reader isReader = new InputStreamReader(is, StandardCharsets.UTF_8);


        String toAppend = "";
        int asterismIndex = 0;
        StringBuffer sb = new StringBuffer();
        String str = "";
        BufferedReader reader = new BufferedReader(isReader);
        System.out.println("SHDHSHHDHDSHDSNJDSN");


//        while((str = reader.readLine())!= null){
//            sb.append(str);
//        }
//        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("txtPath"),"ISO-8859-1"));
//        for(int i = 0; i < 15; ++i){
//            try {
//                Character readerObj = (char) reader.read();
//                if(readerObj == ',') {
//                    map.put(Integer.parseInt(str), asterismIndex);
//                    str = "";
//                    asterismIndex += 1;
//                }
//                else{
//                    str.concat(readerObj.toString());
//                }
//                System.out.println(str);
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            System.out.println(str);

//            for(int in = 0; in < map.size(); ++in){
//                System.out.println(map.get(in));
//            }
//            if(){
//
//            }
//            if(reader == ","){
//                asterismIndex += 1;
//            }
//            int value = 10;
//            value += reader;
//            toAppend.concat(isReader.read());
//            int data = isReader.read();

//            if(isReader.read() == -1){
//
//            }

//            map.put(isReader, );
//            map.put(asterisms.get(i), isReader.read());
//        }

//        for(int i = 0; i < asterisms.size(); ++i){
//            map.put(asterisms.get(i), );
////            map.put(asterisms.get(i), isReader.read());
//            isReader.
//        }

*/

    public List<Star> stars(){
        return stars;
    }

    public Set<Asterism> asterisms(){
        return new HashSet<>(asterisms);
    }
//output index in the catalogue
    public List<Integer> asterismIndices(Asterism asterism) {
        Preconditions.checkArgument(asterisms.contains(asterism));
        List<Integer> indices = map.get(asterism);
        return indices;
    }

        //in test check the lengths of two arrays
        //List<Star> asterismStars = asterism.stars();




//        System.out.println(is);
//        System.out.println(is.readNBytes(10));
        //return null;
// output
//        InputStream imkdds = new FileInputStream("/resources/asterisms.txt");
//
//        InputStream imkfdsdds = new FileInputStream("resources//asterisms.txt");
//
//        InputStream i = new FileInputStream("/home/nicolas/IdeaProjects/Rigel/resources/asterisms.txt");
////        InputStream iss =  ObjectInputFilter.Config.class.getResourceAsStream("dfsfdfsdf");
//
////        InputStream i = new FileInputStream("./home/nicolas/IdeaProjects/Rigel/resources/asterisms.txt");
////        Reader i = new InputStreamReader(
////                new FileInputStream("resources/asterisms.txt"));
////        File file = new File("C:/Java2blog.txt");
//        File file = new File("resources/asterisms.txt");
//        Reader io = new InputStreamReader(
//                new FileInputStream("resources/asterisms.txt"),
//                StandardCharsets.UTF_8);
//        Reader iof = new InputStreamReader(
//                new FileInputStream("resources/asterisms.txt"),
//                StandardCharsets.UTF_8);



    //}


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