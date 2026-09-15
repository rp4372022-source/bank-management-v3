package com.bank.bms.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;

    @Override
    public User createUser(User user){
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email already exists");
        }
        if (userRepository.existsByPhoneNumber(user.getPhoneNumber())) {
            throw new RuntimeException("Phone number already exists");
        }
        return userRepository.save(user);
    }

    @Transactional
    @Override
    public User updateUser(Long id, User user){
        User existing = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Map the updated fields over explicitly
        existing.setName(user.getName());
        existing.setEmail(user.getEmail());
        existing.setPhoneNumber(user.getPhoneNumber());

        return userRepository.save(existing);   
    }

}
