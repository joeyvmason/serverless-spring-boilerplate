package com.joeyvmason.serverless.spring.application;

import com.joeyvmason.serverless.spring.application.exceptions.NotFoundException;
import com.joeyvmason.serverless.spring.application.exceptions.BadRequestException;
import com.joeyvmason.serverless.spring.application.exceptions.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalControllerExceptionHandler {
    private static final Logger LOG = LoggerFactory.getLogger(GlobalControllerExceptionHandler.class);

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)  // 500
    @ExceptionHandler(Throwable.class)
    public @ResponseBody ErrorResponse handleException(HttpServletRequest request, Exception e) {
        LOG.info("Unable to process request for URI({}) with ContentType({})", request.getRequestURI(), request.getContentType(), e);
        return new ErrorResponse("Error occurred. Unable to process request");
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)  // 404
    @ExceptionHandler(NotFoundException.class)
    public @ResponseBody ErrorResponse handleNotFoundException(HttpServletRequest request, NotFoundException e) {
        return new ErrorResponse(e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST) // 400
    @ExceptionHandler(BadRequestException.class)
    public @ResponseBody ErrorResponse handleBadRequestException(HttpServletRequest request, BadRequestException e) {
        return new ErrorResponse(e.getMessage());
    }

}
