package com.eazybytes.accounts.service;

import com.eazybytes.accounts.dto.CustomerDetailsDto;

public interface ICustomerService {

    /*
     * @param mobile number - input mobile number
     * @return CustomerDetailsDto based on the mobile number
     */
    CustomerDetailsDto fetchCustomerDetails(String number, String correlationId);

}
