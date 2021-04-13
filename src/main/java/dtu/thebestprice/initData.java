package dtu.thebestprice;

import dtu.thebestprice.entities.*;
import dtu.thebestprice.entities.enums.ERole;
import dtu.thebestprice.payload.request.CategoryRequest;
import dtu.thebestprice.payload.request.RegisterRequest;
import dtu.thebestprice.repositories.*;
import dtu.thebestprice.services.AuthService;
import dtu.thebestprice.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;

@Component
public class initData {
    @Autowired
    AuthService authService;

    @Autowired
    CategoryService categoryService;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    RetailerRepository retailerRepository;

    @Autowired
    BrandRepository brandRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ImageRepository imageRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    ProductRetailerRepository productRetailerRepository;

    @Autowired
    PriceRepository priceRepository;

//    @PostConstruct
    public void init() {
        initRole();
        initUser();
        initCategory();
        initRetailer();
        initBrand();
        initProduct();
        initManyProductRetailer();
    }

    private void initManyProductRetailer() {
        List<Product> productList = productRepository.findAll();
        initProductRetailerForXuanVinh(
                productList.get(0),
                9890000L,
                "http://xuanvinh.vn/acer-aspire-3-a315-23-r0ml-nx-hvusv-004-r3-3250u-4gb-512gb-ssd-15-6fhd-win-10-fpt"
        );

        initProductRetailerForXuanVinh(
                productList.get(1),
                9990000L,
                "http://xuanvinh.vn/acer-aspire-a514-51-525e-h6vsv-002-i5-8265u-4gb-hdd1tb"
                );

        initProductRetailerForXuanVinh(
                productList.get(2),
                10000000L,
                "http://xuanvinh.vn/acer-aspire-a315-56-37dv-nx-hs5sv-001-i3-1005g1-4gb-256gb-ssd-15-6fhd-win-10-dgw"
                );

        initProductRetailerForXuanVinh(
                productList.get(3),
                11900000L,
                "http://xuanvinh.vn/acer-aspire-a315-56-59xy-i5-1035g1-4gb-256gb-win10-fpt"
                );

        initProductRetailerForXuanVinh(
                productList.get(4),
                11950000L,
                "http://xuanvinh.vn/acer-aspire-a515-55-37hd-nx-hsmsv-006-i3-1005g1-4gb-256gb-15-6-fhd-win-10-fpt"
                );

        initProductRetailerForXuanVinh(
                productList.get(5),
                12890000L,
                "http://xuanvinh.vn/acer-aspire-5-a514-53-346u-nx-hussv-005-i3-1005g1-4gb-512gb-ssd-14-0fhd-win-10-fpt"
                );

        initProductRetailerForXuanVinh(
                productList.get(6),
                13900000L,
                "http://xuanvinh.vn/acer-aspire-a315-55g-504m-i5-10210u-4gb-512gb-ssd-mx230-2gb-15-6fhd-win-10-fpt"
                );

        initProductRetailerForXuanVinh(
                productList.get(7),
                16990000L,
                "http://xuanvinh.vn/acer-aspire-7-a715-42g-r4st-r5-5500u-8gb-256gb-ssd-gtx-1650-4gb-15-6-fhd-win10-chinh-hang"
                );

        initProductRetailerForXuanVinh(
                productList.get(8),
                17290000L,
                "http://xuanvinh.vn/acer-aspire-5-a514-54-540f-i5-1135g7-8gb-512gb-ssd-14-win10-chinh-hang"
                );

        initProductRetailerForXuanVinh(
                productList.get(9),
                17500000L,
                "http://xuanvinh.vn/acer-swift-3-sf314-58-39bz-nx-hpmsv-007-i3-10110u-8gb-512gbssd-win-10-fpt"
                );

        initProductRetailerForXuanVinh(
                productList.get(10),
                18500000L,
                "http://xuanvinh.vn/acer-nitro-5-an515-44-r9jm-nh-q9msv-003-r5-4600h-8gb-512gb-ssd-gtx1650-4gb-15-6-144hz-win10-dgw"
                );

        initProductRetailerForXuanVinh(
                productList.get(11),
                18900000L,
                "http://xuanvinh.vn/acer-swift-3-sf314-41-r8g9-nx-hfdsv-003-r7-3700u-8gb-512gb-ssd-win10-dgw"
                );

        initProductRetailerForXuanVinh(
                productList.get(12),
                19800000L,
                "http://xuanvinh.vn/acer-nitro-5-an515-55-5923-nh-q7nsv-004-i5-10300h-8gb-512gb-ssd-gtx1650ti-4gb-144hz-win-10"
                );
    }

    private void initProductRetailerForXuanVinh(Product product,Long price, String url){
        Retailer xuanVinhRetailer = retailerRepository.findByHomePage("http://xuanvinh.vn/");
        ProductRetailer productRetailer = new ProductRetailer();

        productRetailer.setUrl(url);
        productRetailer.setProduct(product);
        productRetailer.setRetailer(xuanVinhRetailer);
        productRetailerRepository.save(productRetailer);
        initPrice(productRetailer,price);
    }

