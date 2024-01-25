package ru.clevertec.house.entity.dto.response;

import ru.clevertec.house.entity.Passport;
import ru.clevertec.house.enums.Sex;

import java.util.UUID;

public record ResponsePerson(

        UUID uuid,
        String name,
        String surname,
        Sex sex,
        Passport passport,
        String createDate,
        String updateDate) {
}
