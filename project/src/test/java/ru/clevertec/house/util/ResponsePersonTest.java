package ru.clevertec.house.util;

import ru.clevertec.house.entity.Passport;
import ru.clevertec.house.entity.dto.response.ResponsePerson;
import ru.clevertec.house.enums.Sex;

import java.util.UUID;

public class ResponsePersonTest implements TestBuilder{
    private int id = TestConstant.ID;
    private UUID personUuid = TestConstant.PERSON_UUID;
    private String name = TestConstant.NAME;
    private String surname = TestConstant.SURNAME;
    private Sex sex = TestConstant.SEX;
    private String dateTime = TestConstant.DATE_TIME;
    private String passportSeries = TestConstant.PASSPORT_SERIES;
    private String passportNumber = TestConstant.PASSPORT_NUMBER;

    @Override
    public ResponsePerson build() {
        final var passport = new Passport();
        passport.setSeries(passportSeries);
        passport.setNumber(passportNumber);
        final var person= new ResponsePerson(personUuid,
                name,
                surname,
                sex,
                passport,
                dateTime,
                dateTime);
        return  person;
    }
}
