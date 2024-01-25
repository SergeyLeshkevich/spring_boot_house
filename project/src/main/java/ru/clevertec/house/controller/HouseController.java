package ru.clevertec.house.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
import ru.clevertec.house.entity.dto.request.RequestHouse;
import ru.clevertec.house.entity.dto.response.ResponseHouse;
import ru.clevertec.house.service.HouseServiceForController;
import ru.clevertec.house.util.PaginationResponse;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping(path = "/houses")
@RequiredArgsConstructor
public class HouseController {

    private final HouseServiceForController houseService;

    @PostMapping
    public ResponseEntity<ResponseHouse> createHouse(@RequestBody RequestHouse house) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(houseService.create(house));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseHouse> getHouseById(@PathVariable("id") UUID id) {
        ResponseHouse house = houseService.get(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(house);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseHouse> updateHouseById(@PathVariable("id") UUID id,
                                                         @RequestBody RequestHouse requestHouse) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(houseService.update(id, requestHouse));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHouseById(@PathVariable("id") UUID id) {
        houseService.delete(id);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

    @GetMapping
    public ResponseEntity<PaginationResponse<ResponseHouse>> getAllHouses(
            @RequestParam(defaultValue = "15", name = "pageSize") int pageSize,
            @RequestParam(defaultValue = "1", name = "numberPage") int numberPage) {

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(houseService.getAll(pageSize, numberPage));
    }

    @PutMapping("/tenants")
    public ResponseEntity<ResponseHouse> addTenant(@RequestParam("house_uuid") UUID houseUuid,
                                                   @RequestParam("person_uuid") UUID personUuid) {
        ResponseHouse responseHouse = houseService.addOwner(houseUuid, personUuid);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(responseHouse);
    }

    @GetMapping("/owners/{uuid}")
    public ResponseEntity<List<ResponseHouse>> getHouseByOwnerUuid(@PathVariable UUID uuid) {
        List<ResponseHouse> houses = houseService.getHouseByOwnerUuid(uuid);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(houses);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ResponseHouse> patchHouse(@PathVariable UUID id, @RequestBody Map<String, Object> updates) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(houseService.patch(id, updates));
    }
}