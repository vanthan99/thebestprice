package dtu.thebestprice.converters;

import dtu.thebestprice.entities.User;
import dtu.thebestprice.payload.response.UserResponse;
import dtu.thebestprice.payload.response.UserRetailerResponse;
import dtu.thebestprice.repositories.UserRepository;
import org.springframework.stereotype.Component;

@Component
public class UserConverter {
    public UserResponse toUserResponse(User user){
        UserResponse userResponse = new UserResponse();

        userResponse.setUsername(user.getUsername());
        userResponse.setFullName(user.getFullName());
        userResponse.setAddress(user.getAddress());
        userResponse.setPhoneNumber(user.getPhoneNumber());
        userResponse.setEmail(user.getEmail());

        return userResponse;
    }

    public UserRetailerResponse toUserRetailerResponse(User user){
        UserRetailerResponse userResponse = new UserRetailerResponse();
        userResponse.setId(user.getId());
        userResponse.setUsername(user.getUsername());
        userResponse.setFullName(user.getFullName());
        userResponse.setAddress(user.getAddress());
        userResponse.setPhoneNumber(user.getPhoneNumber());
        userResponse.setEmail(user.getEmail());
        userResponse.setRegisterDay(user.getCreatedAt().toLocalDate());
        return userResponse;
    }
}
