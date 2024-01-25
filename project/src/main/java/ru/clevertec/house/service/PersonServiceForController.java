package ru.clevertec.house.service;

import ru.clevertec.house.entity.dto.request.RequestPerson;
import ru.clevertec.house.entity.dto.response.ResponsePerson;
import ru.clevertec.house.exception.NotFoundException;
import ru.clevertec.house.util.PaginationResponse;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface PersonServiceForController {

    ResponsePerson get(UUID uuid) throws NotFoundException;

    PaginationResponse<ResponsePerson> getAll(int pageSize, int numberPage);

    ResponsePerson create(RequestPerson personDto, UUID houseUuid);

    ResponsePerson update(UUID personUuid, RequestPerson personDto) throws NotFoundException;

    void delete(UUID uuid);

    List<ResponsePerson> getPeopleByUuidHouse(UUID id);

    ResponsePerson patch(UUID personUuid, Map<String, Object> param);

}
