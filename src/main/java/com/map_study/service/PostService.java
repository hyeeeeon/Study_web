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
import java.util.List;
import java.util.UUID;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    // 글 작성 처리
    @Transactional
    public void write(Post post, MultipartFile file) throws Exception {
        // 파일이 새로 업로드되었을 때만 처리
        if (file != null && !file.isEmpty()) {
            String projectPath = System.getProperty("user.dir") + File.separator + "src" + File.separator +
                    "main" + File.separator + "resources" + File.separator + "static" + File.separator + "files";

            UUID uuid = UUID.randomUUID(); // 랜덤으로 이름 생성
            String fileName = uuid.toString() + "_" + file.getOriginalFilename(); // 새로운 파일명 생성

            File saveFile = new File(projectPath, fileName);
            file.transferTo(saveFile); // 파일 저장

            post.setFilename(fileName); // 저장된 파일의 이름
            post.setFilepath("/files/" + fileName); // 저장된 파일의 경로, 이름
        } else if (post.getFilename() != null) {
            // 파일이 없을 때, 기존 파일명 유지
            post.setFilepath("/files/" + post.getFilename()); // 기존 파일 경로 유지
        } else {
            // 파일도 없고, 기존 파일도 없는 경우 처리
            post.setFilename(null);
            post.setFilepath(null);
        }

        // DB에 게시글 저장
        postRepository.save(post);
    }

    //게시글 리스트 처리
    public Page<Post> postList(Pageable pageable){
        return postRepository.findAll(pageable);
    }

    //특정 게시글 불러오기
    public Post postView(Integer id){
        return postRepository.findById(id).get();
    }

    //특정 게시글 삭제
    public void postDelete(Integer id){
        postRepository.deleteById(id);
    }

    //파일 크기 초과 예외 처리 (기본 설정으로 설정될 경우)
    public void validateFileSize(MultipartFile file) throws Exception {
        // 예시: 최대 파일 크기 10MB로 제한
        long maxFileSize = 10 * 1024 * 1024; // 10MB
        if (file.getSize() > maxFileSize) {
            throw new Exception("파일 크기가 너무 큽니다. 최대 파일 크기는 10MB입니다.");
        }
    }
}
