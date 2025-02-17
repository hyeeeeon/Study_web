package com.map_study.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.ui.Model;
import com.map_study.entity.Post;
import com.map_study.service.PostService;
import com.map_study.service.HeartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@Controller
public class PostController {

    @Autowired
    private PostService postService;

    @Autowired
    private HeartService heartService;

    @GetMapping("/post/write")
    public String postWriteForm() {
        return "PostWrite";
    }

    @PostMapping("/post/writePro")
    public String postWritePro(Post post, Model model,
                               @RequestParam("file") MultipartFile[] files) throws Exception {
        postService.write(post, files);
        model.addAttribute("message", "글 작성이 완료되었습니다.");
        model.addAttribute("searchURL", "/post/list");
        return "Message";
    }

    @GetMapping("/post/list")
    public String postList(Model model,
                           @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {

        Page<Post> pageList = postService.postList(pageable);
        List<Post> list = pageList.getContent();

        for (Post post : list) {
            long heartCount = heartService.getHeartCount(post.getId());
            post.setHeartCount(heartCount);
        }

        model.addAttribute("list", list);
        model.addAttribute("startPage", Math.max(1, pageList.getNumber() - 4));
        model.addAttribute("endPage", Math.min(pageList.getTotalPages(), pageList.getNumber() + 5));
        model.addAttribute("nowPage", pageList.getNumber() + 1);

        return "PostList";
    }

    @GetMapping("/post/view")
    public String postView(Model model, Integer id) {
        Post post = postService.postView(id);

        if (post == null) {
            model.addAttribute("message", "존재하지 않는 게시글입니다.");
            model.addAttribute("searchURL", "/post/list");
            return "Message";
        }

        long heartCount = heartService.getHeartCount(id);
        post.setHeartCount(heartCount);

        boolean isHearted = heartService.isHeartedByUser(1, id);
        model.addAttribute("post", post);
        model.addAttribute("isHearted", isHearted);

        return "PostView";
    }

    @GetMapping("/post/delete")
    public String postDelete(Integer id) {
        postService.postDelete(id);
        return "redirect:/post/list";
    }

    @GetMapping("/post/modify/{id}")
    public String postModify(@PathVariable("id") Integer id, Model model) {
        model.addAttribute("post", postService.postView(id));
        return "PostModify";
    }

    @PostMapping("/post/update/{postId}")
    public String postUpdate(@PathVariable("postId") Integer postId,
                             Post post,
                             Model model,
                             @RequestParam(name = "files", required = false) MultipartFile[] files,
                             @RequestParam(name = "deleteFiles", required = false) String[] deleteFiles) throws Exception {

        postService.updatePost(postId, post, files, deleteFiles);

        model.addAttribute("message", "글 수정이 완료되었습니다.");
        model.addAttribute("searchUrl", "/post/list");

        return "redirect:/post/list";
    }

}