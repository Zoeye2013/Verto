package com.stringrest.controller;

import com.stringrest.Application;
import com.stringrest.domain.StringSet;
import com.stringrest.dto.StringStats;
import com.stringrest.repository.StringSetRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class StatsControllerTest {

    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));

    private MockMvc mockMvc;

    @Autowired
    private StringSetRepository stringSetRepository;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Before
    public void setUp() throws Exception {
        mockMvc = webAppContextSetup(webApplicationContext).build();
        stringSetRepository.deleteAllInBatch();
        createTestStringSet();
    }

    @Test
    public void testStringStatsById() throws Exception {
        StringStats stringStats = new StringStats(stringSetRepository.findOne(1), 3, 7, 3,
                BigDecimal.valueOf(4.34), BigDecimal.valueOf(3.0));
        mockMvc.perform(get("/stats/summary/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.stringSet['value']", is(toList(stringStats.getValue()))))
                .andExpect(jsonPath("$.numberOfString", is(stringStats.getNumberOfString())))
                .andExpect(jsonPath("$.longestStringLength", is(stringStats.getLongestStringLength())))
                .andExpect(jsonPath("$.shortestStringLength", is(stringStats.getShortestStringLength())))
                .andExpect(jsonPath("$.averageLength").value(stringStats.getAverageLength().doubleValue()))
                .andExpect(jsonPath("$.medianLength").value(stringStats.getMedianLength().doubleValue()));

        stringStats = new StringStats(stringSetRepository.findOne(3), 1, 7, 7,
                BigDecimal.valueOf(7.0), BigDecimal.valueOf(7.0));
        mockMvc.perform(get("/stats/summary/3"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.stringSet['value']", is(toList(stringStats.getValue()))))
                .andExpect(jsonPath("$.numberOfString", is(stringStats.getNumberOfString())))
                .andExpect(jsonPath("$.longestStringLength", is(stringStats.getLongestStringLength())))
                .andExpect(jsonPath("$.shortestStringLength", is(stringStats.getShortestStringLength())))
                .andExpect(jsonPath("$.averageLength").value(stringStats.getAverageLength().doubleValue()))
                .andExpect(jsonPath("$.medianLength").value(stringStats.getMedianLength().doubleValue()));
    }

    @Test
    public void testLongestString() throws Exception {
        mockMvc.perform(get("/stats/longest-string"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("[\"1234567\",\"cdefghi\"]"));
    }

    @Test
    public void testMostCommonString() throws Exception {
        mockMvc.perform(get("/stats/most-common"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("[\"cdefghi\",\"hgf\"]"));
    }

    @Test
    public void testExactlyIn() throws Exception {
        mockMvc.perform(get("/stats/exactly-in").param("occurrence", "1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("[\"1234567\",\"abc\"]"));
        mockMvc.perform(get("/stats/exactly-in").param("occurrence", "2"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("[\"cdefghi\",\"hgf\"]"));
        mockMvc.perform(get("/stats/exactly-in").param("occurrence", "abc"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string("[\"Please type a number.\"]"));
    }

    void createTestStringSet() {
        HashSet<String> value = Stream.of("abc", "cdefghi", "hgf")
                .collect(Collectors.toCollection(HashSet<String>::new));
        stringSetRepository.save(new StringSet(value));
        value = Stream.of("1234567", "hgf")
                .collect(Collectors.toCollection(HashSet<String>::new));
        stringSetRepository.save(new StringSet(value));
        value = Stream.of("cdefghi")
                .collect(Collectors.toCollection(HashSet<String>::new));
        stringSetRepository.save(new StringSet(value));
    }

    List<String> toList(Set<String> value) {
        return value.stream().collect(Collectors.toList());
    }

}