package ru.ayukov.diplom.security;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.ayukov.diplom.domain.Users;
import ru.ayukov.diplom.repos.UsersRepo;

@Service("userDetailsServiceImpl")
public class UserDetailsServiceImpl implements UserDetailsService{

    private final UsersRepo usersRepo;

    @Autowired
    public UserDetailsServiceImpl(UsersRepo usersRepo){
        this.usersRepo = usersRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        Users users = usersRepo.findByUserName(userName).orElseThrow(() ->
                new UsernameNotFoundException("User doesn't exists"));
        return SecurityUser.fromUser(users);
    }
}
