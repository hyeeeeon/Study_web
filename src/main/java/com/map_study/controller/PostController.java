package com.map_study.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.ui.Model;
import com.map_study.entity.Post;
import com.map_study.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
public class PostController {

    @Autowired
    private PostService postService;

    @GetMapping("/post/write") //localhost:8080/active/board/write
    public String postWriteForm() {
        return "PostWrite";
    }

    @PostMapping("/post/writePro")
    public String postWritePro(Post post, Model model, MultipartFile file) throws Exception {

        postService.write(post, file);
        model.addAttribute("message", "글 작성이 완료 되었습니다.");
        model.addAttribute("searchURL", "/post/list");
        return "Message";
    }

    @GetMapping("/post/list")
    public String postList(Model model, @PageableDefault(page = 0, size = 10, sort = "id",
            direction = Sort.Direction.DESC) Pageable pageable) {

        Page<Post> list = postService.postList(pageable);

        int nowPage = list.getPageable().getPageNumber()+ 1;//현재 페이지
        int startPage = Math.max(nowPage - 4, 1); //블럭에서 보여줄 시작 페이지
        int endPage =  Math.min(nowPage + 5, list.getTotalPages()); //블럭에서 보여줄 마지막 페이지

        model.addAttribute("list", list);
        model.addAttribute("nowPage", nowPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        return "PostList";
    }

    @GetMapping("/post/view") //localhost:8080/post/view?id=1
    public String postView(Model model, Integer id){
        model.addAttribute("post", postService.postView(id));
        return "PostView";
    }

    @GetMapping("/post/delete")
    public String postDelete(Integer id){
        postService.postDelete(id);
        return "redirect:/post/list";
    }

    @GetMapping("/post/modify/{id}")
    public String postModify(@PathVariable("id") Integer id, Model model){
        model.addAttribute("post", postService.postView(id));
        return "PostModify";
        }

    @PostMapping("/post/update/{id}")
    public String postUpdate(@PathVariable("id") Integer id, Post post, MultipartFile file) throws Exception {

        Post postTemp = postService.postView(id);
        postTemp.setTitle(post.getTitle());
        postTemp.setContent(post.getContent());

        postService.write(postTemp, file);

        return "redirect:/post/list";
    }

    // 파일 크기 초과 예외 처리
    /*@ExceptionHandler(MaxUploadSizeExceededException.class)
    public String MaxSizeException(MaxUploadSizeExceededException exc, Model model) {
        model.addAttribute("message", "파일 크기가 너무 큽니다. 최대 파일 크기는 10MB입니다.");
        model.addAttribute("searchURL", "/post/write");
        return "Message";  // Message.html로 이동하여 오류 메시지를 표시
    } */
}

