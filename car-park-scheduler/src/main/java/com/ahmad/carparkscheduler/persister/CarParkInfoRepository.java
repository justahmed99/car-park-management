package com.ahmad.carparkscheduler.persister;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface CarParkInfoRepository extends ReactiveCrudRepository<CarParkInfoEntity, String> {

}
