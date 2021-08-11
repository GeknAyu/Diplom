package ru.ayukov.diplom.repos;


import org.springframework.data.jpa.repository.JpaRepository;
import ru.ayukov.diplom.domain.Board;
import ru.ayukov.diplom.domain.Team;

import java.util.Optional;


public interface BoardRepo extends JpaRepository<Board, Long> {
    Optional<Board> findByNameBoard(String nameBoard);
    Optional<Board> findByTeam(Team team);
    Optional<Board> findById(Long id);
}
