package com.map_study.controller;

import com.map_study.entity.Post;
import com.map_study.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class PostController {

    @Autowired
    private PostService postService;

    @GetMapping("/post/write") //localhost:8080/active/board/write
    public String postWriteForm() {
        return "PostWrite";
    }

    @PostMapping("/post/writePro")
    public String postWritePro(Post post) {

        postService.write(post);
        return "redirect:/post/write";
    }

    @GetMapping("/post/view")
    public String postView(){
        return "PostView";
    }
}