package dtu.thebestprice.services.Impl;

import dtu.thebestprice.converters.RetailerConverter;
import dtu.thebestprice.entities.Retailer;
import dtu.thebestprice.entities.User;
import dtu.thebestprice.entities.enums.ERole;
import dtu.thebestprice.mail.MailTransport;
import dtu.thebestprice.payload.request.RetailerForUserRequest;
import dtu.thebestprice.payload.request.RetailerRequest;
import dtu.thebestprice.payload.response.ApiResponse;
import dtu.thebestprice.payload.response.RetailerResponse;
import dtu.thebestprice.payload.response.retailer.RetailerForAdminResponse;
import dtu.thebestprice.payload.response.retailer.RetailerForUserRetailerResponse;
import dtu.thebestprice.repositories.RetailerRepository;
import dtu.thebestprice.repositories.UserRepository;
import dtu.thebestprice.services.RetailerService;
import dtu.thebestprice.services.UserService;
import dtu.thebestprice.specifications.RetailerSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class RetailerServiceImpl implements RetailerService {
    @Autowired
    RetailerConverter retailerConverter;

    @Autowired
    RetailerRepository retailerRepository;

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    MailTransport mailTransport;

    @Override
    public Set<RetailerResponse> findAll() {
        return retailerRepository.findByDeleteFlgAndEnableAndApprove(false, true, true).stream().map(retailer -> retailerConverter.toRetailerResponse(retailer)).collect(Collectors.toSet());
    }

    @Override
    public ResponseEntity<Object> deleteById(String id) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Long retailerId = null;
        try {
            retailerId = Long.parseLong(id);
        } catch (NumberFormatException e) {
            throw new NumberFormatException("id c???a nh?? cung c???p kh??ng ????ng ?????nh d???ng");
        }

        /*
         * Ki???m tra id c?? t???n t???i?
         * */
        if (!retailerRepository.existsById(retailerId))
            return ResponseEntity.status(404).body(new ApiResponse(false, "id c???a nh?? cung c???p kh??ng t???n t???i"));

        Retailer retailer = retailerRepository.getOne(retailerId);

        User user = userRepository.findByUsernameAndDeleteFlgFalse(authentication.getName())
                .orElseThrow(() -> new RuntimeException("H??? th???ng kh??ng th??? nh???n bi???t b???n l?? ai"));


        if (retailer.getUser().equals(user) || user.getRole().equals(ERole.ROLE_ADMIN)) {
            retailer.setDeleteFlg(true);
            retailerRepository.save(retailer);

            return ResponseEntity.ok().body(new ApiResponse(true, "X??a nh?? cung c???p: " + retailer.getName() + " th??nh c??ng"));
        }
        throw new RuntimeException("B???n kh??ng ????? quy???n ????? x??a nh?? b??n l??? n??y");

    }

    @Override
    public ResponseEntity<Object> create(RetailerRequest retailerRequest, Long userId) {
        // ki???m tra xem c?? b??? tr??ng t??n v???i c??c nh?? b??n l??? kh??c hay kh??ng
        if (retailerRepository.existsByNameAndDeleteFlgFalse(retailerRequest.getName().trim()))
            throw new RuntimeException("T??n nh?? b??n l??? n??y b??? tr??ng v???i m???t nh?? b??n l??? kh??c.Vui l??ng nh???p t??n kh??c");

        // ki???m tra xem c?? b??? tr??ng logo v???i c??c nh?? b??n l??? kh??c hay kh??ng?
        if (retailerRepository.existsByLogoImageAndDeleteFlgFalse(retailerRequest.getLogo().trim()))
            throw new RuntimeException("logo c???a nh?? b??n l??? n??y b??? tr??ng v???i m???t nh?? b??n l??? kh??c.Vui l??ng nh???p logo kh??c");

        // ki???m tra xem c?? b??? trung homepage v???i nh?? b??n l??? kh??c hay kh??ng?
        if (retailerRepository.existsByHomePageAndDeleteFlgFalse(retailerRequest.getHomePage().trim()))
            throw new RuntimeException("Homepage nh?? b??n l??? n??y b??? tr??ng v???i m???t nh?? b??n l??? kh??c.Vui l??ng nh???p Homepage kh??c");

        User user = userRepository
                .findById(userId)
                .orElseThrow(() -> new RuntimeException("Kh??ng t???n t???i user"));

        Retailer retailer = retailerConverter.toEntity(retailerRequest);

        retailer.setUser(user);

        retailerRepository.save(retailer);
        return ResponseEntity.ok(new ApiResponse(true, "Th??m m???i th??nh c??ng"));
    }

    @Override
    public ResponseEntity<Object> create(RetailerRequest retailerRequest, User user, boolean approve, boolean isCheckValidate) {
        if (isCheckValidate) {
            // ki???m tra xem c?? b??? tr??ng t??n v???i c??c nh?? b??n l??? kh??c hay kh??ng
            if (retailerRepository.existsByNameAndDeleteFlgFalse(retailerRequest.getName().trim()))
                throw new RuntimeException("T??n nh?? b??n l??? n??y b??? tr??ng v???i m???t nh?? b??n l??? kh??c.Vui l??ng nh???p t??n kh??c");

            // ki???m tra xem c?? b??? tr??ng logo v???i c??c nh?? b??n l??? kh??c hay kh??ng?
            if (retailerRepository.existsByLogoImageAndDeleteFlgFalse(retailerRequest.getLogo().trim()))
                throw new RuntimeException("logo c???a nh?? b??n l??? n??y b??? tr??ng v???i m???t nh?? b??n l??? kh??c.Vui l??ng nh???p logo kh??c");

            // ki???m tra xem c?? b??? trung homepage v???i nh?? b??n l??? kh??c hay kh??ng?
            if (retailerRepository.existsByHomePageAndDeleteFlgFalse(retailerRequest.getHomePage().trim()))
                throw new RuntimeException("Homepage nh?? b??n l??? n??y b??? tr??ng v???i m???t nh?? b??n l??? kh??c.Vui l??ng nh???p Homepage kh??c");
        }

        Retailer retailer = retailerRepository.save(retailerConverter.toEntity(retailerRequest));
        retailer.setApprove(approve);
        retailer.setUser(user);
        retailerRepository.save(retailer);

        if (user.getRole().equals(ERole.ROLE_GUEST)) {
            user.setRole(ERole.ROLE_RETAILER);
            userRepository.save(user);
        }

        return ResponseEntity.ok(new ApiResponse(true, "Th??m m???i nh?? b??n l??? th??nh c??ng"));
    }

    @Override
    public ResponseEntity<Object> create(RetailerRequest retailerRequest, User user, boolean deleteFlg, boolean enable, boolean approve) {

        // ki???m tra xem c?? b??? tr??ng t??n v???i c??c nh?? b??n l??? kh??c hay kh??ng
        if (retailerRepository.existsByNameAndDeleteFlgFalse(retailerRequest.getName().trim()))
            throw new RuntimeException("T??n nh?? b??n l??? n??y b??? tr??ng v???i m???t nh?? b??n l??? kh??c.Vui l??ng nh???p t??n kh??c");

        // ki???m tra xem c?? b??? tr??ng logo v???i c??c nh?? b??n l??? kh??c hay kh??ng?
        if (retailerRepository.existsByLogoImageAndDeleteFlgFalse(retailerRequest.getLogo().trim()))
            throw new RuntimeException("logo c???a nh?? b??n l??? n??y b??? tr??ng v???i m???t nh?? b??n l??? kh??c.Vui l??ng nh???p logo kh??c");

        // ki???m tra xem c?? b??? trung homepage v???i nh?? b??n l??? kh??c hay kh??ng?
        if (retailerRepository.existsByHomePageAndDeleteFlgFalse(retailerRequest.getHomePage().trim()))
            throw new RuntimeException("Homepage nh?? b??n l??? n??y b??? tr??ng v???i m???t nh?? b??n l??? kh??c.Vui l??ng nh???p Homepage kh??c");


        Retailer retailer = retailerConverter.toEntity(retailerRequest);

        retailer.setApprove(approve);
        retailer.setUser(user);
        retailer.setDeleteFlg(deleteFlg);
        retailer.setEnable(enable);

        retailerRepository.save(retailer);

        if (user.getRole().equals(ERole.ROLE_GUEST)) {
            user.setRole(ERole.ROLE_RETAILER);
            userRepository.save(user);
        }

        return ResponseEntity.ok(new ApiResponse(true, "Th??m m???i nh?? b??n l??? th??nh c??ng"));
    }

    @Override
    public ResponseEntity<Object> create(RetailerForUserRequest retailerForUserRequest, User user, boolean deleteFlg, boolean enable, boolean approve) {
        // ki???m tra xem c?? b??? tr??ng t??n v???i c??c nh?? b??n l??? kh??c hay kh??ng
        if (retailerRepository.existsByNameAndDeleteFlgFalse(retailerForUserRequest.getName().trim()))
            throw new RuntimeException("T??n nh?? b??n l??? n??y b??? tr??ng v???i m???t nh?? b??n l??? kh??c.Vui l??ng nh???p t??n kh??c");

        // ki???m tra xem c?? b??? tr??ng logo v???i c??c nh?? b??n l??? kh??c hay kh??ng?
        if (retailerRepository.existsByLogoImageAndDeleteFlgFalse(retailerForUserRequest.getLogo().trim()))
            throw new RuntimeException("logo c???a nh?? b??n l??? n??y b??? tr??ng v???i m???t nh?? b??n l??? kh??c.Vui l??ng nh???p logo kh??c");

        // ki???m tra xem c?? b??? trung homepage v???i nh?? b??n l??? kh??c hay kh??ng?
        if (retailerRepository.existsByHomePageAndDeleteFlgFalse(retailerForUserRequest.getHomePage().trim()))
            throw new RuntimeException("Homepage nh?? b??n l??? n??y b??? tr??ng v???i m???t nh?? b??n l??? kh??c.Vui l??ng nh???p Homepage kh??c");


        Retailer retailer = retailerConverter.toEntity(retailerForUserRequest);

        retailer.setApprove(approve);
        retailer.setUser(user);
        retailer.setDeleteFlg(deleteFlg);
        retailer.setEnable(enable);

        retailerRepository.save(retailer);

//        if (user.getRole().equals(ERole.ROLE_GUEST)) {
//            user.setRole(ERole.ROLE_RETAILER);
//            userRepository.save(user);
//        }

        return ResponseEntity.ok(new ApiResponse(true, "Th??m m???i nh?? b??n l??? th??nh c??ng.H??y ?????i qu???n tr??? vi??n ph?? duy???t"));
    }

    @Override
    public boolean validateWhileCreateRetailer(RetailerRequest request) {
        // ki???m tra xem c?? b??? tr??ng t??n v???i c??c nh?? b??n l??? kh??c hay kh??ng
        if (retailerRepository.existsByNameAndDeleteFlgFalse(request.getName().trim()))
            throw new RuntimeException("T??n nh?? b??n l??? n??y b??? tr??ng v???i m???t nh?? b??n l??? kh??c.Vui l??ng nh???p t??n kh??c");

        // ki???m tra xem c?? b??? tr??ng logo v???i c??c nh?? b??n l??? kh??c hay kh??ng?
        if (retailerRepository.existsByLogoImageAndDeleteFlgFalse(request.getLogo().trim()))
            throw new RuntimeException("logo c???a nh?? b??n l??? n??y b??? tr??ng v???i m???t nh?? b??n l??? kh??c.Vui l??ng nh???p logo kh??c");

        // ki???m tra xem c?? b??? trung homepage v???i nh?? b??n l??? kh??c hay kh??ng?
        if (retailerRepository.existsByHomePageAndDeleteFlgFalse(request.getHomePage().trim()))
            throw new RuntimeException("Homepage nh?? b??n l??? n??y b??? tr??ng v???i m???t nh?? b??n l??? kh??c.Vui l??ng nh???p Homepage kh??c");
        return true;
    }

    @Override
    public ResponseEntity<Object> update(RetailerRequest retailerRequest, Long retailerId) {
        Retailer retailer = retailerRepository
                .findById(retailerId)
                .orElseThrow(() -> new RuntimeException("id c???a nh?? b??n l??? kh??ng t???n t???i"));

        long userId;
        try {
            userId = Long.parseLong(retailerRequest.getUserId());
        } catch (NumberFormatException e) {
            throw new RuntimeException("id user kh??ng t???n t???i");
        }

        User user = userRepository.getOne(userId);

        // ki???m tra xem th??ng tin tr?????c v?? sau khi update c?? gi???ng nhau hay kh??ng?
        if (retailerRepository.existsByIdAndNameAndDescriptionAndLogoImageAndHomePageAndUserAndDeleteFlgFalse(
                retailerId,
                retailerRequest.getName(),
                retailerRequest.getDescription(),
                retailerRequest.getLogo(),
                retailerRequest.getHomePage(),
                user
        ))
            throw new RuntimeException("Th??ng tin tr?????c v?? sau khi thay ?????i l?? gi???ng nhau");

        // ki???m tra t??n m???i c?? tr??ng v???i t??n c???a m???t nh?? b??n l??? kh??c hay kh??ng?
        if (
                retailerRepository.existsByNameAndDeleteFlgFalse(retailerRequest.getName())
                        && !retailer.getName().equalsIgnoreCase(retailerRequest.getName())
        )
            throw new RuntimeException("T??n nh?? b??n l??? n??y b??? tr??ng v???i m???t nh?? b??n l??? kh??c.Vui l??ng nh???p t??n kh??c");

        // ki???m tra h??nh ???nh c?? trung v???i t??n c???a m???t nh?? b??n l??? kh??c hay kh??ng?
        if (
                retailerRepository.existsByLogoImageAndDeleteFlgFalse(retailerRequest.getLogo())
                        && !retailer.getLogoImage().equalsIgnoreCase(retailerRequest.getLogo())
        )
            throw new RuntimeException("logo c???a nh?? b??n l??? n??y b??? tr??ng v???i m???t nh?? b??n l??? kh??c.Vui l??ng nh???p logo kh??c");

        // ki???m tra homepage c?? tr??ng v???i homepage c???a nh?? b??n l??? kh??c hay kh??ng?
        if (
                retailerRepository.existsByHomePageAndDeleteFlgFalse(retailerRequest.getHomePage())
                        && !retailer.getHomePage().equalsIgnoreCase(retailerRequest.getHomePage())
        )
            throw new RuntimeException("Homepage nh?? b??n l??? n??y b??? tr??ng v???i m???t nh?? b??n l??? kh??c.Vui l??ng nh???p Homepage kh??c");


        retailerRepository.save(retailerConverter.toEntity(retailerRequest, retailer));
        return ResponseEntity.ok(new ApiResponse(true, "C???p nh???t nh?? b??n l??? th??nh c??ng"));
    }

    @Override
    public ResponseEntity<Object> pageRetailerByApprove(boolean approve, String keyword, Pageable pageable) {

        Specification<Retailer> condition = Specification.where(
                RetailerSpecification.nameContaining(keyword)
                        .or(RetailerSpecification.descContaining(keyword)))
                .and(RetailerSpecification.deleteFlgFalse())
                .and(RetailerSpecification.isApprove(approve)
        );

        Page<Retailer> entityPage = retailerRepository.findAll(condition, pageable);
        Page<RetailerForAdminResponse> responsePage = entityPage.map(retailer -> retailerConverter.toRetailerForAdminResponse(retailer));

        return ResponseEntity.ok(responsePage);

//        if (keyword.trim().equals(""))
//            return ResponseEntity.ok(
//                    retailerRepository
//                            .findByDeleteFlgFalseAndApprove(approve, pageable)
//                            .map(retailer -> retailerConverter.toRetailerForAdminResponse(retailer))
//            );
//
//        return ResponseEntity.ok(
//                retailerRepository
//                        .findByDeleteFlgFalseAndApproveAndKeyword(approve, keyword, pageable)
//                        .map(retailer -> retailerConverter.toRetailerForAdminResponse(retailer))
//        );
    }

    @Override
    @Transactional
    public ResponseEntity<Object> approveRetailer(Long retailerId) throws MessagingException {
        Retailer retailer = retailerRepository
                .findById(retailerId)
                .orElseThrow(() -> new RuntimeException("Kh??ng t???n t???i nh?? b??n l???"));
        if (retailer.isApprove())
            throw new RuntimeException("Nh?? b??n l??? ???????c ???????c ph?? duy???t tr?????c ????");

        retailer.setApprove(true);
        retailer.setEnable(true);
        retailerRepository.save(retailer);

        // n???u m?? user l?? guest th?? update l??n role retailer
        User user = retailer.getUser();
        if (user.getRole().equals(ERole.ROLE_GUEST)) {
            user.setRole(ERole.ROLE_RETAILER);
            userRepository.save(user);
        }

        // g???i mail th??ng b??o cho user
        mailTransport.send(user.getEmail(),
                "Y??u c???u t???o nh?? cung c???p " + retailer.getName() + " c???a b???n ???? ???????c ph?? duy???t. H??y tr??? l???i trang web ????? ti???p t??c s??? d???ng c??c d???ch v??? c???a ch??ng t??i",
                "The best price th??ng b??o");

        return ResponseEntity.ok(new ApiResponse(true, "X??c nh???n nh?? b??n l??? th??nh c??ng, H??? th???ng ???? g???i mail th??ng b??o ?????n " + user.getUsername()));
    }

    @Override
    public ResponseEntity<Object> findById(long retailerId) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        User user =
                userRepository
                        .findByUsernameAndDeleteFlgFalse(authentication.getName())
                        .orElseThrow(() -> new RuntimeException("Ph???i ????ng nh???p v??o h??? th???ng"));

        Retailer retailer = retailerRepository
                .findById(retailerId)
                .orElseThrow(() -> new RuntimeException("Kh??ng t???n t???i nh?? b??n l???"));

        if (retailer.isDeleteFlg())
            throw new RuntimeException("Nh?? b??n l??? n??y ???? b??? x??a kh???i h??? th???ng");

        if (user.getRole().equals(ERole.ROLE_ADMIN) || user.getRole().equals(ERole.ROLE_SUPER) || user.getId().equals(retailer.getUser().getId()))
            return ResponseEntity.ok(retailerConverter.toRetailerForAdminResponse(retailer));
        throw new RuntimeException("B???n kh??ng th??? xem th??ng tin v?? nh?? b??n l??? n??y kh??ng thu???c b???n s??? h???u");
    }

    @Override
    @Transactional
    public ResponseEntity<Object> toggleEnable(long retailerId) {
        Retailer retailer = retailerRepository
                .findById(retailerId)
                .orElseThrow(() -> new RuntimeException("Kh??ng t???n t???i nh?? b??n l???"));

        String message = "";
        if (retailer.isEnable())
            message = "T???t tr???ng th??i ho???t ?????ng c???a nh?? b??n l??? th??nh c??ng";
        else message = "B???t tr???ng th??i ho???t ?????ng c???a nh?? b??n l??? th??nh c??ng";

        retailer.setEnable(!retailer.isEnable());
        retailerRepository.save(retailer);

        return ResponseEntity.ok(new ApiResponse(true, message));
    }

    @Override
    public ResponseEntity<Object> getRetailerByUser(User user) {
        List<Retailer> retailers = retailerRepository.findByDeleteFlgFalseAndUser(user);

        List<RetailerForUserRetailerResponse> result = retailers.stream().map(retailer -> retailerConverter.toRetailerForUserRetailerResponse(retailer)).collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    @Override
    @Transactional
    public ResponseEntity<Object> roleRetailerUpdate(RetailerForUserRequest retailerForUserRequest, long retailerId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Retailer retailer = retailerRepository
                .findById(retailerId)
                .orElseThrow(() -> new RuntimeException("id c???a nh?? b??n l??? kh??ng t???n t???i"));


        User user = userRepository.findByUsernameAndDeleteFlgFalse(authentication.getName())
                .orElseThrow(() -> new RuntimeException("Kh??ng t???n t???i user"));

        if (retailer.getUser().getRole().equals(ERole.ROLE_RETAILER) && user.getRole().equals(ERole.ROLE_RETAILER) && !retailer.getUser().getUsername().equals(authentication.getName()))
            throw new RuntimeException("B???n kh??ng ph???i l?? ch??? s??? h???u c???a nh?? b??n l??? n??y");

        // ki???m tra xem th??ng tin tr?????c v?? sau khi update c?? gi???ng nhau hay kh??ng?
        if (retailerRepository.existsByIdAndNameAndDescriptionAndLogoImageAndHomePageAndUserAndDeleteFlgFalse(
                retailerId,
                retailerForUserRequest.getName(),
                retailerForUserRequest.getDescription(),
                retailerForUserRequest.getLogo(),
                retailerForUserRequest.getHomePage(),
                user
        ))
            throw new RuntimeException("Th??ng tin tr?????c v?? sau khi thay ?????i l?? gi???ng nhau");

        // ki???m tra t??n m???i c?? tr??ng v???i t??n c???a m???t nh?? b??n l??? kh??c hay kh??ng?
        if (
                retailerRepository.existsByNameAndDeleteFlgFalse(retailerForUserRequest.getName())
                        && !retailer.getName().equalsIgnoreCase(retailerForUserRequest.getName())
        )
            throw new RuntimeException("T??n nh?? b??n l??? n??y b??? tr??ng v???i m???t nh?? b??n l??? kh??c.Vui l??ng nh???p t??n kh??c");

        // ki???m tra h??nh ???nh c?? trung v???i t??n c???a m???t nh?? b??n l??? kh??c hay kh??ng?
        if (
                retailerRepository.existsByLogoImageAndDeleteFlgFalse(retailerForUserRequest.getLogo())
                        && !retailer.getLogoImage().equalsIgnoreCase(retailerForUserRequest.getLogo())
        )
            throw new RuntimeException("logo c???a nh?? b??n l??? n??y b??? tr??ng v???i m???t nh?? b??n l??? kh??c.Vui l??ng nh???p logo kh??c");

        // ki???m tra homepage c?? tr??ng v???i homepage c???a nh?? b??n l??? kh??c hay kh??ng?
        if (
                retailerRepository.existsByHomePageAndDeleteFlgFalse(retailerForUserRequest.getHomePage())
                        && !retailer.getHomePage().equalsIgnoreCase(retailerForUserRequest.getHomePage())
        )
            throw new RuntimeException("Homepage nh?? b??n l??? n??y b??? tr??ng v???i m???t nh?? b??n l??? kh??c.Vui l??ng nh???p Homepage kh??c");

        retailer.setApprove(false);

        retailerRepository.save(retailerConverter.toEntity(retailerForUserRequest, retailer));
        return ResponseEntity.ok(new ApiResponse(true, "C???p nh???t nh?? b??n l??? th??nh c??ng"));
    }
}
