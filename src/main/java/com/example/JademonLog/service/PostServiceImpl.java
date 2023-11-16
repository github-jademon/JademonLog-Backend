package com.example.JademonLog.service;

import com.example.JademonLog.dto.post.ListPostResponse;
import com.example.JademonLog.dto.post.PostResponse;
import com.example.JademonLog.entity.post.Post;
import com.example.JademonLog.entity.post.PostRepository;
import com.example.JademonLog.response.BasicResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    public ResponseEntity getList() {
        try {
            List<Post> posts = postRepository.findAll();
            List<ListPostResponse> data = new ArrayList<>();

            if(posts.isEmpty()) {
                BasicResponse res = new BasicResponse().error("게시글이 존재하지 않습니다.");

                return new ResponseEntity<>(res, res.getHttpStatus());
            }

            for(Post post:posts) {
                data.add(new ListPostResponse(post.getId(), post.getTitle(), post.getDesc(), post.getDate(), post.getImage(), post.getWriter()));
            }

            BasicResponse res = BasicResponse.builder()
                    .code(HttpStatus.OK.value())
                    .httpStatus(HttpStatus.OK)
                    .message("게시글들을 정상적으로 불러왔습니다.")
                    .result(data)
                    .build();

            return new ResponseEntity(res, res.getHttpStatus());
        } catch(Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity getPost(Long id) {
        try{
            Post post = postRepository.getById(id);
            PostResponse data = PostResponse.builder()
                    .title(post.getTitle())
                    .source(post.getSource())
                    .date(post.getDate())
                    .image(post.getImage())
                    .writer(post.getWriter())
                    .build();

            BasicResponse res = BasicResponse.builder()
                    .code(HttpStatus.OK.value())
                    .httpStatus(HttpStatus.OK)
                    .message("게시글을 정상적으로 불러왔습니다.")
                    .result(data)
                    .build();

            return new ResponseEntity(res, res.getHttpStatus());
        } catch(Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
