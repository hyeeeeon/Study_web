package com.map_study.controller;

import com.map_study.service.HeartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/heart")
@RequiredArgsConstructor
public class HeartController {

    private final HeartService heartService;

    @Operation(summary = "게시글 좋아요 추가", description = "특정 회원이 특정 게시글에 좋아요를 추가합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "좋아요 추가 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @PostMapping("/{memberId}/{articleId}")
    public ResponseEntity<String> addHeart(
            @PathVariable Integer articleId,
            @PathVariable Integer memberId) {

        heartService.addHeart(articleId, memberId);
        return ResponseEntity.ok("좋아요 추가");
    }

    @Operation(summary = "게시글 좋아요 취소", description = "특정 회원이 특정 게시글의 좋아요를 취소합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "좋아요 취소 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @DeleteMapping("/{memberId}/{articleId}")
    public ResponseEntity<String> removeHeart(
            @PathVariable Integer articleId,
            @PathVariable Integer memberId) {

        heartService.removeHeart(articleId, memberId);
        return ResponseEntity.ok("좋아요 취소");
    }

    @Operation(summary = "좋아요 여부 확인", description = "특정 회원이 특정 게시글에 좋아요를 눌렀는지 확인합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "좋아요 여부 조회 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @GetMapping("/{memberId}/{articleId}")
    public ResponseEntity<Boolean> getHeartStatus(
            @PathVariable("memberId") Integer memberId,
            @PathVariable("articleId") Integer articleId) {
        return ResponseEntity.ok(heartService.isHeartedByUser(memberId, articleId));
    }
}
