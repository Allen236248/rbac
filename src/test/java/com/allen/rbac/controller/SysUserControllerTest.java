package com.allen.rbac.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

@TestPropertySource("classpath:SysUserControllerTestData.properties")
@SpringBootTest
@RunWith(SpringRunner.class)
public class SysUserControllerTest {

    @Autowired
    private Environment env;

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @Before
    public void setupMockMvc() {
        //初始化MockMvc对象
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    public void testAddSysUser() throws Exception {
        String data = env.getProperty("testAddSysUserData");
        System.out.println(data);
        JSONArray array = JSON.parseArray(data);
        List<String> cntList = array.toJavaList(String.class);
        for (String cnt : cntList) {
            System.out.println("cnt=" + cnt);
            mockMvc.perform(MockMvcRequestBuilders.post("/sys_user/add")
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .accept(MediaType.APPLICATION_JSON_UTF8)
                    .content(cnt))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andDo(MockMvcResultHandlers.print());
        }
    }

    /**
     * 返回EnvConfig对象
     *
     * @throws Exception
     */
    @Test
    public void getEnvConfigWithDomain() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/env_conf_domain")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.envName").value("dev"))
                .andDo(MockMvcResultHandlers.print());
    }

    /**
     * 返回EnvConfig对象
     *
     * @throws Exception
     */
    @Test
    public void addLearnResource() throws Exception {
        String json = "{\"author\":\"MockMvc测试\", \"name\":\"MockMvc测试\", \"url\":\"http://www.baidu.com/\"}";
        mockMvc.perform(MockMvcRequestBuilders.post("/learn/add")
                .contentType(MediaType.APPLICATION_JSON_UTF8) // 表示客户端发送的数据格式
                .accept(MediaType.APPLICATION_JSON_UTF8) // 表示客户端接受的数据格式
                .content(json.getBytes()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }
}
