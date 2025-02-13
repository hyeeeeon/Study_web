package com.map_study.service;

import com.map_study.entity.Heart;
import com.map_study.repository.HeartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

@Service
public class HeartService {

    @Autowired
    private HeartRepository heartRepository;

    // 좋아요 추가
    @Transactional
    public void addHeart(Integer articleId, Integer memberId) {
        // 이미 좋아요가 눌려있는지 확인
        Heart existingHeart = heartRepository.findByArticleIdAndMemberId(articleId, memberId);
        if (existingHeart == null) {
            Heart heart = new Heart();
            heart.setArticleId(articleId);
            heart.setMemberId(memberId);
            heartRepository.save(heart);
        }
    }

    // 좋아요 취소
    @Transactional
    public void removeHeart(Integer articleId, Integer memberId) {
        Heart heart = heartRepository.findByArticleIdAndMemberId(articleId, memberId);
        if (heart != null) {
            heartRepository.delete(heart);
        }
    }

    // 특정 게시글에 대한 좋아요 수 조회
    public long getHeartCount(Integer articleId) {
        return heartRepository.countByArticleId(articleId);
    }

    // 좋아요 여부 확인
    public boolean isHeartedByUser(Integer memberId, Integer articleId) {
        return heartRepository.existsByMemberIdAndArticleId(memberId, articleId);
    }
}
