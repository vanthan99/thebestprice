package dtu.thebestprice;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PasswordValidator {
    public static void main(String[] args) {
        String password = "adminadmin ";

        int index =  StringUtils.indexOfAny(password, String.valueOf(Arrays.asList((
                "À,Á,Â,Ã,È,É,Ê,Ì,Í,Ò,Ó,Ô,Õ,Ù,Ú,Ă,Đ,Ĩ,Ũ,Ơ,à,á,â,ã,è,é," +
                "ê,ì,í,ò,ó,ô,õ,ù,ú,ă,đ,ĩ,ũ,ơ,Ư,Ă,Ạ,Ả,Ấ,Ầ,Ẩ,Ẫ,Ậ,Ắ,Ằ,Ẳ,Ẵ,Ặ,Ẹ," +
                "Ẻ,Ẽ,Ề,Ề,Ể,ư,ă,ạ,ả,ấ,ầ,ẩ,ẫ,ậ,ắ,ằ,ẳ,ẵ,ặ,ẹ,ẻ,ẽ,ề,ề,ể,Ễ,Ệ,Ỉ,Ị," +
                "Ọ,Ỏ,Ố,Ồ,Ổ,Ỗ,Ộ,Ớ,Ờ,Ở,Ỡ,Ợ,Ụ,Ủ,Ứ,Ừ,ễ,ệ,ỉ,ị,ọ,ỏ,ố,ồ,ổ,ỗ,ộ,ớ,ờ," +
                "ở,ỡ,ợ,ụ,ủ,ứ,ừ,Ử,Ữ,Ự,Ỳ,Ỵ,Ý,Ỷ,Ỹ,ử,ữ,ự,ỳ,ỵ,ỷ,ỹ, ").split(","))));
        if (index == -1)
            System.out.println("Hợp lệ");
        else System.out.println("Không hợp lệ");
    }
}
