package ru.clevertec.house.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import ru.clevertec.house.entity.House;
import ru.clevertec.house.entity.dto.request.RequestHouse;
import ru.clevertec.house.entity.dto.response.ResponseHouse;

import java.util.List;

@Mapper(config = MappersConfig.class)
public interface HouseMapper {

    House toEntity(RequestHouse requestHouse);

    ResponseHouse toDto(House house);

    List<ResponseHouse> toDtoList(List<House> houses);

    House merge(@MappingTarget House house, RequestHouse requestHouse);

}