    private void initProduct() {
        Category categoryLaptopAcer = categoryRepository.findByTitle("Laptop ACER");
        Brand brandAcer = brandRepository.findByName("ACER");
        Retailer phiLongRetailer = retailerRepository.findByHomePage("https://philong.com.vn/");
        saveProduct(
                "LAPTOP ACER SWIFT 3 SF314-42-R0TR (Ryzen 5-4500U, Ram 16GB, SSD 1TB, Màn hình 14\" Full HD, Win 10)",
                "CPU:&nbsp;AMD Ryzen 5-4500U>\n" +
                        "RAM:&nbsp;&nbsp;16GB LPDDR4 3733MHz Onboard>\n" +
                        "Lưu trữ:&nbsp;1TB SSD M.2 PCIE Gen3x4>\n" +
                        "Đồ hoạ:&nbsp;AMD Radeon Graphics>\n" +
                        "Màn hình:&nbsp;14\" FHD (1920 x 1080) Acer ComfyView™ IPS LED LCD>\n" +
                        "Hệ điều hành: Windows 10 Home SL 64-bit",
                "Hãng sản xuất Laptop Acer\n" +
                        "Tên sản phẩm Swift 3 SF314-42-R0TR NX.HSESV.002\n" +
                        "Bộ vi xử lý\n" +
                        "Bộ vi xử lý AMD Ryzen™ 5-4500U\n" +
                        "Tốc độ 2.30GHz upto 4.00GHz, 6 cores 6 threads\n" +
                        "Bộ nhớ đệm 8MB Cache\n" +
                        "Bộ nhớ trong (RAM)\n" +
                        "Dung lượng 16GB (4GBx4) LPDDR4X\n" +
                        "Số khe cắm không nâng cấp được Ram\n" +
                        "Ổ cứng\n" +
                        "Dung lượng 1TB PCIe NVMe SSD\n" +
                        "Tốc độ vòng quay\n" +
                        "Khe cắm SSD mở rộng Có (đã cắm sẵn 1TB SSD PCIe NVMe)\n" +
                        "Ổ đĩa quang (ODD)\n" +
                        "Hiển thị\n" +
                        "Màn hình 14.0 inch FHD(1920 x 1080) 60Hz IPS Acer ComfyView™ IPS LED LCD\n" +
                        "Độ phân giải 1920*1080\n" +
                        "Đồ Họa (VGA)\n" +
                        "Card màn hình AMD Radeon™ Graphics\n" +
                        "Kết nối (Network)\n" +
                        "Wireless Intel® Wireless Wi-Fi 6 AX200\n" +
                        "LAN\n" +
                        "Bluetooth Bluetooth® 5.1\n" +
                        "Bàn phím , Chuột\n" +
                        "Kiểu bàn phím Bàn phím tiêu chuẩn - Đèn nền bàn phím\n" +
                        "Chuột Cảm ứng đa điểm\n" +
                        "Giao tiếp mở rộng\n" +
                        "Kết nối USB 1 x USB Type-C port with DC-in 1 x USB 3.2 Gen 1 port with power-off charging 1 x USB 2.0 port\n" +
                        "Kết nối HDMI/VGA 1 x HDMI® port\n" +
                        "Tai nghe 1 x Headset / Speaker jack\n" +
                        "Camera 720p HD audio\n" +
                        "Card mở rộng Không\n" +
                        "LOA 2 Loa\n" +
                        "Kiểu Pin 48Wh\n" +
                        "Sạc pin Đi kèm\n" +
                        "Hệ điều hành (bản quyền) đi kèm Windows 10 Home + Office\n" +
                        "Kích thước (Dài x Rộng x Cao) 323.4 (W) x 218.9 (D) x 15.95 (H) mm\n" +
                        "Trọng Lượng 1.19kg\n" +
                        "Màu sắc Bạc\n" +
                        "Hãng sản xuất Laptop Acer\n" +
                        "Tên sản phẩm Swift 3 SF314-42-R0TR NX.HSESV.002\n" +
                        "Bộ vi xử lý\n" +
                        "Bộ vi xử lý AMD Ryzen™ 5-4500U\n" +
                        "Tốc độ 2.30GHz upto 4.00GHz, 6 cores 6 threads\n" +
                        "Bộ nhớ đệm 8MB Cache\n" +
                        "Bộ nhớ trong (RAM)\n" +
                        "Dung lượng 16GB (4GBx4) LPDDR4X\n" +
                        "Số khe cắm không nâng cấp được Ram\n" +
                        "Ổ cứng\n" +
                        "Dung lượng 1TB PCIe NVMe SSD\n" +
                        "Tốc độ vòng quay\n" +
                        "Khe cắm SSD mở rộng Có (đã cắm sẵn 1TB SSD PCIe NVMe)\n" +
                        "Ổ đĩa quang (ODD)\n" +
                        "Hiển thị\n" +
                        "Màn hình 14.0 inch FHD(1920 x 1080) 60Hz IPS Acer ComfyView™ IPS LED LCD\n" +
                        "Độ phân giải 1920*1080\n" +
                        "Đồ Họa (VGA)\n" +
                        "Card màn hình AMD Radeon™ Graphics\n" +
                        "Kết nối (Network)\n" +
                        "Wireless Intel® Wireless Wi-Fi 6 AX200\n" +
                        "LAN\n" +
                        "Bluetooth Bluetooth® 5.1\n" +
                        "Bàn phím , Chuột\n" +
                        "Kiểu bàn phím Bàn phím tiêu chuẩn - Đèn nền bàn phím\n" +
                        "Chuột Cảm ứng đa điểm\n" +
                        "Giao tiếp mở rộng\n" +
                        "Kết nối USB 1 x USB Type-C port with DC-in 1 x USB 3.2 Gen 1 port with power-off charging 1 x USB 2.0 port\n" +
                        "Kết nối HDMI/VGA 1 x HDMI® port\n" +
                        "Tai nghe 1 x Headset / Speaker jack\n" +
                        "Camera 720p HD audio\n" +
                        "Card mở rộng Không\n" +
                        "LOA 2 Loa\n" +
                        "Kiểu Pin 48Wh\n" +
                        "Sạc pin Đi kèm\n" +
                        "Hệ điều hành (bản quyền) đi kèm Windows 10 Home + Office\n" +
                        "Kích thước (Dài x Rộng x Cao) 323.4 (W) x 218.9 (D) x 15.95 (H) mm\n" +
                        "Trọng Lượng 1.19kg\n" +
                        "Màu sắc Bạc",
                categoryLaptopAcer,
                brandAcer,
                "https://philong.com.vn/media/product/24222-1.jpg",
                phiLongRetailer,
                "https://philong.com.vn/laptop-acer-swift-3-sf314-42-r0tr-ryzen-5-4500u-ram-16gb-ssd-1tb-man-hinh-14quot-full-hd-win-10.html",
                21990000L
        );

        saveProduct(
                "LAPTOP ACER NITRO 5 AN515-55-5923 (Core i5-10300H, Ram 8G, SSD 512GB, Màn hình 15.6 FHD 144Hz, VGA GTX1650Ti, WIN 0 Home 64)\n",
                "CPU: Intel Core i5-10300H\n" +
                        "RAM: 8GB DDR4 2933MHz\n" +
                        "Ổ cứng: 512GB PCIe NVMe SSD\n" +
                        "VGA: NVIDIA GeForce GTX 1650Ti 4GB GDDR6\n" +
                        "Màn hình: 15.6” FHD(1920 x 1080) IPS 144Hz\n" +
                        "Hệ điều hành: Windows 10 Home",
                "Bộ vi xử lý\n" +
                        "Bộ vi xử lý Intel Core i5-10300H\n" +
                        "Tốc độ 2.50 GHz upto 4.50 GHz, 4 cores 8 threads\n" +
                        "Bộ nhớ đệm 8MB Cache\n" +
                        "Bộ nhớ trong (RAM)\n" +
                        "Dung lượng 8GB DDR4 2933MHz\n" +
                        "Số khe cắm 2 khe, tối đa 32GB\n" +
                        "Ổ cứng\n" +
                        "Dung lượng 512GB PCIe NVMe SSD\n" +
                        "Khe cắm SSD mở rộng nâng cấp tối đa thêm HDD: 2TB, SSD: 2 khe M.2, mỗi khe 1TB SSD NVMe PCIe - RAI0\n" +
                        "Ổ đĩa quang (ODD) Không có\n" +
                        "Hiển thị\n" +
                        "Màn hình 15.6 inch FHD(1920 x 1080) IPS 144Hz slim bezel LCD\n" +
                        "Độ phân giải 1920*1080\n" +
                        "Đồ Họa (VGA)\n" +
                        "Card màn hình NVIDIA® GeForce® GTX 1650Ti 4GB GDDR6\n" +
                        "Kết nối (Network)\n" +
                        "Wireless Intel® Wireless Wi-Fi 6 AX201\n" +
                        "LAN 10/100/1000 Mbps\n" +
                        "Bluetooth Bluetooth® 5.0\n" +
                        "Bàn phím , Chuột\n" +
                        "Kiểu bàn phím Bàn phím tiêu chuẩn, Có bàn phím số - Đèn nền bàn phím\n" +
                        "Chuột Cảm ứng đa điểm\n" +
                        "Giao tiếp mở rộng\n" +
                        "Kết nối USB 1 x USB 3.2 Gen 2 2 x USB 3.2 Gen 1 1 x USB Type C\n" +
                        "Kết nối HDMI/VGA 1xHDMI\n" +
                        "Tai nghe 1x jack 3.5mm\n" +
                        "Camera Có\n" +
                        "LOA 2 Loa\n" +
                        "Kiểu Pin 4-cell, 57.5 Wh\n" +
                        "Sạc pin Đi kèm\n" +
                        "Hệ điều hành (bản quyền) đi kèm Windows 10 Home\n" +
                        "Kích thước (Dài x Rộng x Cao) 363.4 (W) x 255 (D) x 23.9 (H) mm\n" +
                        "Trọng Lượng 2.3 kg\n" +
                        "Màu sắc Đen\n" +
                        "Bộ vi xử lý\n" +
                        "Bộ vi xử lý Intel Core i5-10300H\n" +
                        "Tốc độ 2.50 GHz upto 4.50 GHz, 4 cores 8 threads\n" +
                        "Bộ nhớ đệm 8MB Cache\n" +
                        "Bộ nhớ trong (RAM)\n" +
                        "Dung lượng 8GB DDR4 2933MHz\n" +
                        "Số khe cắm 2 khe, tối đa 32GB\n" +
                        "Ổ cứng\n" +
                        "Dung lượng 512GB PCIe NVMe SSD\n" +
                        "Khe cắm SSD mở rộng nâng cấp tối đa thêm HDD: 2TB, SSD: 2 khe M.2, mỗi khe 1TB SSD NVMe PCIe - RAI0\n" +
                        "Ổ đĩa quang (ODD) Không có\n" +
                        "Hiển thị\n" +
                        "Màn hình 15.6 inch FHD(1920 x 1080) IPS 144Hz slim bezel LCD\n" +
                        "Độ phân giải 1920*1080\n" +
                        "Đồ Họa (VGA)\n" +
                        "Card màn hình NVIDIA® GeForce® GTX 1650Ti 4GB GDDR6\n" +
                        "Kết nối (Network)\n" +
                        "Wireless Intel® Wireless Wi-Fi 6 AX201\n" +
                        "LAN 10/100/1000 Mbps\n" +
                        "Bluetooth Bluetooth® 5.0\n" +
                        "Bàn phím , Chuột\n" +
                        "Kiểu bàn phím Bàn phím tiêu chuẩn, Có bàn phím số - Đèn nền bàn phím\n" +
                        "Chuột Cảm ứng đa điểm\n" +
                        "Giao tiếp mở rộng\n" +
                        "Kết nối USB 1 x USB 3.2 Gen 2 2 x USB 3.2 Gen 1 1 x USB Type C\n" +
                        "Kết nối HDMI/VGA 1xHDMI\n" +
                        "Tai nghe 1x jack 3.5mm\n" +
                        "Camera Có\n" +
                        "LOA 2 Loa\n" +
                        "Kiểu Pin 4-cell, 57.5 Wh\n" +
                        "Sạc pin Đi kèm\n" +
                        "Hệ điều hành (bản quyền) đi kèm Windows 10 Home\n" +
                        "Kích thước (Dài x Rộng x Cao) 363.4 (W) x 255 (D) x 23.9 (H) mm\n" +
                        "Trọng Lượng 2.3 kg\n" +
                        "Màu sắc Đen",
                categoryLaptopAcer,
                brandAcer,
                "https://philong.com.vn/media/product/23178-an515-55-1.jpg",
                phiLongRetailer,
                "https://philong.com.vn/laptop-acer-nitro-an515-55-5923-core-i5-10300hram-8gssd-512gman-hinh-15.6-fhdvga-4gb1650tiwin-10.html",
                20090000L
        );

        saveProduct(
                "Laptop Acer Aspire 3 A315 56 502X (i5-1035G1, Ram 4GB, SSD 256GB, Màn hình 15.6\" FHD, Win 10",
                "CPU:&nbsp;Intel Core i5-1035G1>\n" +
                        "RAM:&nbsp;&nbsp;4GB DDR4 2400Mhz onboard>\n" +
                        "Lưu trữ:&nbsp;256GB PCIe NVMe SSD>\n" +
                        "Đồ hoạ: Intel UHD Graphics>\n" +
                        "Màn hình:&nbsp;15.6\" FHD (1920 x 1080) IPS\n" +
                        "Hệ điều hành: Windows 10 Home SL 64-bit",
                "Hãng sản xuất Laptop Acer\n" +
                        "Tên sản phẩm Aspire 3 A315-56-502X NX.HS5SV.00F\n" +
                        "Bộ vi xử lý\n" +
                        "Bộ vi xử lý Intel® Core™ i5-1035G1\n" +
                        "Tốc độ 1.00GHz upto 3.60GHz, 4 cores 8 threads\n" +
                        "Bộ nhớ đệm 6MB Cache\n" +
                        "Bộ nhớ trong (RAM)\n" +
                        "Dung lượng 4GB DDR4 2400Mhz onboard\n" +
                        "Số khe cắm 1 khe, tối đa 12GB\n" +
                        "Ổ cứng\n" +
                        "Dung lượng 256GB PCIe NVMe SSD\n" +
                        "Tốc độ vòng quay\n" +
                        "Khe cắm SSD mở rộng Có (nâng cấp tối đa 1TB PCIe NVMe Gen3 8 Gb/s)\n" +
                        "Ổ đĩa quang (ODD) Không\n" +
                        "Hiển thị\n" +
                        "Màn hình 15.6 inch FHD(1920 x 1080), IPS, 60Hz, Acer ComfyView LED LCD\n" +
                        "Độ phân giải 1920*1080\n" +
                        "Đồ Họa (VGA)\n" +
                        "Card màn hình Intel® UHD Graphics\n" +
                        "Kết nối (Network)\n" +
                        "Wireless Intel® Wireless-AC 9461/9462\n" +
                        "LAN Gigabit Ethernet, Wake-on-LAN ready\n" +
                        "Bluetooth Bluetooth® 4.2 1\n" +
                        "Bàn phím , Chuột\n" +
                        "Kiểu bàn phím Bàn phím tiêu chuẩn - Có bàn phím số\n" +
                        "Chuột Cảm ứng đa điểm\n" +
                        "Giao tiếp mở rộng\n" +
                        "Kết nối USB 1 x USB 3.1 Gen1 port 2 x USB 2.0 ports\n" +
                        "Kết nối HDMI/VGA 1 x HDMI®port with HDCP support\n" +
                        "Tai nghe 1 x 3.5 mm headphone/speaker jack\n" +
                        "Camera Acer webcam with: 640 x 480 resolution\n" +
                        "Card mở rộng\n" +
                        "LOA 2 Loa\n" +
                        "Kiểu Pin 2-cell, 36.7Wh\n" +
                        "Sạc pin Đi kèm\n" +
                        "Hệ điều hành (bản quyền) đi kèm Windows 10 Home\n" +
                        "Kích thước (Dài x Rộng x Cao) 363.4 (W) x 247.5 (D) x 19.9 (H) mm\n" +
                        "Trọng Lượng 1.7kg\n" +
                        "Màu sắc Đen\n" +
                        "Hãng sản xuất Laptop Acer\n" +
                        "Tên sản phẩm Aspire 3 A315-56-502X NX.HS5SV.00F\n" +
                        "Bộ vi xử lý\n" +
                        "Bộ vi xử lý Intel® Core™ i5-1035G1\n" +
                        "Tốc độ 1.00GHz upto 3.60GHz, 4 cores 8 threads\n" +
                        "Bộ nhớ đệm 6MB Cache\n" +
                        "Bộ nhớ trong (RAM)\n" +
                        "Dung lượng 4GB DDR4 2400Mhz onboard\n" +
                        "Số khe cắm 1 khe, tối đa 12GB\n" +
                        "Ổ cứng\n" +
                        "Dung lượng 256GB PCIe NVMe SSD\n" +
                        "Tốc độ vòng quay\n" +
                        "Khe cắm SSD mở rộng Có (nâng cấp tối đa 1TB PCIe NVMe Gen3 8 Gb/s)\n" +
                        "Ổ đĩa quang (ODD) Không\n" +
                        "Hiển thị\n" +
                        "Màn hình 15.6 inch FHD(1920 x 1080), IPS, 60Hz, Acer ComfyView LED LCD\n" +
                        "Độ phân giải 1920*1080\n" +
                        "Đồ Họa (VGA)\n" +
                        "Card màn hình Intel® UHD Graphics\n" +
                        "Kết nối (Network)\n" +
                        "Wireless Intel® Wireless-AC 9461/9462\n" +
                        "LAN Gigabit Ethernet, Wake-on-LAN ready\n" +
                        "Bluetooth Bluetooth® 4.2 1\n" +
                        "Bàn phím , Chuột\n" +
                        "Kiểu bàn phím Bàn phím tiêu chuẩn - Có bàn phím số\n" +
                        "Chuột Cảm ứng đa điểm\n" +
                        "Giao tiếp mở rộng\n" +
                        "Kết nối USB 1 x USB 3.1 Gen1 port 2 x USB 2.0 ports\n" +
                        "Kết nối HDMI/VGA 1 x HDMI®port with HDCP support\n" +
                        "Tai nghe 1 x 3.5 mm headphone/speaker jack\n" +
                        "Camera Acer webcam with: 640 x 480 resolution\n" +
                        "Card mở rộng\n" +
                        "LOA 2 Loa\n" +
                        "Kiểu Pin 2-cell, 36.7Wh\n" +
                        "Sạc pin Đi kèm\n" +
                        "Hệ điều hành (bản quyền) đi kèm Windows 10 Home\n" +
                        "Kích thước (Dài x Rộng x Cao) 363.4 (W) x 247.5 (D) x 19.9 (H) mm\n" +
                        "Trọng Lượng 1.7kg\n" +
                        "Màu sắc Đen",
                categoryLaptopAcer,
                brandAcer,
                "https://philong.com.vn/media/product/24207-1.jpg",
                phiLongRetailer,
"https://philong.com.vn/laptop-acer-aspire-3-a315-56-502x-i5-1035g1-ram-4gb-ssd-256gb-man-hinh-15.6quot-fhd-win-10.html",
                12990000L
        );

        saveProduct(
                "Laptop Acer Aspire 7 A715 42G R4ST (Ryzen 5-5500U, Ram 8GB, SSD 256GB, VGA GTX1650 4GB, Màn hình 15.6\" FHD, Win 10)\n" +
                        "elementsShort = CPU: AMD Ryzen 5-5500U",
                "8GB DDR4 3200Mhz &nbsp;\n" +
                        "Ổ cứng: 256GB PCIe NVMe SSD VGA\n" +
                        "Card đồ họa: NVIDIA GTX 1650 4GB\n" +
                        "Màn hình: 15.6 inch FHD (1920 x 1080), 60Hz\n" +
                        "Hệ điều hành: Windows 10 Home SL 64-bit",
                "CPU AMD Ryzen 5 – 5500U\n" +
                        "RAM 8GB DDR4 (2x SO-DIMM socket, up to 32GB SDRAM)\n" +
                        "Ổ cứng 256GB PCIe® NVMe™ M.2 SSD\n" +
                        "Card đồ họa NVIDIA GeForce GTX 1650 4GB GDDR6\n" +
                        "Màn hình 15.6\" FHD (1920 x 1080) IPS, Anti-Glare, 60Hz\n" +
                        "Cổng giao tiếp 1x USB 3.0 1x USB Type C 2x USB 2.0 1x HDMI 1x RJ45\n" +
                        "Ổ quang None\n" +
                        "Audio True Harmony; Dolby® Audio Premium\n" +
                        "Đọc thẻ nhớ None\n" +
                        "Chuẩn LAN 10/100/1000/Gigabits Base T\n" +
                        "Chuẩn WIFI Wi-Fi 6(Gig+)(802.11ax) (2x2)\n" +
                        "Bluetooth v5.0\n" +
                        "Webcam HD Webcam\n" +
                        "Hệ điều hành Windows 10 Home\n" +
                        "Pin 4 Cell 48Whr\n" +
                        "Trọng lượng 2.1 kg\n" +
                        "Màu sắc Đen, Có đèn bàn phím\n" +
                        "Kích thước 363.4 x 254.5 x 23.25 (mm)\n" +
                        "CPU AMD Ryzen 5 – 5500U\n" +
                        "RAM 8GB DDR4 (2x SO-DIMM socket, up to 32GB SDRAM)\n" +
                        "Ổ cứng 256GB PCIe® NVMe™ M.2 SSD\n" +
                        "Card đồ họa NVIDIA GeForce GTX 1650 4GB GDDR6\n" +
                        "Màn hình 15.6\" FHD (1920 x 1080) IPS, Anti-Glare, 60Hz\n" +
                        "Cổng giao tiếp 1x USB 3.0 1x USB Type C 2x USB 2.0 1x HDMI 1x RJ45\n" +
                        "Ổ quang None\n" +
                        "Audio True Harmony; Dolby® Audio Premium\n" +
                        "Đọc thẻ nhớ None\n" +
                        "Chuẩn LAN 10/100/1000/Gigabits Base T\n" +
                        "Chuẩn WIFI Wi-Fi 6(Gig+)(802.11ax) (2x2)\n" +
                        "Bluetooth v5.0\n" +
                        "Webcam HD Webcam\n" +
                        "Hệ điều hành Windows 10 Home\n" +
                        "Pin 4 Cell 48Whr\n" +
                        "Trọng lượng 2.1 kg\n" +
                        "Màu sắc Đen, Có đèn bàn phím\n" +
                        "Kích thước 363.4 x 254.5 x 23.25 (mm)",
                categoryLaptopAcer,
                brandAcer,
                "https://philong.com.vn/media/product/24205-1.png",
                phiLongRetailer,
"https://philong.com.vn/laptop-acer-aspire-7-a715-42g-r4st-ryzen-5-5500u-ram-8gb-ssd-256gb-vga-gtx1650-4gb-man-hinh-15.6quot-fhd-win-10.html",
                18490000L
        );

        saveProduct(
                "LAPTOP ACER Aspire A514 54 540F (Core i5-1135G7/Ram 8GB/SSD 512GB/Màn hình 14\" FHD/WIN 10/SILVER)\n",
                "CPU: Intel® Core™ i5-1135G7\n" +
                        "RAM: 8GB DDR4 2666Mhz\n" +
                        "Ổ cứng: 512GB PCIe NVMe SSD\n" +
                        "Card đồ hoạ: Intel Iris Xe Graphics\n" +
                        "Màn hình: 14\" FHD(1920*1080)&nbsp;\n" +
                        "Hệ điều hành: Windows 10 Home",
                "Hãng sản xuất Laptop Acer\n" +
                        "Tên sản phẩm Acer Aspire 5 A514-54-540F\n" +
                        "Bộ vi xử lý\n" +
                        "Bộ vi xử lý Intel® Core™ i5-1135G7\n" +
                        "Tốc độ tối đa 4.20GHz, 4 cores 8 threads\n" +
                        "Bộ nhớ đệm 8MB Cache\n" +
                        "Bộ nhớ trong (RAM)\n" +
                        "Dung lượng 8GB DDR4 2666Mhz (4GB onboard + 4GB So-dim)\n" +
                        "Số khe cắm 2\n" +
                        "Ổ cứng\n" +
                        "Dung lượng 512GB PCIe NVMe SSD\n" +
                        "Ổ đĩa quang (ODD) Không\n" +
                        "Hiển thị\n" +
                        "Màn hình 14.0 inch FHD(1920*1080) Acer ComfyView™ IPS LED LCD, 60Hz\n" +
                        "Độ phân giải 1920*1080\n" +
                        "Đồ Họa (VGA)\n" +
                        "Card màn hình Intel Iris Xe Graphics\n" +
                        "Kết nối (Network)\n" +
                        "Wireless Intel® Wireless Wi-Fi 6 AX201\n" +
                        "LAN 1 x RJ-45\n" +
                        "Bluetooth Bluetooth® 5.0\n" +
                        "Bàn phím , Chuột\n" +
                        "Kiểu bàn phím Bàn phím tiêu chuẩn\n" +
                        "Chuột Cảm ứng đa điểm\n" +
                        "Giao tiếp mở rộng\n" +
                        "Kết nối USB 1 x USB Type C, 2 x USB 3.1, 1 x USB 2.0,\n" +
                        "Kết nối HDMI/VGA 1 x HDMI® port\n" +
                        "Tai nghe 1 x 3.5 mm headphone/speaker jack\n" +
                        "Camera HD\n" +
                        "Card mở rộng Không\n" +
                        "LOA 2 Loa\n" +
                        "Kiểu Pin 3 cell, 48Wh\n" +
                        "Sạc pin Đi kèm\n" +
                        "Hệ điều hành (bản quyền) đi kèm Windows 10 Home\n" +
                        "Kích thước (Dài x Rộng x Cao) 328.8 (W) x 236 (D) x 17.95 (H) mm\n" +
                        "Trọng Lượng 1.46kg\n" +
                        "Màu sắc Bạc\n" +
                        "Xuất xứ Trung Quốc\n" +
                        "Hãng sản xuất Laptop Acer\n" +
                        "Tên sản phẩm Acer Aspire 5 A514-54-540F\n" +
                        "Bộ vi xử lý\n" +
                        "Bộ vi xử lý Intel® Core™ i5-1135G7\n" +
                        "Tốc độ tối đa 4.20GHz, 4 cores 8 threads\n" +
                        "Bộ nhớ đệm 8MB Cache\n" +
                        "Bộ nhớ trong (RAM)\n" +
                        "Dung lượng 8GB DDR4 2666Mhz (4GB onboard + 4GB So-dim)\n" +
                        "Số khe cắm 2\n" +
                        "Ổ cứng\n" +
                        "Dung lượng 512GB PCIe NVMe SSD\n" +
                        "Ổ đĩa quang (ODD) Không\n" +
                        "Hiển thị\n" +
                        "Màn hình 14.0 inch FHD(1920*1080) Acer ComfyView™ IPS LED LCD, 60Hz\n" +
                        "Độ phân giải 1920*1080\n" +
                        "Đồ Họa (VGA)\n" +
                        "Card màn hình Intel Iris Xe Graphics\n" +
                        "Kết nối (Network)\n" +
                        "Wireless Intel® Wireless Wi-Fi 6 AX201\n" +
                        "LAN 1 x RJ-45\n" +
                        "Bluetooth Bluetooth® 5.0\n" +
                        "Bàn phím , Chuột\n" +
                        "Kiểu bàn phím Bàn phím tiêu chuẩn\n" +
                        "Chuột Cảm ứng đa điểm\n" +
                        "Giao tiếp mở rộng\n" +
                        "Kết nối USB 1 x USB Type C, 2 x USB 3.1, 1 x USB 2.0,\n" +
                        "Kết nối HDMI/VGA 1 x HDMI® port\n" +
                        "Tai nghe 1 x 3.5 mm headphone/speaker jack\n" +
                        "Camera HD\n" +
                        "Card mở rộng Không\n" +
                        "LOA 2 Loa\n" +
                        "Kiểu Pin 3 cell, 48Wh\n" +
                        "Sạc pin Đi kèm\n" +
                        "Hệ điều hành (bản quyền) đi kèm Windows 10 Home\n" +
                        "Kích thước (Dài x Rộng x Cao) 328.8 (W) x 236 (D) x 17.95 (H) mm\n" +
                        "Trọng Lượng 1.46kg\n" +
                        "Màu sắc Bạc\n" +
                        "Xuất xứ Trung Quốc",
                categoryLaptopAcer,
                brandAcer,
                "https://philong.com.vn/media/product/24202-191084.jpg",
phiLongRetailer,
"https://philong.com.vn/laptop-acer-aspire-a514-54-540f-core-i5-1135g7ram-8gbssd-512gbman-hinh-14quot-fhdwin-10silver.html",
                17690000L
        );

        saveProduct(
                "LAPTOP ACER ASPIRE A515-56G-51YL (Core i5-1135G7, Ram 8GB, SSD 512GB, màn hình 15.6inch Full HD, VGA MX350 2GB, Win 10)",
                "CPU: Intel Core i5-1135G7\n" +
                        "RAM:&nbsp;8GB (4GBx2) DDR4 2666MHz>\n" +
                        "Lưu trữ: 512GB PCIe\n" +
                        "Đồ hoạ:&nbsp;NVIDIA GeForce MX350 2GB GDDR5>\n" +
                        "Màn hình:&nbsp;15.6\" FHD (1920 x 1080) IPS>\n" +
                        "Hệ điều hành: Windows 10 Home SL 64-bit",
                "CPU Intel Core i5-1135G7 2.4GHz up to 4.2GHz 8MB\n" +
                        "RAM 8GB (4GBx2) DDR4 2666MHz (1x SO-DIMM socket, up to 20GB SDRAM)\n" +
                        "Ổ cứng 512GB SSD M.2 PCIE, 1x slot SATA3 2.5\"\n" +
                        "Card đồ họa NVIDIA GeForce MX350 2GB GDDR5 + Intel Iris Xe Graphics\n" +
                        "Màn hình 15.6\" FHD (1920 x 1080) IPS, Anti-Glare, Acer ComfyView LCD\n" +
                        "Cổng giao tiếp 1x USB 3.2 port with power-off charging 2x USB 3.2 port 1x USB Type-C port 1x RJ-45 port 1x HDMI 2.0\n" +
                        "Ổ quang None\n" +
                        "Bàn phím Backlit keyboard\n" +
                        "Audio Acer TrueHarmony\n" +
                        "Đọc thẻ nhớ None\n" +
                        "Chuẩn LAN 10/100/1000 Mbps\n" +
                        "Chuẩn WIFI Wi-Fi 6(Gig+)(802.11ax)\n" +
                        "Bluetooth v5.1\n" +
                        "Webcam HD Webcam\n" +
                        "Hệ điều hành Windows 10 Home\n" +
                        "Pin 3 Cell 48WHrs\n" +
                        "Trọng lượng 1.7 kg\n" +
                        "Màu sắc Pure Silver\n" +
                        "Đèn bàn phím Có\n" +
                        "Kích thước 363.4 x 238.5 x 17.9 (mm)\n" +
                        "CPU Intel Core i5-1135G7 2.4GHz up to 4.2GHz 8MB\n" +
                        "RAM 8GB (4GBx2) DDR4 2666MHz (1x SO-DIMM socket, up to 20GB SDRAM)\n" +
                        "Ổ cứng 512GB SSD M.2 PCIE, 1x slot SATA3 2.5\"\n" +
                        "Card đồ họa NVIDIA GeForce MX350 2GB GDDR5 + Intel Iris Xe Graphics\n" +
                        "Màn hình 15.6\" FHD (1920 x 1080) IPS, Anti-Glare, Acer ComfyView LCD\n" +
                        "Cổng giao tiếp 1x USB 3.2 port with power-off charging 2x USB 3.2 port 1x USB Type-C port 1x RJ-45 port 1x HDMI 2.0\n" +
                        "Ổ quang None\n" +
                        "Bàn phím Backlit keyboard\n" +
                        "Audio Acer TrueHarmony\n" +
                        "Đọc thẻ nhớ None\n" +
                        "Chuẩn LAN 10/100/1000 Mbps\n" +
                        "Chuẩn WIFI Wi-Fi 6(Gig+)(802.11ax)\n" +
                        "Bluetooth v5.1\n" +
                        "Webcam HD Webcam\n" +
                        "Hệ điều hành Windows 10 Home\n" +
                        "Pin 3 Cell 48WHrs\n" +
                        "Trọng lượng 1.7 kg\n" +
                        "Màu sắc Pure Silver\n" +
                        "Đèn bàn phím Có\n" +
                        "Kích thước 363.4 x 238.5 x 17.9 (mm)",
                categoryLaptopAcer,
                brandAcer,
                "https://philong.com.vn/media/product/23826-13.png",
                phiLongRetailer,
"https://philong.com.vn/laptop-acer-aspire-a515-56g-51yl-core-i5-1135g7-ram-8gb-ssd-512gb-man-hinh-15.6inch-full-hd-vga-mx350-2gb-win-10.html",
                18690000L
        );

        saveProduct(
                "LAPTOP ACER PREDATOR PH315 53 770L (Core i7-10750H, Ram 8GB, SSD 512GB, Màn hình 15.6\" Full HD,VGA 6GB 1660Ti, Win 10)",
                "CPU: Intel Core i7-10750H &nbsp;\n" +
                        "RAM: 8GB DDR4 2933MHz\n" +
                        "Ổ cứng: 512GB PCIe NVMe\n" +
                        "Card đồ hoạ: NVIDIA GeForce GTX 1660Ti 6GB\n" +
                        "Màn hình: 15.6” FHD(1920 x 1080) IPS 144Hz\n" +
                        "Hệ điều hành: Windows 10 Home SL 64-bit",
                "Tên sản phẩm Predator Helios 300 PH315-53-770L\n" +
                        "Bộ vi xử lý\n" +
                        "Bộ vi xử lý Intel® Core™ i7-10750H\n" +
                        "Tốc độ 2.60GHz upto 5.00GHz, 12MB cache, 6 cores 12 threads\n" +
                        "Bộ nhớ đệm 12MB Cache\n" +
                        "Bộ nhớ trong (RAM)\n" +
                        "Dung lượng 8GB DDR4 2933MHz\n" +
                        "Số khe cắm 2 khe ram, nâng cấp tối 32GB\n" +
                        "Ổ cứng\n" +
                        "Dung lượng 512GB PCIe NVMe SSD\n" +
                        "Khe cắm SSD mở rộng nâng cấp tối đa thêm 2TB HDD 7200rpm, 2 khe M.2 chạy được RAID0 mỗi khe nâng cấp được 1TBSSD PCIe NVMe\n" +
                        "Ổ đĩa quang (ODD) Không có\n" +
                        "Hiển thị\n" +
                        "Màn hình 15.6 inch FHD(1920 x 1080) IPS 144Hz 3ms slim bezel LCD (300nit)\n" +
                        "Độ phân giải 1920 x 1080\n" +
                        "Đồ Họa (VGA)\n" +
                        "Card màn hình NVIDIA® GeForce® GTX 1660Ti 6GB GDDR6\n" +
                        "Kết nối (Network)\n" +
                        "Wireless Killer™ Wi-Fi 6 AX1650i\n" +
                        "LAN 1 x Ethernet (RJ-45) port\n" +
                        "Bluetooth Bluetooth® 5.0\n" +
                        "Bàn phím , Chuột\n" +
                        "Kiểu bàn phím Bàn phím tiêu chuẩn, Có bàn phím số - Đèn bàn phím RGB 4 zone\n" +
                        "Chuột Cảm ứng đa điểm\n" +
                        "Giao tiếp mở rộng\n" +
                        "Kết nối USB 1 x USB Type C (đọc ghi 50GBps) 1 x USB 3.2 Gen 2 (đọc ghi 10GBps) 2 x USB 3.2 Gen 1 (đọc ghi 5GBps)\n" +
                        "Kết nối HDMI/VGA 1 x HDMI® 2.0 port with HDCP support 1 x Mini DisplayPortTM 1.4\n" +
                        "Tai nghe 1x jack 3.5mm\n" +
                        "Camera Có\n" +
                        "LOA 2 Loa\n" +
                        "Kiểu Pin 4-cell, 59 Wh\n" +
                        "Sạc pin Đi kèm\n" +
                        "Hệ điều hành (bản quyền) đi kèm Windows 10 Home\n" +
                        "Kích thước (Dài x Rộng x Cao) 363.4 (W) x 255 (D) x 22.9 (H) mm\n" +
                        "Trọng Lượng 2.2 kg\n" +
                        "Màu sắc Abyssal Black\n" +
                        "Xuất Xứ Trung Quốc\n" +
                        "Tên sản phẩm Predator Helios 300 PH315-53-770L\n" +
                        "Bộ vi xử lý\n" +
                        "Bộ vi xử lý Intel® Core™ i7-10750H\n" +
                        "Tốc độ 2.60GHz upto 5.00GHz, 12MB cache, 6 cores 12 threads\n" +
                        "Bộ nhớ đệm 12MB Cache\n" +
                        "Bộ nhớ trong (RAM)\n" +
                        "Dung lượng 8GB DDR4 2933MHz\n" +
                        "Số khe cắm 2 khe ram, nâng cấp tối 32GB\n" +
                        "Ổ cứng\n" +
                        "Dung lượng 512GB PCIe NVMe SSD\n" +
                        "Khe cắm SSD mở rộng nâng cấp tối đa thêm 2TB HDD 7200rpm, 2 khe M.2 chạy được RAID0 mỗi khe nâng cấp được 1TBSSD PCIe NVMe\n" +
                        "Ổ đĩa quang (ODD) Không có\n" +
                        "Hiển thị\n" +
                        "Màn hình 15.6 inch FHD(1920 x 1080) IPS 144Hz 3ms slim bezel LCD (300nit)\n" +
                        "Độ phân giải 1920 x 1080\n" +
                        "Đồ Họa (VGA)\n" +
                        "Card màn hình NVIDIA® GeForce® GTX 1660Ti 6GB GDDR6\n" +
                        "Kết nối (Network)\n" +
                        "Wireless Killer™ Wi-Fi 6 AX1650i\n" +
                        "LAN 1 x Ethernet (RJ-45) port\n" +
                        "Bluetooth Bluetooth® 5.0\n" +
                        "Bàn phím , Chuột\n" +
                        "Kiểu bàn phím Bàn phím tiêu chuẩn, Có bàn phím số - Đèn bàn phím RGB 4 zone\n" +
                        "Chuột Cảm ứng đa điểm\n" +
                        "Giao tiếp mở rộng\n" +
                        "Kết nối USB 1 x USB Type C (đọc ghi 50GBps) 1 x USB 3.2 Gen 2 (đọc ghi 10GBps) 2 x USB 3.2 Gen 1 (đọc ghi 5GBps)\n" +
                        "Kết nối HDMI/VGA 1 x HDMI® 2.0 port with HDCP support 1 x Mini DisplayPortTM 1.4\n" +
                        "Tai nghe 1x jack 3.5mm\n" +
                        "Camera Có\n" +
                        "LOA 2 Loa\n" +
                        "Kiểu Pin 4-cell, 59 Wh\n" +
                        "Sạc pin Đi kèm\n" +
                        "Hệ điều hành (bản quyền) đi kèm Windows 10 Home\n" +
                        "Kích thước (Dài x Rộng x Cao) 363.4 (W) x 255 (D) x 22.9 (H) mm\n" +
                        "Trọng Lượng 2.2 kg\n" +
                        "Màu sắc Abyssal Black\n" +
                        "Xuất Xứ Trung Quốc",
                categoryLaptopAcer,
                brandAcer,
                "https://philong.com.vn/media/product/23787-4b969e2e31009785163807b410b54024.jpg",
                phiLongRetailer,
                "https://philong.com.vn/laptop-acer-predator-ph315-53-770l-core-i7-10750h-ram-8gb-ssd-512gb-man-hinh-15.6quot-full-hdvga-6gb-1660ti-win-10.html",
                35790000L
        );

        saveProduct(
                "LAPTOP ACER ASPIRE A514 54 39KU (core i3-1115G4/Ram 4GB/SSD 256GB/Màn hình 14\" FHD/Win 10/SILVER)",
                "CPU: Intel Core™ i3-1115G4\n" +
                        "RAM: 4GB DDR4 2666Mhz\n" +
                        "Ổ cứng: 256GB PCIe NVMe SSD\n" +
                        "Card đồ họa: Intel® UHD Graphics\n" +
                        "Màn hình: 14” FHD(1920 x 1080)\n" +
                        "Hệ điều hành: Win 10 bản quyền",
                "Bộ vi xử lý\n" +
                        "Bộ vi xử lý Intel® Core™ i3-1115G4\n" +
                        "Tốc độ 4.10GHz, 6 MB cache, 2 cores 4 threads\n" +
                        "Bộ nhớ đệm 6MB Cache\n" +
                        "Bộ nhớ trong (RAM)\n" +
                        "Dung lượng 4GB DDR4 2666Mhz\n" +
                        "Ổ cứng\n" +
                        "Dung lượng 256GB PCIe NVMe SSD\n" +
                        "Khe cắm SSD mở rộng Có (đã cắm sẵn SSD), nâng cấp tối đa 2 TB HDD và 1 TB SSD PCIe Gen3 8 Gb/s up to 4 lanes, NVMe\n" +
                        "Ổ đĩa quang (ODD) Không có\n" +
                        "Hiển thị\n" +
                        "Màn hình 14.0 inch FHD(1920 x 1080) Acer ComfyView™ IPS LED LCD, 60Hz\n" +
                        "Độ phân giải 1920 x 1080\n" +
                        "Đồ Họa (VGA)\n" +
                        "Card màn hình Intel® UHD Graphics\n" +
                        "Kết nối (Network)\n" +
                        "Wireless Intel® Wireless Wi-Fi 6 AX201\n" +
                        "LAN Gigabit Ethernet\n" +
                        "Bluetooth Bluetooth® 5.1\n" +
                        "Bàn phím , Chuột\n" +
                        "Kiểu bàn phím Bàn phím tiêu chuẩn\n" +
                        "Chuột Cảm ứng đa điểm\n" +
                        "Giao tiếp mở rộng\n" +
                        "Kết nối USB 1 x USB 3.2 port with power-off charging 2 x USB 3.2 port 1 x USB Type-C port\n" +
                        "Kết nối HDMI/VGA 1 x HDMI® port\n" +
                        "Tai nghe 1 x Headset/speaker jack\n" +
                        "Camera HD webcam with 1280 x 720 resolution, 720p HD audio/video recording\n" +
                        "LOA 2 Loa\n" +
                        "Kiểu Pin 48Wh\n" +
                        "Sạc pin Đi kèm\n" +
                        "Hệ điều hành (bản quyền) đi kèm Windows 10 Home\n" +
                        "Kích thước (Dài x Rộng x Cao) 328 (W) x 223 (D) x 17.95 (H) mm\n" +
                        "Trọng Lượng 1.45 kg\n" +
                        "Màu sắc Bạc\n" +
                        "Xuất Xứ Trung Quốc\n" +
                        "Bộ vi xử lý\n" +
                        "Bộ vi xử lý Intel® Core™ i3-1115G4\n" +
                        "Tốc độ 4.10GHz, 6 MB cache, 2 cores 4 threads\n" +
                        "Bộ nhớ đệm 6MB Cache\n" +
                        "Bộ nhớ trong (RAM)\n" +
                        "Dung lượng 4GB DDR4 2666Mhz\n" +
                        "Ổ cứng\n" +
                        "Dung lượng 256GB PCIe NVMe SSD\n" +
                        "Khe cắm SSD mở rộng Có (đã cắm sẵn SSD), nâng cấp tối đa 2 TB HDD và 1 TB SSD PCIe Gen3 8 Gb/s up to 4 lanes, NVMe\n" +
                        "Ổ đĩa quang (ODD) Không có\n" +
                        "Hiển thị\n" +
                        "Màn hình 14.0 inch FHD(1920 x 1080) Acer ComfyView™ IPS LED LCD, 60Hz\n" +
                        "Độ phân giải 1920 x 1080\n" +
                        "Đồ Họa (VGA)\n" +
                        "Card màn hình Intel® UHD Graphics\n" +
                        "Kết nối (Network)\n" +
                        "Wireless Intel® Wireless Wi-Fi 6 AX201\n" +
                        "LAN Gigabit Ethernet\n" +
                        "Bluetooth Bluetooth® 5.1\n" +
                        "Bàn phím , Chuột\n" +
                        "Kiểu bàn phím Bàn phím tiêu chuẩn\n" +
                        "Chuột Cảm ứng đa điểm\n" +
                        "Giao tiếp mở rộng\n" +
                        "Kết nối USB 1 x USB 3.2 port with power-off charging 2 x USB 3.2 port 1 x USB Type-C port\n" +
                        "Kết nối HDMI/VGA 1 x HDMI® port\n" +
                        "Tai nghe 1 x Headset/speaker jack\n" +
                        "Camera HD webcam with 1280 x 720 resolution, 720p HD audio/video recording\n" +
                        "LOA 2 Loa\n" +
                        "Kiểu Pin 48Wh\n" +
                        "Sạc pin Đi kèm\n" +
                        "Hệ điều hành (bản quyền) đi kèm Windows 10 Home\n" +
                        "Kích thước (Dài x Rộng x Cao) 328 (W) x 223 (D) x 17.95 (H) mm\n" +
                        "Trọng Lượng 1.45 kg\n" +
                        "Màu sắc Bạc\n" +
                        "Xuất Xứ Trung Quốc",
                categoryLaptopAcer,
                brandAcer,
                "https://philong.com.vn/media/product/23723-1.png",
phiLongRetailer,
                "https://philong.com.vn/laptop-acer-aspire-a514-54-39ku-core-i3-1115g4ram-4gbssd-256gbman-hinh-14quot-fhdwin-10silver.html",
                12990000L
        );

        saveProduct(
                "Laptop Acer Swift 5 SF514-55T-51NZ (i5-1135G7, Ram 8GB, SSD 512GB, Intel Iris Xe Graphics, Màn hình 14'' FHD Touch, Win 10)",
                "CPU: Intel Core i5-1135G7\n" +
                        "RAM:&nbsp;8GB Onboard LPDDR4X 2666MHz Không nâng cấp được )>\n" +
                        "Ổ cứng:&nbsp;512GB SSD M.2 NVMe>\n" +
                        "Card đồ hoạ: Intel Iris Xe Graphics\n" +
                        "Màn hình:&nbsp;14\" ( 1920 x 1080 ) Full HD IPS cảm ứng>\n" +
                        "Hệ điều hành: Windows 10 Home SL 64bit",
                "CPU Intel Core i5-1135G7 2.4GHz up to 4.2GHz 8MB\n" +
                        "RAM 8GB LPDDR4x 4267MHz\n" +
                        "Ổ cứng 512GB PCIe NVMe SSD\n" +
                        "Card đồ họa Intel Iris Xe Graphics\n" +
                        "Màn hình 14\" FHD (1920 x 1080) IPS NarrowBoarder Touch LCD, Acer CineCrystalTM LED-Backlit TFT LCD, Multi-Touch, 400 Nits, 100% sRGB, 75% Adobe RGB\n" +
                        "Cổng giao tiếp 1x USB 3.2 Gen 1 port with power-off charging 1x USB Type-C / Thunderbolt TM 4 port 1x USB 3.2 Gen 1 port 1x Power jack (DC-in) 1x HDMI® port 1x Headset / Speaker jack Finger Print\n" +
                        "Ổ quang None\n" +
                        "Audio DTS® Audio Acer TrueHarmony technology\n" +
                        "Bàn phím Led\n" +
                        "Đọc thẻ nhớ None\n" +
                        "Chuẩn LAN None\n" +
                        "Chuẩn WIFI Intel® Wireless Wi-Fi 6 AX201 (2x2)\n" +
                        "Bluetooth v5.1\n" +
                        "Webcam (1280 x 720)\n" +
                        "Hệ điều hành Windows 10 Home\n" +
                        "Pin 4 Cell 40WHrs\n" +
                        "Trọng lượng 1050 gram\n" +
                        "Màu sắc Safari Gold\n" +
                        "Kích thước 318.9 x 206.98 x 14.95 (mm)\n" +
                        "CPU Intel Core i5-1135G7 2.4GHz up to 4.2GHz 8MB\n" +
                        "RAM 8GB LPDDR4x 4267MHz\n" +
                        "Ổ cứng 512GB PCIe NVMe SSD\n" +
                        "Card đồ họa Intel Iris Xe Graphics\n" +
                        "Màn hình 14\" FHD (1920 x 1080) IPS NarrowBoarder Touch LCD, Acer CineCrystalTM LED-Backlit TFT LCD, Multi-Touch, 400 Nits, 100% sRGB, 75% Adobe RGB\n" +
                        "Cổng giao tiếp 1x USB 3.2 Gen 1 port with power-off charging 1x USB Type-C / Thunderbolt TM 4 port 1x USB 3.2 Gen 1 port 1x Power jack (DC-in) 1x HDMI® port 1x Headset / Speaker jack Finger Print\n" +
                        "Ổ quang None\n" +
                        "Audio DTS® Audio Acer TrueHarmony technology\n" +
                        "Bàn phím Led\n" +
                        "Đọc thẻ nhớ None\n" +
                        "Chuẩn LAN None\n" +
                        "Chuẩn WIFI Intel® Wireless Wi-Fi 6 AX201 (2x2)\n" +
                        "Bluetooth v5.1\n" +
                        "Webcam (1280 x 720)\n" +
                        "Hệ điều hành Windows 10 Home\n" +
                        "Pin 4 Cell 40WHrs\n" +
                        "Trọng lượng 1050 gram\n" +
                        "Màu sắc Safari Gold\n" +
                        "Kích thước 318.9 x 206.98 x 14.95 (mm)",
                categoryLaptopAcer,
                brandAcer,
                "https://philong.com.vn/media/product/23664-6.jpg",
                phiLongRetailer,
"https://philong.com.vn/laptop-acer-swift-5-sf514-55t-51nz-i5-1135g7-ram-8gb-ssd-512gb-intel-iris-xe-graphics-man-hinh-14-fhd-touch-win-10.html",
                23990000L
        );

        saveProduct(
                "LAPTOP ACER ASPIRE A514-54-51VT (Core i5-1135G7/Ram 8GB/SSD 512GB/Màn hình 14\" FHD/Win 10/SIVER)",
                "CPU: Intel Core™ i5-1135G7\n" +
                        "RAM: 8GB 2666Mhz &nbsp;\n" +
                        "Ổ cứng: 512GB PCIe NVMe\n" +
                        "Card đồ hoạ: Intel Iris Xe Graphics\n" +
                        "Màn hình: 14” FHD (1920 x 1080)\n" +
                        "Hệ điều hành: Windows 10 Home",
                "Bộ vi xử lý\n" +
                        "Bộ vi xử lý Intel® Core™ i5-1135G7\n" +
                        "Tốc độ upto 4.20GHz, 4 cores 8 threads\n" +
                        "Bộ nhớ đệm 8MB Cache\n" +
                        "Bộ nhớ trong (RAM)\n" +
                        "Dung lượng 8GB (4GB onboard + 4GB So-dim) DDR4 2666Mhz\n" +
                        "Số khe cắm 1 khe\n" +
                        "Ổ cứng\n" +
                        "Dung lượng 512GB PCIe NVMe SSD\n" +
                        "Khe cắm SSD mở rộng nâng cấp tối đa 2 TB HDD và 1 TB SSD PCIe Gen3 8 Gb/s up to 4 lanes, NVMe\n" +
                        "Ổ đĩa quang (ODD) Không có\n" +
                        "Hiển thị\n" +
                        "Màn hình 14.0 inch FHD (1920 x 1080) 60Hz Acer ComfyView™ IPS LED LCD\n" +
                        "Độ phân giải 1920*1080\n" +
                        "Đồ Họa (VGA)\n" +
                        "Card màn hình Intel® Iris® Xe Graphics\n" +
                        "Kết nối (Network)\n" +
                        "Wireless Intel® Wireless Wi-Fi 6 AX201\n" +
                        "LAN Gigabit Ethernet\n" +
                        "Bluetooth Bluetooth® 5.1\n" +
                        "Bàn phím , Chuột\n" +
                        "Kiểu bàn phím Bàn phím tiêu chuẩn\n" +
                        "Chuột Cảm ứng đa điểm\n" +
                        "Giao tiếp mở rộng\n" +
                        "Kết nối USB 1 x USB 3.2 port with power-off charging 2 x USB 3.2 port 1 x USB Type-C port\n" +
                        "Kết nối HDMI/VGA 1 x HDMI\n" +
                        "Tai nghe 1 x Headset/speaker jack\n" +
                        "Camera HD webcam with 1280 x 720 resolution, 720p HD audio/video recording\n" +
                        "LOA 2 Loa\n" +
                        "Kiểu Pin 48Wh\n" +
                        "Sạc pin Đi kèm\n" +
                        "Hệ điều hành (bản quyền) đi kèm Windows 10 Home\n" +
                        "Kích thước (Dài x Rộng x Cao) 328 (W) x 223 (D) x 17.95 (H) mm\n" +
                        "Trọng Lượng 1.46 kg\n" +
                        "Màu sắc Bạc\n" +
                        "Xuất xứ Trung Quốc\n" +
                        "Bộ vi xử lý\n" +
                        "Bộ vi xử lý Intel® Core™ i5-1135G7\n" +
                        "Tốc độ upto 4.20GHz, 4 cores 8 threads\n" +
                        "Bộ nhớ đệm 8MB Cache\n" +
                        "Bộ nhớ trong (RAM)\n" +
                        "Dung lượng 8GB (4GB onboard + 4GB So-dim) DDR4 2666Mhz\n" +
                        "Số khe cắm 1 khe\n" +
                        "Ổ cứng\n" +
                        "Dung lượng 512GB PCIe NVMe SSD\n" +
                        "Khe cắm SSD mở rộng nâng cấp tối đa 2 TB HDD và 1 TB SSD PCIe Gen3 8 Gb/s up to 4 lanes, NVMe\n" +
                        "Ổ đĩa quang (ODD) Không có\n" +
                        "Hiển thị\n" +
                        "Màn hình 14.0 inch FHD (1920 x 1080) 60Hz Acer ComfyView™ IPS LED LCD\n" +
                        "Độ phân giải 1920*1080\n" +
                        "Đồ Họa (VGA)\n" +
                        "Card màn hình Intel® Iris® Xe Graphics\n" +
                        "Kết nối (Network)\n" +
                        "Wireless Intel® Wireless Wi-Fi 6 AX201\n" +
                        "LAN Gigabit Ethernet\n" +
                        "Bluetooth Bluetooth® 5.1\n" +
                        "Bàn phím , Chuột\n" +
                        "Kiểu bàn phím Bàn phím tiêu chuẩn\n" +
                        "Chuột Cảm ứng đa điểm\n" +
                        "Giao tiếp mở rộng\n" +
                        "Kết nối USB 1 x USB 3.2 port with power-off charging 2 x USB 3.2 port 1 x USB Type-C port\n" +
                        "Kết nối HDMI/VGA 1 x HDMI\n" +
                        "Tai nghe 1 x Headset/speaker jack\n" +
                        "Camera HD webcam with 1280 x 720 resolution, 720p HD audio/video recording\n" +
                        "LOA 2 Loa\n" +
                        "Kiểu Pin 48Wh\n" +
                        "Sạc pin Đi kèm\n" +
                        "Hệ điều hành (bản quyền) đi kèm Windows 10 Home\n" +
                        "Kích thước (Dài x Rộng x Cao) 328 (W) x 223 (D) x 17.95 (H) mm\n" +
                        "Trọng Lượng 1.46 kg\n" +
                        "Màu sắc Bạc\n" +
                        "Xuất xứ Trung Quốc",
                categoryLaptopAcer,
                brandAcer,
                "https://philong.com.vn/media/product/23659-thumb650_acer_aspire_5_a514-54_silver_1.jpg",
phiLongRetailer,
                "https://philong.com.vn/laptop-acer-aspire-a514-54-51vt-core-i5-1135g7ram-8gbssd-512gbman-hinh-14quot-fhdwin-10siver.html",
                15990000L
        );

        saveProduct(
                "LAPTOP ACER ASPIRE A514-54-51RB (Core i5-1135G7/ram 8GB/SSD 256GB/Màn hình 14\" FHD/Win 10/GOLD)",
                "CPU: Intel Core &nbsp;i5-1135G7\n" +
                        "RAM: 8GB 2666Mhz\n" +
                        "Ổ cứng: 256GB PCIe NVMe SSD\n" +
                        "Card đồ hoạ: Intel Iris Xe Graphics\n" +
                        "Màn hình: 14.0 inch FHD (1920 x 1080)\n" +
                        "Hệ điều hành: Windows 10 Home",
                "Bộ vi xử lý\n" +
                        "Bộ vi xử lý Intel® Core™ i5-1135G7\n" +
                        "Tốc độ upto 4.20GHz, 4 cores 8 threads\n" +
                        "Bộ nhớ đệm 8MB Cache\n" +
                        "Bộ nhớ trong (RAM)\n" +
                        "Dung lượng 8GB (4GB onboard + 4GB So-dim)\n" +
                        "Số khe cắm 1 khe Nâng cấp tối đa 20GB (4GB onboard + 16GB sodim)\n" +
                        "Ổ cứng\n" +
                        "Dung lượng 256GB PCIe NVMe SSD\n" +
                        "Tốc độ vòng quay\n" +
                        "Khe cắm SSD mở rộng nâng cấp tối đa 2 TB HDD và 1 TB SSD PCIe Gen3 8 Gb/s up to 4 lanes, NVMe\n" +
                        "Ổ đĩa quang (ODD) Không có\n" +
                        "Hiển thị\n" +
                        "Màn hình 14.0 inch FHD (1920 x 1080) Acer ComfyView™ IPS LED LCD\n" +
                        "Độ phân giải 1920*1080\n" +
                        "Đồ Họa (VGA)\n" +
                        "Card màn hình Intel® Iris® Xe Graphics\n" +
                        "Kết nối (Network)\n" +
                        "Wireless Intel® Wireless Wi-Fi 6 AX201\n" +
                        "LAN Gigabit Ethernet\n" +
                        "Bluetooth Bluetooth® 5.1\n" +
                        "Bàn phím , Chuột\n" +
                        "Kiểu bàn phím Bàn phím tiêu chuẩn\n" +
                        "Chuột Cảm ứng đa điểm\n" +
                        "Giao tiếp mở rộng\n" +
                        "Kết nối USB 1 x USB 3.2 port with power-off charging 2 x USB 3.2 port 1 x USB Type-C port\n" +
                        "Kết nối HDMI/VGA 1 x HDMI\n" +
                        "Tai nghe 1 x Headset/speaker jack\n" +
                        "Camera HD webcam with 1280 x 720 resolution, 720p HD audio/video recording\n" +
                        "LOA 2 Loa\n" +
                        "Kiểu Pin 48Wh\n" +
                        "Sạc pin Đi kèm\n" +
                        "Hệ điều hành (bản quyền) đi kèm Windows 10 Home\n" +
                        "Kích thước (Dài x Rộng x Cao) 328 (W) x 223 (D) x 17.95 (H) mm\n" +
                        "Trọng Lượng 1.46 kg\n" +
                        "Màu sắc Vàng\n" +
                        "Xuất xứ Trung Quốc\n" +
                        "Bộ vi xử lý\n" +
                        "Bộ vi xử lý Intel® Core™ i5-1135G7\n" +
                        "Tốc độ upto 4.20GHz, 4 cores 8 threads\n" +
                        "Bộ nhớ đệm 8MB Cache\n" +
                        "Bộ nhớ trong (RAM)\n" +
                        "Dung lượng 8GB (4GB onboard + 4GB So-dim)\n" +
                        "Số khe cắm 1 khe Nâng cấp tối đa 20GB (4GB onboard + 16GB sodim)\n" +
                        "Ổ cứng\n" +
                        "Dung lượng 256GB PCIe NVMe SSD\n" +
                        "Tốc độ vòng quay\n" +
                        "Khe cắm SSD mở rộng nâng cấp tối đa 2 TB HDD và 1 TB SSD PCIe Gen3 8 Gb/s up to 4 lanes, NVMe\n" +
                        "Ổ đĩa quang (ODD) Không có\n" +
                        "Hiển thị\n" +
                        "Màn hình 14.0 inch FHD (1920 x 1080) Acer ComfyView™ IPS LED LCD\n" +
                        "Độ phân giải 1920*1080\n" +
                        "Đồ Họa (VGA)\n" +
                        "Card màn hình Intel® Iris® Xe Graphics\n" +
                        "Kết nối (Network)\n" +
                        "Wireless Intel® Wireless Wi-Fi 6 AX201\n" +
                        "LAN Gigabit Ethernet\n" +
                        "Bluetooth Bluetooth® 5.1\n" +
                        "Bàn phím , Chuột\n" +
                        "Kiểu bàn phím Bàn phím tiêu chuẩn\n" +
                        "Chuột Cảm ứng đa điểm\n" +
                        "Giao tiếp mở rộng\n" +
                        "Kết nối USB 1 x USB 3.2 port with power-off charging 2 x USB 3.2 port 1 x USB Type-C port\n" +
                        "Kết nối HDMI/VGA 1 x HDMI\n" +
                        "Tai nghe 1 x Headset/speaker jack\n" +
                        "Camera HD webcam with 1280 x 720 resolution, 720p HD audio/video recording\n" +
                        "LOA 2 Loa\n" +
                        "Kiểu Pin 48Wh\n" +
                        "Sạc pin Đi kèm\n" +
                        "Hệ điều hành (bản quyền) đi kèm Windows 10 Home\n" +
                        "Kích thước (Dài x Rộng x Cao) 328 (W) x 223 (D) x 17.95 (H) mm\n" +
                        "Trọng Lượng 1.46 kg\n" +
                        "Màu sắc Vàng\n" +
                        "Xuất xứ Trung Quốc",
                categoryLaptopAcer,
                brandAcer,
                "https://philong.com.vn/media/product/23658-5925_35205_1.jpg",
                phiLongRetailer,
"https://philong.com.vn/laptop-acer-aspire-a514-54-51rb-core-i5-1135g7ram-8gbssd-256gbman-hinh-14quot-fhdwin-10gold.html",
                15490000L
        );

        saveProduct(
                "LAPTOP ACER ASPIRE 7 A715-41G-R150 (R7-3750H/Ram 8GB/SSD 512GB/Màn hình 15.6\" FHD/VGA 1650Ti 4G/Win 10)",
                "CPU: AMD Ryzen 7-3750H\n" +
                        "RAM: 8GB DDR4 2400Mhz &nbsp;\n" +
                        "Ổ cứng: 512GB PCIe NVMe SSD VGA\n" +
                        "Card đồ họa: NVIDIA GTX 1650Ti 4GB\n" +
                        "Màn hình: 15.6 inch FHD(1920 x 1080\n" +
                        "Hệ điều hành: Windows 10 Home",
                "Bộ vi xử lý\n" +
                        "Bộ vi xử lý AMD Ryzen 7-3750H\n" +
                        "Tốc độ 2.10GHz upto 3.70GHz, 4MB cache, 4 cores 8 threads\n" +
                        "Bộ nhớ đệm Total L2 Cache 2MB Total L3 Cache 4MB\n" +
                        "Bộ nhớ trong (RAM)\n" +
                        "Dung lượng 8GB DDR4 2400Mhz\n" +
                        "Số khe cắm 2 khe, tối đa 32GB\n" +
                        "Ổ cứng\n" +
                        "Dung lượng 512GB PCIe NVMe SSD (nâng cấp được tối đa 1TB SSD)\n" +
                        "Ổ đĩa quang (ODD) Không có\n" +
                        "Hiển thị\n" +
                        "Màn hình 15.6 inch FHD(1920 x 1080) Acer ComfyView IPS LED LCD 60Hz\n" +
                        "Độ phân giải 1920 x 1080\n" +
                        "Đồ Họa (VGA)\n" +
                        "Card màn hình NVIDIA® GeForce® GTX 1650Ti 4GB GDDR6\n" +
                        "Kết nối (Network)\n" +
                        "Wireless Intel® Wireless-Wi-Fi 6 AX200\n" +
                        "LAN Gigabit Ethernet\n" +
                        "Bluetooth Bluetooth® 4.1\n" +
                        "Bàn phím , Chuột\n" +
                        "Kiểu bàn phím Bàn phím tiêu chuẩn, Có bàn phím số - Đèn nền bàn phím\n" +
                        "Chuột Cảm ứng đa điểm\n" +
                        "Giao tiếp mở rộng\n" +
                        "Kết nối USB 2 x USB 3.2 1 x USB Type C 1 x USB 2.0\n" +
                        "Kết nối HDMI/VGA 1xHDMI\n" +
                        "Tai nghe 1x jack 3.5mm\n" +
                        "Camera HD camera with 1280 x 720 resolution, 720p HD audio/video recording\n" +
                        "Card mở rộng\n" +
                        "LOA 2 Loa\n" +
                        "Kiểu Pin 48Wh Li-ion battery\n" +
                        "Sạc pin Đi kèm\n" +
                        "Hệ điều hành (bản quyền) đi kèm Windows 10 SL\n" +
                        "Kích thước (Dài x Rộng x Cao) 363.4 (W) x 254.5 (D) x 23.25 (H) mm\n" +
                        "Trọng Lượng 2.1kg\n" +
                        "Màu sắc Đen\n" +
                        "Xuất Xứ Trung Quốc\n" +
                        "Bộ vi xử lý\n" +
                        "Bộ vi xử lý AMD Ryzen 7-3750H\n" +
                        "Tốc độ 2.10GHz upto 3.70GHz, 4MB cache, 4 cores 8 threads\n" +
                        "Bộ nhớ đệm Total L2 Cache 2MB Total L3 Cache 4MB\n" +
                        "Bộ nhớ trong (RAM)\n" +
                        "Dung lượng 8GB DDR4 2400Mhz\n" +
                        "Số khe cắm 2 khe, tối đa 32GB\n" +
                        "Ổ cứng\n" +
                        "Dung lượng 512GB PCIe NVMe SSD (nâng cấp được tối đa 1TB SSD)\n" +
                        "Ổ đĩa quang (ODD) Không có\n" +
                        "Hiển thị\n" +
                        "Màn hình 15.6 inch FHD(1920 x 1080) Acer ComfyView IPS LED LCD 60Hz\n" +
                        "Độ phân giải 1920 x 1080\n" +
                        "Đồ Họa (VGA)\n" +
                        "Card màn hình NVIDIA® GeForce® GTX 1650Ti 4GB GDDR6\n" +
                        "Kết nối (Network)\n" +
                        "Wireless Intel® Wireless-Wi-Fi 6 AX200\n" +
                        "LAN Gigabit Ethernet\n" +
                        "Bluetooth Bluetooth® 4.1\n" +
                        "Bàn phím , Chuột\n" +
                        "Kiểu bàn phím Bàn phím tiêu chuẩn, Có bàn phím số - Đèn nền bàn phím\n" +
                        "Chuột Cảm ứng đa điểm\n" +
                        "Giao tiếp mở rộng\n" +
                        "Kết nối USB 2 x USB 3.2 1 x USB Type C 1 x USB 2.0\n" +
                        "Kết nối HDMI/VGA 1xHDMI\n" +
                        "Tai nghe 1x jack 3.5mm\n" +
                        "Camera HD camera with 1280 x 720 resolution, 720p HD audio/video recording\n" +
                        "Card mở rộng\n" +
                        "LOA 2 Loa\n" +
                        "Kiểu Pin 48Wh Li-ion battery\n" +
                        "Sạc pin Đi kèm\n" +
                        "Hệ điều hành (bản quyền) đi kèm Windows 10 SL\n" +
                        "Kích thước (Dài x Rộng x Cao) 363.4 (W) x 254.5 (D) x 23.25 (H) mm\n" +
                        "Trọng Lượng 2.1kg\n" +
                        "Màu sắc Đen\n" +
                        "Xuất Xứ Trung Quốc",
                categoryLaptopAcer,
                brandAcer,
                "https://philong.com.vn/media/product/23259-5774_aspire7_a715_75g_41g_black_01.png",
                phiLongRetailer,
"https://philong.com.vn/laptop-acer-aspire-7-a715-41g-r150-r7-3750hram-8gbssd-512gbman-hinh-15.6quot-fhdvga-1650ti-4gwin-10.html",
                18090000L
        );

        saveProduct(
                "LAPTOP ACER NITRO AN515 55 77P9 (Core i7 10750H/Ram 8GB/SSD 512GB/Màn hình 15.6\" FHD/VGA 4G 1650Ti/Win 10)",
                "CPU: Intel Core i7 10750H &nbsp;\n" +
                        "RAM: 8GB (8GBx1) DDR4\n" +
                        "Ổ cứng: 512GB PCIe NVMe SSD VGA\n" +
                        "Card đồ họa: NVIDIA GTX 1650Ti 4GB GDDR6\n" +
                        "Màn hình: 15.6” FHD(1920 x 1080) IPS 144Hz\n" +
                        "Hệ điều hành: Windows 10 Home",
                "Bộ vi xử lý\n" +
                        "Bộ vi xử lý Intel® Core™ i7-10750H\n" +
                        "Tốc độ 2.60GHz upto 5.00GHz, 12MB cache, 6 cores 12 threads\n" +
                        "Bộ nhớ đệm 12MB Cache\n" +
                        "Bộ nhớ trong (RAM)\n" +
                        "Dung lượng 8GB (8GBx1) DDR4\n" +
                        "Số khe cắm 2\n" +
                        "Ổ cứng\n" +
                        "Dung lượng 512GB PCIe NVMe SSD\n" +
                        "Ổ đĩa quang (ODD) Không có\n" +
                        "Hiển thị\n" +
                        "Màn hình 15.6 inch FHD(1920 x 1080) IPS 144Hz\n" +
                        "Độ phân giải 1920 x 1080\n" +
                        "Đồ Họa (VGA)\n" +
                        "Card màn hình NVIDIA® GeForce® GTX 1650Ti 4GB GDDR6\n" +
                        "Kết nối (Network)\n" +
                        "Wireless Intel® Wireless Wi-Fi 6 AX201\n" +
                        "LAN 10/100/1000 Mbps\n" +
                        "Bluetooth Bluetooth® 5.0\n" +
                        "Bàn phím , Chuột\n" +
                        "Kiểu bàn phím Bàn phím tiêu chuẩn, Có bàn phím số - Đèn nền bàn phím\n" +
                        "Chuột Cảm ứng đa điểm\n" +
                        "Giao tiếp mở rộng\n" +
                        "Kết nối USB 1 x USB 3.2 Gen 2 2 x USB 3.2 Gen 1 1 x USB Type C\n" +
                        "Kết nối HDMI/VGA 1xHDMI\n" +
                        "Tai nghe 1x jack 3.5mm\n" +
                        "Camera Có\n" +
                        "LOA 2 Loa\n" +
                        "Kiểu Pin 4-cell, 57.5 Wh\n" +
                        "Sạc pin Đi kèm\n" +
                        "Hệ điều hành (bản quyền) đi kèm Windows 10 Home\n" +
                        "Kích thước (Dài x Rộng x Cao) 363.4 (W) x 255 (D) x 23.9 (H) mm\n" +
                        "Trọng Lượng 2.3 kg\n" +
                        "Màu sắc Đen\n" +
                        "Xuất Xứ Trung Quốc\n" +
                        "Bộ vi xử lý\n" +
                        "Bộ vi xử lý Intel® Core™ i7-10750H\n" +
                        "Tốc độ 2.60GHz upto 5.00GHz, 12MB cache, 6 cores 12 threads\n" +
                        "Bộ nhớ đệm 12MB Cache\n" +
                        "Bộ nhớ trong (RAM)\n" +
                        "Dung lượng 8GB (8GBx1) DDR4\n" +
                        "Số khe cắm 2\n" +
                        "Ổ cứng\n" +
                        "Dung lượng 512GB PCIe NVMe SSD\n" +
                        "Ổ đĩa quang (ODD) Không có\n" +
                        "Hiển thị\n" +
                        "Màn hình 15.6 inch FHD(1920 x 1080) IPS 144Hz\n" +
                        "Độ phân giải 1920 x 1080\n" +
                        "Đồ Họa (VGA)\n" +
                        "Card màn hình NVIDIA® GeForce® GTX 1650Ti 4GB GDDR6\n" +
                        "Kết nối (Network)\n" +
                        "Wireless Intel® Wireless Wi-Fi 6 AX201\n" +
                        "LAN 10/100/1000 Mbps\n" +
                        "Bluetooth Bluetooth® 5.0\n" +
                        "Bàn phím , Chuột\n" +
                        "Kiểu bàn phím Bàn phím tiêu chuẩn, Có bàn phím số - Đèn nền bàn phím\n" +
                        "Chuột Cảm ứng đa điểm\n" +
                        "Giao tiếp mở rộng\n" +
                        "Kết nối USB 1 x USB 3.2 Gen 2 2 x USB 3.2 Gen 1 1 x USB Type C\n" +
                        "Kết nối HDMI/VGA 1xHDMI\n" +
                        "Tai nghe 1x jack 3.5mm\n" +
                        "Camera Có\n" +
                        "LOA 2 Loa\n" +
                        "Kiểu Pin 4-cell, 57.5 Wh\n" +
                        "Sạc pin Đi kèm\n" +
                        "Hệ điều hành (bản quyền) đi kèm Windows 10 Home\n" +
                        "Kích thước (Dài x Rộng x Cao) 363.4 (W) x 255 (D) x 23.9 (H) mm\n" +
                        "Trọng Lượng 2.3 kg\n" +
                        "Màu sắc Đen\n" +
                        "Xuất Xứ Trung Quốc",
                categoryLaptopAcer,
                brandAcer,
                "https://philong.com.vn/media/product/23258-6731_acer_nitro_5_an515_55_55e3_nh_q7qsv_002_1.jpg",
                phiLongRetailer,
                "https://philong.com.vn/laptop-acer-nitro-an515-55-77p9-core-i7-10750hram-8gbssd-512gbman-hinh-15.6quot-fhdvga-4g-1650tiwin-10.html",
                24090000L
        );
    }


