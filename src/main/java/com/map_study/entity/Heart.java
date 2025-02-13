package com.map_study.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Heart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer heartId; //좋아요 식별 ID
    private Integer articleId; //좋아요가 눌린 게시글 ID
    private Integer memberId; //좋아요 누른 사용자 ID
}
