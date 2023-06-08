package org.example.playerservice.repository;

import org.example.playerservice.model.entity.PlayerStatistic;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayerStatisticRepository extends CrudRepository<PlayerStatistic, Long> {
}
