package com.study.templateapi.api.exceptiontest.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class BindExceptionTestDto {

    @NotBlank(message = "해당 같은 필수 입력값입니다.")
    private String value1;

    @Max(value = 10, message = "최대 입력 값은 10입니다")
    private Integer value2;
}
