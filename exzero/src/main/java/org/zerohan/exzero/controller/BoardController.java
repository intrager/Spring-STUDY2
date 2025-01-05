package org.zerohan.exzero.controller;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.zerohan.exzero.domain.BoardVO;
import org.zerohan.exzero.domain.Criteria;
import org.zerohan.exzero.domain.PageDTO;
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
    public void getList(@ModelAttribute Criteria criteria, Model model) {
        log.info("get board list");
        List<BoardVO> list = boardService.getListAndPage(criteria);
        log.info("board list : {}", list);
        model.addAttribute("list", list);
        log.info("criteria : {}", criteria);
        PageDTO pageDTO = new PageDTO(criteria, boardService.getTotal(criteria));
        model.addAttribute("pageMaker", pageDTO);
    }

//    @GetMapping(value = {"/read/{bno}", "/modify/{bno}"})
    @GetMapping(value = "/{job}/{bno}")
    public String read(
            @PathVariable(name = "job") String job,
            @PathVariable(name = "bno") Long bno,
            @ModelAttribute Criteria criteria,
            Model model) {
        log.info("job : {}", job);
        log.info("bno : {}", bno);

        if(!(StringUtils.equals(job, "read") || StringUtils.equals(job, "modify"))) {
            throw new RuntimeException("Bad Job Request");
        }
        BoardVO boardVO = boardService.getOneBoard(bno);

        log.info("boardVO : {}", boardVO);
        model.addAttribute("vo", boardVO);

        return "/board/" + job;
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

    @PostMapping("/modify/{bno}")
    public String modify(@PathVariable(name = "bno") Long bno,
                         BoardVO boardVO,
                         RedirectAttributes rttr) {
        boardVO.setBno(bno);

        log.info("boardVO : {}", boardVO);
        boardService.modify(boardVO);

        rttr.addFlashAttribute("result", boardVO.getBno());
        return "redirect:/board/read/" + bno;
    }

    @PostMapping("/remove/{bno}")
    public String remove(@PathVariable(name = "bno") Long bno,
                         RedirectAttributes rttr) {
        BoardVO boardVO = new BoardVO();
        boardVO.setBno(bno);
        boardVO.setTitle("해당 글은 삭제되었습니다.");
        boardVO.setContent("해당 글은 삭제되었습니다.");

        log.info("boardVO : {}", boardVO);
        boardService.modify(boardVO);

        rttr.addFlashAttribute("result", boardVO.getBno());
        return "redirect:/board/list";
    }

}
