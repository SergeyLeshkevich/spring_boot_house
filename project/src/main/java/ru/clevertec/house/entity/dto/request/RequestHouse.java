package ru.clevertec.house.entity.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record RequestHouse(

        @NotNull
        @Size(min = 2, max = 50, message = "Area should be between 2 and 50 characters")
        String area,

        @NotNull
        @Size(min = 2, max = 50, message = "Country should be between 2 and 50 characters")
        String country,

        @NotNull
        @Size(min = 2, max = 50, message = "City should be between 2 and 50 characters")
        String city,

        @NotNull
        @Size(min = 2, max = 50, message = "Street should be between 2 and 50 characters")
        String street,

        @NotNull
        @Size(min = 2, max = 50, message = "Number should be between 2 and 50 characters")
        String number) {
}
