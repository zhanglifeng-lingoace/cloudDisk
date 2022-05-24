package com.edu.cloudDisk.core.exception;

import com.edu.common.response.CommonResult;
import com.edu.cloudDisk.core.log.LogPrint;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

@ControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseBody
    public CommonResult handleConstraintViolationException(ConstraintViolationException e, HttpServletRequest httpServletRequest) {
        for (ConstraintViolation constraintViolation : e.getConstraintViolations()) {
            return CommonResult.failed(500, constraintViolation.getMessageTemplate(), null);
        }

        return CommonResult.failed(500, e.getMessage(), null);
    }

    @ExceptionHandler(value= MethodArgumentNotValidException.class)
    @ResponseBody
    public CommonResult HandleByException(MethodArgumentNotValidException ex) {
        BindingResult bindingResult = ex.getBindingResult();
        return CommonResult.failed(bindingResult.getFieldError().getDefaultMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public CommonResult handleException(Exception e, HttpServletRequest httpServletRequest) {
        LogPrint.error(httpServletRequest, e);
        return CommonResult.failed(500, e.getMessage());
    }
}
