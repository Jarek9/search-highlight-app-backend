package com.example.searchhighlight.controller;

import com.example.searchhighlight.model.SearchRequest;
import com.example.searchhighlight.model.SearchResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequestMapping({"/api/search"})
@CrossOrigin(origins = {"http://localhost:4200"})
public class SearchController {
    public SearchController() {
    }

    @PostMapping
    public SearchResult highlightText(@RequestBody SearchRequest searchRequest) {
        long startTime = System.currentTimeMillis();
        String inputText = searchRequest.getInputText();
        String queryText = searchRequest.getQueryText();
        int totalCharacters = inputText.length();
        String[] words = inputText.split("\\s+");
        int totalWords = words.length;
        String[] queryWords = queryText.split("\\s+");
        int totalOccurrences = 0;
        Map<String, Integer> occurrences = new HashMap();
        Map<String, Integer> firstIndex = new HashMap();
        Map<String, Integer> lastIndex = new HashMap();
        StringBuilder regexBuilder = new StringBuilder();
        String[] var15 = queryWords;
        int var16 = queryWords.length;

        String normalizedWord;
        for (int var17 = 0; var17 < var16; ++var17) {
            normalizedWord = var15[var17];
            if (!normalizedWord.isEmpty()) {
                if (regexBuilder.length() > 0) {
                    regexBuilder.append("|");
                }

                regexBuilder.append(Pattern.quote(normalizedWord));
            }
        }

        Pattern pattern = Pattern.compile(regexBuilder.toString(), 2);

        Matcher matcher;
        String highlightedText;
        for (matcher = pattern.matcher(inputText); matcher.find(); lastIndex.put(normalizedWord, matcher.start())) {
            ++totalOccurrences;
            highlightedText = matcher.group();
            normalizedWord = highlightedText.toLowerCase();
            occurrences.put(normalizedWord, (Integer) occurrences.getOrDefault(normalizedWord, 0) + 1);
            if (!firstIndex.containsKey(normalizedWord)) {
                firstIndex.put(normalizedWord, matcher.start());
            }
        }

        matcher.reset();
        highlightedText = matcher.replaceAll("<mark>$0</mark>");
        int distinctWordsCount = occurrences.size();
        if (queryText.isEmpty()) {
            totalOccurrences = 0;
            distinctWordsCount = 0;
            highlightedText = "No such string found in the text: " + queryText;
        } else if (highlightedText.equals(inputText)) {
            highlightedText = "No such string found in the text: " + queryText;
        }

        long endTime = System.currentTimeMillis();
        double processingTimeInSeconds = (double) (endTime - startTime) / 1000.0;
        return new SearchResult(highlightedText, totalCharacters, totalWords, totalOccurrences, distinctWordsCount, occurrences, firstIndex, lastIndex, processingTimeInSeconds);
    }
}

