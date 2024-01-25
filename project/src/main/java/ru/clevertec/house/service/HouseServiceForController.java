package ru.clevertec.house.service;

import ru.clevertec.house.entity.dto.request.RequestHouse;
import ru.clevertec.house.entity.dto.response.ResponseHouse;
import ru.clevertec.house.exception.NotFoundException;
import ru.clevertec.house.util.PaginationResponse;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface HouseServiceForController {

    ResponseHouse get(UUID uuid) throws NotFoundException;

    PaginationResponse<ResponseHouse> getAll(int pageSize, int numberPage);

    ResponseHouse create(RequestHouse houseDto);

    ResponseHouse update(UUID uuid, RequestHouse houseDto) throws NotFoundException;

    void delete(UUID uuid);

    ResponseHouse addOwner(UUID houseUuid, UUID personUuid);

    List<ResponseHouse> getHouseByOwnerUuid(UUID uuid);

    ResponseHouse patch(UUID id, Map<String, Object> param);
}
