package com.map_study.service;

import com.map_study.entity.Post;
import com.map_study.repository.PostRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PostService {

    @Autowired //스프링빈이 알아서 주입해줌
    private PostRepository postRepository;

    @Transactional
    public void write(Post post){

        postRepository.save(post); //Entity를 넣어줌

    }
}
