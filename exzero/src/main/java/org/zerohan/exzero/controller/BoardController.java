package org.zerohan.exzero.controller;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.zerohan.exzero.domain.BoardVO;
import org.zerohan.exzero.service.BoardService;

import java.util.List;

@Controller
@RequestMapping("/board")
public class BoardController {
    private static final Logger log = LogManager.getLogger(BoardController.class);

    private final BoardService boardService;

    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }

    @GetMapping("/list")
    public void getList(Model model) {
        log.info("get board list");
        List<BoardVO> list = boardService.getList();
        log.info("board list : {}", list);
        model.addAttribute("list", list);
    }

//    @GetMapping(value = {"/read/{bno}", "/modify/{bno}"})
    @GetMapping(value = {"/{job}/{bno}"})
    public String read(
            @PathVariable(name = "job") String job,
            @PathVariable(name = "bno") Long bno, Model model) {
        log.info("job : {}", job);
        log.info("bno : {}", bno);

        if(!(StringUtils.equals(job, "read") || StringUtils.equals(job, "read"))) {
            throw new RuntimeException("Bad Job Request");
        }
        BoardVO boardVO = boardService.getOneBoard(bno);

        log.info("boardVO : {}", boardVO);
        model.addAttribute("vo", boardVO);

        return "/board/" + job;
    }

    @GetMapping("/register")
    public void register() {

    }

    @PostMapping("/register")
    public String postRegisterInfo(BoardVO boardVO, RedirectAttributes rttr) {
        log.info("boardVO : {}", boardVO);

        Long bno = boardService.register(boardVO);
        rttr.addFlashAttribute("result", bno);
        return "redirect:/board/list";
    }
//    @GetMapping("/modify/{bno}")
//    public String modify(@PathVariable(name = "bno") Long bno, Model model) {
//        log.info("bno : {}", bno);
//
//        BoardVO boardVO = boardService.getOneBoard(bno);
//        log.info("boardVO : {}", boardVO);
//
//        model.addAttribute("vo", boardVO);
//        return "/board/modify";
//    }
}
