package ru.clevertec.house.util;

import ru.clevertec.house.entity.dto.request.RequestHouse;

import java.time.LocalDateTime;
import java.util.UUID;

public class RequestHouseTest implements TestBuilder{
    private LocalDateTime dateTime = TestConstant.LOCAL_DATE_TIME;
    private UUID houseUuid = TestConstant.HOUSE_UUID;
    private String area = TestConstant.AREA;
    private String country = TestConstant.COUNTRY;
    private String city = TestConstant.CITY;
    private String street = TestConstant.STREET;
    private String number = TestConstant.NUMBER;

    @Override
    public RequestHouse build() {
        final var house = new RequestHouse(
                area,country,city,street,number
        );
        return house;
    }
}
