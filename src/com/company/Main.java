package com.company;

import javafx.util.Pair;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class Main {
    public static void main(String[] args) {
        HashMap<String, Integer> mapa = new HashMap<String, Integer>();

	// write your code here
        System.out.println("Hello world!");
        System.out.println(wtf());

        File plik = new File("pan_tadeusz.txt");
        System.out.println("Czy istnieje: " + plik.exists());

        String current = null;
        try {
            current = new File( "." ).getCanonicalPath();
            System.out.println("Current dir:"+current);
            String currentDir = System.getProperty("user.dir");
            System.out.println("Current dir using System:" +currentDir);
        } catch (IOException e) {
            e.printStackTrace();
        }



        String fileName = "pan_tadeusz.txt";

        //read file into stream, try-with-resources
        /*try (Stream<String> stream = Files.lines(Paths.get(fileName))) {

            stream.forEach(System.out::println);

        } catch (IOException e) {
            e.printStackTrace();
        }*/

        try {
            Files.lines( Paths.get(fileName)).forEach((s) -> analizujWiersz(s, mapa));
            ArrayList<Map.Entry<String,Integer>> lista = new ArrayList<Map.Entry<String,Integer>>(mapa.entrySet());
            lista.sort((a,b) -> b.getValue() - a.getValue());
            for (int i = 0;i<10;i++) {
                System.out.println(String.format("%d Word: %s was found %d times.", i+1, lista.get(i).getKey(), lista.get(i).getValue()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void analizujWiersz(String s, HashMap<String, Integer> tab) {
        if (s != null && !s.trim().isEmpty()) {
            String[] split = s.toLowerCase().replaceAll("[(,:;.?!)'\"]", " ").replaceAll("\\s\\s+", " ").trim().split("\\s");

            Integer tmp;
            for (int i = 0; i<split.length; i++) {
                tmp = tab.get(split[i]);
                tmp = tmp == null ? 1 : tmp+1;
                tab.put(split[i], tmp);
            }
        }
    }

    public static int wtf() {
        return 4;
    }
}
