package com.stringrest.controller;

import com.stringrest.Application;
import com.stringrest.domain.StringSet;
import com.stringrest.repository.StringSetRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.util.HashSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class LongestChainControllerTest {

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
    public void testLongestChain() throws Exception {
        mockMvc.perform(get("/longest-chain"))
                .andDo(print());
    }

    void createTestStringSet() {
        HashSet<String> value = Stream.of("foo", "oomph", "hgf")
                .collect(Collectors.toCollection(HashSet<String>::new));
        stringSetRepository.save(new StringSet(value));
        value = Stream.of("hij", "jkl", "jkm", "lmn")
                .collect(Collectors.toCollection(HashSet<String>::new));
        stringSetRepository.save(new StringSet(value));
        value = Stream.of("abc", "cde", "cdf", "fuf", "fgh")
                .collect(Collectors.toCollection(HashSet<String>::new));
        stringSetRepository.save(new StringSet(value));
    }
}