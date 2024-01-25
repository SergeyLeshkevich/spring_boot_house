package ru.clevertec.house.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.clevertec.house.entity.dto.request.RequestPerson;
import ru.clevertec.house.entity.dto.response.ResponsePerson;
import ru.clevertec.house.service.PersonServiceForController;
import ru.clevertec.house.util.PaginationResponse;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/persons")
public class PersonController {

    private final PersonServiceForController personService;

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponsePerson> createPerson(@RequestBody RequestPerson person,
                                                       @RequestParam("houseUuid") UUID houseUuid) {
        ResponsePerson responsePerson = personService.create(person, houseUuid);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(responsePerson);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponsePerson> getPersonById(@PathVariable("id") UUID id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(personService.get(id));
    }

    @PutMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponsePerson> updatePersonById(@PathVariable("id") UUID id,
                                                           @RequestBody RequestPerson person) {

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(personService.update(id, person));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePersonById(@PathVariable("id") UUID id) {
        personService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PaginationResponse<ResponsePerson>> getAllPersons(@RequestParam(defaultValue = "15", name = "pageSize") int pageSize,
                                                                            @RequestParam(defaultValue = "1", name = "numberPage") int numberPage) {
        return new ResponseEntity<>(personService.getAll(pageSize, numberPage), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<ResponsePerson>> getPersonByUUidHouse(@RequestParam(name = "house_uuid") UUID uuid) {
        List<ResponsePerson> personList = personService.getPeopleByUuidHouse(uuid);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(personList);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ResponsePerson> patchPerson(@PathVariable UUID id, @RequestBody Map<String, Object> updates) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(personService.patch(id, updates));
    }
}

