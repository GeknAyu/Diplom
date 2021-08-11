package ru.ayukov.diplom.controller;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.ayukov.diplom.domain.Employee;
import ru.ayukov.diplom.domain.Role;
import ru.ayukov.diplom.domain.Status;
import ru.ayukov.diplom.domain.Users;
import ru.ayukov.diplom.repos.EmployeeRepo;
import ru.ayukov.diplom.repos.UsersRepo;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private EmployeeRepo employeeRepo;

    @Autowired
    private UsersRepo usersRepo;

    @GetMapping("/createUsers")
    public String createUsersPage(Map<String, Object> model) {

        model.put("label", "Создайте юзера");
        return "create_user_page";
    }

    @PostMapping("/postCreateUsers")
    public String postCreateUsers(@RequestParam String info, @RequestParam String patronymic, @RequestParam String firstName, @RequestParam String lastName, @RequestParam String roleName, @RequestParam String username, @RequestParam String password, Map<String, Object> model) {
        Optional<Users> findUser = usersRepo.findByUserName(username);
        if (findUser.isPresent()) {
            model.put("label", "Юзер не создан: юзер с таким Username уже существует!");
        } else {
            Status status = Status.ACTIVE;

            Role role = Role.valueOf(roleName);

            Users newUser = new Users(username, "{noop}" + password, role, status);

            Users createUsers = usersRepo.save(newUser);

            Employee newEmployee = new Employee(firstName, lastName, patronymic, info, createUsers);

            employeeRepo.save(newEmployee);

            model.put("label", "Юзер создан");
        }
        return "create_user_page";
    }

    @GetMapping("/updateUsers/{id}")
    public String updateUsersPage(@PathVariable Long id, Map<String, Object> model) {

        Optional<Employee> findEmployee = employeeRepo.findById(id);
        Role role = findEmployee.get().getUsers().getRole();
        String roleName;
        if (role == Role.DEVELOPERS) {
            roleName = "разработчик";
        } else if (role == Role.SENIOR_DEVELOPER) {
            roleName = "ст. разработчик";
        } else if (role == Role.MASTER) {
            roleName = "лидер команды";
        } else {
            roleName = "управляющий";
        }
        model.put("employee", findEmployee.get());
        model.put("roleName", roleName);
        model.put("label", "Измените юзера");
        return "update_user_page";
    }

    @PostMapping("/postUpdateUsers")
    public String postUpdateUsers(@RequestParam Long id, @RequestParam String info, @RequestParam String patronymic, @RequestParam String firstName, @RequestParam String lastName, @RequestParam String roleName, @RequestParam String username, @RequestParam String password, Map<String, Object> model) {
        Optional<Employee> employee = employeeRepo.findById(id);
        Optional<Users> checkUsers = usersRepo.findByUserName(username);
        Boolean check = username.equals(employee.get().getUsers().getUserName());
        if ((check == false) && (checkUsers.isPresent())) {
            Optional<Employee> findEmployee = employeeRepo.findById(id);
            Role role = findEmployee.get().getUsers().getRole();
            String roleNameNow;
            if (role == Role.DEVELOPERS) {
                roleNameNow = "мл. разработчик";
            } else if (role == Role.SENIOR_DEVELOPER) {
                roleNameNow = "ст. разработчик";
            } else if (role == Role.MASTER) {
                roleNameNow = "лидер команды";
            } else {
                roleNameNow = "управляющий";
            }

            model.put("employee", employee.get());
            model.put("roleName", roleNameNow);
            model.put("label", "Юзер не изменен: юзер с таким Username уже существует!");
        } else {
            Users users = employee.get().getUsers();
            users.setUserName(username);
            users.setPassword("{noop}" + password);
            String testWord = "old";
            Boolean test = roleName.equals(testWord);
            if (test == false) {
                Role newRole = Role.valueOf(roleName);
                users.setRole(newRole);
            }
            usersRepo.save(users);

            employee.get().setFirstName(firstName);
            employee.get().setLastName(lastName);
            employee.get().setInfo(info);
            employee.get().setPatronymic(patronymic);
            Employee findEmployee = employeeRepo.save(employee.get());

            Role role = findEmployee.getUsers().getRole();
            String roleNameNow;
            if (role == Role.DEVELOPERS) {
                roleNameNow = "мл. разработчик";
            } else if (role == Role.SENIOR_DEVELOPER) {
                roleNameNow = "ст. разработчик";
            } else if (role == Role.MASTER) {
                roleNameNow = "лидер команды";
            } else {
                roleNameNow = "управляющий";
            }

            model.put("employee", employee.get());
            model.put("roleName", roleNameNow);
            model.put("label", "Юзер изменен");
        }


        return "update_user_page";
    }

    @GetMapping("/listUsers")
    public String listUsersPage(Map<String, Object> model) {

        List<Employee> listEmployee = employeeRepo.findAllByUsersStatus(Status.ACTIVE);
        String link = "/admin/listUsersDisabled";

        model.put("employeeList", listEmployee);
        model.put("link", link);
        model.put("label", "Выберете действие");
        model.put("title", "Лист активных юзеров");
        return "user_list_page";
    }

    @GetMapping("/listUsersDisabled")
    public String listUsersDisabledPage(Map<String, Object> model) {

        List<Employee> listEmployee = employeeRepo.findAllByUsersStatus(Status.DISABLED);
        String link = "/admin/listUsers";


        model.put("employeeList", listEmployee);
        model.put("link", link);
        model.put("label", "Выберете действие");
        model.put("title", "Лист неактивных юзеров");
        return "user_list_page";
    }

    @PostMapping("/postСhangeStatusUsers")
    public String postСhangeStatusUsers(@RequestParam Long id, Map<String, Object> model) {
        Employee employee = employeeRepo.findById(id).get();
        String link;
        Status status;
        List<Employee> listEmployee ;
        if(employee.getUsers().getStatus() == Status.ACTIVE){
            employee.getUsers().setStatus(Status.DISABLED);
            status = Status.ACTIVE;
            link = "/admin/listUsersDisabled";
        }
        else {
            employee.getUsers().setStatus(Status.ACTIVE);
            status = Status.DISABLED;
            link = "/admin/listUsers";
        }
        employeeRepo.save(employee);
        listEmployee = employeeRepo.findAllByUsersStatus(status);

        model.put("link", link);
        model.put("employeeList", listEmployee);
        model.put("label", "Юхер убран из списка активных пользователей");
        model.put("title", "Лист неактивных юзеров");
        return "user_list_page";
    }
}
