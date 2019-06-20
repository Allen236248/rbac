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

    @Test
    public void testListSysUser() throws Exception {
        String data = env.getProperty("testListSysUserData");
        mockMvc.perform(MockMvcRequestBuilders.post("/sys_user/list")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8).content(data))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void testGetSysUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/sys_user/get")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .param("id", "23"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void testDelSysUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/sys_user/del")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .param("id", "25"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void testUpdateSysUser() throws Exception {
        String data = env.getProperty("testUpdateSysUserData");
        mockMvc.perform(MockMvcRequestBuilders.post("/sys_user/update")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .content(data))
                .andExpect(MockMvcResultMatchers.status().isOk())
                //.andExpect(MockMvcResultMatchers.jsonPath("$.envName").value("dev"))
                .andDo(MockMvcResultHandlers.print());
    }
}
