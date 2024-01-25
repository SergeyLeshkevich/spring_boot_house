package ru.clevertec.house.service.impl.unit;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import ru.clevertec.house.entity.House;
import ru.clevertec.house.entity.dto.request.RequestHouse;
import ru.clevertec.house.entity.dto.response.ResponseHouse;
import ru.clevertec.house.exception.BadRequestException;
import ru.clevertec.house.exception.NotFoundException;
import ru.clevertec.house.mapper.HouseMapper;
import ru.clevertec.house.repository.HouseRepository;
import ru.clevertec.house.service.impl.HouseServiceImpl;
import ru.clevertec.house.util.HouseTest;
import ru.clevertec.house.util.RequestHouseTest;
import ru.clevertec.house.util.ResponseHouseTest;
import ru.clevertec.house.util.TestConstant;
import ru.clevertec.house.util.PaginationResponse;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HouseServiceImplTest {

    @Mock
    private HouseRepository houseRepository;

    @Mock
    private HouseMapper houseMapper;

    @InjectMocks
    private HouseServiceImpl houseService;

    private final HouseTest houseTest = new HouseTest();
    private final ResponseHouseTest responseHouseTest = new ResponseHouseTest();
    private final RequestHouseTest requestHouseTest = new RequestHouseTest();

    @Test
    void testGetExistingHouse() {
        House house = houseTest.build();

        when(houseRepository.findByUuid(TestConstant.HOUSE_UUID)).thenReturn(Optional.of(house));
        when(houseMapper.toDto(house)).thenReturn(responseHouseTest.build());

        ResponseHouse responseHouse = houseService.get(TestConstant.HOUSE_UUID);

        assertThat(responseHouse).isNotNull();
        verify(houseRepository).findByUuid(TestConstant.HOUSE_UUID);
    }

    @Test
    void testGetHouseThrowsNotFoundExceptionForMissingHouse() {

        when(houseRepository.findByUuid(TestConstant.HOUSE_UUID)).thenReturn(Optional.empty());

        assertThatThrownBy(
                () -> houseService.get(TestConstant.HOUSE_UUID))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("House with 5e213358-c398-49be-945b-e2b32a0c4a41 not found");

        verify(houseRepository).findByUuid(TestConstant.HOUSE_UUID);
        verifyNoMoreInteractions(houseMapper);
    }

    @Test
    void testGetNonExistingHouse() {
        UUID uuid = UUID.randomUUID();

        when(houseRepository.findByUuid(uuid)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> houseService.get(uuid)).isInstanceOf(NotFoundException.class);
        verify(houseRepository).findByUuid(uuid);
    }

    @Test
    void testGetAllHouses() {
        Page<House> page = Page.empty();
        page.map(h -> houseTest.build());
        when(houseRepository.findPageHouses(1, 1)).thenReturn(page);
        when(houseMapper.toDtoList(page.getContent())).thenReturn(Collections.singletonList(responseHouseTest.build()));

        PaginationResponse<ResponseHouse> actual = houseService.getAll(1, 1);

        assertThat(actual).isEqualTo(new PaginationResponse<>(1, 1, List.of(responseHouseTest.build())));
        verify(houseRepository).findPageHouses(1, 1);
    }


    @Test
    void testCreateHouse() {
        RequestHouse requestHouse = requestHouseTest.build();
        House house = houseTest.build();
        house.setCreateDate(LocalDateTime.now());
        when(houseMapper.toEntity(requestHouse)).thenReturn(house);
        when(houseMapper.toDto(house)).thenReturn(responseHouseTest.build());
        when(houseRepository.save(house)).thenReturn(house);
        when(houseRepository.findByUuid(any())).thenReturn(Optional.empty());

        ResponseHouse actual = houseService.create(requestHouse);

        assertThat(actual).isEqualTo(responseHouseTest.build());
        verify(houseRepository).save(house);
        verify(houseMapper).toDto(house);
        verify(houseMapper).toEntity(requestHouse);
    }

    @Test
    void testUpdateHouse() {
        RequestHouse requestHouse = requestHouseTest.build();
        House house = houseTest.build();
        when(houseRepository.findByUuid(house.getUuid())).thenReturn(Optional.of(house));
        when(houseMapper.toDto(house)).thenReturn(responseHouseTest.build());
        when(houseMapper.merge(house,requestHouse)).thenReturn(house);
        when(houseRepository.save(house)).thenReturn(house);

        ResponseHouse actual= houseService.update(TestConstant.HOUSE_UUID, requestHouse);

        assertThat(actual).isEqualTo(responseHouseTest.build());
        verify(houseRepository).save(any());
    }

    @Test
    void testDeleteHouse() {
        UUID uuid = TestConstant.HOUSE_UUID;
        houseService.delete(uuid);

        verify(houseRepository).deleteHouseByUuid(uuid);
    }

    @Test
    void testPatch() {
        House house = houseTest.build();
        house.setCreateDate(LocalDateTime.now());
        ResponseHouse responseHouse = responseHouseTest.build();

        Map<String, Object> updateHouse = new HashMap<>();
        updateHouse.put("area", "area");

        when(houseRepository.findByUuid(TestConstant.HOUSE_UUID)).thenReturn(Optional.of(house));
        when(houseRepository.save(house)).thenReturn(house);
        when(houseMapper.toDto(house)).thenReturn(responseHouse);

        ResponseHouse result = houseService.patch(TestConstant.HOUSE_UUID, updateHouse);

        assertThat(result).isEqualTo(responseHouse);
    }

    @Test
    void testPatchWhenTryingToUpdateInvalidField() {
        UUID houseUuid = UUID.randomUUID();

        Map<String, Object> updateHouse = new HashMap<>();
        updateHouse.put("invalidField", "Some value");

        when(houseRepository.findByUuid(houseUuid)).thenReturn(Optional.of(houseTest.build()));

        assertThrows(BadRequestException.class, () -> houseService.patch(houseUuid, updateHouse));
    }

    @Test
    void testGetHouseByUuid() {
        when(houseRepository.findByUuid(TestConstant.HOUSE_UUID)).thenReturn(Optional.of(houseTest.build()));

        House actual = houseService.getHouseByUuid(TestConstant.HOUSE_UUID);

        assertThat(actual).isEqualTo(houseTest.build());
    }

    @Test
    void getAllHousesById_should_return_correct_pagination_response() {
        List<Integer> allIdHouses = List.of(1);
        PageRequest pageRequest=PageRequest.of(4, 1);
        Page<House> page = Page.empty();
        page.map(h -> houseTest.build());
        when(houseRepository.findDistinctByIdIs(allIdHouses,pageRequest)).thenReturn(page);
        when(houseMapper.toDtoList(page.getContent())).thenReturn(List.of(responseHouseTest.build()));

        PaginationResponse<ResponseHouse> result = houseService.getAllHousesById(allIdHouses, 1, 5);

        assertThat(result).isNotNull();
        assertThat(result.getCountPage()).isEqualTo(1);
        assertThat(result.getPageNumber()).isEqualTo(5);
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent()).extracting(ResponseHouse::uuid).containsExactly(TestConstant.HOUSE_UUID);
    }
}




