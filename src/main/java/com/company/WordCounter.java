package com.company;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class WordCounter {
    private static List<HashMap<String, Integer>> listOfThreadResults = new ArrayList();

    public HashMap<String,Integer> countWords(String fileName) {
        if (doesFileExist(fileName)) {
//            System.out.println("There is 1 file.");
            HashMap<String, Integer> hashMap = new HashMap();
            Scanner scanner = createScanner(fileName);
            scanner.forEachRemaining((s) -> analizujWiersz(s, hashMap));
            return hashMap;
        }
        else {
//            System.out.println("There are multiple files");
            return countWordsWithThreads(fileName);
        }
    }

    public HashMap<String, Integer> countWordsWithThreads(String fileName) {
        List<Thread> threads = new ArrayList();
        String template = createNameTemplate(fileName);

        int counter = 0;
        while (doesFileExist(String.format(template, counter))) {
            Thread t = new WordCountingThread(String.format(template, counter));
            threads.add(t);
            t.start();
            counter++;
        }

        if (counter == 0) {
            return null;
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

    public static Scanner createScanner(String fileName) {
        if (fileName.startsWith("http")) {
            try {
                URL url = new URL(fileName);
                return new Scanner(url.openStream());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            File file = new File(fileName);
            try {
                return new Scanner(file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private boolean doesFileExist(String fileName) {
        if (fileName.startsWith("http")) {
            try {
                URL url = new URL(fileName);
                url.openStream();
                return true;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;
        }
        else {
            File file = new File(fileName);
            return file.exists();
        }
    }

    private void addWord(String word, Integer count, HashMap<String, Integer> result) {
        Integer myCount = result.get(word);
        myCount = myCount == null ? count : myCount + count;
        result.put(word, myCount);
    }

    public String createNameTemplate(String fileName) {
//        int index = fileName.lastIndexOf(".");
//        return fileName.substring(0, index) + "%d" + fileName.substring(index);
        return fileName + "%d";
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
