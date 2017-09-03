package com.company;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;

class WordCountingThread extends Thread{
    private Thread t;
    private String fileName;

    WordCountingThread( String fileName) {
        this.fileName = fileName;
    }

    public void run() {
        HashMap<String, Integer> hashMap = new HashMap();

        try {
            Files.lines( Paths.get(fileName)).forEach((s) -> ThreadedWordCounter.analizujWiersz(s, hashMap));
        } catch (IOException e) {
            e.printStackTrace();
        }

        ThreadedWordCounter.addResult(hashMap);
        System.out.println("Thread working with file " +  fileName + " exiting.");
    }
}