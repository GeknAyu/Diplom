package ru.ayukov.diplom.repos;


import org.springframework.data.jpa.repository.JpaRepository;
import ru.ayukov.diplom.domain.Team;


import java.util.List;
import java.util.Optional;


public interface TeamRepo extends JpaRepository<Team, Long> {
    Optional<Team> findByNameTeam(String nameTeam);
    Optional<Team> findById(Long id);
    List<Team> findAll();
}
