package ru.clevertec.house.util;

import ru.clevertec.house.entity.Person;
import ru.clevertec.house.enums.Sex;

import java.time.LocalDateTime;
import java.util.UUID;

public class TestConstant {

    public static final int ID = 1;
    public static final UUID PERSON_UUID = UUID.fromString("5c3a267c-3175-4826-a7d1-488782a62d98");
    public static final String NAME = "John";
    public static final String SURNAME = "Doe";
    public static final Sex SEX = Sex.MALE;
    public static final String DATE_TIME = "2024-01-16T14:18:08.537";
    public static final LocalDateTime LOCAL_DATE_TIME = LocalDateTime.MIN;
    public static final String PASSPORT_SERIES ="AA";
    public static final String PASSPORT_NUMBER ="123456";
    public static final UUID HOUSE_UUID = UUID.fromString("5e213358-c398-49be-945b-e2b32a0c4a41");
    public static final String AREA = "Suburb";
    public static final String COUNTRY = "Country1";
    public static final String CITY = "City1";
    public static final String STREET = "Street1";
    public static final String NUMBER = "1";
}
