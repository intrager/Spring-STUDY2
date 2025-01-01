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
@RequestMapping(value = "/sample")
public class SampleController {
    private static final Logger log = LogManager.getLogger(SampleController.class);

    @GetMapping("/basic")
    public void testLog() {
        log.info("test log -----------------");
    }

    @GetMapping("/ex1")
    public void exerciseQueryParameter(
            @RequestParam("name") String name,
            @RequestParam(
                    value = "age",
                    required = false,
                    defaultValue = "10") int age
    ) {
        log.info("ex1 --------------");
        log.info("name  : {}", name);
        log.info("age  : {}", age);
    }

    @GetMapping("/ex2")
    public void exerciseDTO(SampleDTO sampleDTO) {
        log.info("ex2 --------------------");
        log.info(sampleDTO);
    }

    @GetMapping("/ex3")
    public String exerciseArray(String[] ids) {
        log.info("--------------------");
        log.info(Arrays.toString(ids));
        return "/sample/ex3";
    }

    @GetMapping("/ex4")
    public void exerciseBean(SampleDTOList list) {
        log.info(list);
    }

    @GetMapping("/ex5")
    public void exerciseDate(TodoDTO todoDTO) {
        log.info("");
    }

    @GetMapping("/ex6")
    public void exerciseModel(
            // DTO는 기본으로 모델로 전달된다
            @ModelAttribute("dto") SampleDTO dto,
            /*
             page라는 이름으로 model 객체를 쓸 거야
             Controller한테는 파라미터로 들어왔지만, @ModelAttribute는 이걸 정식 데이터로 만들어준다
             "아 얘도 Model로 간주되면 좋겠네"
             */
            @ModelAttribute("page") int page,
            Model model) {
        model.addAttribute("list", new String[]{"AAA", "BBB", "CCC"});
    }

    @GetMapping("/ex7")
    public String exerciseRedirect(RedirectAttributes rttr) {
        // Redirect 될 때 파라미터를 전달하는 용도
        rttr.addAttribute("v1", "abc");
        rttr.addAttribute("v2", "qwer");
        
        // 한 번만 전달
        rttr.addFlashAttribute("core", "asdf");
        return "redirect:/sample/basic"; // GET 방식밖에 안 됨
    }
}
