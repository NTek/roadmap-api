package com.ramotion.roadmap.exceptions;

import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

/**
 * Default behavior for exception handlers
 * Custom handlers placed in relevant controllers
 */
@ControllerAdvice
@ResponseBody
public class DefaultExceptionHandler {

    private static final Logger LOG = Logger.getLogger(DefaultExceptionHandler.class.getName());

    public static final String DEFAULT_ERROR_VIEW = "exception";

    @ExceptionHandler(value = ValidationException.class)
    public Object validationException(HttpServletRequest req, ValidationException e) {
        return defaultExceptionView(req, e);
    }

    @ExceptionHandler(value = NotFoundException.class)
    public Object notFoundException(HttpServletRequest req, ValidationException e) {
        return defaultExceptionView(req, e);
    }

    @ExceptionHandler(value = UnauthorizedException.class)
    public Object unauthorizedException(HttpServletRequest req, ValidationException e) {
        return defaultExceptionView(req, e);
    }

    //spring's request binding error handler
    @ExceptionHandler(value = ServletRequestBindingException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Object invalidRequest(HttpServletRequest req, Exception e) throws Exception {
        return defaultExceptionView(req, e);
    }

    //spring's access denied handler
    @ExceptionHandler(value = AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public Object accessDenied(HttpServletRequest req, Exception e) throws Exception {
        return defaultExceptionView(req, e);
    }

    //All exceptions
    @ExceptionHandler(value = Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Object errorsHandler(HttpServletRequest req, HttpServletResponse res, Exception e) throws Exception {
        LOG.error("Unhandled exception detected!", e);
        return defaultExceptionView(req, e);
    }


    private HashMap defaultExceptionView(HttpServletRequest req, Exception e) {
        HashMap<String, Object> resp = new HashMap<>();
        resp.put("exception", e.getClass().getSimpleName());
        resp.put("message", e.getMessage());
        return resp;
    }

//    private ModelAndView defaultExceptionView(HttpServletRequest req, Exception e) {
//        // Otherwise setup and send the user to a default error-view.
//        ModelAndView mav = new ModelAndView();
//        mav.addObject("exception", e);
//        mav.addObject("url", req.getRequestURL());
//        mav.setViewName(DEFAULT_ERROR_VIEW);
//        return mav;
//    }
}
