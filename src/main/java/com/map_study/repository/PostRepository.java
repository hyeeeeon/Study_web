package com.map_study.repository;

import com.map_study.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> { //Entity 클래스 이름, 프라이머리 키의 함수?


}
