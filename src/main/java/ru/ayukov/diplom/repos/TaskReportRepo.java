package ru.ayukov.diplom.repos;

import org.springframework.data.repository.CrudRepository;
import ru.ayukov.diplom.domain.Employee;
import ru.ayukov.diplom.domain.Task;
import ru.ayukov.diplom.domain.TaskReport;
import ru.ayukov.diplom.domain.*;

import java.util.List;
import java.util.Optional;

public interface TaskReportRepo extends CrudRepository<TaskReport, Long> {


    List<TaskReport> findByEmployee(Employee employee);

    List<TaskReport> findByTask(Task task);
    Optional<TaskReport> findById(Long id);

}
