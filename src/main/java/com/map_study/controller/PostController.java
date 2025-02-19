package com.map_study.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import com.map_study.entity.Post;
import com.map_study.service.PostService;
import com.map_study.service.HeartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@Tag(name = "게시글 API", description = "게시글 CRUD API")
public class PostController {

    @Autowired
    private PostService postService;

    @Autowired
    private HeartService heartService;

    @Operation(summary = "게시글 작성 페이지 이동", description = "게시글 작성 페이지를 반환합니다.")
    @ApiResponse(responseCode = "200", description = "게시글 작성 페이지 반환 성공")
    @GetMapping("/post/write")
    public String postWriteForm() {
        return "PostWrite";
    }

    @Operation(summary = "게시글 작성", description = "새로운 게시글을 작성합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "게시글 작성 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @PostMapping("/post/writePro")
    public String postWritePro(Post post, Model model,
                               @RequestParam("file") MultipartFile[] files) throws Exception {
        postService.write(post, files);
        model.addAttribute("message", "글 작성이 완료되었습니다.");
        model.addAttribute("searchURL", "/post/list");
        return "Message";
    }

    @Operation(summary = "게시글 목록 조회", description = "페이지네이션을 적용하여 게시글 목록을 가져옵니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "게시글 목록 조회 성공"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
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

    @Operation(summary = "게시글 상세 조회", description = "특정 ID의 게시글을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "게시글 조회 성공"),
            @ApiResponse(responseCode = "404", description = "게시글을 찾을 수 없음")
    })
    @GetMapping("/post/view")
    public String postView(Model model, Integer id) {
        Post post = postService.postView(id);

        if (post == null) {
            model.addAttribute("message", "존재하지 않는 게시글입니다.");
            model.addAttribute("searchURL", "/post/list");
            return "redirect:/post/list";
        }

        long heartCount = heartService.getHeartCount(id);
        post.setHeartCount(heartCount);

        boolean isHearted = heartService.isHeartedByUser(1, id);
        model.addAttribute("post", post);
        model.addAttribute("isHearted", isHearted);

        return "PostView";
    }

    @Operation(summary = "게시글 삭제", description = "특정 ID의 게시글을 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "게시글 삭제 성공"),
            @ApiResponse(responseCode = "404", description = "게시글을 찾을 수 없음")
    })
    @GetMapping("/post/delete")
    public String postDelete(Integer id, RedirectAttributes redirectAttributes) {
        postService.postDelete(id);
        redirectAttributes.addFlashAttribute("message", "게시글이 삭제되었습니다.");
        return "redirect:/post/list";
    }

    @Operation(summary = "게시글 수정 페이지 이동", description = "게시글 수정 페이지를 반환합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "게시글 수정 페이지 반환 성공"),
            @ApiResponse(responseCode = "404", description = "게시글을 찾을 수 없음")
    })
    @GetMapping("/post/modify/{id}")
    public String postModify(@PathVariable("id") Integer id, Model model) {
        model.addAttribute("post", postService.postView(id));
        return "PostModify";
    }

    @Operation(summary = "게시글 수정", description = "특정 ID의 게시글을 수정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "게시글 수정 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "404", description = "게시글을 찾을 수 없음")
    })
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