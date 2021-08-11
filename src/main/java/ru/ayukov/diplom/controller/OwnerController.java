package ru.ayukov.diplom.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.ayukov.diplom.domain.*;
import ru.ayukov.diplom.repos.*;
import ru.ayukov.diplom.domain.*;
import ru.ayukov.diplom.repos.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping("/owner")
public class OwnerController {

    @Autowired
    private TeamRepo teamRepo;

    @Autowired
    private EmployeeRepo employeeRepo;

    @Autowired
    private BoardRepo boardRepo;

    @Autowired
    private SprintRepo sprintRepo;

    @Autowired
    private TaskRepo taskRepo;

    @GetMapping("/createTeam")
    public String createTeamPage(Map<String, Object> model) {

        model.put("label", "Введите основныйе данные команды");
        return "create_team_page";
    }

    @PostMapping("/postCreateTeam")
    public String postCreateTeam(@RequestParam String info, @RequestParam String nameTeam, Map<String, Object> model) {
        Optional<Team> findTeam = teamRepo.findByNameTeam(nameTeam);
        if (findTeam.isPresent()) {
            model.put("label", "Команда не создан: команда с таким именем уже существует!");
        } else {
            Team team = new Team(nameTeam, info);
            teamRepo.save(team);
            model.put("label", "Команда создана");
        }
        return "create_team_page";
    }

    @GetMapping("/updateTeam/{id}")
    public String updateTeamPage(@PathVariable Long id, Map<String, Object> model) {

        Team team = teamRepo.findById(id).get();
        model.put("team", team);
        model.put("label", "Измените команду");
        return "update_team_page";
    }

    @PostMapping("/postUpdateTeam")
    public String postUpdateTeam(@RequestParam Long id, @RequestParam String info, @RequestParam String nameTeam, Map<String, Object> model) {
        Optional<Team> team = teamRepo.findById(id);
        Optional<Team> checkTeam = teamRepo.findByNameTeam(nameTeam);
        Boolean check = nameTeam.equals(team.get().getNameTeam());
        if ((check == false) && (checkTeam.isPresent())) {
            Optional<Team> findTeam = teamRepo.findById(id);

            model.put("employee", findTeam.get());
            model.put("label", "Команда не изменена: команда с таким именем уже существует!");
        } else {
            team.get().setNameTeam(nameTeam);
            team.get().setInfoTeam(info);

            Team newTeam = teamRepo.save(team.get());

            model.put("team", newTeam);
            model.put("label", "Команда изменена");
        }


        return "update_team_page";
    }

    @GetMapping("/squadList/{id}")
    public String squadListPage(@PathVariable Long id, Map<String, Object> model) {

        Team team = teamRepo.findById(id).get();
        List<Employee> teamList = team.getEmployee();
        List<Employee> employeeList = employeeRepo.findAllByTeam(null);
        Boolean masterPresent = false;
        for (Employee e : teamList) {
            if (e.getUsers().getRole() == Role.MASTER) {
                masterPresent = true;
            }
        }
        List<Employee> employeeListCheck = employeeRepo.findAllByTeam(null);
        for (Employee e : employeeListCheck) {
            if (e.getUsers().getRole() == Role.OWNER) {
                employeeList.remove(e);
            } else if ((masterPresent) && (e.getUsers().getRole() == Role.MASTER)) {
                employeeList.remove(e);
            }
        }
        model.put("roleSearch", "start");
        model.put("idTeam", id);
        model.put("teamList", teamList);
        model.put("employeeList", employeeList);
        model.put("label", "Измените команду");
        return "squad_list_page";
    }

