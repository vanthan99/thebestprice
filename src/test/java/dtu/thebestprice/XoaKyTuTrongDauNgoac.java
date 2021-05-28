package dtu.thebestprice;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class XoaKyTuTrongDauNgoac {
    public static void main(String[] args) {
        String title = "DELL INSPIRON 3501 70243203 I5-1135G7 4GB SSD 256GB MX330 2GB 15.6\" WIN10 (CHÍNH HÃNG)";
        System.out.println(title.indexOf("70"));
        String code = title.substring(title.indexOf("70"), title.indexOf("70" + 8));
        System.out.println(" ket qua:  " + code);
    }
}
