package com.eazybytes.accounts.service.impl;

import com.eazybytes.accounts.dto.AccountsDto;
import com.eazybytes.accounts.dto.CustomerDetailsDto;
import com.eazybytes.accounts.dto.CustomerDto;
import com.eazybytes.accounts.entity.Accounts;
import com.eazybytes.accounts.entity.Customer;
import com.eazybytes.accounts.exception.ResourceNotFoundException;
import com.eazybytes.accounts.feignclient.CardsDto;
import com.eazybytes.accounts.feignclient.CardsFeignClient;
import com.eazybytes.accounts.feignclient.LoansDto;
import com.eazybytes.accounts.feignclient.LoansFeignClient;
import com.eazybytes.accounts.mapper.AccountsMapper;
import com.eazybytes.accounts.mapper.CustomerMapper;
import com.eazybytes.accounts.repository.AccountsRepository;
import com.eazybytes.accounts.repository.CustomerRepository;
import com.eazybytes.accounts.service.ICustomerService;
import lombok.NoArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class CustomerServiceImpl implements ICustomerService {

    private AccountsRepository accountsRepository;
    private CustomerRepository customerRepository;
    private CardsFeignClient cardsFeignClient;
    private LoansFeignClient loansFeignClient;

    public CustomerServiceImpl(AccountsRepository accountsRepository, CustomerRepository customerRepository, CardsFeignClient cardsFeignClient, LoansFeignClient loansFeignClient){
        this.accountsRepository = accountsRepository;
        this.customerRepository = customerRepository;
        this.cardsFeignClient = cardsFeignClient;
        this.loansFeignClient = loansFeignClient;
    }

    @Override
    public CustomerDetailsDto fetchCustomerDetails(String number, String correlationId) {
        Customer customer = customerRepository.findByMobileNumber(number).orElseThrow(
                () -> new ResourceNotFoundException("Customer", "mobileNumber", number)
        );

        Accounts accounts = accountsRepository.findByCustomerId(customer.getCustomerId()).orElseThrow(
                () -> new ResourceNotFoundException("Account", "customerId", customer.getCustomerId().toString())
        );

        CustomerDetailsDto customerDto = CustomerMapper.mapToCustomerDetailsDto(customer, new CustomerDetailsDto());
        customerDto.setAccountsDto(AccountsMapper.mapToAccountsDto(accounts, new AccountsDto()));

        ResponseEntity<LoansDto> loansDto = loansFeignClient.fetchLoanDetails(correlationId, number);
        customerDto.setLoansDto(loansDto.getBody());

        ResponseEntity<CardsDto> cardsDto = cardsFeignClient.fetchCardDetails(correlationId, number);
        customerDto.setCardsDto(cardsDto.getBody());

        return customerDto;
    }
}
