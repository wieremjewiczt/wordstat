package com.company;

import java.util.HashMap;
import java.util.Scanner;

class WordCountingThread extends Thread{
    private String fileName;

    WordCountingThread( String fileName) {
        this.fileName = fileName;
    }

    public void run() {
        HashMap<String, Integer> hashMap = new HashMap();
        Scanner scanner = WordCounter.createScanner(fileName);
        scanner.forEachRemaining((s) -> WordCounter.analizujWiersz(s, hashMap));
        WordCounter.addResult(hashMap);
        System.out.println("Thread working with file " +  fileName + " exiting.");
    }
}