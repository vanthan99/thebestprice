package dtu.thebestprice.converters;

import dtu.thebestprice.entities.User;
import dtu.thebestprice.payload.request.RegisterRequest;
import dtu.thebestprice.payload.request.UserGuestOrRetailerRequest;
import dtu.thebestprice.payload.request.UserUpdateRequest;
import dtu.thebestprice.payload.response.UserResponse;
import dtu.thebestprice.payload.response.UserRetailerResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserConverter {

    @Autowired
    PasswordEncoder passwordEncoder;

    public UserResponse toUserResponse(User user) {
        UserResponse userResponse = new UserResponse();

        userResponse.setUsername(user.getUsername());
        userResponse.setFullName(user.getFullName());
        userResponse.setAddress(user.getAddress());
        userResponse.setPhoneNumber(user.getPhoneNumber());
        userResponse.setEmail(user.getEmail());

        return userResponse;
    }

    public UserRetailerResponse toUserRetailerResponse(User user) {
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

    public User toUser(UserUpdateRequest request, User user) {
        try {
            Long phoneNumber = Long.parseLong(request.getPhoneNumber());
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Số điện thoại không đống định dạng. số nguyên từ 0 - 9");
        }
        user.setFullName(request.getFullName());
        user.setAddress(request.getAddress());
        user.setPhoneNumber(request.getPhoneNumber());

        return user;
    }

    public User toUser(UserGuestOrRetailerRequest request){
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFullName(request.getFullName());
        user.setAddress(request.getAddress());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setEmail(request.getEmail());
        return user;

    }

    public User toUser(RegisterRequest request){
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFullName(request.getFullName());
        user.setAddress(request.getAddress());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setEmail(request.getEmail());
        return user;

    }
}