    private void saveProduct(
            String title,
            String shortDesc,
            String longDesc,
            Category category,
            Brand brand,
            String image,

            Retailer retailer,
            String url,
            Long price
    ) {

        Product product = new Product();
        product.setId(null);
        product.setTitle(title);
        product.setShortDescription(shortDesc);
        product.setLongDescription(longDesc);
        product.setCategory(category);
        product.setBrand(brand);
        productRepository.save(product);
        imageRepository.save(new Image(image, product));

        initProductRetailer(product,retailer,url,price);
    }

    private void initProductRetailer(Product product,Retailer retailer, String url, Long price){
        ProductRetailer productRetailer = new ProductRetailer();
        productRetailer.setProduct(product);
        productRetailer.setRetailer(retailer);
        productRetailer.setUrl(url);
        productRetailerRepository.save(productRetailer);

        initPrice(productRetailer,price);
    }

    private void initPrice(ProductRetailer productRetailer,Long p) {
        Price price = new Price();
        price.setPrice(p);
        price.setProductRetailer(productRetailer);
        priceRepository.save(price);
    }

    private void initRole() {
        Role roleAdmin = new Role();
        Role roleRetailer = new Role();
        Role roleGuest = new Role();
        roleAdmin.setName(ERole.ROLE_ADMIN);
        roleRetailer.setName(ERole.ROLE_RETAILER);
        roleGuest.setName(ERole.ROLE_GUEST);
        roleRepository.save(roleAdmin);
        roleRepository.save(roleRetailer);
        roleRepository.save(roleGuest);
    }

