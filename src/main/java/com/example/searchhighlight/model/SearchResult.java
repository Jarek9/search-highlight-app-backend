package com.example.searchhighlight.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

@Data
@AllArgsConstructor
public class SearchResult {
    private String highlightedText;
    private int totalCharacters;
    private int totalWords;
    private int totalOccurrences;
    private int distinctWordsCount;
    private Map<String, Integer> occurrences;
    private Map<String, Integer> firstIndex;
    private Map<String, Integer> lastIndex;
    private double processingTimeInSeconds;
}
