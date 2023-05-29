package io.github.malikzh.dtox.dto;

import io.github.malikzh.dtox.enums.SampleEnum;
import lombok.Data;

@Data
public class SimpleDto {
    private String field1;
    private Integer field2;
    private SampleEnum field3;
}
