package sample.restdocs.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import sample.restdocs.entity.Board;

public interface BoardRepository extends JpaRepository<Board, Integer> {

}
