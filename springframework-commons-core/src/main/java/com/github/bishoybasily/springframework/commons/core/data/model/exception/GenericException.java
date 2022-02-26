package com.github.bishoybasily.springframework.commons.core.data.model.exception;

import com.github.bishoybasily.springframework.commons.core.data.model.dto.Response;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class GenericException extends RuntimeException {

    private Integer code;
    private Response<String> details;

    public GenericException(Integer code) {
        this.code = code;
    }

    public GenericException(Integer code, String s) {
        super(s);
        this.code = code;
        this.details = new Response<>(s);
    }

    public GenericException(Integer code, String s, Throwable throwable) {
        super(s, throwable);
        this.code = code;
        this.details = new Response<>(s);
    }

    public GenericException(Integer code, Throwable throwable) {
        super(throwable);
        this.code = code;
    }

    public GenericException(Integer code, String s, Throwable throwable, boolean b, boolean b1) {
        super(s, throwable, b, b1);
        this.code = code;
        this.details = new Response<>(s);
    }

}
