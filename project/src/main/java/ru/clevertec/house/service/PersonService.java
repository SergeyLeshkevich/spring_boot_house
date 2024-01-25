package ru.clevertec.house.service;

import ru.clevertec.house.entity.Person;
import ru.clevertec.house.entity.dto.response.ResponsePerson;
import ru.clevertec.house.util.PaginationResponse;

import java.util.List;
import java.util.UUID;

public interface PersonService {

    Person getPersonByUuid(UUID uuid);

    PaginationResponse<ResponsePerson> getAllPeopleById(List<Integer> allIdPeople, int pageSize, int numberPage);
}
