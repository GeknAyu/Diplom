package ru.ayukov.diplom.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table
@Entity
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String nameBoard;

    private String infoBoard;

    @OneToOne(fetch = FetchType.EAGER,targetEntity = Team.class)
    @JoinColumn
    private Team team;

    public Board(String nameBoard, String infoBoard, Team team) {
        this.nameBoard = nameBoard;
        this.infoBoard = infoBoard;
        this.team = team;
    }

    public Board(String nameBoard, String infoBoard) {
        this.nameBoard = nameBoard;
        this.infoBoard = infoBoard;
    }

}
