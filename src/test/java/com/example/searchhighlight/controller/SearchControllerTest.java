package com.example.searchhighlight.controller;

import com.example.searchhighlight.model.SearchRequest;
import com.example.searchhighlight.model.SearchResult;

import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class SearchControllerTest {
    private final SearchController searchController = new SearchController();

    SearchControllerTest() {
    }

    @Test
    void testHighlightText_Found() {
        SearchRequest request = new SearchRequest();
        request.setInputText("This is a sample text for search highlighting.");
        request.setQueryText("sample text");
        SearchResult result = this.searchController.highlightText(request);
        Assertions.assertEquals(46, result.getTotalCharacters());
        Assertions.assertEquals(8, result.getTotalWords());
        Assertions.assertEquals(2, result.getTotalOccurrences());
        Assertions.assertEquals(2, result.getDistinctWordsCount());
        Assertions.assertEquals(Map.of("sample", 1, "text", 1), result.getOccurrences());
        Assertions.assertTrue(result.getHighlightedText().contains("<mark>sample</mark>"));
        Assertions.assertTrue(result.getHighlightedText().contains("<mark>text</mark>"));
    }

    @Test
    void testHighlightText_NotFound() {
        SearchRequest request = new SearchRequest();
        request.setInputText("This is a sample text for search highlighting.");
        request.setQueryText("notfound");
        SearchResult result = this.searchController.highlightText(request);
        Assertions.assertEquals(46, result.getTotalCharacters());
        Assertions.assertEquals(8, result.getTotalWords());
        Assertions.assertEquals(0, result.getTotalOccurrences());
        Assertions.assertEquals(0, result.getDistinctWordsCount());
        Assertions.assertEquals("No such string found in the text: notfound", result.getHighlightedText());
    }

    @Test
    void testHighlightText_EmptyQuery() {
        SearchRequest request = new SearchRequest();
        request.setInputText("This is a sample text for search highlighting.");
        request.setQueryText("");
        SearchResult result = this.searchController.highlightText(request);
        Assertions.assertEquals(46, result.getTotalCharacters());
        Assertions.assertEquals(8, result.getTotalWords());
        Assertions.assertEquals(0, result.getTotalOccurrences());
        Assertions.assertEquals(0, result.getDistinctWordsCount());
        Assertions.assertEquals("No such string found in the text: ", result.getHighlightedText());
    }
}