    private void initBrand() {
        List<String> listBrand = Arrays.asList(
                "ASUS",
                "HP",
                "ACER",
                "Dell",
                "Lenovo",
                "MSI",
                "LG",
                "Avita",
                "MICROSOFT",
                "Huawei",
                "SAMSUNG",
                "Sony",
                "TCL",
                "Casper",
                "Asanzo",
                "SHARP",
                "Akino",
                "Panasonic",
                "Sanco",
                "OPPO (Realme)",
                "Apple",
                "OnePlus",
                "Vivo",
                "Nokia",
                "Vsmart",
                "BlackBerry",
                "Huawei",
                "Xiaomi",
                "ZTE",
                "Meizu"
        );
        listBrand.forEach(s -> {
            brandRepository.save(
                    new Brand(
                            s,
                            "Nhà sản xuất " + s
                    )
            );
        });
    }

    private void initRetailer() {
        retailerRepository.save(new Retailer(
                "didongdanang.com",
                "Siêu thị điện thoại - phụ kiện - linh kiện chính hãng",
                "https://didongdanang.com/",
                "https://didongdanang.com/wp-content/uploads/2020/02/bi-logo1.jpg"
        ));

        retailerRepository.save(new Retailer(
                "SHOPDUNK",
                "ShopDuck Đại lý ủy quyền Apple",
                "https://shopdunk.com/",
                "https://shopdunk.com/wp-content/uploads/2020/12/logo-white.png"
        ));

        retailerRepository.save(new Retailer(
                "PhiLong Techology",
                "Trang web công ty Phi Long",
                "https://philong.com.vn/",
                "https://philong.com.vn/media/banner/logo_PHILONG-LOGO-min-cn.png"
        ));

    }

