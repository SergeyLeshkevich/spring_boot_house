package ru.clevertec.house.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class Passport {

    @Column(name = "passport_series")
    private String series;

    @Column(name = "passport_number")
    private String number;
}
