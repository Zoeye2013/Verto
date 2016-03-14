package com.stringrest.controller;

import com.stringrest.repository.StringSetRepository;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@RestController
public class LongestChainController {
    @Autowired
    private StringSetRepository stringSetRepository;

    @RequestMapping(value = "/longest-chain", method = RequestMethod.GET)
    @ApiOperation(value = "(not implemented) Find the longest chain of strings from the previously uploaded sets;",
            notes = "Every next string starts with the same character as the previous one ends with;" +
                    "Every next string belongs to the same set as previous one, except one jump to another set is allowed;" +
                    "Specific string from specific set may be used only once")
    public ResponseEntity<?> longestChain() {
        Map<Integer, Set<String>> stringSetCollections = new HashMap<>();
        stringSetRepository.streamAll()
                .forEach(stringSet -> stringSetCollections.put(stringSet.getId(), stringSet.getValue()));

        return new ResponseEntity<>(stringSetCollections,HttpStatus.OK);
    }
}
