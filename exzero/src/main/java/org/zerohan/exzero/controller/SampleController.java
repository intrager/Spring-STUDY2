package org.zerohan.exzero.controller;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.zerohan.exzero.domain.SampleDTO;
import org.zerohan.exzero.domain.SampleDTOList;
import org.zerohan.exzero.domain.TodoDTO;

import java.util.Arrays;

@Controller
@RequestMapping(value ="/sample")
public class SampleController {
  private static final Logger log = LogManager.getLogger(BoardController.class);

  @GetMapping("/basic")
  public void basic() {
    log.info("basic-------------------------");
  }



//  @GetMapping("/ex1")
//  public void ex1(SampleDTO sampleDTO) {
//    log.info("ex1-----------------");
//    log.info(sampleDTO);
//
//  }

  @GetMapping("/ex1")
  public void ex1(
          @RequestParam("name") String name,
          @RequestParam(
                  value ="age",
                  required = false,
                  defaultValue = "10") int age
  ) {
    log.info("ex1-----------------");
    log.info(name);
    log.info(age);
    //log.info(sampleDTO);
  }

  @GetMapping("/ex02Array")
  public String ex02Array( String[] ids ){

    log.info("======================");
    log.info(Arrays.toString(ids));
    return "/sample/ex2";
  }

  @GetMapping("/ex02Bean")
  public void ex02Bean(SampleDTOList list){
    log.info(list);
  }

  @GetMapping("/ex03")
  public void ex03(TodoDTO todoDTO){

    log.info("------------------");
    log.info(todoDTO);
  }

  @GetMapping("/ex04")
  public void ex04(
          @ModelAttribute("dto") SampleDTO dto,
          @ModelAttribute("page") int page,
          Model model){

    model.addAttribute("list", new String[]{"AAA","BBB","CCC"});

  }

  @GetMapping("/ex05")
  public String ex05(RedirectAttributes rttr){

    rttr.addAttribute("v1","ABC");
    rttr.addAttribute("v2","XYZ");

    rttr.addFlashAttribute("core", "ABCDE");

    return "redirect:/sample/basic";
  }

}
