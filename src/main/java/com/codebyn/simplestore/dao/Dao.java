package com.codebyn.simplestore.dao;

import java.util.List;
import java.util.Optional;

public interface Dao<T> {
    Optional<T> get(String name);

    void save(T type);

    void update(T type);

    void delete(T type);

    List<T> getAll();
}
