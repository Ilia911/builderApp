package com.itrex.java.lab.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itrex.java.lab.entity.dto.OfferDTO;
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

class OfferControllerTest extends BaseControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(username = "Unnecessary", authorities = "offer:read")
    void find_validData_shouldReturnOffer() throws Exception {
        //given
        int expectedOfferId = 1;
        int ownerId = 3;
        int contractId = 1;
        int expectedPrice = 27500;
        OfferDTO expectedResponseBody = OfferDTO.builder().id(expectedOfferId).offerOwnerId(ownerId).contractId(contractId).price(expectedPrice).build();
        //when
        when(offerService.find(expectedOfferId)).thenReturn(Optional.of(expectedResponseBody));
        //then
        MvcResult mvcResult = mockMvc.perform(get("/offers/{id}", expectedOfferId)
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andReturn();
        String actualResponseBody = mvcResult.getResponse().getContentAsString();

        assertEquals(objectMapper.writeValueAsString(expectedResponseBody), actualResponseBody);

    }

    @Test
    @WithMockUser(username = "Unnecessary", authorities = "offer:read")
    void findAllForGivenContract_validData_shouldReturnOfferList() throws Exception {
        //given
        int id = 1;
        OfferDTO offerDTO = OfferDTO.builder().build();
        List<OfferDTO> expectedResponseBody = Arrays.asList(offerDTO, offerDTO);
        //when
        when(offerService.findAll(id)).thenReturn(expectedResponseBody);
        //then
        MvcResult mvcResult = mockMvc.perform(get("/offers/contracts/{id}", id)
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andReturn();
        String actualResponseBody = mvcResult.getResponse().getContentAsString();

        assertEquals(objectMapper.writeValueAsString(expectedResponseBody), actualResponseBody);
    }

    @Test
    @WithMockUser(username = "Unnecessary", authorities = "offer:crud")
    void delete_validData_shouldReturnResponseCode200() throws Exception {
        //given
        int offerId = 1;
        //when
        when(offerService.delete(offerId)).thenReturn(true);
        //then
        mockMvc.perform(delete("/offers/{id}", offerId)
                        .contentType("application/json"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "Unnecessary", authorities = "offer:crud")
    void delete_invalidData_shouldReturnResponseCode304() throws Exception {
        //given
        int offerId = 5;
        //when
        when(offerService.delete(offerId)).thenReturn(false);
        //then
        mockMvc.perform(delete("/offers/{id}", offerId)
                        .contentType("application/json"))
                .andExpect(status().isNotModified());
    }

    @Test
    @WithMockUser(username = "Unnecessary", authorities = "offer:crud")
    void update_validData_shouldUpdateOffer() throws Exception {
        //given
        int expectedOfferId = 1;
        int offerOwnerId = 3;
        int contractId = 1;
        int expectedPrice = 25000;
        OfferDTO expectedResponseBody = OfferDTO.builder()
                .id(expectedOfferId).offerOwnerId(offerOwnerId).contractId(contractId).price(expectedPrice)
                .build();
        //when
        when(offerService.update(expectedResponseBody)).thenReturn(expectedResponseBody);
        //then
        MvcResult mvcResult = mockMvc.perform(put("/offers/{id}", expectedOfferId)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(expectedResponseBody)))
                .andExpect(status().isOk())
                .andReturn();
        String actualResponseBody = mvcResult.getResponse().getContentAsString();

        assertEquals(objectMapper.writeValueAsString(expectedResponseBody), actualResponseBody);
    }

    @Test
    @WithMockUser(username = "Unnecessary", authorities = "offer:crud")
    void add_validData_shouldCreateOffer() throws Exception {
        //given
        int id = 3;
        int ownerId = 4;
        int contractId = 2;
        int price = 27000;
        OfferDTO expectedResponseBody = OfferDTO.builder()
                .id(id).offerOwnerId(ownerId).contractId(contractId).price(price)
                .build();
        //when
        when(offerService.add(expectedResponseBody)).thenReturn(Optional.of(expectedResponseBody));
        //then
        MvcResult mvcResult = mockMvc.perform(post("/offers")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(expectedResponseBody)))
                .andExpect(status().isOk())
                .andReturn();
        String actualResponseBody = mvcResult.getResponse().getContentAsString();

        assertEquals(objectMapper.writeValueAsString(expectedResponseBody), actualResponseBody);
    }
}