package ru.ayukov.diplom.repos;

import org.springframework.data.repository.CrudRepository;
import ru.ayukov.diplom.domain.Employee;
import ru.ayukov.diplom.domain.Sprint;
import ru.ayukov.diplom.domain.Task;
import ru.ayukov.diplom.domain.*;

import java.util.List;
import java.util.Optional;

public interface TaskRepo extends CrudRepository<Task, Long> {


    Optional<Task> findByTitle(String title);
    List<Task> findBySprint(Sprint sprint);
    List<Task> findByEmployee(Employee employee);
    Optional<Task> findById(Long id);

}
