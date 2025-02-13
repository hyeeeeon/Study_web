package com.map_study.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String title;
    private String content;
    private String filename;
    private String filepath;

    @Column(name = "heart_count", columnDefinition = "bigint default 0")
    private long heartCount;
}
