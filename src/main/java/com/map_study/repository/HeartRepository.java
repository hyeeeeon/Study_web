package com.map_study.repository;

import com.map_study.entity.Heart;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface HeartRepository extends JpaRepository<Heart, Integer> {

    // 특정 게시글에 대한 좋아요 여부 확인
    Heart findByArticleIdAndMemberId(Integer articleId, Integer memberId);

    // 특정 게시글에 대한 좋아요 수 조회
    long countByArticleId(Integer articleId);

    boolean existsByMemberIdAndArticleId(Integer memberId, Integer articleId);
}
