package io.ruv.parcel.userservice.service.customer;

import io.ruv.parcel.user.api.UserRole;
import io.ruv.parcel.user.api.customer.BalanceChangeDto;
import io.ruv.parcel.user.api.customer.CustomerDto;
import io.ruv.parcel.user.api.customer.CustomerRegistrationDto;
import io.ruv.parcel.user.api.customer.CustomerUpdateDto;
import io.ruv.parcel.userservice.service.customer.converter.CustomerConverter;
import io.ruv.parcel.userservice.repo.UserRepository;
import io.ruv.parcel.userservice.service.exception.UserNotFoundException;
import io.ruv.parcel.userservice.service.exception.UsernameConflictException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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

    @Override
    @Retryable(retryFor = OptimisticLockingFailureException.class)
    @Transactional
    public CustomerDto updateBalance(String username, BalanceChangeDto balanceChangeDto) {

        var entity = repository.findById(username)
                .orElseThrow(() -> new UserNotFoundException(username));

        if (balanceChangeDto.getBalanceDelta() == null && balanceChangeDto.getLockedDelta() == null) {
            return converter.dto(entity);
        }

        if (balanceChangeDto.getBalanceDelta() != null) {

            var newBalance = entity.getBalance() + balanceChangeDto.getBalanceDelta();

            if (newBalance >= 0) {

                entity.setBalance(newBalance);
            } else {

                throw new ResponseStatusException(HttpStatus.CONFLICT, "Insufficient balance.");
            }
        }

        if (balanceChangeDto.getLockedDelta() != null) {

            var newLockedBalance = entity.getLockedBalance() + balanceChangeDto.getLockedDelta();

            if (newLockedBalance >= 0) {

                entity.setLockedBalance(newLockedBalance);
            } else {

                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                        String.format("User '%s''s locked balance is broken.", username));
            }
        }

        var saved = repository.save(entity);
        return converter.dto(saved);
    }
}
