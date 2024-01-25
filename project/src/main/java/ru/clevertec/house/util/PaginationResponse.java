package ru.clevertec.house.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaginationResponse<T> {

    private int pageNumber;
    private int countPage;
    private List<T> content;
}
