package ru.clevertec.house.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.house.entity.House;
import ru.clevertec.house.entity.Person;
import ru.clevertec.house.entity.dto.request.RequestHouse;
import ru.clevertec.house.entity.dto.response.ResponseHouse;
import ru.clevertec.house.exception.BadRequestException;
import ru.clevertec.house.exception.NotFoundException;
import ru.clevertec.house.mapper.HouseMapper;
import ru.clevertec.house.repository.HouseRepository;
import ru.clevertec.house.service.HouseService;
import ru.clevertec.house.service.HouseServiceForController;
import ru.clevertec.house.service.PersonService;
import ru.clevertec.house.util.PaginationResponse;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * Service implementation for managing houses.
 */
@Service
@RequiredArgsConstructor
public class HouseServiceImpl implements HouseService, HouseServiceForController {

    private static final String CREATE_DATE_FIELD = "createDate";

    private final HouseRepository houseRepository;
    private final PersonService personService;
    private final HouseMapper houseMapper;

    /**
     * Retrieves a house by UUID.
     *
     * @param uuid The UUID of the house.
     * @return Response containing the house details.
     * @throws NotFoundException if the house is not found.
     */
    @Override
    public ResponseHouse get(UUID uuid) {
        House house = houseRepository.findByUuid(uuid)
                .orElseThrow(() -> NotFoundException.of(House.class, uuid));

        return houseMapper.toDto(house);
    }

    /**
     * Retrieves a paginated list of houses.
     *
     * @param pageSize   Number of items per page.
     * @param numberPage Page number.
     * @return PaginationResponse containing the list of houses.
     */
    @Override
    public PaginationResponse<ResponseHouse> getAll(int pageSize, int numberPage) {
        Page<House> pageHouses = houseRepository.findPageHouses(pageSize, numberPage);
        PaginationResponse<ResponseHouse> paginationResponse = new PaginationResponse<>();
        paginationResponse.setCountPage(pageHouses.getTotalPages());
        paginationResponse.setPageNumber(numberPage);
        paginationResponse.setContent(houseMapper.toDtoList(pageHouses.getContent()));
        return paginationResponse;
    }

    /**
     * Creates a new house.
     *
     * @param houseDto Details of the house to be created.
     * @return Response containing the created house details.
     */
    @Override
    public ResponseHouse create(RequestHouse houseDto) {
        House house = houseMapper.toEntity(houseDto);
        UUID uuid;

        do {
            uuid = UUID.randomUUID();
        } while (isExist(uuid));

        house.setUuid(uuid);
        house.setCreateDate(LocalDateTime.now());
        return houseMapper.toDto(houseRepository.save(house));
    }


    /**
     * Updates an existing house.
     *
     * @param uuid      The UUID of the house to be updated.
     * @param houseDto  Details of the house to be updated.
     * @return Response containing the updated house details.
     * @throws NotFoundException if the house is not found.
     */
    @Override
    public ResponseHouse update(UUID uuid, RequestHouse houseDto) throws NotFoundException {
        House house = houseRepository.findByUuid(uuid).orElseThrow(
                () -> NotFoundException.of(House.class, uuid)
        );
        House updatedHouse = houseRepository.save(houseMapper.merge(house, houseDto));
        return houseMapper.toDto(updatedHouse);
    }

    /**
     * Deletes a house by UUID.
     *
     * @param uuid The UUID of the house to be deleted.
     */
    @Override
    @Transactional
    public void delete(UUID uuid) {
        houseRepository.deleteHouseByUuid(uuid);
    }

    /**
     * Adds an owner to a house.
     *
     * @param houseUuid  The UUID of the house.
     * @param personUuid The UUID of the person to be added as an owner.
     * @return Response containing the updated house details.
     */
    @Override
    public ResponseHouse addOwner(UUID houseUuid, UUID personUuid) {
        Person person = personService.getPersonByUuid(personUuid);
        House house = houseRepository.findByUuid(houseUuid).orElseThrow(
                () -> NotFoundException.of(House.class, houseUuid)
        );
        house.getOwners().add(person);
        return houseMapper.toDto(houseRepository.save(house));
    }

    /**
     * Retrieves a list of houses owned by a person.
     *
     * @param uuid The UUID of the person.
     * @return List of houses owned by the person.
     */
    @Override
    public List<ResponseHouse> getHouseByOwnerUuid(UUID uuid) {
        Person person = personService.getPersonByUuid(uuid);
        List<House> allByOwners = houseRepository.findAllByOwnersIn(Set.of(person));
        return houseMapper.toDtoList(allByOwners);
    }

    /**
     * Partially updates a house with the specified UUID.
     *
     * @param uuid  The UUID of the house to be updated.
     * @param param Map containing the fields to be updated.
     * @return Response containing the updated house details.
     * @throws NotFoundException if the house is not found.
     * @throws BadRequestException if an invalid field is provided for update.
     */
    @Override
    public ResponseHouse patch(UUID uuid, Map<String, Object> param) {
        House house = houseRepository.findByUuid(uuid).orElseThrow(
                () -> NotFoundException.of(House.class, uuid)
        );

        for (Map.Entry<String, Object> entry : param.entrySet()) {
            String fieldName = entry.getKey();
            Object fieldValue = entry.getValue();
            if (fieldName.equals(CREATE_DATE_FIELD)) {
                throw BadRequestException.of(House.class, fieldName);
            }
            try {
                Field field = House.class.getDeclaredField(fieldName);
                field.setAccessible(true);
                field.set(house, fieldValue);
            } catch (NoSuchFieldException
                     | IllegalAccessException
                     | IllegalArgumentException e) {
                throw BadRequestException.of(House.class, fieldName);
            }
        }
        house.setUuid(uuid);
        return houseMapper.toDto(houseRepository.save(house));
    }

    /**
     * Retrieves a specific house by UUID.
     *
     * @param uuid The UUID of the house.
     * @return The house with the specified UUID.
     * @throws NotFoundException if the house is not found.
     */
    @Override
    public House getHouseByUuid(UUID uuid) {
        return houseRepository.findByUuid(uuid).orElseThrow(
                () -> NotFoundException.of(House.class, uuid)
        );
    }

    /**
     * Retrieves a paginated list of houses by a list of UUID.
     *
     * @param allIdHouses List of house IDs.
     * @param pageSize    Number of items per page.
     * @param numberPage  Page number.
     * @return PaginationResponse containing the list of houses.
     */
    @Override
    public PaginationResponse<ResponseHouse> getAllHousesById(List<Integer> allIdHouses, int pageSize, int numberPage) {
        PageRequest pageRequest = PageRequest.of(numberPage - 1, pageSize);
        Page<House> pageHouses = houseRepository.findDistinctByIdIs(allIdHouses, pageRequest);
        PaginationResponse<ResponseHouse> paginationResponse = new PaginationResponse<>();
        paginationResponse.setCountPage(pageHouses.getTotalPages());
        paginationResponse.setPageNumber(numberPage);
        paginationResponse.setContent(houseMapper.toDtoList(pageHouses.getContent()));
        return paginationResponse;
    }

    /**
     * Checks if a house with the specified UUID exists.
     *
     * @param uuid The UUID to check.
     * @return True if the house exists, false otherwise.
     */
    private boolean isExist(UUID uuid) {
        return houseRepository.findByUuid(uuid).isPresent();
    }
}
