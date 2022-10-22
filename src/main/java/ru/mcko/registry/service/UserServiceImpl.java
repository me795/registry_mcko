package ru.mcko.registry.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.mcko.registry.entity.User;
import ru.mcko.registry.repository.read.RoleRepositoryRead;
import ru.mcko.registry.repository.read.UserRepositoryRead;
import ru.mcko.registry.repository.write.UserRepositoryWrite;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepositoryRead userRepositoryRead;
    private final UserRepositoryWrite userRepositoryWrite;
    private final RoleRepositoryRead roleRepositoryRead;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserServiceImpl(UserRepositoryRead userRepositoryRead,
                           UserRepositoryWrite userRepositoryWrite,
                           RoleRepositoryRead roleRepositoryRead,
                           BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepositoryRead = userRepositoryRead;
        this.userRepositoryWrite = userRepositoryWrite;
        this.roleRepositoryRead = roleRepositoryRead;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }


    @Override
    @Transactional(propagation= Propagation.REQUIRED,isolation= Isolation.READ_COMMITTED)
    public User create(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        Integer userId = userRepositoryWrite.create(user);
        user.setId(userId);
        if (user.getRoles() != null) {
            for (var role : user.getRoles()) {
                userRepositoryWrite.addRole(user.getId(), role.getId());
            }
        }

        return user;
    }

    @Override
    @Transactional(propagation= Propagation.REQUIRED,isolation= Isolation.READ_COMMITTED)
    public User update(User user) {

        if (user.getPassword().trim().length() != 0){
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        }else{
            user.setPassword(null);
        }

        userRepositoryWrite.update(user);
        userRepositoryWrite.deleteAllRoles(user.getId());
        if (user.getRoles() != null) {
            for (var role : user.getRoles()) {
                userRepositoryWrite.addRole(user.getId(), role.getId());
            }
        }

        return user;
    }


    @Override
    public User findByUsername(String username) {
        return userRepositoryRead.findByName(username);
    }

    @Override
    public User findActiveByUsername(String username) {
        return userRepositoryRead.findActiveByName(username);
    }

    @Override
    public User findById(Integer id) {
        return userRepositoryRead.findById(id);
    }

    @Override
    public List<User> listAll() {
        return userRepositoryRead.listAll();
    }
}
