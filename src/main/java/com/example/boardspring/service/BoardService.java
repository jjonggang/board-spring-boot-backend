package com.example.boardspring.service;

import com.example.boardspring.entity.Board;
import com.example.boardspring.repository.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service // Service임을 지정해주는 annotation
// Service는 controller에서 이용한다.
public class BoardService {

    @Autowired // dependency injection, Autowired가 있으면, Spring Bean이 알아서 읽어와서 injection을 해준다.
    private BoardRepository boardRepository; // repository를 선언해준다. Autowired가 알아서 injection해줌.

    // 글 작성 처리
    public void write(Board board){ // write함수를 만들고, 입력으로는 Board entity를 넣어준다.
        boardRepository.save(board); // boardRepository의 함수 save()의 임력으로 write함수에서 받아오는 Board Entity를 그대로 넣어준다.
        // Repository 상에 Board 타입으로 하나를 저장한다.
    }

    // 게시글 리스트 처리
    public Page<Board> boardList(Pageable pageable){
    //http://localhost:8080/board/list?page=3&size=20
        return boardRepository.findAll(pageable); // List<Board> Board라는 클래스가 담긴 리스트를 반환해준다.
        // Repository 상의 Board를 모두 찾아서 List에 담아서 반환해준다.
        // Pageable이 있는 경우 Page라는 클래스로 반환한다.
        // Pageable이라는 클래스를 findAll에 넘겨주게 되면, 그 안에 페이지가 몇 페이지인지, 한 페이지에 보여줄 개수가 몇개인지를 담아서 반환할 수 있다.

    }

    public Page<Board> boardSearchList(String searchKeyword, Pageable pageable){

        return boardRepository.findByTitleContaining(searchKeyword, pageable);
        // 리포지토리를 수정하고, 수정된 것을 서비스에서 사용했으니, 컨트롤러도 수정해줘야한다.
    }

    // 특정 게시글 불러오기
    public Board boardView(Integer id){
        return boardRepository.findById(id).get();
        // id로 해당하는 Board를 찾아서 반환해준다.
    }

    // 특정 게시글 삭제 처리
    public void boardDelete(Integer id){
        boardRepository.deleteById(id);
        // id로 해당하는 Board를 찾아서 삭제한다.
    }



}
