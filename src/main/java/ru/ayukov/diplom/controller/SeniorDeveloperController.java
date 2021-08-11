package ru.ayukov.diplom.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.ayukov.diplom.domain.*;
import ru.ayukov.diplom.repos.*;
import ru.ayukov.diplom.domain.*;
import ru.ayukov.diplom.repos.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/seniorDeveloper")
public class SeniorDeveloperController {


    @Autowired
    private TaskReportRepo taskReportRepo;
    @Autowired
    private TaskReportChangeRepo taskReportChangeRepo;
    @Autowired
    private TaskRepo taskRepo;
    @Autowired
    private EmployeeRepo employeeRepo;
    @Autowired
    private TaskRequestRepo taskRequestRepo;
    @Autowired
    private UsersRepo usersRepo;
    @Autowired
    private SprintRepo sprintRepo;


    @GetMapping("/taskReportAccept/{id}")
    public String taskReportChangePage(@PathVariable Long id, Map<String, Object> model) {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Users users = usersRepo.findByUserName(user.getUsername()).get();
        Employee employee = employeeRepo.findByUsers(users).get();

        Task task = taskRepo.findById(id).get();
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        TaskReportChange findTaskReport = null;
        List<TaskReportChange> listTaskReportChange = taskReportChangeRepo.findByTask(task);
        Date dateNow = new Date();
        for(TaskReportChange tr : listTaskReportChange){
            String dateTr = format.format(tr.getDateReport());
            if((tr.getEmployee() == employee)&&(dateTr.equals(format.format(dateNow)))){
                findTaskReport = tr;
                model.put("reportId", findTaskReport.getId());
                model.put("reportText", findTaskReport.getTextReport());
                model.put("reportTitle", findTaskReport.getTitleReport());
                model.put("label", "Измените отчет");
            }
        }
        if((findTaskReport == null)&&(task.getStatusTask() == StatusTask.WAITING)){
            model.put("reportId", "");
            model.put("reportText", "");
            model.put("reportTitle", "");
            model.put("label", "Создайте отчет");
        }
        else if((findTaskReport == null)&&(task.getStatusTask() != StatusTask.WAITING)){
            model.put("reportId", "");
            model.put("reportText", "");
            model.put("reportTitle", "");
            model.put("label", "Отчет уже создан и его нельзя изменить");
        }
        model.put("task", task);
        model.put("id", id);
        return "create_task_report_accept_page";
    }

    @PostMapping("/postAcceptTaskReport")
    public String postCreateUpdateTaskReport(@RequestParam Long id,@RequestParam String idReport,@RequestParam String text,@RequestParam String title,@RequestParam String status,Map<String, Object> model) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Users users = usersRepo.findByUserName(user.getUsername()).get();
        Employee employee = employeeRepo.findByUsers(users).get();
        TaskReportChange report;
        Task task = taskRepo.findById(id).get();
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        Date dateNow = new Date();


        if((!idReport.equals(""))){
            Long idReportLong = Long.parseLong(idReport, 10);
            report = taskReportChangeRepo.findById(idReportLong).get();
            if(format.format(dateNow).equals(format.format(report.getDateReport()))){
                report.setTextReport(text);
                report.setTitleReport(title);
                report.setNewStatus(StatusTask.valueOf(status));
                task.setStatusTask(StatusTask.valueOf(status));
                taskRepo.save(task);
                taskReportChangeRepo.save(report);

            }


        }
        else if(task.getStatusTask() == StatusTask.WAITING){
            report = new TaskReportChange(title,text,"",dateNow,task,employee,StatusTask.valueOf(status));
            task.setStatusTask(StatusTask.valueOf(status));
            taskRepo.save(task);
            taskReportChangeRepo.save(report);
        }

        TaskReportChange findTaskReport = null;
        List<TaskReportChange> listTaskReportChange = taskReportChangeRepo.findByTask(task);

        for(TaskReportChange tr : listTaskReportChange){
            String dateTr = format.format(tr.getDateReport());
            if((tr.getEmployee() == employee)&&(dateTr.equals(format.format(dateNow)))){
                findTaskReport = tr;
                model.put("reportId", findTaskReport.getId());
                model.put("reportText", findTaskReport.getTextReport());
                model.put("reportTitle", findTaskReport.getTitleReport());

                model.put("label", "Измените отчет");
            }
        }
        if((findTaskReport == null)&&(task.getStatusTask() == StatusTask.WAITING)){
            model.put("reportId", "");
            model.put("reportText", "");
            model.put("reportTitle", "");

            model.put("label", "Создайте отчет");
        }
        else if((findTaskReport == null)&&(task.getStatusTask() != StatusTask.WAITING)){
            model.put("reportId", "");
            model.put("reportText", "");
            model.put("reportTitle", "");

            model.put("label", "Отчет уже создан и его нельзя изменить");
        }
        model.put("task", task);
        model.put("id", id);
        return "create_task_report_accept_page";
    }

    @GetMapping("/listTaskWorkSeniorDeveloper/{id}")
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

        return "list_task_work_senior_developer_page";
    }

}
