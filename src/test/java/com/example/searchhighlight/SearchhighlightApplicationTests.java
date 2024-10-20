package com.example.searchhighlight;

import com.example.searchhighlight.model.SearchRequest;
import com.example.searchhighlight.model.SearchResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class SearchHighlightApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testHighlightText_Found() throws Exception {
        SearchRequest request = new SearchRequest();
        request.setInputText("This is a sample text for search highlighting.");
        request.setQueryText("sample text");

        MvcResult result = mockMvc.perform(post("/api/search")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn();

        SearchResult response = objectMapper.readValue(result.getResponse().getContentAsString(), SearchResult.class);

        assertEquals(46, response.getTotalCharacters());
        assertEquals(8, response.getTotalWords());
        assertEquals(2, response.getTotalOccurrences());
        assertEquals(2, response.getDistinctWordsCount());
        assertEquals(Map.of("sample", 1, "text", 1), response.getOccurrences());
        assertTrue(response.getHighlightedText().contains("<mark>sample</mark>"));
        assertTrue(response.getHighlightedText().contains("<mark>text</mark>"));
    }

    @Test
    void testHighlightText_NotFound() throws Exception {
        SearchRequest request = new SearchRequest();
        request.setInputText("This is a sample text for search highlighting.");
        request.setQueryText("notfound");

        MvcResult result = mockMvc.perform(post("/api/search")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn();

        SearchResult response = objectMapper.readValue(result.getResponse().getContentAsString(), SearchResult.class);

        assertEquals(46, response.getTotalCharacters());
        assertEquals(8, response.getTotalWords());
        assertEquals(0, response.getTotalOccurrences());
        assertEquals(0, response.getDistinctWordsCount());
        assertEquals("No such string found in the text: notfound", response.getHighlightedText());
    }
}
