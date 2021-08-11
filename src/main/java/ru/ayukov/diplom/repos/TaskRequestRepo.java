package ru.ayukov.diplom.repos;

import org.springframework.data.repository.CrudRepository;
import ru.ayukov.diplom.domain.Task;
import ru.ayukov.diplom.domain.TaskRequest;
import ru.ayukov.diplom.domain.Employee;

import java.util.List;
import java.util.Optional;

public interface TaskRequestRepo extends CrudRepository<TaskRequest, Long> {


    List<TaskRequest> findByEmployee(Employee employee);
    List<TaskRequest> findByTask(Task task);
    Optional<TaskRequest> findById(Long id);

}
