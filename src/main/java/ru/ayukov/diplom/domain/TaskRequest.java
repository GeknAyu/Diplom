package ru.ayukov.diplom.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Table
@Entity
public class TaskRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private StatusTaskRequest statusTaskRequest;

    @ManyToOne(fetch = FetchType.EAGER,targetEntity = Task.class)
    @JoinColumn
    private Task task;

    @ManyToOne(fetch = FetchType.EAGER,targetEntity = Employee.class)
    @JoinColumn
    private Employee employee;

    public TaskRequest( StatusTaskRequest statusTaskRequest, Task task, Employee employee) {

        this.statusTaskRequest = statusTaskRequest;
        this.task = task;
        this.employee = employee;
    }
}
