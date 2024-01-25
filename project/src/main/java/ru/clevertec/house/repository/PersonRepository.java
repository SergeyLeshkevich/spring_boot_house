package ru.clevertec.house.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.clevertec.house.entity.Person;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PersonRepository extends JpaRepository<Person, Integer> {

    Optional<Person> findByUuid(UUID uuid);

    default Page<Person> findPagePeople(int pageSize, int numberPage) {
        PageRequest pageRequest = PageRequest.of(numberPage - 1, pageSize);
        return findAll(pageRequest);
    }

    @Query("SELECT DISTINCT p FROM Person p WHERE p.id IN :allIdPeople")
    Page<Person> findDistinctByIdIs(@Param("allIdPeople") List<Integer> allIdPeople, PageRequest pageRequest);

    List<Person> findAllByHouse_Uuid(UUID houseUuid);

    Optional<Person> findByPassport_SeriesAndPassport_Number(String series, String number);

    void deleteByUuid(UUID uuid);
}
