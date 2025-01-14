package com.map_study.controller;

import org.springframework.ui.Model;
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

    @GetMapping("/post/list")
    public String postList(Model model){

        model.addAttribute("list", postService.postList());
        return "PostList";
    }


    @GetMapping("/post/view") //localhost:8080/post/view?id=1
    public String postView(Model model, Integer id){
        model.addAttribute("post", postService.postView(id));
        return "PostView";
    }
}