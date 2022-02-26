package com.github.bishoybasily.springframework.commons.core.data.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author bishoybasily
 * @since 2020-06-24
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class Response<T> {

    private T data;

}
