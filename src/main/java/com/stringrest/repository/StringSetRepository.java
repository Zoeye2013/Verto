package com.stringrest.repository;

import com.stringrest.domain.StringSet;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.util.stream.Stream;

public interface StringSetRepository extends Repository<StringSet, Integer> {

    // Create Methods
    <S extends StringSet> S save(S entity);

    // Finder Methods
    StringSet findOne(Integer id);

    Iterable<StringSet> findAll();

    @Query("select s from StringSet s")
    Stream<StringSet> streamAll();

    // Delete Methods
    void delete(Integer id);
    void deleteAllInBatch();
}
