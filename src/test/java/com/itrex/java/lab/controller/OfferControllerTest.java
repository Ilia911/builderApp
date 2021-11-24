package com.itrex.java.lab.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itrex.java.lab.entity.dto.ContractDTO;
import com.itrex.java.lab.entity.dto.OfferDTO;
import com.itrex.java.lab.entity.dto.UserDTO;
import com.itrex.java.lab.service.OfferService;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = OfferController.class)
class OfferControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OfferService service;

    @Test
    void find_validData_shouldReturnOffer() throws Exception {
        //given
        int expectedOfferId = 1;
        int expectedOwnerId = 3;
        int expectedContractId = 1;
        int expectedPrice = 27500;
        UserDTO ownerDTO = UserDTO.builder().id(expectedOwnerId).build();
        ContractDTO contractDTO = ContractDTO.builder().id(expectedContractId).build();
        OfferDTO expectedResponseBody = OfferDTO.builder().id(expectedOfferId).offerOwner(ownerDTO).contract(contractDTO).price(expectedPrice).build();
        //when
        when(service.find(expectedOfferId)).thenReturn(Optional.of(expectedResponseBody));
        //then
        MvcResult mvcResult = mockMvc.perform(get("/offers/{id}", expectedOfferId)
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andReturn();

        String actualResponseBody = mvcResult.getResponse().getContentAsString();

        assertEquals(objectMapper.writeValueAsString(expectedResponseBody), actualResponseBody);

    }

    @Test
    void findAllForGivenContract_validData_shouldReturnOfferList() throws Exception {
        //given
        int contractId = 1;
        OfferDTO offerDTO = OfferDTO.builder().build();
        List<OfferDTO> expectedResponseBody = Arrays.asList(offerDTO, offerDTO);
        //when
        when(service.findAll(contractId)).thenReturn(expectedResponseBody);
        //then
        MvcResult mvcResult = mockMvc.perform(get("/contracts/offers/{contractId}", contractId)
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andReturn();

        String actualResponseBody = mvcResult.getResponse().getContentAsString();

        assertEquals(objectMapper.writeValueAsString(expectedResponseBody), actualResponseBody);
    }

    @Test
    void delete_validData_shouldReturnResponseCode200() throws Exception {
        //given
        int offerId = 1;
        //when
        when(service.delete(offerId)).thenReturn(true);
        //then
        mockMvc.perform(delete("/offers/delete/{id}", offerId)
                        .contentType("application/json"))
                .andExpect(status().isOk());
    }

    @Test
    void delete_invalidData_shouldReturnResponseCode304() throws Exception {
        //given
        int offerId = 5;
        //when
        when(service.delete(offerId)).thenReturn(false);
        //then
        mockMvc.perform(delete("/offers/delete/{id}", offerId)
                        .contentType("application/json"))
                .andExpect(status().isNotModified());
    }

    @Test
    void update_validData_shouldUpdateOffer() throws Exception {
        //given
        int expectedOfferId = 1;
        int expectedOfferOwnerId = 3;
        int expectedContractId = 1;
        int expectedPrice = 25000;
        UserDTO expectedOfferOwnerDTO = UserDTO.builder().id(expectedOfferOwnerId).build();
        ContractDTO expectedContractDTO = ContractDTO.builder().id(expectedContractId).build();
        OfferDTO expectedResponseBody = OfferDTO.builder()
                .id(expectedOfferId).offerOwner(expectedOfferOwnerDTO).contract(expectedContractDTO).price(expectedPrice)
                .build();
        //when
        when(service.update(expectedResponseBody)).thenReturn(expectedResponseBody);
        //then
        MvcResult mvcResult = mockMvc.perform(put("/offers/update")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(expectedResponseBody)))
                .andExpect(status().isOk())
                .andReturn();
        String actualResponseBody = mvcResult.getResponse().getContentAsString();

        assertEquals(objectMapper.writeValueAsString(expectedResponseBody), actualResponseBody);
    }

    @Test
    void add_validData_shouldCreateOffer() throws Exception {
        //given
        int id = 3;
        int ownerId = 4;
        int contractId = 2;
        int price = 27000;
        UserDTO userDTO = UserDTO.builder().id(ownerId).build();
        ContractDTO contractDTO = ContractDTO.builder().id(contractId).build();

        OfferDTO expectedResponseBody = OfferDTO.builder()
                .id(id).offerOwner(userDTO).contract(contractDTO).price(price)
                .build();

        //when
        when(service.add(expectedResponseBody)).thenReturn(Optional.of(expectedResponseBody));
        //then
        MvcResult mvcResult = mockMvc.perform(post("/offer/new")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(expectedResponseBody)))
                .andExpect(status().isOk())
                .andReturn();
        String actualResponseBody = mvcResult.getResponse().getContentAsString();

        assertEquals(objectMapper.writeValueAsString(expectedResponseBody), actualResponseBody);
    }
}