    @GetMapping("/squadListDevelopers/{id}")
    public String squadListDevelopersPage(@PathVariable Long id, Map<String, Object> model) {

        Team team = teamRepo.findById(id).get();
        List<Employee> teamList = team.getEmployee();
        List<Employee> employeeList = employeeRepo.findAllByTeamAndUsersRole(null, Role.DEVELOPERS);
        Boolean masterPresent = false;
        for (Employee e : teamList) {
            if (e.getUsers().getRole() == Role.MASTER) {
                masterPresent = true;
            }
        }
        List<Employee> employeeListCheck = employeeRepo.findAllByTeamAndUsersRole(null, Role.DEVELOPERS);
        for (Employee e : employeeListCheck) {
            if (e.getUsers().getRole() == Role.OWNER) {
                employeeList.remove(e);
            } else if ((masterPresent) && (e.getUsers().getRole() == Role.MASTER)) {
                employeeList.remove(e);
            }
        }
        model.put("roleSearch", "Developers");
        model.put("idTeam", id);
        model.put("teamList", teamList);
        model.put("employeeList", employeeList);
        model.put("label", "Измените команду");
        return "squad_list_page";
    }

    @GetMapping("/squadListSeniorDevelopers/{id}")
    public String squadListSeniorDevelopersPage(@PathVariable Long id, Map<String, Object> model) {

        Team team = teamRepo.findById(id).get();
        List<Employee> teamList = team.getEmployee();
        List<Employee> employeeList = employeeRepo.findAllByTeamAndUsersRole(null, Role.SENIOR_DEVELOPER);
        Boolean masterPresent = false;
        for (Employee e : teamList) {
            if (e.getUsers().getRole() == Role.MASTER) {
                masterPresent = true;
            }
        }
        List<Employee> employeeListCheck = employeeRepo.findAllByTeamAndUsersRole(null, Role.SENIOR_DEVELOPER);
        for (Employee e : employeeListCheck) {
            if (e.getUsers().getRole() == Role.OWNER) {
                employeeList.remove(e);
            } else if ((masterPresent) && (e.getUsers().getRole() == Role.MASTER)) {
                employeeList.remove(e);
            }
        }
        model.put("roleSearch", "Senior");
        model.put("idTeam", id);
        model.put("teamList", teamList);
        model.put("employeeList", employeeList);
        model.put("label", "Измените команду");
        return "squad_list_page";
    }

    @GetMapping("/squadListMaster/{id}")
    public String squadListMasterPage(@PathVariable Long id, Map<String, Object> model) {

        Team team = teamRepo.findById(id).get();
        List<Employee> teamList = team.getEmployee();
        List<Employee> employeeList = employeeRepo.findAllByTeamAndUsersRole(null, Role.MASTER);
        Boolean masterPresent = false;
        for (Employee e : teamList) {
            if (e.getUsers().getRole() == Role.MASTER) {
                masterPresent = true;
            }
        }
        List<Employee> employeeListCheck = employeeRepo.findAllByTeamAndUsersRole(null, Role.MASTER);
        for (Employee e : employeeListCheck) {
            if (e.getUsers().getRole() == Role.OWNER) {
                employeeList.remove(e);
            } else if ((masterPresent) && (e.getUsers().getRole() == Role.MASTER)) {
                employeeList.remove(e);
            }
        }
        model.put("roleSearch", "Master");
        model.put("idTeam", id);
        model.put("teamList", teamList);
        model.put("employeeList", employeeList);
        model.put("label", "Измените команду");
        return "squad_list_page";
    }

