package ru.clevertec.house.service;

import ru.clevertec.house.entity.House;
import ru.clevertec.house.entity.dto.response.ResponseHouse;
import ru.clevertec.house.util.PaginationResponse;

import java.util.List;
import java.util.UUID;

public interface HouseService {

    House getHouseByUuid(UUID uuid);

    PaginationResponse<ResponseHouse> getAllHousesById(List<Integer> allIdHouses, int pageSize, int numberPage);
}
