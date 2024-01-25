package ru.clevertec.house.service.impl;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import ru.clevertec.house.config.TestConfig;
import ru.clevertec.house.entity.Passport;
import ru.clevertec.house.entity.dto.request.RequestHouse;
import ru.clevertec.house.entity.dto.request.RequestPerson;
import ru.clevertec.house.entity.dto.response.ResponseHouse;
import ru.clevertec.house.entity.dto.response.ResponsePerson;
import ru.clevertec.house.enums.Sex;
import ru.clevertec.house.exception.NotFoundException;
import ru.clevertec.house.util.TestConstant;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest(classes = TestConfig.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
class HouseServiceIntegrationTest {

    private final HouseServiceImpl houseService;

    @Autowired
    public HouseServiceIntegrationTest(HouseServiceImpl houseService) {
        this.houseService = houseService;
    }

    @Test
    void testGetInParallel() throws Exception {
        int numberOfThreads = 6;
        List<Callable<ResponseHouse>> tasks = new ArrayList<>();

        tasks.add(() -> houseService.get(UUID.fromString("e96346e9-9239-44f8-be11-fcbc86a541eb")));

        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
        List<Future<ResponseHouse>> futures = executorService.invokeAll(tasks);
        executorService.shutdown();

        for (Future<ResponseHouse> future : futures) {
            ResponseHouse actual = future.get();
            assertThat(actual.number()).isEqualTo("2");
            assertThat(actual.street()).isEqualTo("Street2");
            assertThat(actual.area()).isEqualTo("Downtown");
            assertThat(actual.uuid()).isEqualTo(UUID.fromString("e96346e9-9239-44f8-be11-fcbc86a541eb"));
            assertThat(actual.city()).isEqualTo("City2");
            assertThat(actual.country()).isEqualTo("Country2");
            assertThat(actual.createDate()).isEqualTo(TestConstant.DATE_TIME);
        }
    }

    @Test
    void testCreateInParallel() throws Exception {
        int numberOfThreads = 6;
        List<RequestHouse> houseList = new ArrayList<>();
        List<Callable<ResponseHouse>> tasks = new ArrayList<>();
        for (int i = 0; i < numberOfThreads; i++) {
            houseList.add(RequestHouse.builder()
                    .area("Area")
                    .city("City")
                    .country("Country")
                    .street("Street")
                    .number("1")
                    .build());
        }
        houseList.forEach(house -> tasks.add(() -> houseService.create(house)));

        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
        List<Future<ResponseHouse>> futures = executorService.invokeAll(tasks);
        executorService.shutdown();

        for (Future<ResponseHouse> future : futures) {
            ResponseHouse actual = future.get();
            assertThat(actual.number()).isEqualTo("1");
            assertThat(actual.street()).isEqualTo("Street");
            assertThat(actual.area()).isEqualTo("Area");
            assertThat(actual.uuid()).isNotNull();
            assertThat(actual.city()).isEqualTo("City");
            assertThat(actual.country()).isEqualTo("Country");
            assertThat(actual.createDate()).isNotNull();
        }
    }

    @Test
    void testUpdateInParallel() throws Exception {
        int numberOfThreads = 6;
        List<Callable<ResponseHouse>> tasks = new ArrayList<>();
        RequestHouse requestHouse = RequestHouse.builder()
                .area("Area")
                .city("City")
                .country("Country")
                .street("Street")
                .number("1")
                .build();
        tasks.add(() -> houseService.update(UUID.fromString("1f5e112c-ebdc-4935-ae07-ee05eb1a2de5"),
                requestHouse));

        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
        List<Future<ResponseHouse>> futures = executorService.invokeAll(tasks);
        executorService.shutdown();

        for (Future<ResponseHouse> future : futures) {
            ResponseHouse actual = future.get();
            assertThat(actual.number()).isEqualTo("1");
            assertThat(actual.street()).isEqualTo("Street");
            assertThat(actual.area()).isEqualTo("Area");
            assertThat(actual.uuid()).isEqualTo(UUID.fromString("1f5e112c-ebdc-4935-ae07-ee05eb1a2de5"));
            assertThat(actual.city()).isEqualTo("City");
            assertThat(actual.country()).isEqualTo("Country");
            assertThat(actual.createDate()).isNotNull();
        }
    }

    @Test
    void testDeleteInParallel() {
        int numberOfThreads = 6;
        List<Runnable> tasks = new ArrayList<>();
        UUID uuid = UUID.fromString("5c3a267c-3175-4826-a7d1-488782a62d98");

        tasks.add(() -> houseService.delete(TestConstant.PERSON_UUID));

        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);

        tasks.forEach(executorService::submit);

        executorService.shutdown();

        assertThatThrownBy(
                () -> houseService.get(uuid))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("House with 5c3a267c-3175-4826-a7d1-488782a62d98 not found");
    }
}