    private void initCategory() {
        categoryService.create(new CategoryRequest(
                "Điện thoại",
                "Danh mục điện thoại",
                null
        ));

        List<String> listSmartPhoneName = Arrays.asList(
                "Điện thoại Samsung",
                "Điện thoại OPPO (Realme)",
                "Điện thoại Asus",
                "Điện thoại Google",
                "Điện thoại Sony",
                "Điện thoại BKAV",
                "Điện thoại Meiigoo",
                "Điện thoại Iphone",
                "Điện thoại OnePlus",
                "Điện thoại Vivo",
                "Điện thoại Nokia",
                "Điện thoại Vsmart",
                "Điện thoại BlackBerry",
                "Điện thoại Huawei",
                "Điện thoại Xiaomi",
                "Điện thoại ZTE",
                "Điện thoại LG",
                "Điện thoại Meizu",
                "Điện thoại Lenovo"
        );

        listSmartPhoneName.forEach(s -> {
            categoryService.create(new CategoryRequest(
                    s,
                    "Danh mục điện thoại " + s,
                    categoryRepository.findByTitle("Điện thoại").getId()
            ));
        });

        categoryService.create(new CategoryRequest(
                "Laptop",
                "Danh mục laptop",
                null
        ));

        List<String> laptopNameList = Arrays.asList(
                "Laptop ASUS",
                "Laptop HP",
                "Laptop ACER",
                "Laptop Dell",
                "Laptop Lenovo",
                "Laptop MSI",
                "Laptop LG",
                "Laptop Avita",
                "Laptop MICROSOFT",
                "Laptop Huawei"
        );

        laptopNameList.forEach(s -> {
            categoryService.create(new CategoryRequest(
                    s,
                    "Danh muc laptop " + s,
                    categoryRepository.findByTitle("Laptop").getId()
            ));
        });

        categoryService.create(new CategoryRequest(
                "Màn hình",
                "Danh mục màn hình máy tính",
                null
        ));

        List<String> monitorNameList = Arrays.asList(
                "SAMSUNG",
                "LG",
                "Dell",
                "ASUS",
                "HP",
                "ACER",
                "PHILIPS",
                "VIEWSONIC",
                "AOC",
                "MSI",
                "BenQ"
        );

        monitorNameList.forEach(s -> {
            categoryService.create(new CategoryRequest(
                    "Màn hình " + s,
                    "Danh mục màn hình máy tính " + s,
                    categoryRepository.findByTitle("Màn hình").getId()
            ));
        });

//        categoryService.create(new CategoryRequest(
//                "Computer Components",
//                "Danh mục linh kiện máy tính",
//                ""
//        ));
//
//        categoryRepository.findAll().forEach(category -> System.out.println("category.getTitle() = " + category.getTitle()));
//
//        categoryService.create(new CategoryRequest(
//                "bộ vi xử lý - cpu".toUpperCase(),
//                "Danh mục linh kiện máy tính - cpu",
//                categoryRepository.findByTitle("Computer Components").getId().toString()
//        ));
//
//        categoryService.create(new CategoryRequest(
//                "CPU Intel",
//                "Danh mục cpu - intell",
//                categoryRepository.findByTitle("bộ vi xử lý - cpu".toUpperCase()).getId().toString()
//        ));
//
//        categoryService.create(new CategoryRequest(
//                "Intel Celeron",
//                "Danh mục cpu - intell - celeron",
//                categoryRepository.findByTitle("CPU Intel").getId().toString()
//        ));
//
//        categoryService.create(new CategoryRequest(
//                "Intel Pentium",
//                "Danh mục cpu - intell - pentium",
//                categoryRepository.findByTitle("CPU Intel").getId().toString()
//        ));
//
//        categoryService.create(new CategoryRequest(
//                "Intel Core i3",
//                "Danh mục cpu - intell - i3",
//                categoryRepository.findByTitle("CPU Intel").getId().toString()
//        ));
//
//        categoryService.create(new CategoryRequest(
//                "Intel Core i5",
//                "Danh mục cpu - intell - i5",
//                categoryRepository.findByTitle("CPU Intel").getId().toString()
//        ));
//        categoryService.create(new CategoryRequest(
//                "Intel Core i7",
//                "Danh mục cpu - intell - i7",
//                categoryRepository.findByTitle("CPU Intel").getId().toString()
//        ));
//        categoryService.create(new CategoryRequest(
//                "Intel Core i9",
//                "Danh mục cpu - intell - i9",
//                categoryRepository.findByTitle("CPU Intel").getId().toString()
//        ));
//
//        categoryService.create(new CategoryRequest(
//                "Intel Xeon",
//                "Danh mục cpu - intell - Xeon",
//                categoryRepository.findByTitle("CPU Intel").getId().toString()
//        ));
//
//
//        categoryService.create(new CategoryRequest(
//                "CPU AMD",
//                "Danh mục cpu - AMD",
//                categoryRepository.findByTitle("bộ vi xử lý - cpu".toUpperCase()).getId().toString()
//        ));
//
//        categoryService.create(new CategoryRequest(
//                "AMD Athlon",
//                "Danh mục cpu - AMD - Athlon",
//                categoryRepository.findByTitle("CPU AMD").getId().toString()
//        ));
//
//        categoryService.create(new CategoryRequest(
//                "AMD Ryzen 3",
//                "Danh mục cpu - AMD - r3",
//                categoryRepository.findByTitle("CPU AMD").getId().toString()
//        ));
//
//        categoryService.create(new CategoryRequest(
//                "AMD Ryzen 5",
//                "Danh mục cpu - AMD - r5",
//                categoryRepository.findByTitle("CPU AMD").getId().toString()
//        ));
//
//        categoryService.create(new CategoryRequest(
//                "AMD Ryzen 7",
//                "Danh mục cpu - AMD - r7",
//                categoryRepository.findByTitle("CPU AMD").getId().toString()
//        ));
//
//        categoryService.create(new CategoryRequest(
//                "AMD Ryzen 9",
//                "Danh mục cpu - AMD - r9",
//                categoryRepository.findByTitle("CPU AMD").getId().toString()
//        ));
//
//        categoryService.create(new CategoryRequest(
//                "AMD Threadripper",
//                "Danh mục cpu - AMD - Threadripper",
//                categoryRepository.findByTitle("CPU AMD").getId().toString()
//        ));
//
//        categoryService.create(new CategoryRequest(
//                "bo mạch chủ - mainboard".toUpperCase(),
//                "Danh mục linh kiện máy tính - MAINBOARD",
//                categoryRepository.findByTitle("Computer Components").getId().toString()
//        ));
//
//        categoryService.create(new CategoryRequest(
//                "Mainboard Asus",
//                "Bo mạch chủ - asus",
//                categoryRepository.findByTitle("bo mạch chủ - mainboard".toUpperCase()).getId().toString()
//        ));
//
//        categoryService.create(new CategoryRequest(
//                "Mainboard Gigabyte",
//                "Bo mạch chủ - Gigabyte",
//                categoryRepository.findByTitle("bo mạch chủ - mainboard".toUpperCase()).getId().toString()
//        ));
//
//        categoryService.create(new CategoryRequest(
//                "Mainboard MSI",
//                "Bo mạch chủ - MSI",
//                categoryRepository.findByTitle("bo mạch chủ - mainboard".toUpperCase()).getId().toString()
//
//        ));
//
//        categoryService.create(new CategoryRequest(
//                "Mainboard Asrock",
//                "Bo mạch chủ - Asrock",
//                categoryRepository.findByTitle("bo mạch chủ - mainboard".toUpperCase()).getId().toString()
//        ));
//
//        categoryService.create(new CategoryRequest(
//                "Mainboard Server",
//                "Bo mạch chủ - Server",
//                categoryRepository.findByTitle("bo mạch chủ - mainboard".toUpperCase()).getId().toString()
//        ));
//
//        categoryService.create(new CategoryRequest(
//                "Mainboard Intel",
//                "Bo mạch chủ - Intel",
//                categoryRepository.findByTitle("bo mạch chủ - mainboard".toUpperCase()).getId().toString()
//        ));
//
//        categoryService.create(new CategoryRequest(
//                "bộ nhớ trong - ram".toUpperCase(),
//                "Danh mục linh kiện máy tính - RAM",
//                categoryRepository.findByTitle("Computer Components").getId().toString()
//        ));
//
//
//        categoryService.create(new CategoryRequest(
//                "Ram G.Skill",
//                "Danh mục linh kiện máy tính - RAM - Gskill",
//                categoryRepository.findByTitle("bộ nhớ trong - ram".toUpperCase()).getId().toString()
//        ));
//
//        categoryService.create(new CategoryRequest(
//                "Ram Corsair",
//                "Danh mục linh kiện máy tính - RAM - Corsair",
//                categoryRepository.findByTitle("bộ nhớ trong - ram".toUpperCase()).getId().toString()
//        ));
//
//        categoryService.create(new CategoryRequest(
//                "Ram Kingston",
//                "Danh mục linh kiện máy tính - RAM - Kingston",
//                categoryRepository.findByTitle("bộ nhớ trong - ram".toUpperCase()).getId().toString()
//        ));
//
//        categoryService.create(new CategoryRequest(
//                "Ram Patriot",
//                "Danh mục linh kiện máy tính - RAM - Patriot",
//                categoryRepository.findByTitle("bộ nhớ trong - ram".toUpperCase()).getId().toString()
//        ));
//
//        categoryService.create(new CategoryRequest(
//                "Ram Geil",
//                "Danh mục linh kiện máy tính - RAM - Geil",
//                categoryRepository.findByTitle("bộ nhớ trong - ram".toUpperCase()).getId().toString()
//        ));
//
//        categoryService.create(new CategoryRequest(
//                "Ram ADATA",
//                "Danh mục linh kiện máy tính - RAM - ADATA",
//                categoryRepository.findByTitle("bộ nhớ trong - ram".toUpperCase()).getId().toString()
//        ));
//
//        categoryService.create(new CategoryRequest(
//                "Ram Kingmax",
//                "Danh mục linh kiện máy tính - RAM - Kingmax",
//                categoryRepository.findByTitle("bộ nhớ trong - ram".toUpperCase()).getId().toString()
//        ));
//
//        categoryService.create(new CategoryRequest(
//                "Ram Gigabyte",
//                "Danh mục linh kiện máy tính - RAM - Gigabyte",
//                categoryRepository.findByTitle("bộ nhớ trong - ram".toUpperCase()).getId().toString()
//        ));
//
//        categoryService.create(new CategoryRequest(
//                "Ram TEAM",
//                "Danh mục linh kiện máy tính - RAM - TEAM",
//                categoryRepository.findByTitle("bộ nhớ trong - ram".toUpperCase()).getId().toString()
//        ));
//
//        categoryService.create(new CategoryRequest(
//                "Ram samsung",
//                "Danh mục linh kiện máy tính - RAM - samsung",
//                categoryRepository.findByTitle("bộ nhớ trong - ram".toUpperCase()).getId().toString()
//        ));
//
//
//        categoryService.create(new CategoryRequest(
//                "ỗ đĩa hdd, dvd".toUpperCase(),
//                "Danh mục linh kiện máy tính - HDD,DVD",
//                categoryRepository.findByTitle("Computer Components").getId().toString()
//        ));
//
//        categoryService.create(new CategoryRequest(
//                "ổ cứng ssd".toUpperCase(),
//                "Danh mục linh kiện máy tính - SSD",
//                categoryRepository.findByTitle("Computer Components").getId().toString()
//        ));
//
//        categoryService.create(new CategoryRequest(
//                "card đồ họa - vga".toUpperCase(),
//                "Danh mục linh kiện máy tính - VGA",
//                categoryRepository.findByTitle("Computer Components").getId().toString()
//        ));
//
//        categoryService.create(new CategoryRequest(
//                "nguồn máy tính - psu".toUpperCase(),
//                "Danh mục linh kiện máy tính - PSU",
//                categoryRepository.findByTitle("Computer Components").getId().toString()
//        ));
//
//        categoryService.create(new CategoryRequest(
//                "Vỏ máy tính - case".toUpperCase(),
//                "Danh mục linh kiện máy tính - CASE",
//                categoryRepository.findByTitle("Computer Components").getId().toString()
//        ));
    }

    public void initUser() {

        authService.register(new RegisterRequest(
                "vanthan",
                "than",
                "Trương Văn Thân",
                "Triệu Phong - Quảng Trị",
                "0365843463"
        ), ERole.ROLE_ADMIN);

        authService.register(new RegisterRequest(
                "huutho",
                "tho",
                "Nguyễn Hữu Thọ",
                "Hải Lăng - Quảng Trị",
                "0723423643"

        ), ERole.ROLE_RETAILER);

        authService.register(new RegisterRequest(
                "nhungnguyen",
                "nhung",
                "Nguyễn Thị Nhung",
                "Triệu Phong - Quảng Trị",
                "0723423603"

        ), ERole.ROLE_GUEST);
    }
}
