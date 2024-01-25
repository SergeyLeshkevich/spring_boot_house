package ru.clevertec.house.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.clevertec.house.entity.House;
import ru.clevertec.house.entity.Person;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository
public interface HouseRepository extends JpaRepository<House, Integer> {

    default Page<House> findPageHouses(int pageSize, int numberPage) {
        PageRequest pageRequest = PageRequest.of(numberPage - 1, pageSize);
        return findAll(pageRequest);
    }

    Optional<House> findByUuid(UUID uuid);

    List<House> findAllByOwnersIn(Set<Person> owners);

    @Query("SELECT DISTINCT h FROM House h WHERE h.id IN :allIdHouses")
    Page<House> findDistinctByIdIs(@Param("allIdHouses") List<Integer> allIdHouses, PageRequest pageRequest);

    void deleteHouseByUuid(UUID uuid);
}
