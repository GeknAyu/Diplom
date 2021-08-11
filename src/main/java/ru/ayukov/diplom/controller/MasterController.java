package ru.ayukov.diplom.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.ayukov.diplom.domain.*;
import ru.ayukov.diplom.repos.*;


import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping("/master")
public class MasterController {


    @Autowired
    private SprintRepo sprintRepo;
    @Autowired
    private TaskRepo taskRepo;
    @Autowired
    private EmployeeRepo employeeRepo;
    @Autowired
    private TaskRequestRepo taskRequestRepo;
    @Autowired
    private UsersRepo usersRepo;
    @Autowired
    private TaskReportChangeRepo taskReportChangeRepo;


    @GetMapping("/setEmployeeTask/{id}")
    public String setEmployeeTaskPage(@PathVariable Long id, Map<String, Object> model) {

        Task task = taskRepo.findById(id).get();
        List<TaskRequest> taskRequests = taskRequestRepo.findByTask(task);
        Sprint sprint = task.getSprint();
        List <Employee> listEmployee = sprint.getBoard().getTeam().getEmployee();
        List <Employee> listEmployeeCheck = sprint.getBoard().getTeam().getEmployee();

        List<TaskRequest> taskRequestsCheck = taskRequestRepo.findByTask(task);
        for(TaskRequest trc : taskRequestsCheck){

            if(trc.getStatusTaskRequest() == StatusTaskRequest.NOT_ACTIVE){
                taskRequests.remove(trc);
            }

        }
        for(Employee e : listEmployeeCheck) {
            List<Task> taskCheck = taskRepo.findByEmployee(e);
            for(Task t : taskCheck) {
                if((t.getStatusTask()== StatusTask.STARTED)||(t.getStatusTask()== StatusTask.WAITING)){
                    listEmployee.remove(e);
                }
            }
        }

        model.put("label", "Выберете работника для выполнения работы");
        model.put("task", task);
        model.put("idTask", id);
        model.put("listEmployee", listEmployee);
        model.put("taskRequests", taskRequests);
        return "set_employee_task_page";
    }
    @PostMapping("/postSetEmployeeTask")
    public String postSetEmployeeTask(@RequestParam Long idTask, @RequestParam Long idEmployee,Map<String, Object> model) {
        Task task = taskRepo.findById(idTask).get();
        Employee employee = employeeRepo.findById(idEmployee).get();
        List<Task> taskCheck = taskRepo.findByEmployee(employee);
        for(Task t : taskCheck) {
            if((t.getStatusTask() == StatusTask.STARTED)||(t.getStatusTask() == StatusTask.WAITING)){
                List<TaskRequest> taskRequests = taskRequestRepo.findByTask(task);
                Sprint sprint = task.getSprint();
                List <Employee> listEmployee = sprint.getBoard().getTeam().getEmployee();
                List <Employee> listEmployeeCheck = sprint.getBoard().getTeam().getEmployee();

                for(Employee e : listEmployeeCheck) {
                    List<Task> taskCheck1 = taskRepo.findByEmployee(e);
                    for(Task t1 : taskCheck1) {
                        if((t1.getStatusTask()== StatusTask.STARTED)||(t1.getStatusTask()== StatusTask.WAITING)){
                            listEmployee.remove(e);
                        }
                    }
                }
                model.put("label", "Вы не можете поставить этого работника");
                model.put("task", task);
                model.put("idTask", idTask);
                model.put("listEmployee", listEmployee);
                model.put("taskRequests", taskRequests);
                return "set_employee_task_page";
            }
        }
        task.setEmployee(employee);
        task.setStatusTask(StatusTask.STARTED);
        taskRepo.save(task);

        List<TaskRequest> taskRequests = taskRequestRepo.findByTask(task);
        List<TaskRequest> taskRequestsCheck = taskRequestRepo.findByTask(task);
        for(TaskRequest tr : taskRequests){

            tr.setStatusTaskRequest(StatusTaskRequest.NOT_ACTIVE);
            taskRequestRepo.save(tr);

        }
        for(TaskRequest trc : taskRequestsCheck){

            if(trc.getStatusTaskRequest() == StatusTaskRequest.NOT_ACTIVE){
                taskRequests.remove(trc);
            }

        }
        Sprint sprint = task.getSprint();
        List <Employee> listEmployee = sprint.getBoard().getTeam().getEmployee();
        List <Employee> listEmployeeCheck = sprint.getBoard().getTeam().getEmployee();

        for(Employee e : listEmployeeCheck) {
            List<Task> taskCheck1 = taskRepo.findByEmployee(e);
            for(Task t1 : taskCheck1) {
                if((t1.getStatusTask()== StatusTask.STARTED)||(t1.getStatusTask()== StatusTask.WAITING)){
                    listEmployee.remove(e);
                }
            }
        }
        model.put("label", "Работник поставлен на задачу");
        model.put("task", task);
        model.put("idTask", idTask);
        model.put("listEmployee", listEmployee);
        model.put("taskRequests", taskRequests);

        return "set_employee_task_page";
    }

