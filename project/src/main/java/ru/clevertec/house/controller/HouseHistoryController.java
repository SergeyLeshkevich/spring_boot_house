package ru.clevertec.house.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.clevertec.house.entity.dto.response.ResponseHouse;
import ru.clevertec.house.entity.dto.response.ResponsePerson;
import ru.clevertec.house.enums.Type;
import ru.clevertec.house.service.HouseHistoryService;
import ru.clevertec.house.util.PaginationResponse;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/history")
public class HouseHistoryController {

    private final HouseHistoryService historyService;

    @GetMapping("/houses/owners/{id}")
    public ResponseEntity<PaginationResponse<ResponseHouse>> getOwnersHouse(@PathVariable("id") UUID personUuid,
                                                                            Integer pageSize, Integer numberPage) {
        PaginationResponse<ResponseHouse> houses = historyService.getAllIdHouses(personUuid, Type.OWNER, pageSize, numberPage);
        return new ResponseEntity<>(houses, HttpStatus.OK);
    }

    @GetMapping("/houses/tenants/{id}")
    public ResponseEntity<PaginationResponse<ResponseHouse>> getTenantsHouse(@PathVariable("id") UUID personUuid,
                                                                             Integer pageSize, Integer numberPage) {
        PaginationResponse<ResponseHouse> houses = historyService.getAllIdHouses(personUuid, Type.TENANT, pageSize, numberPage);
        return new ResponseEntity<>(houses, HttpStatus.OK);
    }

    @GetMapping("/people/owners/{id}")
    public ResponseEntity<PaginationResponse<ResponsePerson>> getOwners(@PathVariable("id") UUID houseUuid,
                                                                        Integer pageSize, Integer numberPage) {
        PaginationResponse<ResponsePerson> people = historyService.getAllIdPeople(houseUuid, Type.OWNER, pageSize, numberPage);
        return new ResponseEntity<>(people, HttpStatus.OK);
    }

    @GetMapping("/people/tenants/{id}")
    public ResponseEntity<PaginationResponse<ResponsePerson>> getTenants(@PathVariable("id") UUID houseUuid,
                                                           Integer pageSize, Integer numberPage) {
        PaginationResponse<ResponsePerson> people = historyService.getAllIdPeople(houseUuid, Type.TENANT, pageSize, numberPage);
        return new ResponseEntity<>(people, HttpStatus.OK);
    }

}
