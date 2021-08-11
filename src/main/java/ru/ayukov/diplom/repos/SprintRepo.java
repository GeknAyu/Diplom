package ru.ayukov.diplom.repos;


import org.springframework.data.jpa.repository.JpaRepository;
import ru.ayukov.diplom.domain.Board;
import ru.ayukov.diplom.domain.Sprint;

import java.util.List;
import java.util.Optional;


public interface SprintRepo extends JpaRepository<Sprint, Long> {

    Optional<Sprint> findById(Long id);

    Optional<Sprint> findByNameSprint(String s);
    List<Sprint> findByBoard(Board board);
}
