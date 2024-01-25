package ru.clevertec.house.entity.dto.response;

import java.util.UUID;

public record ResponseHouse(

        UUID uuid,
        String area,
        String country,
        String city,
        String street,
        String number,
        String createDate) {
}
