package com.onions.timer.repository;

import com.onions.timer.model.App;

import java.util.List;
import java.util.Optional;

public interface AppRepository extends BaseRepository<App> {
    @Override
    Optional<App> findById(Long aLong);

    @Override
    List<App> findAll();
}
