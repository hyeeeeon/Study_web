package com.map_study.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String title;
    private String content;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> filename = new ArrayList<>();  // 다중 파일명 저장

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> filepath = new ArrayList<>();  // 다중 파일 경로 저장

    @Column(name = "heart_count", columnDefinition = "bigint default 0")
    private long heartCount;
}