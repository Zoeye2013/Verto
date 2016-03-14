package com.stringrest.controller;

import com.stringrest.domain.StringSet;
import com.stringrest.repository.StringSetRepository;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/stringsets")
public class StringSetController {


    private final StringSetRepository stringSetRepository;

    @RequestMapping(method = RequestMethod.POST)
    @ApiOperation(value = "Upload a set of strings",
            notes = "The newly created string set Id will be sent in the location response header",
            response = StringSet.class)
    public ResponseEntity<?> createStringSet(@RequestBody StringSet stringSet) {
        stringSet = stringSetRepository.save(stringSet);
        setLocationHeader(stringSet);
        return new ResponseEntity<>(stringSet, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/{setId}", method = RequestMethod.DELETE)
    @ApiOperation(value = "Delete the specified set of strings from the server by string set Id")
    public ResponseEntity<?> deleteStringSet(@PathVariable int setId) {
        stringSetRepository.delete(setId);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @RequestMapping(value = "/query", method = RequestMethod.DELETE)
    @ApiOperation(value = "Delete the specified set of strings from the server by given string")
    public ResponseEntity<?> deleteStringSetByValue(@RequestParam(value = "q") String query) {
        valueInQuery(query).forEach(element -> stringSetRepository.delete(element.getId()));
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @RequestMapping(method = RequestMethod.GET)
    @ApiOperation(value = "(not in requirement) find all the sets", response = StringSet.class)
    public ResponseEntity<Iterable<StringSet>> getAllStringSets() {
        return new ResponseEntity<>(stringSetRepository.findAll(), HttpStatus.OK);
    }

    @RequestMapping(value = "/{setId}", method = RequestMethod.GET)
    @ApiOperation(value = "(not in requirement) find the sets by sting set id", response = StringSet.class)
    public ResponseEntity<?> getStringSet(@PathVariable int setId) {
        StringSet s = stringSetRepository.findOne(setId);
        return new ResponseEntity<>(s, HttpStatus.OK);
    }

    @RequestMapping(value = "/query", method = RequestMethod.GET)
    @ApiOperation(value = "Find all the sets containing given string", response = StringSet.class)
    public ResponseEntity<?> findValueInQuery(@RequestParam(value = "q") String query) {
        final List<StringSet> result = valueInQuery(query);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @RequestMapping(value = "/intersection", method = RequestMethod.POST)
    @ApiOperation(value = "Create a new set of strings which is an intersection of two previously uploaded sets",
            response = StringSet.class)
    public ResponseEntity<?> intersection(@RequestParam(value = "firstId") int firstId,
                                          @RequestParam(value = "secondId") int secondId) {
        Set<String> firstSringValues = stringSetRepository.findOne(firstId).getValue();
        Set<String> secondStringValues = stringSetRepository.findOne(secondId).getValue();
        Set<String> interStringValues = firstSringValues.stream().filter(secondStringValues::contains).collect(Collectors.toSet());
        StringSet intersection = new StringSet((HashSet<String>) interStringValues);
        stringSetRepository.save(intersection);
        setLocationHeader(intersection);
        return new ResponseEntity<Object>(intersection, HttpStatus.CREATED);
    }


    private List<StringSet> valueInQuery(String query) {
        return stringSetRepository.streamAll()
                .filter(stringSet -> stringSet.getValue().contains(query))
                .collect(Collectors.toList());
    }

    // Set the location header for the newly created resource
    private void setLocationHeader(StringSet stringSet) {
        HttpHeaders responseHeaders = new HttpHeaders();
        URI newStringSetUri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{setId}")
                .buildAndExpand(stringSet.getId())
                .toUri();
        responseHeaders.setLocation(newStringSetUri);
    }

    @Autowired
    StringSetController(StringSetRepository stringSetRepository) {
        this.stringSetRepository = stringSetRepository;
    }

}
