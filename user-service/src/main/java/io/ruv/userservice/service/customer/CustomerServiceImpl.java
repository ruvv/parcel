package io.ruv.userservice.service.customer;

import io.ruv.userservice.api.UserRole;
import io.ruv.userservice.api.customer.CustomerDto;
import io.ruv.userservice.api.customer.CustomerRegistrationDto;
import io.ruv.userservice.api.customer.CustomerUpdateDto;
import io.ruv.userservice.repo.UserRepository;
import io.ruv.userservice.service.customer.converter.CustomerConverter;
import io.ruv.userservice.service.exception.UserNotFoundException;
import io.ruv.userservice.service.exception.UsernameConflictException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final UserRepository repository;
    private final CustomerConverter converter;

    @Override
    public CustomerDto getCustomer(String username) {

        var entity = repository.findOneByUsernameAndHasRole(username, UserRole.CUSTOMER)
                .orElseThrow(() -> new UserNotFoundException(username));

        return converter.dto(entity);
    }

    @Override
    @Transactional
    public CustomerDto createCustomer(CustomerRegistrationDto customerRegistrationDto) {

        repository.findById(customerRegistrationDto.getUsername()).
                ifPresent(alreadyPresent -> UsernameConflictException.throwFor(alreadyPresent.getUsername()));

        var toSave = converter.entity(customerRegistrationDto);
        var saved = repository.save(toSave);

        return converter.dto(saved);
    }

    @Override
    @Transactional
    public CustomerDto updateCustomer(String username, CustomerUpdateDto customerUpdateDto) {

        var entity = repository.findById(username)
                .orElseThrow(() -> new UserNotFoundException(username));

        if (customerUpdateDto.getEmail() == null && customerUpdateDto.getPassword() == null) {
            return converter.dto(entity);
        }

        var toSave = converter.update(entity, customerUpdateDto);
        var saved = repository.save(toSave);

        return converter.dto(saved);
    }
}
