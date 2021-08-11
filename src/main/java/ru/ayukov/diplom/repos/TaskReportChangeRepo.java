package ru.ayukov.diplom.repos;

import org.springframework.data.repository.CrudRepository;
import ru.ayukov.diplom.domain.TaskReportChange;
import ru.ayukov.diplom.domain.Employee;
import ru.ayukov.diplom.domain.Task;

import java.util.List;
import java.util.Optional;

public interface TaskReportChangeRepo extends CrudRepository<TaskReportChange, Long> {


    List<TaskReportChange> findByEmployee(Employee employee);
    List<TaskReportChange> findByTask(Task task);
    Optional<TaskReportChange> findById(Long id);

}
