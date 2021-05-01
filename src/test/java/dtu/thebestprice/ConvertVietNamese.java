package dtu.thebestprice;


import java.text.Normalizer;
import java.util.regex.Pattern;

public class ConvertVietNamese {
    public static void main(String[] args) {
        String s = "Trương Văn Thân yÊu Nhun đó í ì i";
        String temp = Normalizer.normalize(s, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        System.out.println("kết quả: " + pattern.matcher(temp)
                .replaceAll("")
                .replaceAll("đ","d")
                .replaceAll("Đ","D").toLowerCase()
        );
    }
}
