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

    @GetMapping("/post/write") //localhost:8080/active/post/write
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
    public String postList(Model model,
                           @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {

        Page<Post> pageList = postService.postList(pageable);
        List<Post> list = pageList.getContent();

        // 게시글에 좋아요 수 추가
        for (Post post : list) {
            long heartCount = heartService.getHeartCount(post.getId());
            post.setHeartCount(heartCount); // 게시글 객체에 좋아요 수 설정
        }

        model.addAttribute("list", list);
        model.addAttribute("nowPage", pageList.getNumber() + 1); // 현재 페이지
        model.addAttribute("startPage", Math.max(1, pageList.getNumber() - 4)); // 블럭에서 보여줄 시작 페이지
        model.addAttribute("endPage", Math.min(pageList.getTotalPages(), pageList.getNumber() + 5)); // 블럭에서 보여줄 마지막 페이지

        return "PostList";
    }

    // 게시글에 좋아요 수 추가

    @GetMapping("/post/view") //localhost:8080/post/view?id=1
    public String postView(Model model, Integer id) {
        Post post = postService.postView(id);

        if (post == null) {  //게시글 없을 경우 예외 처리
            model.addAttribute("message", "존재하지 않는 게시글입니다.");
            model.addAttribute("searchURL", "/post/list");
            return "Message";
        }

        long likeCount = heartService.getHeartCount(id);
        post.setHeartCount(likeCount);  //좋아요 수 추가

        boolean isHearted = heartService.isHeartedByUser(1, id);  //임시 회원 ID 사용
        model.addAttribute("post", post);
        model.addAttribute("isHearted", isHearted);

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

    @PostMapping("/post/update/{postId}")
    public String postUpdate(@PathVariable("postId") Integer postId,
                             Post post,
                             Model model,
                             @RequestParam(name = "file", required = false) MultipartFile file) throws Exception {

        // 수정할 게시글을 불러옴
        Post postTemp = postService.postView(postId); // postId를 사용하여 수정할 게시글을 조회합니다.

        if (postTemp == null) {
            model.addAttribute("message", "존재하지 않는 게시글입니다.");
            model.addAttribute("searchUrl", "/post/list");
            return "Message"; // 게시글 없는 경우 예외처리
        }

        // 게시글 수정 내용 적용
        postTemp.setTitle(post.getTitle());
        postTemp.setContent(post.getContent());

        // 파일 존재 -> 처리, 없으면 기존 파일 유지
        if (file != null && !file.isEmpty()) {
            // 새로운 파일 업로드된 경우
            postService.write(postTemp, file); // 파일을 처리하는 로직
        } else {
            // 파일 없을 경우 기존 파일 그대로 유지하거나 null 넘김
            postService.write(postTemp, null); // 기존 파일 그대로 유지
        }

        model.addAttribute("message", "글 수정이 완료되었습니다.");
        model.addAttribute("searchUrl", "/post/list");

        return "redirect:/post/list";
    }

}
