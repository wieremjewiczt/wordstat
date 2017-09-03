package com.company;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ThreadedWordCounter {
    private static List<HashMap<String, Integer>> listOfThreadResults = new ArrayList();

    public HashMap<String, Integer> CountWords(String fileName) {
        List<Thread> threads = new ArrayList();
        String template = CreateNameTemplate(fileName);

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

    private String CreateNameTemplate(String fileName) {
        String[] splitName = fileName.split("\\.");
        String nameTemplate = "";
        if (splitName.length == 2) {
            nameTemplate = splitName[0] + "%d." + splitName[1];
        }
        else {
            nameTemplate = splitName[0];
            for (int i=1;i<splitName.length-1;i++)
                nameTemplate = String.format("%s.%s", nameTemplate, splitName[i]);
            nameTemplate = nameTemplate + "%d." + splitName[splitName.length-1];
        }

        return nameTemplate;
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
