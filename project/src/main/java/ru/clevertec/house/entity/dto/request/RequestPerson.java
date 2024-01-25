package ru.clevertec.house.entity.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import ru.clevertec.house.entity.Passport;
import ru.clevertec.house.enums.Sex;

@Builder
public record RequestPerson(

        @NotNull
        @Size(min = 2, max = 50, message = "Name should be between 2 and 50 characters")
        String name,

        @NotNull
        @Size(min = 2, max = 50, message = "Surname should be between 2 and 50 characters")
        String surname,
        Sex sex,
        Passport passport) {
}
