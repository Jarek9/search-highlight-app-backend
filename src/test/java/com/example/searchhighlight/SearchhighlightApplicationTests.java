package com.example.searchhighlight;

import com.example.searchhighlight.model.SearchRequest;
import com.example.searchhighlight.model.SearchResult;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
class SearchhighlightApplicationTests {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    SearchhighlightApplicationTests() {
    }

    @Test
    void testHighlightText_Found() throws Exception {
        SearchRequest request = new SearchRequest();
        request.setInputText("This is a sample text for search highlighting.");
        request.setQueryText("sample text");
        MvcResult result = this.mockMvc.perform(MockMvcRequestBuilders.post("/api/search", new Object[0]).contentType(MediaType.APPLICATION_JSON).content(this.objectMapper.writeValueAsString(request))).andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
        SearchResult response = (SearchResult) this.objectMapper.readValue(result.getResponse().getContentAsString(), SearchResult.class);
        Assertions.assertEquals(46, response.getTotalCharacters());
        Assertions.assertEquals(8, response.getTotalWords());
        Assertions.assertEquals(2, response.getTotalOccurrences());
        Assertions.assertEquals(2, response.getDistinctWordsCount());
        Assertions.assertEquals(Map.of("sample", 1, "text", 1), response.getOccurrences());
        Assertions.assertTrue(response.getHighlightedText().contains("<mark>sample</mark>"));
        Assertions.assertTrue(response.getHighlightedText().contains("<mark>text</mark>"));
    }

    @Test
    void testHighlightText_NotFound() throws Exception {
        SearchRequest request = new SearchRequest();
        request.setInputText("This is a sample text for search highlighting.");
        request.setQueryText("notfound");
        MvcResult result = this.mockMvc.perform(MockMvcRequestBuilders.post("/api/search", new Object[0]).contentType(MediaType.APPLICATION_JSON).content(this.objectMapper.writeValueAsString(request))).andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
        SearchResult response = (SearchResult) this.objectMapper.readValue(result.getResponse().getContentAsString(), SearchResult.class);
        Assertions.assertEquals(46, response.getTotalCharacters());
        Assertions.assertEquals(8, response.getTotalWords());
        Assertions.assertEquals(0, response.getTotalOccurrences());
        Assertions.assertEquals(0, response.getDistinctWordsCount());
        Assertions.assertEquals("No such string found in the text: notfound", response.getHighlightedText());
    }
}