    @GetMapping("/squadListAdd/{id}/{idEmployee}/{roleSearch}")
    public String squadListAdd(@PathVariable Long id, @PathVariable Long idEmployee, @PathVariable String roleSearch, Map<String, Object> model) {

        Team team = teamRepo.findById(id).get();
        List<Employee> teamList = team.getEmployee();
        Employee employee = employeeRepo.findById(idEmployee).get();
        teamList.add(employee);
        employee.setTeam(team);
        team.setEmployee(teamList);
        teamRepo.save(team);

        team = teamRepo.findById(id).get();
        teamList = team.getEmployee();
        List<Employee> employeeList;
        List<Employee> employeeListCheck;
        if (roleSearch.equals("Master")) {
            employeeList = employeeRepo.findAllByTeamAndUsersRole(null, Role.MASTER);
            employeeListCheck = employeeRepo.findAllByTeamAndUsersRole(null, Role.MASTER);
        } else if (roleSearch.equals("Senior")) {
            employeeList = employeeRepo.findAllByTeamAndUsersRole(null, Role.SENIOR_DEVELOPER);
            employeeListCheck = employeeRepo.findAllByTeamAndUsersRole(null, Role.SENIOR_DEVELOPER);
        } else if (roleSearch.equals("Developers")) {
            employeeList = employeeRepo.findAllByTeamAndUsersRole(null, Role.DEVELOPERS);
            employeeListCheck = employeeRepo.findAllByTeamAndUsersRole(null, Role.DEVELOPERS);
        } else {
            employeeList = employeeRepo.findAllByTeam(null);
            employeeListCheck = employeeRepo.findAllByTeam(null);
        }
        Boolean masterPresent = false;
        for (Employee e : teamList) {
            if (e.getUsers().getRole() == Role.MASTER) {
                masterPresent = true;
            }
        }

        for (Employee e : employeeListCheck) {
            if (e.getUsers().getRole() == Role.OWNER) {
                employeeList.remove(e);
            } else if ((masterPresent) && (e.getUsers().getRole() == Role.MASTER)) {
                employeeList.remove(e);
            }
        }
        team = teamRepo.findById(id).get();
        model.put("roleSearch", roleSearch);
        model.put("idTeam", id);
        model.put("teamList", teamList);
        model.put("employeeList", employeeList);
        model.put("label", "Измените команду");
        return "squad_list_page";
    }

    @GetMapping("/squadListDel/{id}/{idEmployee}/{roleSearch}")
    public String squadListDel(@PathVariable Long id, @PathVariable Long idEmployee, @PathVariable String roleSearch, Map<String, Object> model) {

        Team team = teamRepo.findById(id).get();
        List<Employee> teamList = team.getEmployee();
        Employee employee = employeeRepo.findById(idEmployee).get();

        teamList.remove(employee);

        employee.setTeam(null);
        team.setEmployee(teamList);
        teamRepo.save(team);
        employeeRepo.save(employee);
        team = teamRepo.findById(id).get();
        teamList = team.getEmployee();
        List<Employee> employeeList;
        List<Employee> employeeListCheck;
        if (roleSearch.equals("Master")) {
            employeeList = employeeRepo.findAllByTeamAndUsersRole(null, Role.MASTER);
            employeeListCheck = employeeRepo.findAllByTeamAndUsersRole(null, Role.MASTER);
        } else if (roleSearch.equals("Senior")) {
            employeeList = employeeRepo.findAllByTeamAndUsersRole(null, Role.SENIOR_DEVELOPER);
            employeeListCheck = employeeRepo.findAllByTeamAndUsersRole(null, Role.SENIOR_DEVELOPER);
        } else if (roleSearch.equals("Developers")) {
            employeeList = employeeRepo.findAllByTeamAndUsersRole(null, Role.DEVELOPERS);
            employeeListCheck = employeeRepo.findAllByTeamAndUsersRole(null, Role.DEVELOPERS);
        } else {
            employeeList = employeeRepo.findAllByTeam(null);
            employeeListCheck = employeeRepo.findAllByTeam(null);
        }
        Boolean masterPresent = false;
        for (Employee e : teamList) {
            if (e.getUsers().getRole() == Role.MASTER) {
                masterPresent = true;
            }
        }

        for (Employee e : employeeListCheck) {
            if (e.getUsers().getRole() == Role.OWNER) {
                employeeList.remove(e);
            } else if ((masterPresent) && (e.getUsers().getRole() == Role.MASTER)) {
                employeeList.remove(e);
            }
        }
        team = teamRepo.findById(id).get();
        model.put("roleSearch", roleSearch);
        model.put("idTeam", id);
        model.put("teamList", teamList);
        model.put("employeeList", employeeList);
        model.put("label", "Измените команду");
        return "squad_list_page";
    }


