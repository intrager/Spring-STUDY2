package org.zerohan.exzero.controller.advice;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ControllerExceptionAdvice {
    private static final Logger log = LogManager.getLogger(ControllerExceptionAdvice.class);

    @ExceptionHandler(NumberFormatException.class)
    public String exceptNumber(NumberFormatException exception, Model model) {
        log.warn("------------------------");
        log.warn(exception.getMessage());

        model.addAttribute("msg", "Number check");

        return "error_page";
    }
}
