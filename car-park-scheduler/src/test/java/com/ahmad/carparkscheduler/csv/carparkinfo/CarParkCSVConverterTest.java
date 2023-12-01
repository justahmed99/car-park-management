package com.ahmad.carparkscheduler.csv.carparkinfo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.apache.commons.csv.CSVRecord;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CarParkCSVConverterTest {

  @Test
  void convertStringLineToCarParkInfo() {
    final CSVRecord mockRecord = mock(CSVRecord.class);
    when(mockRecord.get(0)).thenReturn("CP001");
    when(mockRecord.get(1)).thenReturn("1234 Street Name");
    when(mockRecord.get(2)).thenReturn("1.234");
    when(mockRecord.get(3)).thenReturn("5.678");
    when(mockRecord.get(4)).thenReturn("Surface");
    when(mockRecord.get(5)).thenReturn("Coupon");
    when(mockRecord.get(6)).thenReturn("YES");
    when(mockRecord.get(7)).thenReturn("NO");
    when(mockRecord.get(8)).thenReturn("YES");
    when(mockRecord.get(9)).thenReturn("2");
    when(mockRecord.get(10)).thenReturn("4.5");
    when(mockRecord.get(11)).thenReturn("NO");

    final CarParkInfoCSV result = CarParkCSVConverter.convertStringLineToCarParkInfo(mockRecord);

    assertEquals("CP001", result.getCarParkNo());
    assertEquals("1234 Street Name", result.getAddress());
    assertEquals(1.234, result.getXCoord());
    assertEquals(5.678, result.getYCoord());
    assertEquals("Surface", result.getCarParkType());
    assertEquals("Coupon", result.getTypeOfParkingSystem());
    assertEquals("YES", result.getShortTermPark());
    assertEquals("NO", result.getFreeParking());
    assertEquals("YES", result.getNightParking());
    assertEquals("2", result.getCarParkDecks());
    assertEquals("4.5", result.getGantryHeight());
    assertEquals("NO", result.getCarParkBasement());
  }
}