package ru.ayukov.diplom.domain;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Table
@Entity
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String nameTeam;

    private String infoTeam;

    @OneToMany(fetch = FetchType.EAGER,targetEntity = Employee.class)
    @JoinColumn
    private List<Employee> employee;

    public Team(String nameTeam, String infoTeam) {
        this.nameTeam = nameTeam;
        this.infoTeam = infoTeam;
    }

    public Team(String nameTeam, String infoTeam, List<Employee> employee) {
        this.nameTeam = nameTeam;
        this.infoTeam = infoTeam;
        this.employee = employee;
    }
}
