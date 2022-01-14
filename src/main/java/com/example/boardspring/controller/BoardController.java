package com.example.boardspring.controller;

import com.example.boardspring.entity.Board;
import com.example.boardspring.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller // 스프링이 재실행할 때 이곳이 컨트롤러라는 것을 인지할 수 있도록 해준다.
public class BoardController {

    @Autowired
    private BoardService boardService; // BoardController에서 BoardService를 인지할 수 있도록 주입해준다.

    @GetMapping("/board/write") //localhost:8080/board/write
    public String boardWriteForm(){

        return "boardwrite";
    }

    @PostMapping("/board/writepro")
    public String boardWritePro(Board board, Model model){
        boardService.write(board);

        model.addAttribute("message", "글 작성이 완료되었습니다.");
//        model.addAttribute("message", "글 작성이 완료되지 않았습니다.");

        model.addAttribute("searchUrl", "/board/list");

        return "message";
    }

    @GetMapping("/board/list")
    public String boardList(Model model,
                            @PageableDefault(page = 0, size=10, sort="id", direction = Sort.Direction.DESC) Pageable pageable,
                            String searchKeyword
    ){  // 데이터를 담아서 보게될 페이지로 넘겨줘야한다. 이럴 때 Model을 사용한다.
        // http://localhost:8080/board/list?page=3&size=20

        // searchKeyword가 존재하는 경우와 존재하지않는 경우를 구분해야한다.

        Page<Board> list = null;

        if(searchKeyword == null){ // 일반적인 view
            list = boardService.boardList(pageable);
        }else{ // 검색기능 수행
            list = boardService.boardSearchList(searchKeyword, pageable);
        }



        int nowPage = list.getPageable().getPageNumber()+1;
        int startPage = Math.max(nowPage-4, 1); // 두 값 중 높은 값을 반환한다. 음수 페이지가 없도록 한다.
        int endPage = Math.min(nowPage+5, list.getTotalPages());

        model.addAttribute("list", list);
        // list라는 이름으로 보낼건데, boardService의 boardList()를
        // 실행해서 반환되는 리스트를 list라는 이름으로 받아서 넘긴다는 의미.
        // boardList로 pageable을 넘겨준다. @PageableDefault를 이용해 설정한 pageable을 넘겨준다.


        model.addAttribute("nowPage", nowPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        // nowPage -> 현재 페이지
        // startPage -> 블럭에서 보여줄 시작 페이지
        // endPage -> 블럭에서 보여줄 마지막 페이지


        return "boardlist";
    }

    @GetMapping("/board/view") // localhost:8080/board/view?id=1
    public String boardView(Model model, Integer id) { // 다시 넘겨줄 때는 매개변수에 Model을 적어줘야한다.

        model.addAttribute("article", boardService.boardView(id));
        return "boardview";
    }

    @GetMapping("/board/delete")
    public String boardDelete(Integer id){
        boardService.boardDelete(id);
        // 삭제한 뒤에 어디로 가야할지?
        // 삭제한 뒤에 return문을 따라서 board/list 경로로 돌아간다.
        return "redirect:/board/list";
    }

    @GetMapping("/board/modify/{id}")
    public String boardModify(Model model, @PathVariable("id") Integer id) {
        model.addAttribute("article", boardService.boardView(id));

        return "boardmodify";
    }

    @PostMapping("/board/update/{id}")
    public String boardUpdate(@PathVariable("id") Integer id, Board board, Model model){

        Board boardTemp = boardService.boardView(id); // boardTemp로 기존에 있던 글이 담겨져서 온다.
        boardTemp.setTitle(board.getTitle()); // 새로 작성한 객체의 내용을 덮어씌운다.
        boardTemp.setContent(board.getContent());

        boardService.write(boardTemp);

        model.addAttribute("message", "글 수정이 완료되었습니다.");
//        model.addAttribute("message", "글 작성이 완료되지 않았습니다.");

        model.addAttribute("searchUrl", "/board/list");


        return "message";
    }
}
