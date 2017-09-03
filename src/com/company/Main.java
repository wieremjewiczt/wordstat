package com.company;

import javafx.util.Pair;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class Main {
    //test args: pan_tadeusz.txt 7
    public static void main(String[] args) throws Exception {
        long startTime = System.currentTimeMillis();
        HashMap<String, Integer> hashMap = new HashMap();
        String fileName;
        int numberOfAnswers;

        if (args.length != 2) {
            throw new Exception("Wrong number of arguments! There should be 2 arguments (fileName, numberOfAnswers), but there are " + args.length + " arguments.");
        }

        fileName = args[0];
        try {
            numberOfAnswers = Integer.parseInt(args[1]);
        }
        catch (NumberFormatException ex) {
            throw new Exception("Second argument should be a number!");
        }

        File plik = new File(fileName);
        if (plik.exists()) {
            //1 file solution
            System.out.println("Going with single file.");
            try {
                HashMap<String, Integer> finalHashMap = hashMap;
                Files.lines( Paths.get(fileName)).forEach((s) -> analizujWiersz(s, finalHashMap));
                hashMap = finalHashMap;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            //multi-therad solution
            System.out.println("Going with multiple files.");
            ThreadedWordCounter counter = new ThreadedWordCounter();
            hashMap = counter.CountWords(fileName);
        }

        ArrayList<Map.Entry<String,Integer>> lista = new ArrayList(hashMap.entrySet());
        lista.sort((a,b) -> b.getValue() - a.getValue());
        for (int i = 0;i<numberOfAnswers;i++) {
            System.out.println(String.format("%d Word: %s was found %d times.", i+1, lista.get(i).getKey(), lista.get(i).getValue()));
        }

        long endTime = System.currentTimeMillis();
        System.out.println("Time taken: " + (endTime - startTime));
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
}
