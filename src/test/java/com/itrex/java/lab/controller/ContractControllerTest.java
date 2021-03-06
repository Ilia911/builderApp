package com.itrex.java.lab.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itrex.java.lab.entity.dto.ContractDTO;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ContractControllerTest extends BaseControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(username = "Unnecessary", authorities = "contract:read")
    void find_validData_shouldReturnContract() throws Exception {
        //given
        int expectedContractId = 1;
        int ownerId = 1;
        String expectedDescription = "first contract";
        LocalDate expectedStartDate = LocalDate.parse("2022-01-01");
        LocalDate expectedEndDate = LocalDate.parse("2022-12-31");
        Integer expectedPrice = 28000;
        //when
        ContractDTO expectedResponseBody = ContractDTO.builder()
                .id(expectedContractId).ownerId(ownerId).description(expectedDescription).startDate(expectedStartDate)
                .endDate(expectedEndDate).startPrice(expectedPrice)
                .build();
        when(contractService.find(expectedContractId)).thenReturn(Optional.of(expectedResponseBody));
        //then
        MvcResult mvcResult = mockMvc.perform(get("/contracts/{id}", expectedContractId)
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andReturn();

        String actualResponseBody = mvcResult.getResponse().getContentAsString();

        assertEquals(objectMapper.writeValueAsString(expectedResponseBody), actualResponseBody);
    }

    @Test
    @WithMockUser(username = "Unnecessary", authorities = "contract:read")
    void findAll_validData_shouldReturnContractList() throws Exception {
        //given
        ContractDTO contractDTO = ContractDTO.builder().build();
        List<ContractDTO> expectedResponseBody = Arrays.asList(contractDTO, contractDTO);
        //when
        when(contractService.findAll()).thenReturn(expectedResponseBody);
        List<ContractDTO> actualList = contractService.findAll();
        //then
        MvcResult mvcResult = mockMvc.perform(get("/contracts")
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andReturn();

        String actualResponseBody = mvcResult.getResponse().getContentAsString();

        assertEquals(objectMapper.writeValueAsString(expectedResponseBody), actualResponseBody);
    }

    @Test
    @WithMockUser(username = "Unnecessary", authorities = "contract:crud")
    void delete_validData_shouldDeleteContract() throws Exception {
        //given
        int contractId = 1;
        //when
        when(contractService.delete(contractId)).thenReturn(true);
        //then
        mockMvc.perform(delete("/contracts/{id}", contractId)
                        .contentType("application/json"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "Unnecessary", authorities = "contract:crud")
    void update_validData_shouldUpdateExistedContract() throws Exception {
        //given
        int contractId = 1;
        int ownerId = 1;
        ContractDTO expectedResponseBody = ContractDTO.builder()
                .id(contractId).ownerId(ownerId).description("edited contract").startDate(LocalDate.now().plusDays(2L))
                .endDate(LocalDate.now().plusDays(5L)).startPrice(50000)
                .build();
        //when
        when(contractService.update(expectedResponseBody)).thenReturn(expectedResponseBody);
        //then
        MvcResult mvcResult = mockMvc.perform(put("/contracts/{id}", contractId)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(expectedResponseBody)))
                .andExpect(status().isOk())
                .andReturn();
        String actualResponseBody = mvcResult.getResponse().getContentAsString();

        assertEquals(objectMapper.writeValueAsString(expectedResponseBody), actualResponseBody);
    }

    @Test
    @WithMockUser(username = "Unnecessary", authorities = "contract:crud")
    void add_validData_shouldReturnNewCreatedContract() throws Exception {
        //given
        int ownerId = 1;
        ContractDTO expectedResponseBody = ContractDTO.builder()
                .id(3).ownerId(ownerId).description("new contract").startDate(LocalDate.now().plusDays(1L))
                .endDate(LocalDate.now().plusDays(2L)).startPrice(50000).build();
        //when
        when(contractService.add(expectedResponseBody)).thenReturn(Optional.of(expectedResponseBody));
        //then
        MvcResult mvcResult = mockMvc.perform(post("/contracts")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(expectedResponseBody)))
                .andExpect(status().isOk())
                .andReturn();
        String actualResponseBody = mvcResult.getResponse().getContentAsString();

        assertEquals(objectMapper.writeValueAsString(expectedResponseBody), actualResponseBody);
    }
}