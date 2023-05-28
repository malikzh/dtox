package io.github.malikzh.dtox.dto;

import lombok.Data;

@Data
public class SimpleDto {
    private String field1;
    private String field2;
    private SimpleDto2 field3;
}
