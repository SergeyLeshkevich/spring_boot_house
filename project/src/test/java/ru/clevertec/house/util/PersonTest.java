package ru.clevertec.house.util;

import ru.clevertec.house.entity.House;
import ru.clevertec.house.entity.Passport;
import ru.clevertec.house.entity.Person;
import ru.clevertec.house.enums.Sex;

import java.time.LocalDateTime;
import java.util.UUID;

public class PersonTest implements TestBuilder{

    private int id = TestConstant.ID;
    private UUID personUuid = TestConstant.PERSON_UUID;
    private String name = TestConstant.NAME;
    private String surname = TestConstant.SURNAME;
    private Sex sex = TestConstant.SEX;
    private LocalDateTime dateTime = TestConstant.LOCAL_DATE_TIME;
    private String passportSeries = TestConstant.PASSPORT_SERIES;
    private String passportNumber = TestConstant.PASSPORT_NUMBER;
    private UUID houseUuid = TestConstant.HOUSE_UUID;
    private String area = TestConstant.AREA;
    private String country = TestConstant.COUNTRY;
    private String city = TestConstant.CITY;
    private String street = TestConstant.STREET;
    private String number = TestConstant.NUMBER;

    @Override
    public Person build() {
        final var passport = new Passport();
        passport.setSeries(passportSeries);
        passport.setNumber(passportNumber);
        final var person = new Person(id, personUuid, name, surname, sex, passport, dateTime, dateTime, null, null);
        final var house = new House();
        house.setId(id);
        house.setUuid(houseUuid);
        house.setArea(area);
        house.setCountry(country);
        house.setCity(city);
        house.setStreet(street);
        house.setNumber(number);
//        house.setRegisteredPeople(Set.of(person));
        person.setHouse(house);
        return person;
    }
}
