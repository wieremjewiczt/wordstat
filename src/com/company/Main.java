package com.company;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Main {
    //test args: pan_tadeusz.txt 7
    //pan_tadeuszX.txt 7
    public static void main(String[] args) throws Exception {
        long startTime = System.currentTimeMillis();
        HashMap<String, Integer> hashMap;
        String fileName;
        int numberOfAnswers;
        WordCounter wordCounter = new WordCounter();

        if (args.length != 2) {
            throw new Exception("Wrong number of arguments! There should be 2 arguments (fileName, numberOfAnswers), but there are " + args.length + " arguments.");
        }

        fileName = args[1];
        try {
            numberOfAnswers = Integer.parseInt(args[0]);
        }
        catch (NumberFormatException ex) {
            ex.printStackTrace();
            throw new Exception("Second argument should be a number!");
        }

        hashMap = wordCounter.countWords(fileName);
        if (hashMap != null) {
            displayAnswers(hashMap, numberOfAnswers);
            long endTime = System.currentTimeMillis();
            System.out.println("Time taken: " + (endTime - startTime));
        }
    }

    private static void displayAnswers(HashMap<String, Integer> hashMap, int numberOfAnswers) {
        ArrayList<Map.Entry<String,Integer>> lista = new ArrayList(hashMap.entrySet());
        lista.sort((a,b) -> b.getValue() - a.getValue());

        for (int i = 0; i<numberOfAnswers && i<lista.size(); i++) {
            System.out.println(String.format("%d Word: %s was found %d times.", i+1, lista.get(i).getKey(), lista.get(i).getValue()));
        }
    }
}
