package org.springboot.integration.exception;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springboot.integration.web.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class ExceptionHandlerAdvice {
    private ExceptionHandlerAdvice(){}

    private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionHandlerAdvice.class);


    @ExceptionHandler(RepeatSubmitException.class)
    @ResponseBody
    public Result handleRepeatSubmitException(RepeatSubmitException e) {
        return Result.fail(e.getCode(),e.getMessage());
    }

    //    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)  // 500
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result handleOtherException(Exception e) {
        LOGGER.error("Unhandled exception:{}", e);
        return Result.fail(e);
    }


}
