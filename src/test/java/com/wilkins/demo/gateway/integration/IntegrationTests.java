package com.wilkins.demo.gateway.integration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wilkins.demo.gateway.model.Resource;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class IntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @Test
    public void listOfResourcesIsReturnedWhenGetResources() throws Exception {
        String responseBody = mockMvc.perform(get("/resource")).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        List<Resource> resources = mapper.readValue(responseBody, new TypeReference<List<Resource>>(){});
        assertThat(resources.size()).isEqualTo(1);
    }

    @Test
    public void singleResourceIsReturnedWhenGetResource() throws Exception {
        String responseBody = mockMvc.perform(get("/resource/r1")).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        Resource resource = mapper.readValue(responseBody, Resource.class);
        assertThat(resource).isNotNull();
    }

}
