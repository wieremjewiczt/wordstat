package com.company.tests

import com.company.WordCounter
import org.testng.Assert
import org.testng.annotations.Test

class WordCounterTest {
    @Test
    void testCreateNameTemplate() {
        WordCounter wordCounter = new WordCounter();

        Assert.assertEquals(wordCounter.createNameTemplate("test.txt"), "test%d.txt");
        Assert.assertEquals(wordCounter.createNameTemplate("t-test.test.test.txt"), "t-test.test.test%d.txt");
        Assert.assertEquals(wordCounter.createNameTemplate("http://test:5000/test.txt"), "http://test:5000/test%d.txt");
    }
}
