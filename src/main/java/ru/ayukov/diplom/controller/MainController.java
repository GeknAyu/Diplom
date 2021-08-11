package ru.ayukov.diplom.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.ayukov.diplom.domain.*;
import ru.ayukov.diplom.repos.*;
import ru.ayukov.diplom.domain.*;
import ru.ayukov.diplom.repos.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/main")
public class MainController {

    @Autowired
    private UsersRepo usersRepo;
    @Autowired
    private TaskRepo taskRepo;
    @Autowired
    private TaskReportRepo taskReportRepo;
    @Autowired
    private TaskReportChangeRepo taskReportChangeRepo;
    @Autowired
    private EmployeeRepo employeeRepo;
    @Autowired
    private SprintRepo sprintRepo;

    @GetMapping("/viewingTask/{id}")
    public String viewingTaskPage(@PathVariable Long id, Map<String, Object> model) {

        Task task = taskRepo.findById(id).get();
        List<TaskReport> taskReport = taskReportRepo.findByTask(task);
        List<TaskReportChange> taskReportChange = taskReportChangeRepo.findByTask(task);

        model.put("task", task);
        model.put("taskReport", taskReport);
        model.put("taskReportChange", taskReportChange);
        return "task_viewing_page";
    }


    @GetMapping("/listTaskViewing/{id}")
    public String listTaskWorkDeveloperPage(@PathVariable Long id, Map<String, Object> model) {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Users users = usersRepo.findByUserName(user.getUsername()).get();
        Employee employee = employeeRepo.findByUsers(users).get();

        Sprint sprint = sprintRepo.findById(id).get();

        List<Task> taskNotStarted = new ArrayList<Task>();
        List<Task> taskStarted = new ArrayList<Task>();
        List<Task> taskWaiting = new ArrayList<Task>();
        List<Task> taskFinished = new ArrayList<Task>();
        List<Task> taskBug = new ArrayList<Task>();


        List<Task> taskList = taskRepo.findBySprint(sprint);
        for (Task t : taskList){
            if(t.getStatusTask() == StatusTask.NOT_STARTED){
                taskNotStarted.add(t);
            }
            if(t.getStatusTask() == StatusTask.STARTED){

                taskStarted.add(t);

            }
            if(t.getStatusTask() == StatusTask.WAITING){

                taskWaiting.add(t);

            }
            if(t.getStatusTask() == StatusTask.FINISHED){

                taskFinished.add(t);

            }
            if(t.getStatusTask() == StatusTask.BUG){

                taskBug.add(t);

            }
        }

        model.put("taskNotStarted", taskNotStarted);
        model.put("taskStarted", taskStarted);
        model.put("taskWaiting", taskWaiting);
        model.put("taskFinished", taskFinished);
        model.put("taskBug", taskBug);


        return "list_task_viewing_page";
    }

}