    @GetMapping("/createBoard")
    public String createBoardPage(Map<String, Object> model) {

        model.put("label", "Введите основныйе данные доски");
        return "create_board_page";
    }

    @PostMapping("/postCreateBoard")
    public String postCreateBoard(@RequestParam String info, @RequestParam String nameBoard, Map<String, Object> model) {
        Optional<Board> findBoard = boardRepo.findByNameBoard(nameBoard);
        if (findBoard.isPresent()) {
            model.put("label", "Доска не создан: доска с таким именем уже существует!");
        } else {
            Board board = new Board(nameBoard, info);
            boardRepo.save(board);
            model.put("label", "Доска создана");
        }
        return "create_board_page";
    }

    @GetMapping("/updateBoard/{id}")
    public String updateBoardPage(@PathVariable Long id, Map<String, Object> model) {

        Board board = boardRepo.findById(id).get();
        model.put("board", board);
        model.put("label", "Измените доску");
        return "update_board_page";
    }

    @PostMapping("/postUpdateBoard")
    public String postUpdateBoard(@RequestParam Long id, @RequestParam String info, @RequestParam String nameBoard, Map<String, Object> model) {
        Optional<Board> board = boardRepo.findById(id);
        Optional<Board> checkBoard = boardRepo.findByNameBoard(nameBoard);
        Boolean check = nameBoard.equals(board.get().getNameBoard());
        if ((check == false) && (checkBoard.isPresent())) {
            Optional<Board> findBoard = boardRepo.findById(id);

            model.put("employee", findBoard.get());
            model.put("label", "Доска не изменена: доска с таким именем уже существует!");
        } else {
            board.get().setNameBoard(nameBoard);
            board.get().setInfoBoard(info);

            Board newBoard = boardRepo.save(board.get());

            model.put("board", newBoard);
            model.put("label", "Доска изменена");
        }


        return "update_board_page";
    }

    @GetMapping("/boardTeam/{id}")
    public String boardTeamPage(@PathVariable Long id, Map<String, Object> model) {

        Board board = boardRepo.findById(id).get();
        List<Team> teamList = teamRepo.findAll();
        Team team = board.getTeam();
        if (team != null) {
            teamList.remove(team);
        }
        if (board.getTeam() == null) {
            model.put("team", null);
            Team teamFalse = new Team("", "");
            board.setTeam(teamFalse);
        } else {
            model.put("team", team.getEmployee());
        }
        model.put("board", board);

        model.put("teamList", teamList);
        model.put("label", "Выбрать команду для доски");
        return "board_team_page";
    }

    @GetMapping("/setBoardTeam/{id}/{idTeam}")
    public String setBoardTeam(@PathVariable Long id, @PathVariable Long idTeam, Map<String, Object> model) {

        Team setTeam = teamRepo.findById(idTeam).get();
        Board board = boardRepo.findById(id).get();
        board.setTeam(setTeam);
        boardRepo.save(board);

        List<Team> teamList = teamRepo.findAll();
        Team team = board.getTeam();
        if (team != null) {
            teamList.remove(team);
        }
        if (board.getTeam() == null) {
            model.put("team", "");
        } else {
            model.put("team", setTeam.getEmployee());
        }
        model.put("board", board);

        model.put("teamList", teamList);
        model.put("label", "Выбрать команду для доски");
        return "board_team_page";
    }

