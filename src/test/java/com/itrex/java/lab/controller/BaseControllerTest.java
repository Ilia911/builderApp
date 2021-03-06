package com.itrex.java.lab.controller;

import com.itrex.java.lab.service.ReporterService;
import com.itrex.java.lab.security.jwt.JwtConfigurer;
import com.itrex.java.lab.security.jwt.JwtTokenProvider;
import com.itrex.java.lab.service.CertificateService;
import com.itrex.java.lab.service.ContractService;
import com.itrex.java.lab.service.OfferService;
import com.itrex.java.lab.service.UserService;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@WebMvcTest
public abstract class BaseControllerTest {
    @MockBean
    protected CertificateService certificateService;
    @MockBean
    protected ContractService contractService;
    @MockBean
    protected OfferService offerService;
    @MockBean
    protected UserService userService;
    @MockBean
    protected ReporterService reporterService;
    @MockBean
    protected TestDatabaseFillingController testDatabaseFillingController;
    @MockBean
    protected JwtConfigurer jwtConfigurer;
    @MockBean
    protected JwtTokenProvider jwtTokenProvider;
}
