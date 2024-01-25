package ru.clevertec.house.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import ru.clevertec.house.entity.Person;
import ru.clevertec.house.entity.dto.request.RequestPerson;
import ru.clevertec.house.entity.dto.response.ResponsePerson;

import java.util.List;

@Mapper(config = MappersConfig.class)
public interface PersonMapper {

    Person toEntity(RequestPerson personDto);

    ResponsePerson toDto(Person person);

    List<ResponsePerson> toDtoList(List<Person> persons);

    Person merge(@MappingTarget Person person, RequestPerson requestPerson);
}
