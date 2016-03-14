package com.stringrest.controller;

import com.stringrest.Application;
import com.stringrest.domain.StringSet;
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

import java.nio.charset.Charset;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class StringSetControllerTest {

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
    public void testCreateStringSet() throws Exception {
        mockMvc.perform(post("/stringsets")
                .contentType(contentType)
                .content("{\"value\": [\"1234567\",\"cdefghi\"]}"))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(this.stringSetRepository.findOne(4).getId())))
                .andExpect(jsonPath("$.value", is(toList(stringSetRepository.findOne(4).getValue()))));
    }

    @Test
    public void testDeleteStringSet() throws Exception {
        mockMvc.perform(delete("/stringsets/3"))
                .andDo(print())
                .andExpect(status().isOk());
        assertNull(stringSetRepository.findOne(3));
    }

    @Test
    public void testDeleteStringSetByValue() throws Exception {
        mockMvc.perform(delete("/stringsets/query").param("q", "cdefghi"))
                .andDo(print())
                .andExpect(status().isOk());
        assertNull(stringSetRepository.findOne(1));
        assertNotNull(stringSetRepository.findOne(2));
        assertNull(stringSetRepository.findOne(3));
    }

    @Test
    public void testGetAllStringSets() throws Exception {
        mockMvc.perform(get("/stringsets"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].id", is(this.stringSetRepository.findOne(1).getId())))
                .andExpect(jsonPath("$[0].value", is(toList(stringSetRepository.findOne(1).getValue()))))
                .andExpect(jsonPath("$[1].id", is(this.stringSetRepository.findOne(2).getId())))
                .andExpect(jsonPath("$[1].value", is(toList(stringSetRepository.findOne(2).getValue()))))
                .andExpect(jsonPath("$[2].id", is(this.stringSetRepository.findOne(3).getId())))
                .andExpect(jsonPath("$[2].value", is(toList(stringSetRepository.findOne(3).getValue()))));
    }

    @Test
    public void testGetStringSet() throws Exception {
        mockMvc.perform(get("/stringsets/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(this.stringSetRepository.findOne(1).getId())))
                .andExpect(jsonPath("$.value", is(toList(stringSetRepository.findOne(1).getValue()))));
    }

    @Test
    public void testIntersection() throws Exception {
        mockMvc.perform(post("/stringsets/intersection").param("firstId", "1").param("secondId", "2"))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(this.stringSetRepository.findOne(4).getId())))
                .andExpect(jsonPath("$.value", is(toList(stringSetRepository.findOne(4).getValue()))));
    }

    void createTestStringSet() {
        HashSet<String> value = Stream.of("1234567", "cdefghi", "hgf")
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