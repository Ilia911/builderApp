package com.itrex.java.lab.repository.impl;

import com.itrex.java.lab.entity.Contract;
import com.itrex.java.lab.entity.Offer;
import com.itrex.java.lab.entity.User;
import com.itrex.java.lab.exeption.RepositoryException;
import com.itrex.java.lab.repository.BaseRepositoryTest;
import com.itrex.java.lab.repository.OfferRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class HibernateOfferRepositoryImplTest extends BaseRepositoryTest {

    private final OfferRepository repository;

    public HibernateOfferRepositoryImplTest() {
        super();
        this.repository = getApplicationContext().getBean(HibernateOfferRepositoryImpl.class);
    }

    @Test
    void find_validData_shouldReturnOffer() throws RepositoryException {
        //given
        int expectedOfferId = 1;
        int expectedOwnerId = 3;
        int expectedContractId = 1;
        int expectedPrice = 27500;
        //when
        Offer actualOffer = repository.find(expectedOfferId).get();
        //then
        assertEquals(expectedOfferId, actualOffer.getId());
        assertEquals(expectedOwnerId, actualOffer.getOfferOwner().getId());
        assertEquals(expectedContractId, actualOffer.getContract().getId());
        assertEquals(expectedPrice, actualOffer.getPrice());
    }

    @Test
    void find_notExistsOfferId_shouldReturnOptionalEmpty() throws RepositoryException {
        //given && when
        int offerId = 11;
        //then
        assertTrue(repository.find(offerId).isEmpty());
    }

    @Test
    void findAll_validData_shouldReturnOfferList() throws RepositoryException {
        //given
        int expectedOfferListSize = 2;
        //when
        int contractId = 1;
        List<Offer> actualOfferList = repository.findAll(contractId);
        //then
        assertEquals(expectedOfferListSize, actualOfferList.size());
    }

    @Test
    void delete_validData_shouldDeleteContract() throws RepositoryException {
        //when
        int offerId = 1;
        //then
        assertTrue(repository.delete(offerId));
    }

    @Test
    void delete_invalidData_shouldDeleteContract() throws RepositoryException {
        //when
        int offerId = 5;
        //then
        assertFalse(repository.delete(offerId));
    }

    @Test
    void update_validData_shouldUpdateOffer() throws RepositoryException {
        //given
        int expectedOfferId = 1;
        int expectedOfferOwnerId = 3;
        int expectedContractId = 1;
        int expectedPrice = 25000;

        User expectedOfferOwner = new User();
        expectedOfferOwner.setId(expectedOfferOwnerId);

        Contract expectedContract = new Contract();
        expectedContract.setId(expectedContractId);

        Offer expectedUpdatedOffer = new Offer(expectedOfferId, expectedOfferOwner, expectedContract, expectedPrice);
        //when
        Offer actualUpdatedOffer = repository.update(expectedUpdatedOffer);
        //then
        assertOfferEquals(expectedUpdatedOffer, actualUpdatedOffer);
    }

    @Test
    void add_validData_shouldCreateOffer() throws RepositoryException {
        //given
        int expectedOfferId = 3;
        int expectedOfferOwnerId = 4;
        int expectedContractId = 2;
        int expectedPrice = 27000;

        User expectedOfferOwner = new User();
        expectedOfferOwner.setId(expectedOfferOwnerId);

        Contract expectedContract = new Contract();
        expectedContract.setId(expectedContractId);

        Offer expectedNewOffer = new Offer(expectedOfferId, expectedOfferOwner, expectedContract, expectedPrice);
        //when
        Optional<Offer> actualNewOffer = repository.add(expectedNewOffer);
        //then
        assertOfferEquals(expectedNewOffer, actualNewOffer.get());
    }

    @Test
    void add_null_shouldThrowRepositoryException() {
        //given && when
        Offer offer = null;
        //then
        assertThrows(RepositoryException.class, () -> repository.add(offer));
    }

    @Test
    void add_offerWithNullPrice_shouldThrowRepositoryException() {
        //given && when
        int expectedOfferId = 3;
        int expectedOfferOwnerId = 4;
        int expectedContractId = 2;
        Integer expectedPrice = null;

        User expectedOfferOwner = new User();
        expectedOfferOwner.setId(expectedOfferOwnerId);

        Contract expectedContract = new Contract();
        expectedContract.setId(expectedContractId);

        Offer offer = new Offer(expectedOfferId, expectedOfferOwner, expectedContract, expectedPrice);
        //then
        assertThrows(RepositoryException.class, () -> repository.add(offer));
    }

    @Test
    void add_offerWithZeroPrice_shouldThrowRepositoryException() {
        //given && when
        int expectedOfferId = 3;
        int expectedOfferOwnerId = 4;
        int expectedContractId = 2;
        Integer expectedPrice = 0;

        User expectedOfferOwner = new User();
        expectedOfferOwner.setId(expectedOfferOwnerId);

        Contract expectedContract = new Contract();
        expectedContract.setId(expectedContractId);

        Offer offer = new Offer(expectedOfferId, expectedOfferOwner, expectedContract, expectedPrice);
        //then
        assertThrows(RepositoryException.class, () -> repository.add(offer));
    }

    private void assertOfferEquals(Offer expected, Offer actualOffer) {
        assertEquals(expected.getId(), actualOffer.getId());
        assertEquals(expected.getOfferOwner().getId(), actualOffer.getOfferOwner().getId());
        assertEquals(expected.getContract().getId(), actualOffer.getContract().getId());
        assertEquals(expected.getPrice(), actualOffer.getPrice());
    }
}