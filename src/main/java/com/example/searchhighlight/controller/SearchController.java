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
        String[] words = inputText.split("\\s+");
        String[] queryWords = queryText.split("\\s+");
        int totalCharacters = inputText.length();
        int totalWords = words.length;
        int totalOccurrences = 0;
        Map<String, Integer> occurrences = new HashMap<>();
        Map<String, Integer> firstIndex = new HashMap<>();
        Map<String, Integer> lastIndex = new HashMap<>();
        StringBuilder regexBuilder = new StringBuilder();

        for (String queryWord : queryWords) {
            if (!queryWord.isEmpty()) {
                if (regexBuilder.length() > 0) {
                    regexBuilder.append("|");
                }
                regexBuilder.append(Pattern.quote(queryWord));
            }
        }
        Pattern pattern = Pattern.compile(regexBuilder.toString(), Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputText);
        while (matcher.find()) {
            totalOccurrences++;
            String matchedWord = matcher.group().toLowerCase();
            occurrences.put(matchedWord, occurrences.getOrDefault(matchedWord, 0) + 1);
            if (!firstIndex.containsKey(matchedWord)) {
                firstIndex.put(matchedWord, matcher.start());
            }
            lastIndex.put(matchedWord, matcher.start());
        }
        matcher.reset();
        String highlightedText = matcher.replaceAll("<mark>$0</mark>");
        int distinctWordsCount = occurrences.size();
        if (queryText.isEmpty() || highlightedText.equals(inputText)) {
            totalOccurrences = 0;
            distinctWordsCount = 0;
            highlightedText = "No such string found in the text: " + queryText;
        }
        long endTime = System.currentTimeMillis();
        double processingTimeInSeconds = (double) (endTime - startTime) / 1000.0;
        return new SearchResult(
                highlightedText,
                totalCharacters,
                totalWords,
                totalOccurrences,
                distinctWordsCount,
                occurrences,
                firstIndex,
                lastIndex,
                processingTimeInSeconds
        );
    }
}


