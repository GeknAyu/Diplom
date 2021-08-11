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
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String title;

    private String info;

    private StatusTask statusTask;

    private Important important;

    @ManyToOne(fetch = FetchType.EAGER,targetEntity = Sprint.class)
    @JoinColumn
    private Sprint sprint;

    @ManyToOne(fetch = FetchType.EAGER,targetEntity = Employee.class)
    @JoinColumn
    private Employee employee;

    public Task(String title, String info, StatusTask statusTask, Important important, Sprint sprint) {
        this.title = title;
        this.info = info;
        this.statusTask = statusTask;
        this.important = important;
        this.sprint = sprint;
    }
}
