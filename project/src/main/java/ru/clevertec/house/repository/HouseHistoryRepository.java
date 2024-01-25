package ru.clevertec.house.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.house.entity.HouseHistory;

import java.util.List;
import java.util.UUID;

@Repository
public interface HouseHistoryRepository extends CrudRepository<HouseHistory,Integer>{


    String SELECT_PEOPLE = "SELECT DISTINCT " +
            "hh.person_id " +
            "FROM persons p " +
            "JOIN house_history hh ON p.id = hh.person_id " +
            "JOIN houses h on h.id = hh.house_id " +
            "WHERE h.uuid = :uuid and hh.type = CAST (:type AS history_type)";
    String SELECT_HOUSES = "SELECT DISTINCT hh.house_id " +
            "FROM houses h " +
            "JOIN house_history hh ON h.id = hh.house_id " +
            "JOIN persons p on p.id = hh.person_id " +
            "WHERE p.uuid = :uuid " +
            "and hh.type = CAST (:type AS history_type)";

    @Transactional
    @Query(value = SELECT_PEOPLE, nativeQuery = true)
    List<Integer> findAllPeople(@Param("uuid") UUID houseUuid, @Param("type") String typeHistory);

    @Transactional
    @Query(value = SELECT_HOUSES, nativeQuery = true)
   List<Integer> findAllHouses(@Param("uuid") UUID personUuid,@Param("type") String typeHistory);

}