    @GetMapping("/delBoardTeam/{id}")
    public String delBoardTeam(@PathVariable Long id, Map<String, Object> model) {


        Board board = boardRepo.findById(id).get();
        board.setTeam(null);
        boardRepo.save(board);

        List<Team> teamList = teamRepo.findAll();
        Team team = board.getTeam();
        if (team != null) {
            teamList.remove(team);
        }
        model.put("team", null);
        Team teamFalse = new Team("", "");
        board.setTeam(teamFalse);
        model.put("board", board);

        model.put("teamList", teamList);
        model.put("label", "Выбрать команду для доски");
        return "board_team_page";
    }


    @GetMapping("/createSprint/{id}")
    public String createSprintPage(@PathVariable Long id, Map<String, Object> model) {

        model.put("label", "Введите основныйе данные спринта");
        model.put("id", id);
        return "create_sprint_page";
    }

    @PostMapping("/postCreateSprint")
    public String postCreateSprint(@RequestParam Long idBoard, @RequestParam String nameSprint, @RequestParam String ds, @RequestParam String de, Map<String, Object> model) {
        Date dateNow = new Date();
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        Date dateS = new Date();
        Date dateE = new Date();
        Board board = boardRepo.findById(idBoard).get();
        List<Sprint> findSprint = sprintRepo.findByBoard(board);
        Optional<Sprint> findSprintByName = sprintRepo.findByNameSprint(nameSprint);
        Sprint newSprint = new Sprint();
        Integer problem = 0;
        try {
            dateS = format.parse(ds);
            dateE = format.parse(de);
        } catch (ParseException e) {
            model.put("label", "Спринт не создан: неправильный формат времени");
        }
        if ((dateS.before(dateE)) && (dateS.after(dateNow)) && (findSprintByName.isEmpty())) {

            newSprint.setDateStart(dateS);
            newSprint.setDateEnd(dateE);
            newSprint.setNameSprint(nameSprint);
            newSprint.setBoard(board);
            if (findSprint.isEmpty()) {

                newSprint.setNumber(1L);
            } else {

                Long number = 0L;
                for (Sprint s : findSprint) {
                    if (s.getDateEnd().after(dateNow)) {
                        model.put("label", "Старый спринт еще не закончен");
                        model.put("id", idBoard);
                        return "create_sprint_page";
                    }
                    number = s.getNumber();
                }
                newSprint.setNumber(number + 1L);
            }
            sprintRepo.save(newSprint);
            model.put("label", "Спринт создан");
        } else if (dateS.after(dateE)) {
            model.put("label", "Спринт не создан: начало спринта находиться позже его конца");
        } else if (dateS.before(dateNow)) {
            model.put("label", "Спринт не создан: дата начала спринта уже прошла");
        } else {
            model.put("label", "Спринт не создан: с таким именем уже существует");
        }

        model.put("id", idBoard);
        return "create_sprint_page";
    }

    @GetMapping("/updateSprint/{id}")
    public String updateSprintPage(@PathVariable Long id, Map<String, Object> model) {
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        Sprint sprint = sprintRepo.findById(id).get();
        String dateS = format.format(sprint.getDateStart());
        String dateE = format.format(sprint.getDateEnd());
        model.put("sprint", sprint);
        model.put("dateS", dateS);
        model.put("dateE", dateE);
        model.put("label", "Измените спринт");
        return "update_sprint_page";
    }

