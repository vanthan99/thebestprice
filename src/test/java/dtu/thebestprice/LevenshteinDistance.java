package dtu.thebestprice;

import info.debatty.java.stringsimilarity.JaroWinkler;

public class LevenshteinDistance {
    public static int dist(char[] s1, char[] s2) {

        // distance matrix - to memoize distances between substrings
        // needed to avoid recursion
        int[][] d = new int[s1.length + 1][s2.length + 1];

        // d[i][j] - would contain distance between such substrings:
        // s1.subString(0, i) and s2.subString(0, j)

        for (int i = 0; i < s1.length + 1; i++) {
            d[i][0] = i;
        }

        for (int j = 0; j < s2.length + 1; j++) {
            d[0][j] = j;
        }

        for (int i = 1; i < s1.length + 1; i++) {
            for (int j = 1; j < s2.length + 1; j++) {
                int d1 = d[i - 1][j] + 1;
                int d2 = d[i][j - 1] + 1;
                int d3 = d[i - 1][j - 1];
                if (s1[i - 1] != s2[j - 1]) {
                    d3 += 1;
                }
                d[i][j] = Math.min(Math.min(d1, d2), d3);
            }
        }
        return d[s1.length][s2.length];
    }

    public static void main(String[] args) {
        String s1 = "CPU: AMD Ryzen 7-5800H (3.20GHz upto 4.40GHz, 16MB)\n" +
                "RAM: 8GB DDR4 3200MHz (2 khe, tối đa 64GB)\n" +
                "Ổ cứng: 512GB NVMe PCIe Gen3x4 SSD\n" +
                "VGA: AMD Radeon RX 5500M 4GB GDDR6\n" +
                "Màn hình: 15.6 inch FHD (1920*1080), 144Hz 45%NTSC IPS-Level\n" +
                "Pin: 3 cell, 52Whr\n" +
                "Cân nặng: 1.96 kg\n" +
                "Tính năng: Đèn nền bàn phím\n" +
                "Màu sắc: Đen\n" +
                "OS: Windows 10 Home";
        String s2 = "MSI GF63 Thin 10SC-014VN";

        JaroWinkler jw = new JaroWinkler();
        System.out.println("tỷ lệ trung: " + jw.similarity(s1, s2) * 100);


    }
}
