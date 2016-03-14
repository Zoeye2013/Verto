package com.stringrest.controller;

import com.google.common.collect.Iterables;
import com.stringrest.dto.StringStats;
import com.stringrest.repository.StringSetRepository;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/stats", method = RequestMethod.GET)
public class StatsController {

    @Autowired
    private StringSetRepository stringSetRepository;

    @RequestMapping(value = "/summary/{setId}", method = RequestMethod.GET)
    @ApiOperation(value = "Return the statistics for one of the previously uploaded sets",
            notes = "number of strings, length of the shortest string, length of the longest string, average length, median length",
            response = StringStats.class)
    public ResponseEntity<?> stringStatsById(@PathVariable int setId) {
        StringStats result = new StringStats(stringSetRepository.findOne(setId));
        List<Integer> valueList = result.getValue().stream()
                .sorted(byStringLengthAndAlphabet())
                .map(String::length)
                .collect(Collectors.toList());

        // Calculate number of strings
        int numberOfString = valueList.size();
        result.setNumberOfString(numberOfString);

        // Calculate average length
        int totalLengthOfString = valueList.stream().mapToInt(Integer::intValue).sum();
        BigDecimal averageLength = BigDecimal.valueOf(totalLengthOfString)
                .divide(BigDecimal.valueOf(numberOfString), 2, RoundingMode.CEILING);
        result.setAverageLength(averageLength);

        // Calculate the longest and shortest string
        result.setShortestStringLength(Iterables.getLast(valueList, null));
        result.setLongestStringLength(Iterables.getFirst(valueList, null));

        // Calculate the median
        int middle = Iterables.get(valueList, numberOfString / 2);
        int secondMiddle = middle;
        if (valueList.size() > 1) {
            secondMiddle = Iterables.get(valueList, numberOfString / 2 - 1);
        }
        BigDecimal medianLength = isEven(numberOfString) ?
                BigDecimal.valueOf((middle + secondMiddle)).divide(BigDecimal.valueOf(2.0),2, RoundingMode.CEILING) :
                BigDecimal.valueOf(middle).divide(BigDecimal.valueOf(1.0),2, RoundingMode.CEILING);
        result.setMedianLength(medianLength);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @RequestMapping(value = "/longest-string", method = RequestMethod.GET)
    @ApiOperation(value = "Find the longest string in all the sets",
            notes = " if there are a few such strings, return them all, sorted alphabetically")
    public ResponseEntity<?> longestString() {
        // filter the longest strings
        List<String> result = stringSetList().stream()
                .distinct()
                .filter(s -> s.length() >= stringSetList().get(0).length())
                .collect(Collectors.toList());
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @RequestMapping(value = "/most-common", method = RequestMethod.GET)
    @ApiOperation(value = "Find the string used in the most sets",
            notes = " if there are a few such strings, return them all, sorted alphabetically")
    public ResponseEntity<?> mostCommonString() {
        Map<Long, ArrayList<String>> frequency = frequencySummary();
        List<String> result = frequency.get(Collections.max(frequency.keySet()));
        // Sorted by alphabetically
        Collections.sort(result);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @RequestMapping(value = "/exactly-in", method = RequestMethod.GET)
    @ApiOperation(value = "Find the string which is present in exactly given number of sets",
            notes = " if there are a few such strings, return them all, sorted alphabetically")
    public ResponseEntity<?> exactlyIn(@RequestParam(value = "occurrence") String input) {
        Map<Long, ArrayList<String>> frequency = frequencySummary();
        List<String> result = new ArrayList<>();
        try {
            Long occurrence = Long.parseLong(input, 10);
            if (frequency.containsKey(occurrence)) {
                result = frequency.get(occurrence);
                // Sorted by alphabetically
                Collections.sort(result);
            }
        } catch (NumberFormatException ex) {
            result.add("Please type a number.");
            return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    static Boolean isEven(int number) {
        return (number % 2 == 0) ? true : false;
    }

    static Comparator<String> byStringLengthAndAlphabet() {
        return Comparator.comparing(java.lang.String::length).reversed().thenComparing(String::compareTo);
    }

    List<String> stringSetList() {
        // put all the strings into one list, sorted by string length and alphabetically
        return stringSetRepository.streamAll()
                .map(stringSet -> stringSet.getValue())
                .flatMap(strings -> strings.stream())
                .sorted(byStringLengthAndAlphabet())
                .collect(Collectors.toList());
    }

    Map<Long, ArrayList<String>> frequencySummary() {
        // calculate the frequency of string
        Map<String, Long> frequencyDetail = stringSetList().parallelStream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        // filter the string used in the most set
        return frequencyDetail.entrySet().stream()
                .collect(Collectors.groupingBy(Map.Entry<String, Long>::getValue,
                        Collectors.mapping(Map.Entry<String, Long>::getKey, Collectors.toCollection(ArrayList::new))));
    }

}
