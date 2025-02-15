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
                    UUID uuid = UUID.randomUUID(); // 랜덤으로 이름 생성
                    String fileName = uuid.toString() + "_" + file.getOriginalFilename(); // 새로운 파일명 생성

                    // 파일 저장 경로
                    File saveFile = new File(projectPath, fileName);
                    file.transferTo(saveFile); // 파일 저장

                    fileNames.add(fileName); // 파일명 리스트에 추가
                    filePaths.add("/files/" + fileName); // 웹에서 접근할 경로는 /files/ 로 시작
                }
            }

            post.setFilename(fileNames); // 다중 파일명을 저장
            post.setFilepath(filePaths); // 다중 파일 경로를 저장
        } else {
            post.setFilename(null);
            post.setFilepath(null);
        }

        // DB에 게시글 저장
        postRepository.save(post);
    }


    // 파일 삭제 처리 (파일이 교체될 때 기존 파일 삭제)
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

    // 게시글 리스트 처리
    public Page<Post> postList(Pageable pageable){
        return postRepository.findAll(pageable);
    }

    // 특정 게시글 불러오기
    public Post postView(Integer id){
        Post post = postRepository.findById(id).orElse(null);

        if (post != null) {
            // Lazy Loading 컬렉션 초기화
            post.getFilename().size();  // 컬렉션을 명시적으로 초기화
            post.getFilepath().size();  // 컬렉션을 명시적으로 초기화
        }

        return post;
    }

    // 특정 게시글 삭제
    public void postDelete(Integer id){
        postRepository.deleteById(id);
    }
}
