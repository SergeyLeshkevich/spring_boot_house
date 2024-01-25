package ru.clevertec.house.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.house.entity.House;
import ru.clevertec.house.entity.Passport;
import ru.clevertec.house.entity.Person;
import ru.clevertec.house.entity.dto.request.RequestPerson;
import ru.clevertec.house.entity.dto.response.ResponsePerson;
import ru.clevertec.house.exception.BadRequestException;
import ru.clevertec.house.exception.NotFoundException;
import ru.clevertec.house.exception.PassportUniqueException;
import ru.clevertec.house.mapper.PersonMapper;
import ru.clevertec.house.repository.PersonRepository;
import ru.clevertec.house.service.HouseService;
import ru.clevertec.house.service.PersonService;
import ru.clevertec.house.service.PersonServiceForController;
import ru.clevertec.house.util.PaginationResponse;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class PersonServiceImpl implements PersonService, PersonServiceForController {

    private static final String CREATE_DATE_FIELD = "createDate";
    private final PersonRepository personRepository;
    private final HouseService houseService;
    private final PersonMapper personMapper;

    public PersonServiceImpl(PersonRepository personRepository, @Lazy HouseService houseService, PersonMapper personMapper) {
        this.personRepository = personRepository;
        this.houseService = houseService;
        this.personMapper = personMapper;
    }

    /**
     * Retrieves a person by UUID.
     *
     * @param uuid The UUID of the person to retrieve.
     * @return A ResponsePerson DTO representing the retrieved person.
     * @throws NotFoundException If the person with the given UUID is not found.
     */
    @Override
    public ResponsePerson get(UUID uuid) throws NotFoundException {
        Person person = personRepository.findByUuid(uuid).orElseThrow(
                () -> NotFoundException.of(Person.class, uuid)
        );
        return personMapper.toDto(person);
    }

    /**
     * Retrieves a paginated list of persons.
     *
     * @param pageSize   The size of each page.
     * @param numberPage The page number to retrieve.
     * @return A PaginationResponse containing the list of ResponsePerson DTOs.
     */
    @Override
    public PaginationResponse<ResponsePerson> getAll(int pageSize, int numberPage) {
        Page<Person> pageHouses = personRepository.findPagePeople(pageSize, numberPage);
        PaginationResponse<ResponsePerson> paginationResponse = new PaginationResponse<>();
        paginationResponse.setCountPage(pageHouses.getTotalPages());
        paginationResponse.setPageNumber(numberPage);
        paginationResponse.setContent(personMapper.toDtoList(pageHouses.getContent()));
        return paginationResponse;

    }

    /**
     * Creates a new person.
     *
     * @param personDto The RequestPerson DTO containing information for creating the person.
     * @param houseUuid The UUID of the house to associate the person with.
     * @return A ResponsePerson DTO representing the created person.
     */
    @Override
    public ResponsePerson create(RequestPerson personDto, UUID houseUuid) {
        House house = houseService.getHouseByUuid(houseUuid);

        if (isAvailabilityPassport(personDto.passport())) {
            throw new PassportUniqueException("Passport with this number has already been saved");
        }

        UUID uuid;

        do {
            uuid = UUID.randomUUID();
        } while (isExist(uuid));

        Person person = personMapper.toEntity(personDto);
        person.setUuid(uuid);
        person.setCreateDate(LocalDateTime.now());
        person.setHouse(house);
        return personMapper.toDto(personRepository.save(person));
    }

    /**
     * Updates an existing person.
     *
     * @param personUuid The UUID of the person to update.
     * @param personDto  The updated information in the RequestPerson DTO.
     * @return A ResponsePerson DTO representing the updated person.
     * @throws NotFoundException If the person with the given UUID is not found.
     */
    @Override
    public ResponsePerson update(UUID personUuid, RequestPerson personDto) throws NotFoundException {
        Person personFormRepo = personRepository.findByUuid(personUuid).orElseThrow(
                () -> NotFoundException.of(Person.class, personUuid)
        );

        Person savePerson = personRepository.save(personMapper.merge(personFormRepo, personDto));
        return personMapper.toDto(savePerson);
    }

    /**
     * Deletes a person by UUID.
     *
     * @param uuid The UUID of the person to delete.
     */
    @Override
    @Transactional
    public void delete(UUID uuid) {
        personRepository.deleteByUuid(uuid);
    }

    /**
     * Retrieves a list of persons belonging to a specific house.
     *
     * @param idHouse The UUID of the house to retrieve persons for.
     * @return A list of ResponsePerson DTOs representing the persons.
     */
    @Override
    public List<ResponsePerson> getPeopleByUuidHouse(UUID idHouse) {
        List<Person> people = personRepository.findAllByHouse_Uuid(idHouse);
        return personMapper.toDtoList(people);
    }

    /**
     * Partially updates a person with specified parameters.
     *
     * @param personUuid The UUID of the person to patch.
     * @param param      A Map containing field names and values to update.
     * @return A ResponsePerson DTO representing the patched person.
     */
    @Override
    public ResponsePerson patch(UUID personUuid, Map<String, Object> param) {
        Person person = personRepository.findByUuid(personUuid).orElseThrow(
                () -> NotFoundException.of(House.class, personUuid)
        );

        for (Map.Entry<String, Object> entry : param.entrySet()) {
            String fieldName = entry.getKey();
            Object fieldValue = entry.getValue();
            if (fieldName.equals(CREATE_DATE_FIELD)) {
                throw BadRequestException.of(Person.class, fieldName);
            }
            try {
                Field field = Person.class.getDeclaredField(fieldName);
                field.setAccessible(true);
                field.set(person, fieldValue);
            } catch (NoSuchFieldException
                     | IllegalArgumentException e) {
                throw BadRequestException.of(Person.class, fieldName);
            } catch (IllegalAccessException e) {
                log.error(e.getMessage());
            }
        }
        person.setUuid(personUuid);
        return personMapper.toDto(personRepository.save(person));
    }

    /**
     * Retrieves a person by UUID.
     *
     * @param uuid The UUID of the person to retrieve.
     * @return The Person entity.
     * @throws NotFoundException If the person with the given UUID is not found.
     */
    @Override
    public Person getPersonByUuid(UUID uuid) {
        return personRepository.findByUuid(uuid).orElseThrow(
                () -> NotFoundException.of(Person.class, uuid)
        );
    }

    /**
     * Retrieves a paginated list of persons by a list of IDs.
     *
     * @param allIdPeople The list of person IDs to retrieve.
     * @param pageSize    The size of each page.
     * @param numberPage  The page number to retrieve.
     * @return A PaginationResponse containing the list of ResponsePerson DTOs.
     */
    @Override
    public PaginationResponse<ResponsePerson> getAllPeopleById(List<Integer> allIdPeople, int pageSize, int numberPage) {
        PageRequest pageRequest = PageRequest.of(numberPage - 1, pageSize);
        Page<Person> pageHouses = personRepository.findDistinctByIdIs(allIdPeople, pageRequest);
        PaginationResponse<ResponsePerson> paginationResponse = new PaginationResponse<>();
        paginationResponse.setCountPage(pageHouses.getTotalPages());
        paginationResponse.setPageNumber(numberPage);
        paginationResponse.setContent(personMapper.toDtoList(pageHouses.getContent()));
        return paginationResponse;
    }

    /**
     * Checks if a passport exists.
     *
     * @param passport The Passport to check.
     * @return True if the passport exists, false otherwise.
     */
    private boolean isAvailabilityPassport(Passport passport) {
        return personRepository.findByPassport_SeriesAndPassport_Number(passport.getSeries(),
                        passport.getNumber())
                .isPresent();
    }

    /**
     * Checks if a person with the specified UUID exists.
     *
     * @param uuid The UUID to check.
     * @return True if the house exists, false otherwise.
     */
    private boolean isExist(UUID uuid) {
        Optional<Person> person = personRepository.findByUuid(uuid);
        return person.isPresent();
    }
}
