package io.ruv.userservice.service.security;

import io.ruv.userservice.entity.User;
import io.ruv.userservice.repo.UserRepository;
import io.ruv.userservice.service.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailServiceImpl implements UserDetailsService {

    private final UserRepository repository;

    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {

        return repository.findById(username)
                .orElseThrow(() -> new UserNotFoundException(username));
    }
}
