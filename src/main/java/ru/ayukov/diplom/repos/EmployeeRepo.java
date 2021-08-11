package ru.ayukov.diplom.repos;

import org.springframework.data.repository.CrudRepository;
import ru.ayukov.diplom.domain.*;
import ru.ayukov.diplom.domain.*;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepo extends CrudRepository<Employee, Long> {


    Optional<Employee> findByUsers(Users users);
    List<Employee> findAllByUsersStatus(Status status);
    List<Employee> findAllByTeam(Team team);
    List<Employee> findAllByTeamAndUsersRole(Team team, Role role);
    Optional<Employee> findById(Long aLong);
}
