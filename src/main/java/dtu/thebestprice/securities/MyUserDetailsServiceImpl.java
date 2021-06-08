package dtu.thebestprice.securities;

import dtu.thebestprice.entities.User;
import dtu.thebestprice.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class MyUserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {

        User user = userRepository.findByUsernameAndDeleteFlgFalse(s).orElse(null);

        if (user == null){
            throw new UsernameNotFoundException("could not find by user!");
        }
        return new MyUserDetails(user);
    }
}
