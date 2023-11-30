package com.ahmad.carparkscheduler.persister.postgre;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface CarParkInfoRepository extends ReactiveCrudRepository<CarParkInfoEntity, String> {

}
