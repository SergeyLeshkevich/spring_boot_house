package ru.clevertec.house.service;

import ru.clevertec.house.entity.dto.response.ResponseHouse;
import ru.clevertec.house.entity.dto.response.ResponsePerson;
import ru.clevertec.house.enums.Type;
import ru.clevertec.house.util.PaginationResponse;

import java.util.UUID;

public interface HouseHistoryService {

  PaginationResponse<ResponsePerson> getAllIdPeople(UUID houseUuid, Type typeHistory, Integer pageSize, Integer numberPage);

  PaginationResponse<ResponseHouse> getAllIdHouses(UUID personUuid, Type typeHistory, Integer pageSize, Integer numberPage);
}
