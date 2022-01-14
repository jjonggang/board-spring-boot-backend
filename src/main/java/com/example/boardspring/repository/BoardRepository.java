package com.example.boardspring.repository;

import com.example.boardspring.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository // Repository 지정 annotation
// interface는 JpaRepository<Entity name, ID type>을 extend 해야한다.
public interface BoardRepository extends JpaRepository<Board, Integer> {
    Page<Board> findByTitleContaining(String searchKeyword, Pageable pageable);
    // 레포지토리에 메서드를 추가했으니, 서비스에도 메서드를 하나 추가해야한다.

}
