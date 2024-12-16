package com.example.joinup.user.service;

import com.example.joinup.challengeboard.dto.MyChallengePageDTO;
import com.example.joinup.challengeboard.dto.MyChallengePostDTO;
import com.example.joinup.challengeboard.entity.ChallengePage;
import com.example.joinup.challengeboard.repository.ChallengePageRepository;
import com.example.joinup.competitionboard.dto.MyCompetitionPageDTO;
import com.example.joinup.competitionboard.entity.CompetitionPage;
import com.example.joinup.competitionboard.repository.CompetitionPageRepository;
import com.example.joinup.challengeboard.entity.ChallengePost;
import com.example.joinup.challengeboard.repository.ChallengePostRepository;
import com.example.joinup.security.JwtUtil;
import com.example.joinup.user.UserDTO.MyUserDTO;
import com.example.joinup.user.entity.User;
import com.example.joinup.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    // 추가된 레포지토리 필드
    private final ChallengePageRepository challengePageRepository;
    private final CompetitionPageRepository competitionPageRepository;
    private final ChallengePostRepository challengePostRepository;

    public UserService(UserRepository userRepository, JwtUtil jwtUtil,
                       ChallengePageRepository challengePageRepository,
                       CompetitionPageRepository competitionPageRepository,
                       ChallengePostRepository challengePostRepository) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.challengePageRepository = challengePageRepository;
        this.competitionPageRepository = competitionPageRepository;
        this.challengePostRepository = challengePostRepository;
    }

    @Transactional
    public User registerUser(User user) {
        boolean exists = userRepository.findById(user.getId()).isPresent();
        if (exists) {
            throw new RuntimeException("이미 존재하는 ID입니다: " + user.getId());
        }
        return userRepository.save(user);
    }

    @Transactional
    public User login(String id, String password) throws Exception {
        Optional<User> userOptional = userRepository.findById(id);
        User user = userOptional.orElseThrow(() -> new Exception("해당 ID가 존재하지 않습니다."));
        if (!user.getPassword().equals(password)) {
            throw new Exception("비밀번호가 일치하지 않습니다.");
        }
        return user;
    }

    public String generateToken(User user) {
        return jwtUtil.generateToken(user.getId());
    }

    @Transactional(readOnly = true)
    public User findById(String userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다: " + userId));
    }

    @Transactional(readOnly = true)
    public Map<String, Object> getMyPageData(User user) {
        Map<String, Object> myPageData = new HashMap<>();

        // 유저 정보 추가
        MyUserDTO userInfo = new MyUserDTO(user);
        myPageData.put("userInfo", userInfo);

        // ChallengePost 조회 및 DTO 변환
        List<MyChallengePostDTO> challengePosts = challengePostRepository.findByUser(user)
                .stream()
                .map(MyChallengePostDTO::new)
                .collect(Collectors.toList());

        // ChallengePage 조회 및 DTO 변환
        List<MyChallengePageDTO> challengePages = challengePageRepository.findByUser(user)
                .stream()
                .map(MyChallengePageDTO::new)
                .collect(Collectors.toList());

        // CompetitionPage 조회 및 DTO 변환
        List<MyCompetitionPageDTO> competitionPages = competitionPageRepository.findByUser(user)
                .stream()
                .map(MyCompetitionPageDTO::new)
                .collect(Collectors.toList());

        // 마이페이지 데이터 구성
        myPageData.put("challengePosts", challengePosts);
        myPageData.put("challengePages", challengePages);
        myPageData.put("competitionPages", competitionPages);

        return myPageData;
    }



}
