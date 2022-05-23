package ru.charot.model;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class DepartmentShortResponse {
    private Integer id;
    private String name;
}
