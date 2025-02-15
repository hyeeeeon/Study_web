package com.map_study.controller;

import com.map_study.service.HeartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/post")
@RequiredArgsConstructor
public class HeartController {

    private final HeartService heartService;

    // 좋아요 추가
    @PostMapping("/{memberId}/{articleId}")
    public ResponseEntity<String> addHeart(
            @RequestParam Integer articleId,
            @RequestParam Integer memberId) {

        heartService.addHeart(articleId, memberId);
        return ResponseEntity.ok("좋아요 추가됨");
    }

    // 좋아요 취소
    @PostMapping("/{memberId}/{articleId}")
    public ResponseEntity<String> removeHeart(
            @RequestParam Integer articleId,
            @RequestParam Integer memberId) {

        heartService.removeHeart(articleId, memberId);
        return ResponseEntity.ok("좋아요 취소됨");
    }

    // 특정 게시글의 좋아요 개수 조회
    @GetMapping("/{memberId}/{articleId}")
    public ResponseEntity<Boolean> getHeartCount(@PathVariable("memberId") Integer memberId,
                                              @PathVariable("articleId") Integer articleId) {
        return ResponseEntity.ok(heartService.isHeartedByUser(memberId, articleId));
    }
}
