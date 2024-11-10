package com.example.sns.user;

import com.example.sns.exception.ErrorCode;
import com.example.sns.exception.SnsException;
import com.example.sns.user.model.User;
import com.example.sns.user.model.request.UserJoinRequest;
import com.example.sns.user.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    public void 회원가입() throws Exception {
        String userName = "test1";
        String password = "test1";

        when(userService.join(userName, password)).thenReturn(mock(User.class));

        mockMvc.perform(
                post("/api/users/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new UserJoinRequest(userName, password)))
        ).andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void 회원가입시_이미_가입된_userName인_경우_에러() throws Exception {
        String userName = "test1";
        String password = "test1";

        when(userService.join(userName, password))
                .thenThrow(new SnsException(ErrorCode.DUPLICATED_USER_NAME));

        mockMvc.perform(
                        post("/api/users/join")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(new UserJoinRequest(userName, password)))
                ).andDo(print())
                .andExpect(status().isConflict());
    }


    @Test
    public void 로그인() throws Exception {
        String userName = "test1";
        String password = "test1";

        when(userService.login(userName, password)).thenReturn("token");

        mockMvc.perform(
                        post("/api/users/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(new UserJoinRequest(userName, password)))
                ).andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void 로그인시_회원가입안된_userName_경우_에러() throws Exception {
        String userName = "test1";
        String password = "test1";

        when(userService.login(userName, password)).thenThrow(new SnsException(ErrorCode.USER_NOT_FOUND));

        mockMvc.perform(
                        post("/api/users/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(new UserJoinRequest(userName, password)))
                ).andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void 로그인시_틀린_패스워드_경우_에러() throws Exception {
        String userName = "test1";
        String password = "test1";

        when(userService.login(userName, password)).thenThrow(new SnsException(ErrorCode.INVALID_PASSWORD));

        mockMvc.perform(
                        post("/api/users/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(new UserJoinRequest(userName, password)))
                ).andDo(print())
                .andExpect(status().isUnauthorized());
    }

}
