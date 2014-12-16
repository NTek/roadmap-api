package com.ramotion.roadmap.exceptions;

import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * Default behavior for exception handlers
 * Custom handlers placed in relevant controllers
 */
@ControllerAdvice
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

    @ExceptionHandler(value = ServletRequestBindingException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ModelAndView invalidRequest(HttpServletRequest req, Exception e) throws Exception {
        return defaultExceptionView(req, e);
    }

    //spring's access denied handler
    @ExceptionHandler(value = AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ModelAndView accessDenied(HttpServletRequest req, Exception e) throws Exception {
        return defaultExceptionView(req, e);
    }

    @ExceptionHandler(value = Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ModelAndView errorsHandler(HttpServletRequest req, Exception e) throws Exception {
        LOG.error("Unhandled exception detected!", e);
        return defaultExceptionView(req, e);
    }

    private ModelAndView defaultExceptionView(HttpServletRequest req, Exception e) {
        // Otherwise setup and send the user to a default error-view.
        ModelAndView mav = new ModelAndView();
        mav.addObject("exception", e);
        mav.addObject("url", req.getRequestURL());
        mav.setViewName(DEFAULT_ERROR_VIEW);
        return mav;
    }
}