    @PostMapping("/postUpdateSprint")
    public String postUpdateSprint(@RequestParam Long id, @RequestParam String nameSprint, @RequestParam String ds, @RequestParam String de, Map<String, Object> model) {
        Date dateNow = new Date();
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        Date dateS = new Date();
        Date dateE = new Date();
        Sprint sprintById = sprintRepo.findById(id).get();
        List<Sprint> findSprint = sprintRepo.findByBoard(sprintById.getBoard());
        List<Sprint> findCheckSprint = sprintRepo.findByBoard(sprintById.getBoard());
        for (Sprint s : findCheckSprint) {
            if (sprintById == s) {
                findSprint.remove(s);
            }
        }
        Optional<Sprint> findSprintByName = sprintRepo.findByNameSprint(nameSprint);
        if (sprintById != findSprintByName.get()) {
            model.put("label", "Спринт не обновлен: с таким именем уже существует");
        }
        Integer problem = 0;
        try {
            dateS = format.parse(ds);
            dateE = format.parse(de);
        } catch (ParseException e) {
            model.put("label", "Спринт не обновлен: неправильный формат времени");
            return "update_sprint_page";
        }
        if ((dateS.before(dateE)) && (dateS.after(dateNow))) {

            sprintById.setDateStart(dateS);
            sprintById.setDateEnd(dateE);
            sprintById.setNameSprint(nameSprint);
            for (Sprint s : findSprint) {
                if (s.getDateEnd().after(dateNow)) {
                    model.put("label", "Старый спринт еще не закончен");
                    model.put("id", id);
                    return "create_sprint_page";
                }

            }
            sprintRepo.save(sprintById);

            model.put("label", "Спринт обновлен");
        } else if (dateS.after(dateE)) {
            model.put("label", "Спринт не обновлен: начало спринта находиться позже его конца");
        } else if (dateS.before(dateNow)) {
            model.put("label", "Спринт не обновлен: дата начала спринта уже прошла");
        } else {
            model.put("label", "Спринт не обновлен: с таким именем уже существует");
        }

        Sprint sprint = sprintRepo.findById(id).get();
        String dateStart = format.format(sprint.getDateStart());
        String dateEnd = format.format(sprint.getDateEnd());
        model.put("sprint", sprint);
        model.put("dateS", dateStart);
        model.put("dateE", dateEnd);

        return "update_sprint_page";
    }


    @GetMapping("/createTask/{id}")
    public String createUsersPage(@PathVariable Long id, Map<String, Object> model) {
        model.put("id", id);
        model.put("label", "Создайте юзера");
        return "create_task_page";
    }

    @PostMapping("/postCreateTask")
    public String postCreateTask(@RequestParam Long id, @RequestParam String info, @RequestParam String important, @RequestParam String title, Map<String, Object> model) {
        Optional<Task> findTask = taskRepo.findByTitle(title);
        if (findTask.isPresent()) {
            model.put("label", "Задача не создана: задача с таким заголовком уже существует!");
        } else {
            Status status = Status.ACTIVE;

            Important importantTask = Important.valueOf(important);

            Optional<Sprint> sprint = sprintRepo.findById(id);

            Task newTask = new Task(title, info, StatusTask.NOT_STARTED, importantTask, sprint.get());

            taskRepo.save(newTask);

            model.put("id", id);
            model.put("label", "Задача создана");
        }
        return "create_task_page";
    }


    @GetMapping("/updateTask/{id}")
    public String updateTaskPage(@PathVariable Long id, Map<String, Object> model) {

        Optional<Task> task = taskRepo.findById(id);
        Important important = task.get().getImportant();
        Sprint sprint = task.get().getSprint();
        Date dateNow = new Date();
        String importantName = "";
        if (task.get().getStatusTask() == StatusTask.NOT_STARTED) {
            if (important == Important.UNIMPORTANT) {
                importantName = "Не важно";
            } else if (important == Important.MINOR_IMPORTANCE) {
                importantName = "Минимальная важность";
            } else if (important == Important.MEDIUM_IMPORTANCE) {
                importantName = "Средняя важность";
            } else {
                importantName = "Высокая важность";
            }
            model.put("label", "Измените задачу");
        } else {
            model.put("label", "Задачу невозможно изменить над ней уже работают. Изменить можно только важность задачи");
            if (important == Important.UNIMPORTANT) {
                importantName = "Не важно";
            } else if (important == Important.MINOR_IMPORTANCE) {
                importantName = "Минимальная важность";
            } else if (important == Important.MEDIUM_IMPORTANCE) {
                importantName = "Средняя важность";
            } else {
                importantName = "Высокая важность";
            }
            model.put("label", "Измените важность задачи");
        }


        model.put("task", task.get());
        model.put("importantName", importantName);

        return "update_task_page";
    }

