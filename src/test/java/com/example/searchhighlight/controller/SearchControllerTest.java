package com.example.searchhighlight.controller;

import com.example.searchhighlight.model.SearchRequest;
import com.example.searchhighlight.model.SearchResult;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class SearchControllerTest {

    private final SearchController searchController = new SearchController();

    @Test
    void testHighlightText_Found() {
        SearchRequest request = new SearchRequest();
        request.setInputText("This is a sample text for search highlighting.");
        request.setQueryText("sample text");

        SearchResult result = searchController.highlightText(request);

        assertEquals(46, result.getTotalCharacters());
        assertEquals(8, result.getTotalWords());
        assertEquals(2, result.getTotalOccurrences());
        assertEquals(2, result.getDistinctWordsCount());
        assertEquals(Map.of("sample", 1, "text", 1), result.getOccurrences());
        assertTrue(result.getHighlightedText().contains("<mark>sample</mark>"));
        assertTrue(result.getHighlightedText().contains("<mark>text</mark>"));
    }

    @Test
    void testHighlightText_NotFound() {
        SearchRequest request = new SearchRequest();
        request.setInputText("This is a sample text for search highlighting.");
        request.setQueryText("notfound");

        SearchResult result = searchController.highlightText(request);

        assertEquals(46, result.getTotalCharacters());
        assertEquals(8, result.getTotalWords());
        assertEquals(0, result.getTotalOccurrences());
        assertEquals(0, result.getDistinctWordsCount());
        assertEquals("No such string found in the text: notfound", result.getHighlightedText());
    }

    @Test
    void testHighlightText_EmptyQuery() {
        SearchRequest request = new SearchRequest();
        request.setInputText("This is a sample text for search highlighting.");
        request.setQueryText("");

        SearchResult result = searchController.highlightText(request);

        assertEquals(46, result.getTotalCharacters());
        assertEquals(8, result.getTotalWords());
        assertEquals(0, result.getTotalOccurrences());
        assertEquals(0, result.getDistinctWordsCount());
        assertEquals("No such string found in the text: ", result.getHighlightedText());
    }
}
