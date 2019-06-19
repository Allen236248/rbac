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
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

@TestPropertySource("classpath:SysRoleControllerTestData.properties")
@SpringBootTest
@RunWith(SpringRunner.class)
public class SysRoleControllerTest {

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

    /**
     * @throws Exception
     */
    @Test
    public void testAddSysRole() throws Exception {
        String data = env.getProperty("testAddSysRoleData");
        System.out.println(data);
        JSONArray array = JSON.parseArray(data);
        List<String> cntList = array.toJavaList(String.class);
        for (String cnt : cntList) {
            System.out.println("cnt=" + cnt);
            mockMvc.perform(MockMvcRequestBuilders.post("/sys_role/add")
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .accept(MediaType.APPLICATION_JSON_UTF8)
                    .content(cnt))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andDo(MockMvcResultHandlers.print());
        }
    }

    @Test
    public void testListSysRole() throws Exception {
        String data = env.getProperty("testListSysRoleData");
        mockMvc.perform(MockMvcRequestBuilders.post("/sys_role/list")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8).content(data))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void testGetSysRole() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/sys_role/get")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .param("id", "10"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void testDelSysRole() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/sys_role/del")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .param("id", "10"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void testUpdateSysRole() throws Exception {
        String data = env.getProperty("testUpdateSysRoleData");
        mockMvc.perform(MockMvcRequestBuilders.post("/sys_role/update")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .content(data))
                .andExpect(MockMvcResultMatchers.status().isOk())
                //.andExpect(MockMvcResultMatchers.jsonPath("$.envName").value("dev"))
                .andDo(MockMvcResultHandlers.print());
    }

}
