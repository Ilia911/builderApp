package com.itrex.java.lab.report.repository;

import com.itrex.java.lab.report.entity.CustomerReportDTO;

import java.time.LocalDate;
import java.util.List;

public interface ReportRepository {

    List<CustomerReportDTO> getCustomerReport(LocalDate firstStartContractDate,
                                              LocalDate endStartContractDate, int startWithContractCount, int size);
}