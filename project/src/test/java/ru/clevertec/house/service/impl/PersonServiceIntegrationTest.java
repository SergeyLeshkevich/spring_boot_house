package ru.clevertec.house.service.impl;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import ru.clevertec.house.config.TestConfig;
import ru.clevertec.house.entity.Passport;
import ru.clevertec.house.entity.dto.request.RequestPerson;
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
class PersonServiceIntegrationTest {

    private final PersonServiceImpl personService;

    @Autowired
    public PersonServiceIntegrationTest(PersonServiceImpl personService) {
        this.personService = personService;
    }

    @Test
    void testGetInParallel() throws Exception {
        int numberOfThreads = 6;
        List<Callable<ResponsePerson>> tasks = new ArrayList<>();

        tasks.add(() -> personService.get(TestConstant.PERSON_UUID));

        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
        List<Future<ResponsePerson>> futures = executorService.invokeAll(tasks);
        executorService.shutdown();

        for (Future<ResponsePerson> future : futures) {
            ResponsePerson actual = future.get();
            assertThat(actual.name()).isEqualTo(TestConstant.NAME);
            assertThat(actual.surname()).isEqualTo(TestConstant.SURNAME);
            assertThat(actual.sex()).isEqualTo(TestConstant.SEX);
            assertThat(actual.passport().getNumber()).isEqualTo(TestConstant.PASSPORT_NUMBER);
            assertThat(actual.passport().getSeries()).isEqualTo("AB");
            assertThat(actual.uuid()).isEqualTo(TestConstant.PERSON_UUID);
            assertThat(actual.createDate()).isEqualTo(TestConstant.DATE_TIME);
        }
    }

    @Test
    void testCreateInParallel() throws Exception {
        int numberOfThreads = 6;
        List<RequestPerson> personList = new ArrayList<>();
        List<Callable<ResponsePerson>> tasks = new ArrayList<>();
        for (int i = 0; i < numberOfThreads; i++) {
            personList.add(RequestPerson.builder()
                    .name("Name")
                    .surname("Surname")
                    .sex(Sex.MALE)
                    .passport(new Passport(UUID.randomUUID().toString(), UUID.randomUUID().toString()))
                    .build());
        }
        personList.forEach(person -> tasks.add(() -> personService.create(person, TestConstant.HOUSE_UUID)));

        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
        List<Future<ResponsePerson>> futures = executorService.invokeAll(tasks);
        executorService.shutdown();

        for (Future<ResponsePerson> future : futures) {
            ResponsePerson actual = future.get();
            assertThat(actual.name()).isEqualTo("Name");
            assertThat(actual.surname()).isEqualTo("Surname");
            assertThat(actual.sex()).isEqualTo(Sex.MALE);
            assertThat(actual.passport()).isNotNull();
            assertThat(actual.uuid()).isNotNull();
            assertThat(actual.createDate()).isNotNull();
        }
    }

    @Test
    void testUpdateInParallel() throws Exception {
        int numberOfThreads = 6;
        Passport passport = new Passport("AA", "Test");
        List<Callable<ResponsePerson>> tasks = new ArrayList<>();
        RequestPerson requestPerson = RequestPerson.builder()
                .name("Name")
                .surname("Surname")
                .sex(Sex.MALE)
                .passport(passport)
                .build();

        tasks.add(() -> personService.update(TestConstant.PERSON_UUID, requestPerson));

        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
        List<Future<ResponsePerson>> futures = executorService.invokeAll(tasks);
        executorService.shutdown();

        for (Future<ResponsePerson> future : futures) {
            ResponsePerson actual = future.get();
            assertThat(actual.name()).isEqualTo("Name");
            assertThat(actual.surname()).isEqualTo("Surname");
            assertThat(actual.sex()).isEqualTo(Sex.MALE);
            assertThat(actual.passport()).isEqualTo(passport);
            assertThat(actual.uuid()).isEqualTo(TestConstant.PERSON_UUID);
            assertThat(actual.createDate()).isNotNull();
        }
    }

    @Test
    void testDeleteInParallel() {
        int numberOfThreads = 6;
        List<Runnable> tasks = new ArrayList<>();
        UUID uuid = UUID.fromString("9eeec6ea-a762-42e2-b472-02a5e11f6dd5");

        tasks.add(() -> personService.delete(uuid));

        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);

        tasks.forEach(executorService::submit);

        executorService.shutdown();

        assertThatThrownBy(
                () -> personService.get(uuid))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Person with 9eeec6ea-a762-42e2-b472-02a5e11f6dd5 not found");
    }
}

