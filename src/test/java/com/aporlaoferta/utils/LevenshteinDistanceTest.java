package com.aporlaoferta.utils;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created with IntelliJ IDEA.
 * User: hasiermetal
 * Date: 18/02/16
 * Time: 22:56
 */
public class LevenshteinDistanceTest {

    @Test
    public void testThresholdIsReached() throws Exception {
        String name = "amazon";
        String typoName = "namazoa";
        Assert.assertTrue("Expected true match because of a typo",
                LevenshteinDistance.process(typoName, name, 3));
    }

    @Test
    public void testErrorIsFarFromThreshold() throws Exception {
        String name = "amazon";
        String typoName = "xamuzan";
        Assert.assertFalse("Expected false match because threshold is smaller than distance",
                LevenshteinDistance.process(typoName, name, 3));
    }

    @Test
    public void testMatchWhenWordIsPartOfTargetByTwoLetters() throws Exception {
        String name = "Amazon";
        String typoName = "amazon es";
        Assert.assertTrue("Expected true match because it is part of the word",
                LevenshteinDistance.process(typoName, name, -1));
    }

    @Test
    public void testMatchWhenTargetIsPartOfWordByTwoLetters() throws Exception {
        String name = "Amazon ES";
        String typoName = "amazon";
        Assert.assertTrue("Expected true match because it is part of the word",
                LevenshteinDistance.process(typoName, name, -1));
    }
    @Test
    public void testNotMatchWhenWordIsPartOfTargetButThereAreMoreThanTwoLetters() throws Exception {
        String name = "Amazon";
        String typoName = "amazon españa";
        Assert.assertFalse("Expected false match because although it is part of the word there is a big length difference",
                LevenshteinDistance.process(typoName, name, -1));
    }

    @Test
    public void testNotMatchWhenTargetIsPartOfWordButThereAreMoreThanTwoLetters() throws Exception {
        String name = "Amazon España";
        String typoName = "amazon";
        Assert.assertFalse("Expected false match because although it is part of the word there is a big length difference",
                LevenshteinDistance.process(typoName, name, -1));
    }

    @Test
    public void testMatchWithShortWordIsIgnored() throws Exception {
        String name = "Amazon jpain";
        String typoName = "am";
        Assert.assertFalse("Expected false match as word is too short",
                LevenshteinDistance.process(typoName, name, 3));

    }
}
