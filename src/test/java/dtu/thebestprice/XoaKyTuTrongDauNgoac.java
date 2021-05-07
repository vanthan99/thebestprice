package dtu.thebestprice;

public class XoaKyTuTrongDauNgoac {
    public static void main(String[] args) {
        String key = "LAPTOP DELL INSPIRON N5406 (70232602) (i5-1135G7, Ram 8GB, Màn hình cảm ứng 14\" Full HD, SSD 512GB, Win 10)";
        System.out.println("key. = " + key.indexOf("("));

        System.out.println(" keytqua " + key.substring(0,key.indexOf("(")));
    }
}
