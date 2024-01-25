package ru.clevertec.house.util;

import ru.clevertec.house.entity.dto.response.ResponseHouse;

import java.util.UUID;

public class ResponseHouseTest implements TestBuilder{
    private String dateTime = TestConstant.DATE_TIME;
    private UUID houseUuid = TestConstant.HOUSE_UUID;
    private String area = TestConstant.AREA;
    private String country = TestConstant.COUNTRY;
    private String city = TestConstant.CITY;
    private String street = TestConstant.STREET;
    private String number = TestConstant.NUMBER;

    @Override
    public ResponseHouse build() {
        final var house = new ResponseHouse(
                houseUuid,area,country,city,street,number,dateTime
        );
        return house;
    }
}
