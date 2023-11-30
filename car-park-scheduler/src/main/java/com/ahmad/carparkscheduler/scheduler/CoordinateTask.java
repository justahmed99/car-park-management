package com.ahmad.carparkscheduler.scheduler;

import com.ahmad.carparkscheduler.csv.CarParkCSVService;
import com.ahmad.carparkscheduler.csv.carparkinfo.CarParkInfoCSV;
import com.ahmad.carparkscheduler.persister.CarParkInfo;
import com.ahmad.carparkscheduler.persister.CarParkInfoConverter;
import com.ahmad.carparkscheduler.persister.CarParkInfoEntity;
import com.ahmad.carparkscheduler.persister.CarParkInfoPersister;
import com.ahmad.carparkscheduler.persister.CarParkInfoRetriever;
import com.ahmad.carparkscheduler.webclient.CarParkAvailabilityService;
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
public class CoordinateTask {

  private final CarParkInfoPersister carParkInfoPersister;
  private final CarParkInfoRetriever carParkInfoRetriever;
  private final CarParkCSVService carParkCSVService;
  private final CarParkAvailabilityService carParkAvailabilityService;

  // @Scheduled(cron = "0 * * * * ?")
  public void test() {
    System.out.println("test scheduler");
  }

  @Scheduled(cron = "0 0/15 * * * ?")
  public void populateLots() {
    System.out.println("Populate car park lots | " + java.time.LocalDateTime.now());
    final Mono<Map<String, CarParkAvailabilityInfo>> carParkAvailability = carParkAvailabilityService
        .getCarParkAvailabilityInfo();
    carParkAvailability.subscribe(carParkAvailabilityInfos -> {
      Flux<CarParkInfo> carParkInfoFlux = carParkInfoRetriever.findAllCarParkInfo()
          .flatMap(carParkInfo -> {
            CarParkAvailabilityInfo selectedInfo = carParkAvailabilityInfos.get(carParkInfo.getCarParkNo());
            if (selectedInfo.getAvailableLots() != null)
              carParkInfo.setLotsAvailable(selectedInfo.getAvailableLots());
            return Mono.just(carParkInfo);
          });

      carParkInfoFlux.subscribe(carParkInfo -> {
        System.out.print("car park no : " + carParkInfo.getCarParkNo());
        System.out.print(" car park latitude : " + carParkInfo.getLatitude());
        System.out.print(" car park longitude : " + carParkInfo.getLongitude());
        System.out.print(" car park availability : " + carParkInfo.getLotsAvailable());
      });
    });
  }

  @Scheduled(cron = "0 */2 * * * ?")
  public void populateCarParkInfo() {
    System.out.println("Populate car park info | " + java.time.LocalDateTime.now());
    final List<CarParkInfoCSV> carParkInfoCSVList = carParkCSVService.readCarParkInfoFile();
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
              return carParkAvailabilityService.getWgs84Coordinate(
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
