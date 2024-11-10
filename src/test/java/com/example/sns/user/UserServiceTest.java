package com.example.sns.user;

import com.example.sns.exception.ErrorCode;
import com.example.sns.exception.SnsException;
import com.example.sns.user.model.entity.UserEntity;
import com.example.sns.user.repository.UserRepository;
import com.example.sns.user.service.UserService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private BCryptPasswordEncoder passwordEncoder;

    @Test
    public void 회원가입_정상일_경우() {
        String userName = "test1";
        String password = "password";

        when(userRepository.findByUserName(userName)).thenReturn(Optional.empty());
        when(passwordEncoder.encode(password)).thenReturn("encrypt_password");
        when(userRepository.save(any(UserEntity.class))).thenReturn(UserEntityFixture.get(userName, password));

        userService.join(userName, password);
    }

    @Test
    public void 회원가입시_존재하는_userName인_경우_에러() {
        String userName = "test1";
        String password = "password";

        UserEntity userFixture = UserEntityFixture.get(userName, password);

        when(userRepository.findByUserName(userName)).thenReturn(Optional.of(userFixture));
        when(passwordEncoder.encode(password)).thenReturn("encrypt_password");
        when(userRepository.save(any())).thenReturn(Optional.of(userFixture));

        Assertions.assertThatThrownBy(() -> userService.join(userName, password))
                .isInstanceOf(SnsException.class)
                .hasMessageContaining(ErrorCode.DUPLICATED_USER_NAME.getMessage())
                .extracting(e -> ((SnsException)e).getErrorCode()).isEqualTo(ErrorCode.DUPLICATED_USER_NAME);
    }

    @Test
    public void 로그인_정상일_경우() {
        String userName = "test1";
        String password = "password";

        UserEntity userFixture = UserEntityFixture.get(userName, password);

        when(userRepository.findByUserName(userName)).thenReturn(Optional.of(userFixture));
        when(passwordEncoder.matches(password, userFixture.getPassword())).thenReturn(true);

        userService.login(userName, password);
    }

    @Test
    public void 로그인시_userName으로_회원가입한_유저가_없는_경우_에러() {
        String userName = "test1";
        String password = "password";

        when(userRepository.findByUserName(userName)).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> userService.login(userName, password))
                .isInstanceOf(SnsException.class)
                .hasMessageContaining(ErrorCode.USER_NOT_FOUND.getMessage());

        SnsException e = Assertions.catchThrowableOfType(() ->
                userService.login(userName, password), SnsException.class);
        Assertions.assertThat(e.getErrorCode()).isEqualTo(ErrorCode.USER_NOT_FOUND);
    }

    @Test
    public void 로그인시_password가_틀린_경우_에러() {
        String userName = "test1";
        String password = "password";
        String wrongPassword = "wrong";

        UserEntity userEntity = UserEntityFixture.get(userName, password);

        when(userRepository.findByUserName(userName)).thenReturn(Optional.of(userEntity));

        Assertions.assertThatThrownBy(() -> userService.login(userName, wrongPassword))
                .isInstanceOf(SnsException.class);
    }
}
