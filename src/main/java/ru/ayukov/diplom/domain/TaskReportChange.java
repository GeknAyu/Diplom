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
public class TaskReportChange {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String titleReport;

    private String textReport;

    private String linkReport;

    private StatusTask newStatus;

    private Date dateReport;

    @ManyToOne(fetch = FetchType.EAGER,targetEntity = Task.class)
    @JoinColumn
    private Task task;

    @ManyToOne(fetch = FetchType.EAGER,targetEntity = Employee.class)
    @JoinColumn
    private Employee employee;

    public TaskReportChange(String titleReport, String textReport,String linkReport, Date dateReport, Task task, Employee employee,StatusTask newStatus) {
        this.titleReport = titleReport;
        this.textReport = textReport;
        this.linkReport = linkReport;
        this.dateReport = dateReport;
        this.task = task;
        this.employee = employee;
        this.newStatus = newStatus;
    }
}
