package ru.clevertec.house.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.clevertec.house.entity.dto.response.ResponseHouse;
import ru.clevertec.house.entity.dto.response.ResponsePerson;
import ru.clevertec.house.enums.Type;
import ru.clevertec.house.repository.HouseHistoryRepository;
import ru.clevertec.house.service.HouseHistoryService;
import ru.clevertec.house.service.HouseService;
import ru.clevertec.house.service.PersonService;
import ru.clevertec.house.util.PaginationResponse;

import java.util.List;
import java.util.UUID;

/**
 * Service implementation for managing house history.
 */
@Service
@RequiredArgsConstructor
public class HouseHistoryServiceImpl implements HouseHistoryService {

    private final PersonService personService;
    private final HouseService houseService;
    private final HouseHistoryRepository historyRepository;

    /**
     * Retrieves all people IDs associated with a specific house UUID and history type.
     *
     * @param houseUuid    The UUID of the house.
     * @param typeHistory  The type of history.
     * @param pageSize     The size of each page for pagination.
     * @param numberPage   The page number for pagination.
     * @return             PaginationResponse containing the requested people.
     */
    @Override
    public PaginationResponse<ResponsePerson> getAllIdPeople(UUID houseUuid, Type typeHistory, Integer pageSize, Integer numberPage) {

        List<Integer> allIdPeople = historyRepository.findAllPeople(houseUuid, typeHistory.name());

        return personService.getAllPeopleById(allIdPeople, pageSize, numberPage);
    }

    /**
     * Retrieves all house IDs associated with a specific person UUID and history type.
     *
     * @param personUuid   The UUID of the person.
     * @param typeHistory  The type of history.
     * @param pageSize     The size of each page for pagination.
     * @param numberPage   The page number for pagination.
     * @return             PaginationResponse containing the requested houses.
     */
    @Override
    public PaginationResponse<ResponseHouse> getAllIdHouses(UUID personUuid, Type typeHistory, Integer pageSize, Integer numberPage) {

        List<Integer> allIdHouses = historyRepository.findAllHouses(personUuid, typeHistory.name());

        return houseService.getAllHousesById(allIdHouses, pageSize, numberPage);
    }
}
