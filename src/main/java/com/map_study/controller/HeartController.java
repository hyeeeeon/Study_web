package com.map_study.controller;

import com.map_study.service.HeartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HeartController {

    @Autowired
    private HeartService heartService;

    // 좋아요 추가
    @PostMapping("/post/heart")
    public String addHeart(@RequestParam Integer articleId, @RequestParam Integer memberId, Model model) {
        heartService.addHeart(articleId, memberId);
        model.addAttribute("articleId", articleId);
        return "redirect:/post/view?id=" + articleId;
    }

    // 좋아요 취소
    @PostMapping("/post/unHeart")
    public String removeLike(@RequestParam Integer articleId, @RequestParam Integer memberId, Model model) {
        heartService.removeHeart(articleId, memberId);
        model.addAttribute("articleId", articleId);
        return "redirect:/post/view?id=" + articleId;
    }

    // 특정 게시글에 대한 좋아요 수 조회
    @PostMapping("/post/heartCount")
    public String getHeartCount(@RequestParam Integer articleId, Model model) {
        long heartCount = heartService.getHeartCount(articleId);
        model.addAttribute("likeCount", heartCount);
        return "PostView";  // 좋아요 수를 표시하는 게시글 보기 페이지
    }
}