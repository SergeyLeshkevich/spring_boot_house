package ru.clevertec.house.util;

import ru.clevertec.house.entity.Passport;
import ru.clevertec.house.entity.dto.request.RequestPerson;
import ru.clevertec.house.enums.Sex;

import java.time.LocalDateTime;
import java.util.UUID;

public class RequestPersonTest implements TestBuilder{
    private int id = TestConstant.ID;
    private UUID personUuid = TestConstant.PERSON_UUID;
    private String name = TestConstant.NAME;
    private String surname = TestConstant.SURNAME;
    private Sex sex = TestConstant.SEX;
    private LocalDateTime dateTime = TestConstant.LOCAL_DATE_TIME;
    private String passportSeries = TestConstant.PASSPORT_SERIES;
    private String passportNumber = TestConstant.PASSPORT_NUMBER;

    @Override
    public RequestPerson build() {
        final var passport = new Passport();
        passport.setSeries(passportSeries);
        passport.setNumber(passportNumber);
        final var person= new RequestPerson(
                name,
                surname,
                sex,
                passport);
        return  person;
    }
}
