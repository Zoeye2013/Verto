package com.stringrest.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.stringrest.domain.StringSet;

import java.math.BigDecimal;
import java.util.Set;

public class StringStats {

    private final StringSet stringSet;
    private int numberOfString;
    private int shortestStringLength;
    private int longestStringLength;
    private BigDecimal averageLength;
    private BigDecimal medianLength;

    public StringStats(StringSet stringSet) {
        this.stringSet = stringSet;
    }

    public StringStats(StringSet stringSet, int numberOfString,
                       int longestStringLength, int shortestStringLength,
                       BigDecimal averageLength, BigDecimal medianLength) {
        this.averageLength = averageLength;
        this.longestStringLength = longestStringLength;
        this.medianLength = medianLength;
        this.numberOfString = numberOfString;
        this.shortestStringLength = shortestStringLength;
        this.stringSet = stringSet;
    }

    public StringSet getStringSet() {
        return stringSet;
    }

    @JsonIgnore
    public Set<String> getValue() {
        return this.stringSet.getValue();
    }

    public void setNumberOfString(int numberOfString) {
        this.numberOfString = numberOfString;
    }

    public void setShortestStringLength(int shortestStringLength) {
        this.shortestStringLength = shortestStringLength;
    }

    public void setLongestStringLength(int longestStringLength) {
        this.longestStringLength = longestStringLength;
    }

    public void setAverageLength(BigDecimal averageLength) {
        this.averageLength = averageLength;
    }

    public void setMedianLength(BigDecimal medianLength) {
        this.medianLength = medianLength;
    }

    public int getNumberOfString() {
        return numberOfString;
    }

    public int getShortestStringLength() {
        return shortestStringLength;
    }

    public int getLongestStringLength() {
        return longestStringLength;
    }

    public BigDecimal getAverageLength() {
        return averageLength;
    }

    public BigDecimal getMedianLength() {
        return medianLength;
    }

}
