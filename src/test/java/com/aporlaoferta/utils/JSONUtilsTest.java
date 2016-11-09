package com.aporlaoferta.utils;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * Created with IntelliJ IDEA.
 * User: hasiermetal
 * Date: 09/11/2016
 * Time: 21:45
 */
public class JSONUtilsTest {

    @Test
    public void testQuotesAndDoubleQuotesAreEscapedWithSpecialCharacters() throws Exception {
        String rawText = "This is \\\"that\\\" because you're 'here'";
        String parsedText = "This is xx#!!#xxthatxx#!!#xx because youxx#!#xxre xx#!#xxherexx#!#xx";
        assertTrue(parsedText.equals(JSONUtils.escapeQuotes(rawText)));
    }

}