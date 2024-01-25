package ru.clevertec.house.util;

import ru.clevertec.house.entity.House;
import ru.clevertec.house.entity.Passport;
import ru.clevertec.house.entity.Person;
import ru.clevertec.house.enums.Sex;

import java.time.LocalDateTime;
import java.util.UUID;

public class HouseTest implements TestBuilder {

    private int id = 1;
    private UUID personUuid = TestConstant.PERSON_UUID;
    private String name = TestConstant.NAME;
    private String surname = TestConstant.SURNAME;
    private Sex sex = TestConstant.SEX;
    private String dateTime = TestConstant.DATE_TIME;
    private LocalDateTime localDateTime = TestConstant.LOCAL_DATE_TIME;
    private String passportSeries = TestConstant.PASSPORT_SERIES;
    private String passportNumber = TestConstant.PASSPORT_NUMBER;
    private UUID houseUuid = TestConstant.HOUSE_UUID;
    private String area = TestConstant.AREA;
    private String country = TestConstant.COUNTRY;
    private String city = TestConstant.CITY;
    private String street = TestConstant.STREET;
    private String number = TestConstant.NUMBER;

    @Override
    public House build() {
        final var passport = new Passport();
        passport.setSeries(passportSeries);
        passport.setNumber(passportNumber);
        final var person = new Person(id, personUuid, name, surname, sex, passport, localDateTime, localDateTime, null, null);
        final var house = new House();
        house.setId(id);
        house.setUuid(houseUuid);
        house.setArea(area);
        house.setCountry(country);
        house.setCity(city);
        house.setStreet(street);
        house.setNumber(number);
        person.setHouse(house);
//        house.setRegisteredPeople(Set.of(person));
        return house;
    }
}