    @GetMapping("/listTaskWorkMaster/{id}")
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

        List<Task> taskStartedEmp = new ArrayList<Task>();
        List<Task> taskWaitingEmp = new ArrayList<Task>();
        List<Task> taskFinishedEmp = new ArrayList<Task>();
        List<Task> taskBugEmp = new ArrayList<Task>();

        List<Task> taskList = taskRepo.findBySprint(sprint);
        for (Task t : taskList){
            if(t.getStatusTask() == StatusTask.NOT_STARTED){
                taskNotStarted.add(t);
            }
            if(t.getStatusTask() == StatusTask.STARTED){
                if(t.getEmployee() == employee){
                    taskStartedEmp.add(t);
                }
                else {
                    taskStarted.add(t);
                }
            }
            if(t.getStatusTask() == StatusTask.WAITING){
                if(t.getEmployee() == employee){
                    taskWaitingEmp.add(t);
                }
                else {
                    taskWaiting.add(t);
                }
            }
            if(t.getStatusTask() == StatusTask.FINISHED){
                if(t.getEmployee() == employee){
                    taskFinishedEmp.add(t);
                }
                else {
                    taskFinished.add(t);
                }
            }
            if(t.getStatusTask() == StatusTask.BUG){
                if(t.getEmployee() == employee){
                    taskBugEmp.add(t);
                }
                else {
                    taskBug.add(t);
                }
            }
        }
        List<Task> taskStartedReport = new ArrayList<Task>();
        List<Task> taskFinishedReport = new ArrayList<Task>();
        List<Task> taskBugReport = new ArrayList<Task>();

        List<TaskReportChange> taskReportChangeList = taskReportChangeRepo.findByEmployee(employee);

        for(TaskReportChange trc : taskReportChangeList){
            Date dateNow = new Date();
            SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
            if(format.format(dateNow).equals(format.format(trc.getDateReport()))){
                if(trc.getNewStatus() == StatusTask.BUG){
                    taskBugReport.add(trc.getTask());
                    taskBug.remove(trc.getTask());
                    taskBugEmp.remove(trc.getTask());
                }
                if(trc.getNewStatus() == StatusTask.FINISHED){
                    taskFinishedReport.add(trc.getTask());
                    taskFinished.remove(trc.getTask());
                    taskFinishedEmp.remove(trc.getTask());
                }
                if(trc.getNewStatus() == StatusTask.STARTED){
                    taskStartedReport.add(trc.getTask());
                    taskStarted.remove(trc.getTask());
                    taskStartedEmp.remove(trc.getTask());
                }
            }
        }

        model.put("taskNotStarted", taskNotStarted);
        model.put("taskStarted", taskStarted);
        model.put("taskWaiting", taskWaiting);
        model.put("taskFinished", taskFinished);
        model.put("taskBug", taskBug);

        model.put("taskStartedEmp", taskStartedEmp);
        model.put("taskWaitingEmp", taskWaitingEmp);
        model.put("taskFinishedEmp", taskFinishedEmp);
        model.put("taskBugEmp", taskBugEmp);

        model.put("taskStartedReport", taskStartedReport);
        model.put("taskFinishedReport", taskFinishedReport);
        model.put("taskBugReport", taskBugReport);

        return "list_task_work_master_page";
    }

}
