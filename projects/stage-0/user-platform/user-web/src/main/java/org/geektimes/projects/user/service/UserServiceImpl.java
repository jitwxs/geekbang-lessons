package org.geektimes.projects.user.service;

import org.geektimes.projects.user.domain.User;
import org.geektimes.projects.user.repository.DatabaseUserRepository;

/**
 * @author jitwxs
 * @date 2021年03月06日 17:20
 */
public class UserServiceImpl implements UserService {
    private final DatabaseUserRepository databaseUserRepository = new DatabaseUserRepository();

    @Override
    public boolean register(User user) {
        return databaseUserRepository.save(user);
    }

    @Override
    public boolean deregister(User user) {
        return false;
    }

    @Override
    public boolean update(User user) {
        return false;
    }

    @Override
    public User queryUserById(Long id) {
        return null;
    }

    @Override
    public User queryUserByNameAndPassword(String name, String password) {
        return null;
    }
}
