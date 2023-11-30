package com.ahmad.carparkscheduler.scheduler;

import com.ahmad.carparkscheduler.csv.CarParkCSVUseCase;
import com.ahmad.carparkscheduler.csv.carparkinfo.CarParkInfoCSV;
import com.ahmad.carparkscheduler.persister.postgre.CarParkInfo;
import com.ahmad.carparkscheduler.persister.postgre.CarParkInfoConverter;
import com.ahmad.carparkscheduler.persister.postgre.CarParkInfoEntity;
import com.ahmad.carparkscheduler.persister.postgre.CarParkInfoPersister;
import com.ahmad.carparkscheduler.persister.postgre.CarParkInfoRetriever;
import com.ahmad.carparkscheduler.persister.redis.CarParkGeoDataConverter;
import com.ahmad.carparkscheduler.persister.redis.CarParkGeoDataProvider;
import com.ahmad.carparkscheduler.webclient.CarParkAvailabilityUseCase;
import com.ahmad.carparkscheduler.webclient.carparkinfo.CarParkAvailabilityInfo;
import com.ahmad.carparkscheduler.webclient.coordinate.SVY21Coordinate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class CarParkSchedulerTask {

  private final CarParkInfoPersister carParkInfoPersister;
  private final CarParkInfoRetriever carParkInfoRetriever;
  private final CarParkCSVUseCase carParkCSVUseCase;
  private final CarParkAvailabilityUseCase carParkAvailabilityUseCase;
  private final CarParkGeoDataProvider carParkGeoDataProvider;

  @Scheduled(cron = "0 0/15 * * * ?")
  public void populateLots() {
    System.out.println("Populate car park lots | " + java.time.LocalDateTime.now());
    final Mono<Map<String, CarParkAvailabilityInfo>> carParkAvailability = carParkAvailabilityUseCase
        .getCarParkAvailabilityInfo();
    carParkAvailability.subscribe(carParkAvailabilityInfos -> {
      Flux<CarParkInfo> carParkInfoFlux = carParkInfoRetriever.findAllCarParkInfo()
          .flatMap(carParkInfo -> {
            CarParkAvailabilityInfo selectedInfo = carParkAvailabilityInfos.get(
                carParkInfo.getCarParkNo());
            if (selectedInfo != null) {
              carParkInfo.setLotsAvailable(selectedInfo.getAvailableLots());
            }
            return Mono.just(carParkInfo);
          }).filter(carParkInfo -> carParkInfo.getLotsAvailable() != null);
      carParkGeoDataProvider.deleteLocation().subscribe(aLong -> {
        carParkInfoFlux.subscribe(carParkInfo -> {
          carParkGeoDataProvider.addLocation(
                  CarParkGeoDataConverter.fromScheduler(carParkInfo))
              .subscribe();
          // System.out.println(carParkInfo.getCarParkNo() + " | (" + carParkInfo.getLatitude() + " , "
          //     + carParkInfo.getLongitude() + ") | " + carParkInfo.getLotsAvailable());

        });
      });
    });
  }

  @Scheduled(cron = "0 0/2 * * * ?")
  public void populateCarParkInfo() {
    System.out.println("Populate car park info | " + java.time.LocalDateTime.now());
    final List<CarParkInfoCSV> carParkInfoCSVList = carParkCSVUseCase.readCarParkInfoFile();
    System.out.println("CSV Size : " + carParkInfoCSVList.size());
    List<CarParkInfoCSV> selectedCarParkInfoCSVList = new ArrayList<>();
    Mono<Long> countDB = carParkInfoRetriever.count();
    countDB.subscribe(dbSize -> {
      System.out.println("Imported Size : " + dbSize);
      if (dbSize < carParkInfoCSVList.size()) {
        int startIndex = dbSize.intValue() == 0 ? 0 : dbSize.intValue();
        int endIndex =
            carParkInfoCSVList.size() - dbSize.intValue() < 240 ? carParkInfoCSVList.size()
                : dbSize.intValue() + 240;
        selectedCarParkInfoCSVList.addAll(carParkInfoCSVList.subList(startIndex,
            endIndex));

        System.out.println("Import " + selectedCarParkInfoCSVList.size() + " on progress!");
        System.out.println("First Data: " + selectedCarParkInfoCSVList.get(0).getCarParkNo());
        System.out.println(
            "Last Data: " + selectedCarParkInfoCSVList.get(selectedCarParkInfoCSVList.size() - 1)
                .getCarParkNo());

        Flux<CarParkInfoEntity> infoEntityFlux = Flux.fromIterable(selectedCarParkInfoCSVList)
            .flatMap(CarParkInfoConverter::fromCSVToEntity)
            .flatMap(carParkInfoEntity -> {
              return carParkAvailabilityUseCase.getWgs84Coordinate(
                  SVY21Coordinate.builder()
                      .x(carParkInfoEntity.getX())
                      .y(carParkInfoEntity.getY())
                      .build()
              ).flatMap(coordinate -> {
                carParkInfoEntity.setLatitude(coordinate.getLatitude());
                carParkInfoEntity.setLongitude(coordinate.getLongitude());
                return Mono.just(carParkInfoEntity);
              });
            });

        carParkInfoPersister.saveCarParkInfo(infoEntityFlux).subscribe();
      } else {
        System.out.println("All data is stored");
      }
    });
  }
}
