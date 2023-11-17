package com.example.JademonLog.service;

import com.example.JademonLog.dto.post.ListPostResponse;
import com.example.JademonLog.dto.post.PostResponse;
import com.example.JademonLog.entity.image.Image;
import com.example.JademonLog.entity.member.Member;
import com.example.JademonLog.entity.post.Post;
import com.example.JademonLog.entity.post.PostRepository;
import com.example.JademonLog.jwt.TokenProvider;
import com.example.JademonLog.response.BasicResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final ImageServiceImpl imageService;
    private final TokenProvider tokenProvider;
    private final HttpServletRequest httpServletRequest;
    @Override
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

    @Override
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

    public ResponseEntity<BasicResponse> savePost(Post request, MultipartFile multipartFile) throws IOException {

        Optional<Member> member = tokenProvider.getMemberByToken(httpServletRequest);

        if(member.isEmpty()) {
            BasicResponse res = new BasicResponse().error("사용자를 찾을 수 없습니다.");

            return new ResponseEntity<>(res, res.getHttpStatus());
        }

        Image image = imageService.saveImage(List.of(multipartFile)).get(0);

        Post post = Post.builder()
                .title(request.getTitle())
                .desc(request.getDesc())
                .source(request.getSource())
                .image(image)
                .date(LocalDateTime.now())
                .writer(member.get())
                .build();

        postRepository.save(post);

        BasicResponse basicResponse = BasicResponse.builder()
                .code(HttpStatus.OK.value())
                .httpStatus(HttpStatus.OK)
                .message("이미지 등록이 정상적으로 되었습니다.")
                .result(post)
                .build();

        return new ResponseEntity<>(basicResponse, basicResponse.getHttpStatus());
    }
}
