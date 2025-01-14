package com.map_study.service;

import com.map_study.entity.Post;
import com.map_study.repository.PostRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    //글 작성
    @Transactional
    public void write(Post post){
        postRepository.save(post);

    }

    //게시글 리스트 처리
    public List<Post> postList(){
        return postRepository.findAll();
    }

    //특정 게시글 불러오기
    public Post postView(Integer id){
        return postRepository.findById(id).get();
    }

    //특정 게시글 삭제
    public void postDelete(Integer id){
        postRepository.deleteById(id);
    }

}