    @PostMapping("/postUpdateTask")
    public String postUpdateTask(@RequestParam Long id, @RequestParam String importantTitle, @RequestParam String info, @RequestParam String nameTask, Map<String, Object> model) {


        Optional<Task> task = taskRepo.findById(id);
        Optional<Task> taskByName = taskRepo.findByTitle(nameTask);
        Important important = task.get().getImportant();
        Sprint sprint = task.get().getSprint();
        Date dateNow = new Date();
        String importantName = "";
        if ((taskByName.isPresent()) && (taskByName.get() != task.get())) {
            if (important == Important.UNIMPORTANT) {
                importantName = "Не важно";
            } else if (important == Important.MINOR_IMPORTANCE) {
                importantName = "Минимальная важность";
            } else if (important == Important.MEDIUM_IMPORTANCE) {
                importantName = "Средняя важность";
            } else {
                importantName = "Высокая важность";
            }
            model.put("label", "Задача не изменена: задача с таким изменена уже существует");
            model.put("task", task.get());
            model.put("importantName", importantName);

            return "update_user_page";
        }
        if (task.get().getStatusTask() == StatusTask.NOT_STARTED) {
            if (important == Important.UNIMPORTANT) {
                importantName = "Не важно";
            } else if (important == Important.MINOR_IMPORTANCE) {
                importantName = "Минимальная важность";
            } else if (important == Important.MEDIUM_IMPORTANCE) {
                importantName = "Средняя важность";
            } else {
                importantName = "Высокая важность";

            }
            if (importantName.equals("old")) {
                task.get().setTitle(nameTask);
                task.get().setInfo(info);

            } else {
                task.get().setTitle(nameTask);
                task.get().setInfo(info);
                task.get().setImportant(Important.valueOf(importantTitle));
            }
            model.put("label", "Изменена задача");
            taskRepo.save(task.get());
        } else {

            if (important == Important.UNIMPORTANT) {
                importantName = "Не важно";
            } else if (important == Important.MINOR_IMPORTANCE) {
                importantName = "Минимальная важность";
            } else if (important == Important.MEDIUM_IMPORTANCE) {
                importantName = "Средняя важность";
            } else {
                importantName = "Высокая важность";
            }
            if (importantName.equals("old")) {
                model.put("label", "Данные остались прежними");
            } else {
                task.get().setImportant(Important.valueOf(importantTitle));
                model.put("label", "Изменена важность задачи");

            }
            taskRepo.save(task.get());
        }


        model.put("task", task.get());
        model.put("importantName", importantName);


        return "update_task_page";
    }

    @GetMapping("/listTeam")
    public String ListTeam(Map<String, Object> model) {
        List<Team> teamList = teamRepo.findAll();
        model.put("teamList", teamList);
        return "list_team_page";
    }

    @GetMapping("/listBoard")
    public String ListBoard(Map<String, Object> model) {
        List<Board> boardList = boardRepo.findAll();
        model.put("boardList", boardList);
        return "list_board_page";
    }
    @GetMapping("/listSprint/{id}")
    public String ListSprint(@PathVariable Long id, Map<String, Object> model) {
        Board board = boardRepo.findById(id).get();
        List<Sprint> sprintList = sprintRepo.findByBoard(board);
        model.put("idBoard", id);
        model.put("sprintList", sprintList);
        return "list_sprint_page";
    }
    @GetMapping("/listTask/{id}")
    public String ListTask(@PathVariable Long id, Map<String, Object> model) {
        Sprint sprint = sprintRepo.findById(id).get();
        List<Task> taskList = taskRepo.findBySprint(sprint);
        model.put("idSprint", id);
        model.put("taskList", taskList);
        return "list_task_page";
    }
}
