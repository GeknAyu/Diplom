package ru.ayukov.diplom.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@Table
@Entity
public class TaskReport {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String titleReport;

    private String textReport;

    private Date dateReport;

    @ManyToOne(fetch = FetchType.EAGER,targetEntity = Task.class)
    @JoinColumn
    private Task task;

    @ManyToOne(fetch = FetchType.EAGER,targetEntity = Employee.class)
    @JoinColumn
    private Employee employee;

    public TaskReport(String titleReport, String textReport, Date dateReport, Task task, Employee employee) {
        this.titleReport = titleReport;
        this.textReport = textReport;
        this.dateReport = dateReport;
        this.task = task;
        this.employee = employee;
    }
}
