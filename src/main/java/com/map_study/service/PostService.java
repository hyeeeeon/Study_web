package com.map_study.service;

import com.map_study.entity.Post;
import com.map_study.repository.PostRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    // 글 작성 처리 (다중 파일)
    @Transactional
    public void write(Post post, MultipartFile[] files) throws Exception {
        if (files != null && files.length > 0) {
            String projectPath = System.getProperty("user.dir") + File.separator + "src" + File.separator +
                    "main" + File.separator + "resources" + File.separator + "static" + File.separator + "files";

            List<String> fileNames = new ArrayList<>();
            List<String> filePaths = new ArrayList<>();

            for (MultipartFile file : files) {
                if (!file.isEmpty()) {
                    UUID uuid = UUID.randomUUID(); // 랜덤으로 파일명 생성
                    String fileName = uuid.toString() + "_" + file.getOriginalFilename(); // 새로운 파일명

                    // 파일 저장 경로
                    File saveFile = new File(projectPath, fileName);
                    file.transferTo(saveFile); // 파일 저장

                    fileNames.add(fileName); // 파일명 리스트 추가
                    filePaths.add("/files/" + fileName); // 접근 가능한 파일 경로
                }
            }

            post.setFilename(fileNames); // 다중 파일명 저장
            post.setFilepath(filePaths); // 다중 파일 경로 저장
        }

        // 게시글 저장
        postRepository.save(post);
    }

    // 기존 파일 삭제 (수정 시 호출)
    public void deleteFile(List<String> fileNames) throws IOException {
        if (fileNames != null) {
            String projectPath = System.getProperty("user.dir") + File.separator + "src" + File.separator +
                    "main" + File.separator + "resources" + File.separator + "static" + File.separator + "files";

            for (String fileName : fileNames) {
                File file = new File(projectPath, fileName);
                if (file.exists()) {
                    file.delete();  // 파일 삭제
                }
            }
        }
    }

    // 게시글 리스트 조회
    public Page<Post> postList(Pageable pageable) {
        return postRepository.findAll(pageable);
    }

    // 특정 게시글 조회
    public Post postView(Integer id) {
        return postRepository.findById(id).orElse(null);
    }

    // 특정 게시글 삭제
    public void postDelete(Integer id) {
        Post post = postRepository.findById(id).orElse(null);
        if (post != null && post.getFilename() != null) {
            try {
                deleteFile(post.getFilename()); // 파일 삭제
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        postRepository.deleteById(id);
    }

    // 파일 수정
    @Transactional
    public void updatePost(Integer postId, Post updatedPost, MultipartFile[] files) throws Exception {
        Post postTemp = postRepository.findById(postId).orElse(null);

        if (postTemp != null) {
            postTemp.setTitle(updatedPost.getTitle());
            postTemp.setContent(updatedPost.getContent());

            if (files != null && files.length > 0) {
                // 기존 파일 삭제
                if (postTemp.getFilename() != null) {
                    deleteFile(postTemp.getFilename());
                }

                // 새로운 파일 저장
                String projectPath = System.getProperty("user.dir") + File.separator + "src" + File.separator +
                        "main" + File.separator + "resources" + File.separator + "static" + File.separator + "files";

                List<String> fileNames = new ArrayList<>();
                List<String> filePaths = new ArrayList<>();

                for (MultipartFile file : files) {
                    if (!file.isEmpty()) {
                        UUID uuid = UUID.randomUUID();
                        String fileName = uuid.toString() + "_" + file.getOriginalFilename();
                        File saveFile = new File(projectPath, fileName);
                        file.transferTo(saveFile);

                        fileNames.add(fileName);
                        filePaths.add("/files/" + fileName);
                    }
                }

                // 새로운 파일 정보 저장
                postTemp.setFilename(fileNames);
                postTemp.setFilepath(filePaths);
            }

            // 수정된 정보 저장
            postRepository.save(postTemp);
        }
    }

}
