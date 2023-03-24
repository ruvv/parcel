package io.ruv.proto.user.service;

import io.ruv.proto.user.api.UserRole;
import io.ruv.proto.user.api.courier.*;
import io.ruv.proto.user.api.customer.CustomerDto;
import io.ruv.proto.user.api.customer.CustomerRegistrationDto;
import io.ruv.proto.user.api.customer.CustomerUpdateDto;
import io.ruv.proto.user.entity.User;
import io.ruv.proto.user.repo.UserRepository;
import io.ruv.proto.user.service.converter.UserConverter;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements CustomerService, CourierService, UserDetailsService {

    private final UserRepository userRepository;
    private final UserConverter userConverter;

    @Override
    public CustomerDto currentCustomer() {

        var userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        var entity = userRepository.findById(userDetails.getUsername())
                .orElseThrow(() -> throwUserNotFound(userDetails.getUsername()));

        return userConverter.toCustomerDto(entity);
    }

    @Override
    @Transactional
    public CustomerDto createCustomer(CustomerRegistrationDto customerRegistrationDto) {

        userRepository.findById(customerRegistrationDto.getUsername()).ifPresent(this::throwUsernameConflict);

        var toSave = userConverter.toEntity(customerRegistrationDto);
        var saved = userRepository.save(toSave);

        return userConverter.toCustomerDto(saved);
    }

    @Override
    @Transactional
    public CustomerDto updateCurrentCustomer(CustomerUpdateDto customerUpdateDto) {

        var userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        var entity = userRepository.findById(userDetails.getUsername())
                .orElseThrow(() -> throwUserNotFound(userDetails.getUsername()));

        var toSave = userConverter.updateEntity(entity, customerUpdateDto);
        var saved = userRepository.save(toSave);

        return userConverter.toCustomerDto(saved);
    }

    @Override
    public CourierDto currentCourier() {

        var userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        var entity = userRepository.findById(userDetails.getUsername())
                .orElseThrow(() -> throwUserNotFound(userDetails.getUsername()));

        return userConverter.toCourierDto(entity);
    }

    @Override
    public CourierDto updateCurrentCourierStatus(CourierStatus courierStatus) {

        var userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        var entity = userRepository.findById(userDetails.getUsername())
                .orElseThrow(() -> throwUserNotFound(userDetails.getUsername()));

        entity.getCourierExtension().setStatus(courierStatus);
        var saved = userRepository.save(entity);

        return userConverter.toCourierDto(saved);
    }

    @Override
    public CourierDto updateCurrentCourierLocation(Location location) {

        var userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        var entity = userRepository.findById(userDetails.getUsername())
                .orElseThrow(() -> throwUserNotFound(userDetails.getUsername()));

        entity.getCourierExtension()
                .setLatitude(location.getLatitude())
                .setLongitude(location.getLongitude());
        var saved = userRepository.save(entity);

        return userConverter.toCourierDto(saved);
    }

    @Override
    @Transactional
    public CourierDto createCourier(CourierCreateDto courierCreateDto) {

        userRepository.findById(courierCreateDto.getUsername()).ifPresent(this::throwUsernameConflict);

        var toSave = userConverter.toEntity(courierCreateDto);
        var saved = userRepository.save(toSave);

        return userConverter.toCourierDto(saved);
    }

    @Override
    @Transactional
    public CourierDto updateCourierByUsername(String username, CourierUpdateDto courierUpdateDto) {

        var entity = userRepository.findById(username).orElseThrow(() -> throwUserNotFound(username));

        var toSave = userConverter.updateEntity(entity, courierUpdateDto);
        var saved = userRepository.save(toSave);

        return userConverter.toCourierDto(saved);
    }

    @Override
    public CourierDto findCourierById(String username) {

        var entity = userRepository.findById(username)
                .orElseThrow(() -> throwUserNotFound(username));

        if (!entity.getRoles().contains("courier")) { //todo
            throwUserNotFound(username);
        }

        return userConverter.toCourierDto(entity);
    }

    @Override
    public List<CourierDto> findCouriers(int page, int size) {

        return findByHasRole(page, size, UserRole.COURIER.value());
    }

    private List<CourierDto> findByHasRole(int page, int size, String role) {

        var pageable = PageRequest.of(page, size);

        return userRepository.findByHasRole(role, pageable).stream()
                .map(userConverter::toCourierDto)
                .collect(Collectors.toList());
    }

//    @Override
//    public UserDto findByUsername(String username) {
//
//        return userRepository.findById(username)
//                .map(userConverter::toDto)
//                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("User '%s' was not found.", username)));
//    }

//    @Override
//    @Transactional
//    public UserDto createCustomer(UserDto userDto) {
//
//        userRepository.findById(userDto.getUsername()).ifPresent(usernameConflict -> {
//            throw new ResponseStatusException(HttpStatus.CONFLICT, String.format("Username '%s' is not available.", userDto.getUsername()));
//        });
//
//        var toSave = userConverter.toEntity(userDto);
//        var saved = userRepository.save(toSave);
//
//        return userConverter.toDto(saved);
//    }


//    @Override
//    @Transactional
//    public UserDto updateByUsername(String username, UserDto userDto) {
//
//        userDto.setUsername(username);
//
//        var current = userRepository.findById(username)
//                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("User '%s' was not found.", username)));
//
//        var toSave = userConverter.updateEntity(current, userDto);
//        var saved = userRepository.save(toSave);
//
//        return userConverter.toDto(saved);
//    }


//    @Override
//    @Transactional
//    public void deleteByUsername(String username) {
//
//        var current = userRepository.findById(username)
//                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("User '%s' was not found.", username)));
//
//        current.setEnabled(false);
//
//        userRepository.save(current);
//    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        return userRepository.findById(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("User '%s' was not found.", username)));
    }


    private void throwUsernameConflict(User alreadyExisting) {

        var message = String.format("Username '%s' is not available.", alreadyExisting.getUsername());

        throw new ResponseStatusException(HttpStatus.CONFLICT, message);
    }

    private RuntimeException throwUserNotFound(String username) {

        var message = String.format("User '%s' was not found.", username);

        throw new ResponseStatusException(HttpStatus.NOT_FOUND, message);
    }
}
