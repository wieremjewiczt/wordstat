package com.company;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class WordCounter {
    private static List<HashMap<String, Integer>> listOfThreadResults = new ArrayList();

    public HashMap<String,Integer> countWords(String fileName) {
        File file = new File(fileName);
        if (file.exists()) {
            System.out.println("There is 1 file.");
            try {
                HashMap<String, Integer> hashMap = new HashMap();
                Files.lines( Paths.get(fileName)).forEach((s) -> analizujWiersz(s, hashMap));
                return hashMap;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
        else {
            System.out.println("There are multiple files");
            return countWordsWithThreads(fileName);
        }
    }

    public HashMap<String, Integer> countWordsWithThreads(String fileName) {
        List<Thread> threads = new ArrayList();
        String template = createNameTemplate(fileName);

        int counter = 0;
        File file = new File(String.format(template, counter));
        while (file.exists()) {
            Thread t = new WordCountingThread(String.format(template, counter));
            threads.add(t);
            t.start();
            counter++;
            file = new File(String.format(template, counter));
        }

        for (int i = 0; i < threads.size(); i++ ) {
            try {
                threads.get(i).join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        HashMap<String, Integer> result = listOfThreadResults.get(0);
        for (int i=1; i<listOfThreadResults.size(); i++) {
            listOfThreadResults.get(i).forEach((word, count) -> addWord(word, count, result));
        }

        return result;
    }

    private void addWord(String word, Integer count, HashMap<String, Integer> result) {
        Integer myCount = result.get(word);
        myCount = myCount == null ? count : myCount + count;
        result.put(word, myCount);
    }

    public String createNameTemplate(String fileName) {
        int index = fileName.lastIndexOf(".");
        return fileName.substring(0, index) + "%d" + fileName.substring(index);
    }

    public static void addResult(HashMap<String, Integer> hashMap) {
        listOfThreadResults.add(hashMap);
    }

    public static void analizujWiersz(String s, HashMap<String, Integer> tab) {
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
