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
import java.util.*;

@Controller
@RequestMapping("/developer")
public class DeveloperController {

    @Autowired
    private BoardRepo boardRepo;
    @Autowired
    private TeamRepo teamRepo;
    @Autowired
    private SprintRepo sprintRepo;
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
    @GetMapping("/taskRequestCreate/{id}")
    public String taskRequestPage(@PathVariable Long id, Map<String, Object> model) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Users users = usersRepo.findByUserName(user.getUsername()).get();
        Employee employee = employeeRepo.findByUsers(users).get();
        List<TaskRequest> taskRequests = taskRequestRepo.findByEmployee(employee);
        Integer trCheck = 0;
        Task taskById = taskRepo.findById(id).get();
        for(TaskRequest tr : taskRequests) {
            if(tr.getStatusTaskRequest() == StatusTaskRequest.ACTIVE){
                trCheck += 1;
            }

            if(taskById == tr.getTask()){

                model.put("id", id);
                model.put("label", "Вы уже создали запрос на эту задачу!");
                model.put("hidden", "hidden");
                return "create_task_request_page";
            }
        }
        String hidden;
        List<Task> task = taskRepo.findByEmployee(employee);
        if(!task.isEmpty()){
            for(Task t : task) {
                if ((t.getStatusTask() == StatusTask.STARTED) || (t.getStatusTask() == StatusTask.WAITING)) {
                    model.put("label", "У вас уже есть не завершенная задача!");

                    model.put("hidden", "hidden");
                    model.put("id", id);
                    return "create_task_request_page";
                }
            }
        }
        if((trCheck == 2) || (trCheck > 2)){
            hidden = "hidden";
            model.put("label", "Вы не можете создать больше двух запросов на задачи!");
        }
        else{
            hidden = "submit";
            model.put("label", "Вы точно хотите созать запрос на эту задачу!");
        }
        model.put("id", id);
        model.put("hidden", hidden);
        return "create_task_request_page";
    }
    @PostMapping("/postCreateTaskRequest")
    public String postCreateTaskRequest(@RequestParam Long id,Map<String, Object> model) {
         User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
         Users users = usersRepo.findByUserName(user.getUsername()).get();
         Employee employee = employeeRepo.findByUsers(users).get();
         List<TaskRequest> taskRequests = taskRequestRepo.findByEmployee(employee);
         Integer trCheck = 0;
         Task taskById = taskRepo.findById(id).get();
         for(TaskRequest tr : taskRequests) {
             trCheck += 1;
             if(taskById == tr.getTask()){

                 model.put("id", id);
                 model.put("label", "Вы уже создали запрос на эту задачу!");
                 model.put("hidden", "hidden");
                 return "create_task_request_page";
             }
         }
         String hidden;
         List<Task> task = taskRepo.findByEmployee(employee);
        for(Task t : task) {
            if ((t.getStatusTask() == StatusTask.STARTED) || (t.getStatusTask() == StatusTask.WAITING)) {
                model.put("label", "У вас уже есть не завершенная задача!");

                model.put("hidden", "hidden");
                model.put("id", id);
                return "create_task_request_page";
            }
        }
         if((trCheck == 2) || (trCheck > 2)){
             hidden = "hidden";
             model.put("label", "Вы не можете создать больше двух запросов на задачи!");
         }
         else{
             hidden = "hidden";
             model.put("label", "Вы создали запрос!");
             Task selectTask = taskRepo.findById(id).get();
             TaskRequest taskRequest = new TaskRequest(StatusTaskRequest.ACTIVE,selectTask,employee);
             taskRequestRepo.save(taskRequest);
         }

         model.put("id", id);
         model.put("hidden", hidden);
        return "create_task_request_page";
    }









    @GetMapping("/taskReportCreateUpdate/{id}")
    public String taskReportCreateUpdatePage(@PathVariable Long id, Map<String, Object> model) {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Users users = usersRepo.findByUserName(user.getUsername()).get();
        Employee employee = employeeRepo.findByUsers(users).get();

        Task task = taskRepo.findById(id).get();
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        TaskReport findTaskReport = null;
        List<TaskReport> listTaskReport = taskReportRepo.findByTask(task);
        Date dateNow = new Date();
        for(TaskReport tr : listTaskReport){
            String dateTr = format.format(tr.getDateReport());
            if((tr.getEmployee() == employee)&&(dateTr.equals(format.format(dateNow)))){
                findTaskReport = tr;
                model.put("reportId", findTaskReport.getId());
                model.put("reportText", findTaskReport.getTextReport());
                model.put("reportTitle", findTaskReport.getTitleReport());
                model.put("label", "Измените отчет");
            }
        }
        if(findTaskReport == null){
            model.put("reportId", "");
            model.put("reportText", "");
            model.put("reportTitle", "");

            model.put("label", "Создайте отчет");
        }
        model.put("task", task);
        model.put("id", id);
        return "create_task_report_page";
    }

    @PostMapping("/postCreateUpdateTaskReport")
    public String postCreateUpdateTaskReport(@RequestParam Long id,@RequestParam String idReport,@RequestParam String text,@RequestParam String title,Map<String, Object> model) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Users users = usersRepo.findByUserName(user.getUsername()).get();
        Employee employee = employeeRepo.findByUsers(users).get();
        TaskReport report;
        Task task = taskRepo.findById(id).get();
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        if(!idReport.equals("")){
            Long idReportLong = Long.parseLong(idReport, 10);
            report = taskReportRepo.findById(idReportLong).get();
            report.setTextReport(text);
            report.setTitleReport(title);
            taskReportRepo.save(report);
        }
        else{
            Date dateNow = new Date();
            report = new TaskReport(title,text,dateNow,task,employee);
            taskReportRepo.save(report);
        }

        TaskReport findTaskReport = null;
        List<TaskReport> listTaskReport = taskReportRepo.findByTask(task);
        Date dateNow = new Date();
        for(TaskReport tr : listTaskReport){
            String dateTr = format.format(tr.getDateReport());
            if((tr.getEmployee() == employee)&&(dateTr.equals(format.format(dateNow)))){
                findTaskReport = tr;
                model.put("reportId", findTaskReport.getId());
                model.put("reportText", findTaskReport.getTextReport());
                model.put("reportTitle", findTaskReport.getTitleReport());
                model.put("label", "Измените отчет");
            }
        }
        if(findTaskReport == null){
            model.put("reportId", "");
            model.put("reportText", "");
            model.put("reportTitle", "");

            model.put("label", "Создайте отчет");
        }
        model.put("task", task);
        model.put("id", id);
        return "create_task_report_page";
    }






    @GetMapping("/taskReportChange/{id}")
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
                model.put("reportLink", findTaskReport.getLinkReport());
                model.put("label", "Измените отчет");
            }
        }
        if((findTaskReport == null)&&(task.getStatusTask() == StatusTask.STARTED)){
            model.put("reportId", "");
            model.put("reportText", "");
            model.put("reportTitle", "");
            model.put("reportLink", "");
            model.put("label", "Создайте отчет");
        }
        else if((findTaskReport == null)&&(task.getStatusTask() != StatusTask.STARTED)){
            model.put("reportId", "");
            model.put("reportText", "");
            model.put("reportTitle", "");
            model.put("reportLink", "");
            model.put("label", "Отчет уже создан и его нельзя изменить");
        }
        model.put("task", task);
        model.put("id", id);
        return "create_task_report_change_page";
    }

    @PostMapping("/postChangeTaskReport")
    public String postCreateUpdateTaskReport(@RequestParam Long id,@RequestParam String idReport,@RequestParam String link,@RequestParam String text,@RequestParam String title,Map<String, Object> model) {
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
                report.setLinkReport(title);
                taskReportChangeRepo.save(report);
            }


        }
        else if(task.getStatusTask() == StatusTask.STARTED){
            report = new TaskReportChange(title,text,link,dateNow,task,employee,StatusTask.WAITING);
            task.setStatusTask(StatusTask.WAITING);
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
                model.put("reportLink", findTaskReport.getLinkReport());
                model.put("label", "Измените отчет");
            }
        }
        if((findTaskReport == null)&&(task.getStatusTask() == StatusTask.STARTED)){
            model.put("reportId", "");
            model.put("reportText", "");
            model.put("reportTitle", "");
            model.put("reportLink", "");
            model.put("label", "Создайте отчет");
        }
        else if((findTaskReport == null)&&(task.getStatusTask() != StatusTask.STARTED)){
            model.put("reportId", "");
            model.put("reportText", "");
            model.put("reportTitle", "");
            model.put("reportLink", "");
            model.put("label", "Отчет уже создан и его нельзя изменить");
        }
        model.put("task", task);
        model.put("id", id);
        return "create_task_report_change_page";
    }




    @GetMapping("/taskReportBug/{id}")
    public String taskReportBugPage(@PathVariable Long id, Map<String, Object> model) {

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
                model.put("reportLink", findTaskReport.getLinkReport());
                model.put("label", "Измените отчет");
            }
        }
        if((findTaskReport == null)&&(task.getStatusTask() == StatusTask.FINISHED)){
            model.put("reportId", "");
            model.put("reportText", "");
            model.put("reportTitle", "");
            model.put("reportLink", "");
            model.put("label", "Создайте отчет");
        }
        else if((findTaskReport == null)&&(task.getStatusTask() != StatusTask.FINISHED)){
            model.put("reportId", "");
            model.put("reportText", "");
            model.put("reportTitle", "");
            model.put("reportLink", "");
            model.put("label", "Отчет уже создан и его нельзя изменить");
        }
        model.put("task", task);
        model.put("id", id);
        return "create_task_report_bug_page";
    }

    @PostMapping("/postBugTaskReport")
    public String postBugUpdateTaskReport(@RequestParam Long id,@RequestParam String idReport,@RequestParam String link,@RequestParam String text,@RequestParam String title,Map<String, Object> model) {
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
                report.setLinkReport(title);
                taskReportChangeRepo.save(report);
            }


        }
        else if(task.getStatusTask() == StatusTask.FINISHED){
            report = new TaskReportChange(title,text,link,dateNow,task,employee,StatusTask.BUG);
            task.setStatusTask(StatusTask.BUG);
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
                model.put("reportLink", findTaskReport.getLinkReport());
                model.put("label", "Измените отчет");
            }
        }
        if((findTaskReport == null)&&(task.getStatusTask() == StatusTask.FINISHED)){
            model.put("reportId", "");
            model.put("reportText", "");
            model.put("reportTitle", "");
            model.put("reportLink", "");
            model.put("label", "Создайте отчет");
        }
        else if((findTaskReport == null)&&(task.getStatusTask() != StatusTask.FINISHED)){
            model.put("reportId", "");
            model.put("reportText", "");
            model.put("reportTitle", "");
            model.put("reportLink", "");
            model.put("label", "Отчет уже создан и его нельзя изменить");
        }
        model.put("task", task);
        model.put("id", id);
        return "create_task_report_bug_page";
    }










    @GetMapping("/taskReportAcceptBug/{id}")
    public String taskReportAcceptBugPage(@PathVariable Long id, Map<String, Object> model) {

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
                model.put("reportLink", findTaskReport.getLinkReport());
                model.put("label", "Измените отчет");
            }
        }
        if((findTaskReport == null)&&(task.getStatusTask() == StatusTask.BUG)){
            model.put("reportId", "");
            model.put("reportText", "");
            model.put("reportTitle", "");
            model.put("reportLink", "");
            model.put("label", "Создайте отчет");
        }
        else if((findTaskReport == null)&&(task.getStatusTask() != StatusTask.BUG)){
            model.put("reportId", "");
            model.put("reportText", "");
            model.put("reportTitle", "");
            model.put("reportLink", "");
            model.put("label", "Отчет уже создан и его нельзя изменить");
        }
        model.put("task", task);
        model.put("id", id);
        return "create_task_report_accept_bug_page";
    }

    @PostMapping("/postAcceptBugTaskReport")
    public String postAcceptBugTaskReport(@RequestParam Long id,@RequestParam String idReport,@RequestParam String link,@RequestParam String text,@RequestParam String title,Map<String, Object> model) {
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
                report.setLinkReport(title);
                taskReportChangeRepo.save(report);
            }


        }
        else if(task.getStatusTask() == StatusTask.BUG){
            report = new TaskReportChange(title,text,link,dateNow,task,employee,StatusTask.FINISHED);
            task.setStatusTask(StatusTask.FINISHED);
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
                model.put("reportLink", findTaskReport.getLinkReport());
                model.put("label", "Измените отчет");
            }
        }
        if((findTaskReport == null)&&(task.getStatusTask() == StatusTask.BUG)){
            model.put("reportId", "");
            model.put("reportText", "");
            model.put("reportTitle", "");
            model.put("reportLink", "");
            model.put("label", "Создайте отчет");
        }
        else if((findTaskReport == null)&&(task.getStatusTask() != StatusTask.BUG)){
            model.put("reportId", "");
            model.put("reportText", "");
            model.put("reportTitle", "");
            model.put("reportLink", "");
            model.put("label", "Отчет уже создан и его нельзя изменить");
        }
        model.put("task", task);
        model.put("id", id);
        return "create_task_report_accept_bug_page";
    }





    @GetMapping("/listSprintDeveloper/{id}")
    public String listSprintDeveloperPage(@PathVariable Long id, Map<String, Object> model) {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Users users = usersRepo.findByUserName(user.getUsername()).get();
        Employee employee = employeeRepo.findByUsers(users).get();
        Board board = boardRepo.findById(id).get();
        List<Sprint> sprintList = sprintRepo.findByBoard(board);
        Sprint sprint = new Sprint();
        for(Sprint s : sprintList){
            Date dateS = s.getDateStart();
            Date dateE = s.getDateEnd();
            Date dateNow = new Date();
            if((dateNow.after(dateS))&&(dateNow.before(dateE))){
                sprint = s;
            }
        }
        model.put("sprint", sprint);
        model.put("sprintList", sprintList);
        return "list_sprint_developer_page";
    }
    @GetMapping("/listTaskWorkDeveloper/{id}")
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

        return "list_task_work_developer_page";
    }

}
