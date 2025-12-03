package com.example.otech.repository;

import com.example.otech.model.Address;
import com.example.otech.model.Banner;
import com.example.otech.model.CartItem;
import com.example.otech.model.Notification;
import com.example.otech.model.Order;
import com.example.otech.model.Product;
import com.example.otech.model.Review;
import com.example.otech.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class MockDataStore {
    private static MockDataStore instance;
    
    private ArrayList<Product> products;
    private ArrayList<User> users;
    private ArrayList<Order> orders;
    private ArrayList<Notification> notifications;
    private ArrayList<Banner> banners;
    private HashMap<String, ArrayList<CartItem>> userCarts; // userId -> CartItems
    private HashMap<String, ArrayList<Product>> userWishlists; // userId -> Products
    private HashMap<String, ArrayList<Address>> userAddresses; // userId -> Addresses
    private HashMap<String, ArrayList<Review>> productReviews; // productId -> Reviews
    
    private MockDataStore() {
        products = new ArrayList<>();
        users = new ArrayList<>();
        orders = new ArrayList<>();
        notifications = new ArrayList<>();
        banners = new ArrayList<>();
        userCarts = new HashMap<>();
        userWishlists = new HashMap<>();
        userAddresses = new HashMap<>();
        productReviews = new HashMap<>();
        
        initUsers();
        initProducts();
        initReviews();
        initBanners();
    }
    
    public static synchronized MockDataStore getInstance() {
        if (instance == null) {
            instance = new MockDataStore();
        }
        return instance;
    }
    
    private void initUsers() {
        users.add(new User("1", "admin", "admin", "Admin User", 
                "0123456789", "123 Admin St", "admin@otech.com", "admin"));
        users.add(new User("2", "user", "user", "Test User", 
                "0987654321", "456 User Ave", "user@otech.com", "user"));
    }
    
    private void initProducts() {
        // Văn phòng - 7 products
        addProduct("1", "Dell Inspiron 15 3530", 16990000, 20000000, "Dell", "Văn phòng",
                "Dell Inspiron 15 3530 là lựa chọn hoàn hảo cho môi trường văn phòng hiện đại. Với thiết kế thanh lịch, vỏ máy màu bạc tinh tế cùng với bản lề mở 180 độ tiện lợi cho việc chia sẻ màn hình. Máy trang bị bộ xử lý Intel Core i5 thế hệ 13 mới nhất, mang lại hiệu năng ổn định cho các tác vụ văn phòng như soạn thảo văn bản, làm việc với bảng tính Excel, trình chiếu PowerPoint hay duyệt web đa tab. RAM 16GB DDR4 đảm bảo đa nhiệm mượt mà, ổ SSD 512GB NVMe cho tốc độ khởi động nhanh chóng chỉ trong vài giây. Màn hình 15.6 inch FHD với tần số quét 120Hz mang lại trải nghiệm hình ảnh sắc nét, màu sắc sống động, đặc biệt phù hợp cho các công việc đòi hỏi độ chính xác màu cao như thiết kế đồ họa nhẹ. Card đồ họa tích hợp Intel UHD Graphics xử lý tốt các tác vụ cơ bản và một số phần mềm thiết kế 2D. Thiết kế bàn phím chiclet với đèn nền LED tiện lợi cho việc làm việc trong môi trường thiếu sáng. Khối lượng chỉ 1.65kg và độ dày 17.9mm giúp máy dễ dàng di chuyển. Thời lượng pin lên đến 7-8 tiếng sử dụng liên tục, đủ cho một ngày làm việc dài. Các cổng kết nối đầy đủ bao gồm USB 3.2, HDMI, USB-C, jack tai nghe 3.5mm và khe thẻ SD đáp ứng mọi nhu cầu kết nối ngoại vi.",
                "CPU: Intel Core i5-1335U, RAM: 16GB DDR4 3200MHz, Storage: 512GB SSD NVMe, GPU: Intel UHD Graphics, Screen: 15.6 inch FHD 120Hz", 4.2f, 20, 45);
        addProduct("2", "HP Pavilion 15-eg3000", 17500000, 21000000, "HP", "Văn phòng",
                "HP Pavilion 15-eg3000 mang đến sự cân bằng hoàn hảo giữa hiệu năng và thiết kế tinh tế cho người dùng văn phòng. Vỏ máy kim loại nguyên khối với màu bạc sang trọng, các cạnh viền được bo tròn tinh tế tạo cảm giác cầm nắm thoải mái. Bộ xử lý Intel Core i5-13500H thế hệ 13 với 12 nhân xử lý, mang lại sức mạnh vượt trội cho các tác vụ đa nhiệm nặng như chạy đồng thời nhiều ứng dụng Office, trình duyệt web với hàng chục tab, phần mềm kế toán hay thậm chí là chỉnh sửa ảnh cơ bản. RAM 16GB DDR4 kênh đôi đảm bảo không bị lag khi làm việc với file dữ liệu lớn. Ổ cứng SSD 512GB PCIe NVMe Gen 4 cho tốc độ đọc/ghi lên đến 7000MB/s, khởi động Windows chỉ trong 5-7 giây. Màn hình 15.6 inch IPS Full HD với góc nhìn rộng 178 độ, độ sáng 250 nits và công nghệ chống chói Anti-Glare giúp làm việc ngoài trời hay dưới đèn huỳnh quang mà không bị chói mắt. Âm thanh được tinh chỉnh bởi Bang & Olufsen với loa kép hướng lên trên, âm thanh sống động và bass sâu. Bàn phím full-size với hành trình phím 1.5mm, cảm giác gõ thoải mái và chính xác. Touchpad lớn với bề mặt kính, hỗ trợ đa điểm cảm ứng mượt mà. Thiết kế bản lề chắc chắn, mở 180 độ tiện lợi. Khối lượng 1.75kg phù hợp cho việc di chuyển hàng ngày. Thời lượng pin khoảng 6-7 tiếng sử dụng văn phòng. Các cổng kết nối đa dạng: 2x USB-A 3.2, 1x USB-C, HDMI 2.1, jack tai nghe, khe thẻ SD.",
                "CPU: Intel Core i5-13500H, RAM: 16GB DDR4, Storage: 512GB SSD, GPU: Intel Iris Xe Graphics, Screen: 15.6 inch FHD IPS", 4.3f, 20, 38);
        addProduct("3", "Asus Vivobook Go 15", 12990000, 14500000, "Asus", "Văn phòng",
                "Asus Vivobook Go 15 là người bạn đồng hành lý tưởng cho sinh viên và nhân viên văn phòng với mức giá phải chăng nhưng vẫn đảm bảo hiệu năng ổn định. Thiết kế mỏng nhẹ chỉ 1.63kg, độ dày 17.9mm giúp dễ dàng bỏ vào balo và mang theo mọi lúc mọi nơi. Vỏ máy nhựa ABS với kết cấu vân carbon cao cấp, bền bỉ và chống trầy xước tốt. Bộ xử lý AMD Ryzen 5 7520U với 6 nhân 12 luồng, mang lại hiệu năng đủ dùng cho các tác vụ văn phòng thông thường như Word, Excel, PowerPoint, duyệt web, học online. RAM 16GB LPDDR5 tốc độ cao 5500MHz cho đa nhiệm mượt mà, không bị lag khi mở nhiều ứng dụng cùng lúc. Ổ SSD 512GB PCIe Gen 4 cho tốc độ khởi động siêu nhanh. Màn hình 15.6 inch OLED với độ phân giải 1920x1080, tỷ lệ tương phản 1.000.000:1, màu đen sâu và góc nhìn rộng 178 độ. Công nghệ OLED mang lại màu sắc sống động, độ sáng cao và tiết kiệm pin hơn LCD. Bàn phím chiclet với hành trình phím 1.4mm, cảm giác gõ êm ái. Touchpad lớn với driver ASUS Precision Touchpad cho thao tác vuốt chạm mượt mà. Thiết kế bản lề mở 180 độ tiện lợi cho việc chia sẻ nội dung. Đạt chuẩn độ bền quân đội MIL-STD-810H, chịu được va đập và nhiệt độ khắc nghiệt. Thời lượng pin lên đến 8-9 tiếng sử dụng văn phòng. Cổng kết nối bao gồm 1x USB-C 3.2, 1x USB-A 3.2, HDMI 1.4, jack tai nghe 3.5mm.",
                "CPU: AMD Ryzen 5 7520U, RAM: 16GB LPDDR5, Storage: 512GB SSD, GPU: AMD Radeon Graphics, Screen: 15.6 inch FHD OLED", 4.1f, 20, 52);
        addProduct("4", "Acer Aspire 5 Slim", 13990000, 17000000, "Acer", "Văn phòng",
                "Acer Aspire 5 Slim kết hợp hoàn hảo giữa thiết kế mỏng nhẹ và hiệu năng mạnh mẽ cho công việc văn phòng chuyên nghiệp. Với trọng lượng chỉ 1.45kg và độ dày 15.9mm, đây là chiếc laptop dễ dàng đồng hành cùng bạn trong mọi chuyến công tác. Vỏ máy kim loại nguyên khối màu bạc tinh tế, các góc cạnh được bo tròn mềm mại tạo cảm giác hiện đại. Bộ xử lý Intel Core i5-13420H với 8 nhân hiệu năng và 4 nhân tiết kiệm điện, mang lại sức mạnh vượt trội cho đa nhiệm và các tác vụ văn phòng nặng. RAM 16GB DDR5 kênh đôi tốc độ 4800MHz đảm bảo không bị bottleneck, xử lý mượt mà file Excel hàng triệu dòng hay database lớn. Ổ SSD 512GB NVMe PCIe Gen 3 cho tốc độ đọc/ghi nhanh chóng. Màn hình 14 inch IPS Full HD với độ sáng 300 nits, công nghệ chống chói ComfyView và viền màn hình mỏng chỉ 7.15mm ở 3 cạnh, tỷ lệ màn hình so với thân máy lên đến 81%. Card đồ họa Intel UHD Graphics tích hợp xử lý tốt các tác vụ văn phòng và một số phần mềm thiết kế cơ bản. Hệ thống tản nhiệt Acer TwinAir với 2 quạt tản nhiệt và 2 ống dẫn nhiệt, giữ máy luôn mát mẻ ngay cả khi làm việc liên tục. Bàn phím full-size với đèn nền LED trắng, hành trình phím 1.5mm thoải mái. Touchpad Precision Touchpad driver Microsoft. Thời lượng pin khoảng 7-8 tiếng sử dụng văn phòng. Cổng kết nối: 2x USB-A 3.2, 1x USB-C 3.2, HDMI 2.1, jack tai nghe 3.5mm.",
                "CPU: Intel Core i5-13420H, RAM: 16GB DDR5, Storage: 512GB SSD, GPU: Intel UHD Graphics, Screen: 14 inch FHD IPS", 4.2f, 20, 41);
        addProduct("5", "Lenovo IdeaPad Slim 5", 18990000, 20500000, "Lenovo", "Văn phòng",
                "Lenovo IdeaPad Slim 5 là biểu tượng của sự tinh tế và hiệu năng trong phân khúc laptop văn phòng cao cấp. Thiết kế tối giản với vỏ máy màu xám Storm Grey hoặc xanh Abyss Blue, logo Lenovo ẩn tinh tế chỉ hiện khi có ánh sáng. Bộ xử lý Intel Core i5-1340P với 12 nhân 16 luồng, mang lại hiệu năng vượt trội cho các tác vụ văn phòng chuyên nghiệp như xử lý bảng tính lớn, báo cáo tài chính, hay thậm chí là chỉnh sửa video cơ bản. RAM 16GB LPDDR5 kênh đôi tốc độ 5200MHz cho đa nhiệm siêu mượt. Ổ SSD 512GB PCIe Gen 4 với tốc độ đọc lên đến 7000MB/s. Màn hình 16 inch 2.5K (2560x1600) với tỷ lệ 16:10 hiện đại hơn so với 16:9 truyền thống, mang lại không gian làm việc rộng rãi hơn cho đa nhiệm. Công nghệ Dolby Vision và màu sắc 100% sRGB cho độ chính xác màu cao, phù hợp cho designer và content creator. Webcam có nắp che vật lý bảo vệ quyền riêng tư. Bàn phím AccuType với đèn nền LED, cảm giác gõ thoải mái và yên tĩnh. Touchpad Precision Touchpad với driver Microsoft. Thiết kế bản lề chắc chắn, mở 180 độ. Khối lượng 1.89kg và độ dày 16.9mm. Thời lượng pin lên đến 9-10 tiếng. Hệ thống âm thanh Dolby Atmos với 2 loa hướng lên. Cổng kết nối: 2x USB-A 3.2, 1x USB-C (hỗ trợ sạc và DisplayPort), HDMI 2.1, jack tai nghe 3.5mm.",
                "CPU: Intel Core i5-1340P, RAM: 16GB LPDDR5, Storage: 512GB SSD, GPU: Intel Iris Xe Graphics, Screen: 16 inch WUXGA IPS", 4.4f, 20, 36);
        addProduct("6", "MSI Modern 14 C13M", 11990000, 15000000, "MSI", "Văn phòng",
                "MSI Modern 14 C13M là lựa chọn hoàn hảo cho người dùng cần laptop văn phòng siêu nhẹ và thời lượng pin dài. Với trọng lượng chỉ 1.3kg và độ dày 14.9mm, đây là một trong những chiếc laptop mỏng nhẹ nhất phân khúc. Thiết kế vỏ kim loại nguyên khối màu xanh dương thanh lịch, logo MSI ẩn tinh tế. Bộ xử lý Intel Core i3-1315U với 6 nhân 8 luồng, đủ mạnh cho các tác vụ văn phòng thông thường như soạn thảo, trình chiếu, duyệt web. RAM 8GB DDR4 kênh đơn, có thể nâng cấp lên 64GB. Ổ SSD 512GB NVMe PCIe Gen 3 cho tốc độ khởi động nhanh. Màn hình 14 inch IPS Full HD với độ sáng 400 nits, tỷ lệ tương phản 1000:1 và góc nhìn rộng 178 độ. Công nghệ chống chói Anti-Glare giúp làm việc thoải mái dưới nhiều điều kiện ánh sáng. Card đồ họa Intel UHD Graphics xử lý tốt các tác vụ cơ bản. Bàn phím chiclet với hành trình phím 1.5mm, cảm giác gõ êm và chính xác. Touchpad Precision Touchpad với bề mặt kính, hỗ trợ đa điểm cảm ứng. Thiết kế bản lề chắc chắn, mở 180 độ. Thời lượng pin lên đến 10-12 tiếng sử dụng văn phòng nhờ viên pin 52.4Whr dung lượng cao. Cổng kết nối bao gồm 2x USB-A 3.2, 1x USB-C (hỗ trợ DisplayPort và sạc 65W), HDMI 1.4, jack tai nghe 3.5mm. Đạt chuẩn độ bền quân đội MIL-STD-810G.",
                "CPU: Intel Core i3-1315U, RAM: 8GB DDR4, Storage: 512GB SSD, GPU: Intel UHD Graphics, Screen: 14 inch FHD IPS", 4.0f, 20, 29);
        addProduct("7", "LG Gram 14 2023", 24990000, 28900000, "LG", "Văn phòng",
                "LG Gram 14 2023 là chiếc laptop siêu nhẹ nhất thế giới với trọng lượng chỉ 999g, độ dày 16.8mm, dễ dàng bỏ vào túi xách hay balo nhỏ. Thiết kế vỏ magie nguyên khối cao cấp, màu trắng tinh khôi hoặc xám graphite, các cạnh viền bo tròn tinh tế tạo cảm giác sang trọng. Bộ xử lý Intel Core i5-1340P với 12 nhân 16 luồng, mang lại hiệu năng vượt trội cho đa nhiệm văn phòng. RAM 16GB LPDDR5 kênh đôi tốc độ 5200MHz cho tốc độ xử lý siêu nhanh. Ổ SSD 512GB PCIe Gen 4 với tốc độ đọc lên đến 7000MB/s. Màn hình 14 inch WUXGA (1920x1200) với tỷ lệ 16:10 hiện đại, độ sáng 350 nits, công nghệ Dolby Vision IQ tự động điều chỉnh màu sắc theo môi trường, và viền màn hình siêu mỏng chỉ 3.5mm. Webcam 1080p với AI giảm noise và tăng độ nét. Bàn phím chiclet với đèn nền LED, hành trình phím 1.5mm thoải mái. Touchpad Precision Touchpad với driver LG. Thời lượng pin lên đến 20-22 tiếng sử dụng văn phòng nhờ viên pin 72Whr dung lượng lớn và công nghệ tiết kiệm pin tiên tiến. Hệ thống âm thanh DTS:X Ultra với 2 loa hướng lên trên. Cổng kết nối: 2x USB-C (hỗ trợ Thunderbolt 4, DisplayPort, sạc 65W), HDMI 2.1, jack tai nghe 3.5mm. Đạt chuẩn độ bền quân đội MIL-STD-810G và xếp hạng IP52 chống bụi nước.",
                "CPU: Intel Core i5-1340P, RAM: 16GB LPDDR5, Storage: 512GB SSD, GPU: Intel Iris Xe Graphics, Screen: 14 inch WUXGA IPS", 4.5f, 20, 33);

        // Gaming - 8 products
        addProduct("8", "Asus ROG Strix G16", 39990000, 45000000, "Asus", "Gaming",
                "Asus ROG Strix G16 là chiến binh gaming đỉnh cao với hiệu năng khủng hoảng cho mọi tựa game AAA hiện đại. Thiết kế vỏ kim loại nguyên khối màu Eclipse Gray với các đường nét góc cạnh hầm hố, logo ROG phát sáng RGB nổi bật. Bộ xử lý Intel Core i9-13980HX với 24 nhân 32 luồng, xung nhịp lên đến 5.6GHz, mang lại sức mạnh vượt trội cho gaming và streaming đồng thời. RAM 16GB DDR5 kênh đôi tốc độ 4800MHz cho đa nhiệm mượt mà khi chạy game và ứng dụng nền. Ổ SSD 1TB PCIe Gen 4 với tốc độ đọc lên đến 7000MB/s, thời gian loading game siêu nhanh. Card đồ họa NVIDIA GeForce RTX 4060 8GB GDDR6 với kiến trúc Ada Lovelace, ray tracing thế hệ mới và DLSS 3, xử lý mượt mà các game như Cyberpunk 2077, Hogwarts Legacy ở 1440p. Màn hình 16 inch QHD+ (2560x1600) với tần số quét 240Hz, thời gian phản hồi 3ms, công nghệ Adaptive-Sync và 100% DCI-P3 màu sắc. Hệ thống tản nhiệt ROG Intelligent Cooling với 3 quạt Axial-tech, 4 ống dẫn nhiệt và dung dịch kim loại lỏng, giữ nhiệt độ CPU/GPU luôn dưới 90°C ngay cả khi gaming marathon. Bàn phím chiclet RGB 4 vùng với anti-ghosting full, switch phím quang học 0.2ms. Touchpad Precision Touchpad. Âm thanh Dolby Atmos với 2 loa hướng lên và công nghệ Smart Amp. Cổng kết nối: 3x USB-A 3.2, 1x USB-C (DisplayPort/Thunderbolt 4), HDMI 2.1, LAN RJ45, jack tai nghe 3.5mm. Pin 90Whr với sạc nhanh 100W. Khối lượng 2.5kg.",
                "CPU: Intel Core i9-13980HX, RAM: 16GB DDR5, Storage: 1TB SSD NVMe, GPU: NVIDIA GeForce RTX 4060 8GB, Screen: 16 inch QHD+ 240Hz", 4.6f, 20, 78);
        addProduct("9", "MSI Katana 15", 28500000, 38000000, "MSI", "Gaming",
                "MSI Katana 15 là laptop gaming giá rẻ nhưng hiệu năng không kém cạnh với thiết kế bàn phím cắt gọt độc đáo. Vỏ máy màu Core Black với các đường nét gaming hầm hố, logo MSI Dragon phát sáng. Bộ xử lý Intel Core i7-13620H với 10 nhân hiệu năng và 4 nhân tiết kiệm, xung nhịp tối đa 4.9GHz, đủ mạnh cho mọi game AAA ở 1080p. RAM 16GB DDR5 kênh đôi tốc độ 4800MHz. Ổ SSD 1TB NVMe PCIe Gen 4. Card đồ họa NVIDIA GeForce RTX 4050 6GB với kiến trúc Ada Lovelace, DLSS 3 và ray tracing, chơi mượt mà các game như FIFA 23, Valorant, CS2 ở 1440p. Màn hình 15.6 inch FHD IPS với tần số quét 144Hz, thời gian phản hồi 3ms, công nghệ Anti-Glare và 100% sRGB. Hệ thống tản nhiệt Cooler Boost 5 với 2 quạt và 4 ống dẫn nhiệt, công nghệ MSI Center AI để tối ưu hiệu năng. Bàn phím cắt gọt với layout WASD nổi bật, RGB 4 vùng, anti-ghosting và switch phím 1.8mm. Touchpad Precision Touchpad. Webcam HD 720p với công nghệ True Color. Âm thanh Nahimic 3 với 2 loa và micro kép. Cổng kết nối: 3x USB-A 3.2, 1x USB-C (DisplayPort), HDMI 2.1, LAN RJ45, jack tai nghe 3.5mm. Pin 53.5Whr. Khối lượng 2.25kg, độ dày 25.9mm.",
                "CPU: Intel Core i7-13620H, RAM: 16GB DDR5, Storage: 1TB SSD, GPU: NVIDIA GeForce RTX 4050 6GB, Screen: 15.6 inch FHD 144Hz", 4.5f, 20, 92);
        addProduct("10", "Acer Predator Helios Neo 16", 35990000, 38900000, "Acer", "Gaming",
                "Acer Predator Helios Neo 16 sở hữu công nghệ tản nhiệt Aeroblade 3D độc quyền với 4 quạt tản và 5 ống dẫn nhiệt, giữ máy luôn mát mẻ trong mọi trận chiến. Thiết kế vỏ kim loại màu Abyssal Black với các đường nét góc cạnh, logo Predator phát sáng xanh. Bộ xử lý Intel Core i7-13700HX với 16 nhân 24 luồng, xung nhịp tối đa 5.0GHz, sức mạnh vượt trội cho gaming và content creation. RAM 16GB DDR5 kênh đôi tốc độ 4800MHz. Ổ SSD 512GB PCIe Gen 4. Card đồ họa NVIDIA GeForce RTX 4060 8GB với DLSS 3, ray tracing và 128-bit bus, chơi mượt mà 1440p ở 60-100fps. Màn hình 16 inch WQXGA (2560x1600) với tần số quét 165Hz, thời gian phản hồi 3ms, công nghệ G-Sync và 100% DCI-P3. Phím nóng Turbo boost hiệu năng tức thì. Bàn phím RGB 4 vùng với anti-ghosting, switch phím 1.8mm. Touchpad Precision Touchpad. Webcam 1080p với Temporal Noise Reduction. Âm thanh DTS:X Ultra với 2 loa và 2 tweeter. Cổng kết nối: 3x USB-A 3.2, 1x USB-C (Thunderbolt 4), HDMI 2.1, LAN RJ45, jack tai nghe 3.5mm. Pin 90Whr với sạc nhanh 230W. Khối lượng 2.6kg.",
                "CPU: Intel Core i7-13700HX, RAM: 16GB DDR5, Storage: 512GB SSD, GPU: NVIDIA GeForce RTX 4060 8GB, Screen: 16 inch WQXGA 165Hz", 4.6f, 20, 65);
        addProduct("11", "Lenovo Legion Pro 5", 38990000, 42000000, "Lenovo", "Gaming",
                "Lenovo Legion Pro 5 kết hợp thiết kế tối giản với hiệu năng gaming khủng, màn hình chuẩn màu 100% sRGB cho cả gamer và creator. Vỏ máy màu Onyx Grey với các chi tiết Legion tinh tế, logo Legion phát sáng RGB. Bộ xử lý AMD Ryzen 7 7745HX với 8 nhân 16 luồng, xung nhịp tối đa 5.1GHz, sức mạnh vượt trội cho gaming đa dạng. RAM 16GB DDR5 kênh đôi tốc độ 4800MHz. Ổ SSD 1TB PCIe Gen 4. Card đồ họa NVIDIA GeForce RTX 4060 8GB với kiến trúc Ada Lovelace, DLSS 3 và ray tracing, chơi mượt mà 1440p. Màn hình 16 inch WQXGA (2560x1600) với tần số quét 240Hz, thời gian phản hồi 3ms, công nghệ G-Sync và 100% sRGB. Hệ thống tản nhiệt Legion ColdFront 5.0 với 2 quạt và 3 ống dẫn nhiệt, công nghệ AI tự động điều chỉnh. Bàn phím TrueStrike với RGB 4 vùng, anti-ghosting và switch phím quang học. Touchpad Precision Touchpad. Webcam 1080p với AI noise reduction. Âm thanh Nahimic với 2 loa và 2 tweeter. Cổng kết nối: 3x USB-A 3.2, 1x USB-C (Thunderbolt 4), HDMI 2.1, LAN RJ45, jack tai nghe 3.5mm. Pin 80Whr. Khối lượng 2.45kg.",
                "CPU: AMD Ryzen 7 7745HX, RAM: 16GB DDR5, Storage: 1TB SSD, GPU: NVIDIA GeForce RTX 4060 8GB, Screen: 16 inch WQXGA 240Hz", 4.7f, 20, 71);
        addProduct("12", "Gigabyte Aorus 15", 33990000, 36500000, "Gigabyte", "Gaming",
                "Gigabyte Aorus 15 là laptop gaming siêu mỏng với thiết kế CNC nguyên khối và hỗ trợ AI cho game thủ. Vỏ máy màu Matte Black với logo Aorus phát sáng RGB, các cạnh viền bo tròn tinh tế. Bộ xử lý Intel Core i7-13700H với 14 nhân 20 luồng, xung nhịp tối đa 5.0GHz. RAM 16GB DDR5 kênh đôi tốc độ 4800MHz. Ổ SSD 1TB PCIe Gen 4. Card đồ họa NVIDIA GeForce RTX 4070 8GB với DLSS 3, ray tracing và 128-bit bus, chơi mượt mà 1440p ở 80-120fps. Màn hình 15.6 inch QHD IPS với tần số quét 165Hz, thời gian phản hồi 3ms, công nghệ G-Sync và 100% DCI-P3. Hệ thống tản nhiệt Aorus Engine với AI tự động điều chỉnh quạt và hiệu năng. Bàn phím Aorus Fusion RGB với anti-ghosting, switch phím 1.8mm. Touchpad Precision Touchpad. Webcam 1080p. Âm thanh DTS:X với 2 loa. Cổng kết nối: 3x USB-A 3.2, 1x USB-C (Thunderbolt 4), HDMI 2.1, LAN RJ45, jack tai nghe 3.5mm. Pin 99Whr dung lượng lớn. Khối lượng 2.0kg, độ dày 23.9mm.",
                "CPU: Intel Core i7-13700H, RAM: 16GB DDR5, Storage: 1TB SSD, GPU: NVIDIA GeForce RTX 4070 8GB, Screen: 15.6 inch QHD 165Hz", 4.6f, 20, 58);
        addProduct("13", "Razer Blade 15", 59990000, 65000000, "Razer", "Gaming",
                "Razer Blade 15 là biểu tượng của laptop gaming cao cấp với vỏ nhôm nguyên khối và thiết kế Legend 3 huyền thoại. Màu sắc Mercury White hoặc Matte Black tinh tế, logo Razer phát sáng RGB 3 vùng. Bộ xử lý Intel Core i7-13800H với 14 nhân 20 luồng, xung nhịp tối đa 5.2GHz. RAM 16GB DDR5 kênh đôi tốc độ 4800MHz. Ổ SSD 1TB PCIe Gen 4. Card đồ họa NVIDIA GeForce RTX 4070 8GB với DLSS 3 và ray tracing, chơi mượt mà 1440p. Màn hình 15.6 inch QHD IPS với tần số quét 240Hz, thời gian phản hồi 3ms, công nghệ G-Sync và 100% DCI-P3. Hệ thống tản nhiệt Vapor Chamber với quạt nén và ống dẫn nhiệt, giữ nhiệt độ thấp. Bàn phím Razer Opto-Mechanical với RGB Chroma, switch quang học 0.2ms. Touchpad Precision Touchpad. Webcam 1080p với Windows Hello. Âm thanh THX Spatial Audio với 2 loa. Cổng kết nối: 2x USB-A 3.2, 1x USB-C (Thunderbolt 4), HDMI 2.1, jack tai nghe 3.5mm. Pin 80.2Whr. Khối lượng 2.09kg, độ dày 19.9mm.",
                "CPU: Intel Core i7-13800H, RAM: 16GB DDR5, Storage: 1TB SSD, GPU: NVIDIA GeForce RTX 4070 8GB, Screen: 15.6 inch QHD 240Hz", 4.7f, 20, 44);
        addProduct("14", "Dell Alienware M16", 49990000, 55000000, "Dell", "Gaming",
                "Dell Alienware M16 là biểu tượng sức mạnh với hệ thống tản nhiệt Cryo-Tech tiên tiến và thiết kế Legend 3. Vỏ máy màu Lunar Light hoặc Dark Metallic Moon với logo Alienware phát sáng RGB. Bộ xử lý Intel Core i9-13900HX với 24 nhân 32 luồng, xung nhịp tối đa 5.4GHz. RAM 32GB DDR5 kênh đôi tốc độ 4800MHz. Ổ SSD 1TB PCIe Gen 4. Card đồ họa NVIDIA GeForce RTX 4070 8GB với DLSS 3, chơi mượt mà 1440p. Màn hình 16 inch QHD+ (2560x1600) với tần số quét 165Hz, thời gian phản hồi 3ms, công nghệ G-Sync và 100% DCI-P3. Hệ thống tản nhiệt với 4 quạt và dung dịch kim loại lỏng. Bàn phím CherryMX RGB với anti-ghosting. Touchpad Precision Touchpad. Webcam 1080p. Âm thanh Waves MaxxAudio với 2 loa. Cổng kết nối: 3x USB-A 3.2, 1x USB-C (Thunderbolt 4), HDMI 2.1, LAN RJ45, jack tai nghe 3.5mm. Pin 86Whr. Khối lượng 2.83kg.",
                "CPU: Intel Core i9-13900HX, RAM: 32GB DDR5, Storage: 1TB SSD, GPU: NVIDIA GeForce RTX 4070 8GB, Screen: 16 inch QHD+ 165Hz", 4.8f, 20, 37);

        // Mỏng nhẹ - 7 products
        addProduct("15", "Asus Zenbook S 13 OLED", 36990000, 39900000, "Asus", "Mỏng nhẹ",
                "Asus Zenbook S 13 OLED là laptop OLED mỏng nhất thế giới với thiết kế vỏ gốm plasma độc đáo và màn hình 3K rực rỡ. Trọng lượng chỉ 1.1kg, độ dày 10.9mm, dễ dàng bỏ vào túi xách nhỏ. Vỏ máy gốm plasma màu Ponder Blue hoặc Pebble Grey cao cấp, không bám vân tay. Bộ xử lý Intel Core Ultra 5 125H với 14 nhân 18 luồng, tích hợp NPU AI, mang lại hiệu năng vượt trội cho đa nhiệm và sáng tạo nội dung. RAM 32GB LPDDR5X kênh đôi tốc độ 6400MHz. Ổ SSD 1TB PCIe Gen 4. Màn hình 13.3 inch 3K OLED (2880x1800) với tỷ lệ 16:10, độ sáng 550 nits, tỷ lệ tương phản 1.000.000:1, màu sắc 100% DCI-P3 và công nghệ Dolby Vision. Card đồ họa Intel Arc Graphics với AI hỗ trợ. Bàn phím ZenBook chiclet với đèn nền LED, hành trình phím 1.4mm. Touchpad ASUS Precision Touchpad. Webcam 1080p với AI noise reduction. Âm thanh Harman Kardon với 4 loa và công nghệ Smart Amp. Cổng kết nối: 2x Thunderbolt 4 USB-C, HDMI 2.1. Pin 73Whr với thời lượng 13-15 tiếng. Đạt chuẩn độ bền quân đội MIL-STD-810H.",
                "CPU: Intel Core i7-1355U, RAM: 32GB LPDDR5, Storage: 1TB SSD, GPU: Intel Iris Xe Graphics, Screen: 13.3 inch 2.8K OLED", 4.7f, 20, 52);
        addProduct("16", "Dell XPS 13 Plus", 44990000, 48000000, "Dell", "Mỏng nhẹ",
                "Dell XPS 13 Plus mang đến trải nghiệm tương lai với thiết kế bàn phím tràn viền và trackpad vô hình InfinityEdge. Trọng lượng 1.26kg, độ dày 15.28mm. Vỏ máy nhôm nguyên khối màu Platinum Silver hoặc Graphite tinh tế. Bộ xử lý Intel Core i7-1360P với 12 nhân 16 luồng, hiệu năng vượt trội cho đa nhiệm văn phòng và sáng tạo. RAM 16GB LPDDR5 kênh đôi tốc độ 5200MHz. Ổ SSD 512GB PCIe Gen 4. Màn hình 13.4 inch 3.5K OLED (3456x2160) với tỷ lệ 16:10, độ sáng 400 nits, màu sắc 100% DCI-P3 và công nghệ Dolby Vision. Card đồ họa Intel Iris Xe Graphics. Bàn phím tràn viền với haptic feedback, không có phím vật lý truyền thống. Touchpad vô hình với haptic feedback. Webcam 1080p với AI. Âm thanh Waves Nx với 2 loa hướng lên. Cổng kết nối: 2x Thunderbolt 4 USB-C. Pin 55Whr với thời lượng 10-12 tiếng. Đạt chuẩn độ bền quân đội MIL-STD-810G.",
                "CPU: Intel Core i7-1360P, RAM: 16GB LPDDR5, Storage: 512GB SSD, GPU: Intel Iris Xe Graphics, Screen: 13.4 inch 3.5K OLED Touch", 4.8f, 20, 46);
        addProduct("17", "HP Spectre x360 14", 39900000, 42500000, "HP", "Mỏng nhẹ",
                "HP Spectre x360 14 là laptop 2-in-1 xoay gập 360 độ với thiết kế vát cắt kim cương tinh tế và bảo mật vân tay. Trọng lượng 1.37kg, độ dày 16.9mm. Vỏ máy magie nguyên khối màu Natural Silver hoặc Nightfall Black. Bộ xử lý Intel Core i7-1355U với 10 nhân 12 luồng, hiệu năng đủ dùng cho đa nhiệm văn phòng. RAM 16GB LPDDR4x kênh đôi tốc độ 4266MHz. Ổ SSD 1TB PCIe Gen 4. Màn hình 13.5 inch 3K2K OLED (3000x2000) với tỷ lệ 16:10, độ sáng 400 nits, màu sắc 100% DCI-P3. Card đồ họa Intel Iris Xe Graphics. Bàn phím HP Premium với đèn nền, hành trình phím 1.5mm. Touchpad Precision Touchpad. Webcam 1080p với Windows Hello. Âm thanh Bang & Olufsen với 4 loa. Cổng kết nối: 2x Thunderbolt 4 USB-C, HDMI 2.1. Pin 66Whr với thời lượng 10-12 tiếng. Chế độ tablet với bút HP MPP 2.0 đi kèm.",
                "CPU: Intel Core i7-1355U, RAM: 16GB LPDDR4x, Storage: 1TB SSD, GPU: Intel Iris Xe Graphics, Screen: 13.5 inch 3K2K OLED", 4.7f, 20, 41);
        addProduct("18", "Lenovo Yoga Slim 7i Carbon", 26990000, 36000000, "Lenovo", "Mỏng nhẹ",
                "Lenovo Yoga Slim 7i Carbon sở hữu vỏ Carbon siêu bền siêu nhẹ màu Cloud Grey tinh khôi, trọng lượng chỉ 1.35kg. Độ dày 14.9mm, dễ dàng bỏ vào balo mỏng. Bộ xử lý Intel Core i5-1340P với 12 nhân 16 luồng, hiệu năng vượt trội cho đa nhiệm. RAM 16GB LPDDR5 kênh đôi tốc độ 5200MHz. Ổ SSD 512GB PCIe Gen 4. Màn hình 13.3 inch 2.5K OLED (2560x1600) với tỷ lệ 16:10, tần số quét 90Hz, độ sáng 400 nits, màu sắc 100% DCI-P3. Card đồ họa Intel Iris Xe Graphics. Bàn phím AccuType với đèn nền LED, cảm giác gõ thoải mái. Touchpad Precision Touchpad. Webcam 1080p với AI. Âm thanh Dolby Atmos với 2 loa hướng lên. Cổng kết nối: 2x USB-C (Thunderbolt 4), HDMI 2.1. Pin 61Whr với thời lượng 10-12 tiếng. Đạt chuẩn độ bền quân đội MIL-STD-810H.",
                "CPU: Intel Core i5-1340P, RAM: 16GB LPDDR5, Storage: 512GB SSD, GPU: Intel Iris Xe Graphics, Screen: 13.3 inch 2.5K 90Hz", 4.5f, 20, 55);
        addProduct("19", "MacBook Air M2 13 inch", 24990000, 33000000, "Apple", "Mỏng nhẹ",
                "MacBook Air M2 13 inch với thiết kế phẳng mới và chip M2 mạnh mẽ, pin cả ngày dài. Trọng lượng 1.24kg, độ dày 11.3mm. Vỏ máy nhôm nguyên khối màu Midnight, Starlight, Silver hoặc Space Grey. Chip Apple M2 với CPU 8 nhân, GPU 8 nhân, Neural Engine 16 nhân, hiệu năng vượt trội cho mọi tác vụ. RAM 8GB Unified Memory. Ổ SSD 256GB với tốc độ siêu nhanh. Màn hình 13.6 inch Liquid Retina XDR với độ sáng 500 nits, màu sắc P3, tỷ lệ tương phản 1.000.000:1. Webcam 1080p với M2 Pro engine xử lý hình ảnh. Bàn phím Magic Keyboard với đèn nền, switch cánh bướm. Touchpad Force Touch. Âm thanh 4 loa với Spatial Audio. Cổng kết nối: 2x Thunderbolt/USB 4, jack tai nghe 3.5mm. Pin 52.6Whr với thời lượng lên đến 18 tiếng. Hệ sinh thái macOS hoàn hảo.",
                "CPU: Apple M2 (8-core CPU), RAM: 8GB Unified, Storage: 256GB SSD, GPU: Apple M2 (8-core GPU), Screen: 13.6 inch Liquid Retina", 4.8f, 20, 187);
        addProduct("20", "Acer Swift Go 14", 18990000, 25000000, "Acer", "Mỏng nhẹ",
                "Acer Swift Go 14 là laptop mỏng nhẹ với màn hình OLED rực rỡ và đầy đủ cổng kết nối. Trọng lượng 1.37kg, độ dày 15.9mm. Vỏ máy kim loại màu Pure Silver tinh tế. Bộ xử lý Intel Core i5-13500H với 12 nhân 16 luồng, hiệu năng vượt trội. RAM 16GB LPDDR5 kênh đôi tốc độ 4800MHz. Ổ SSD 512GB PCIe Gen 4. Màn hình 14 inch 2.8K OLED (2880x1800) với tần số quét 90Hz, độ sáng 400 nits, màu sắc 100% DCI-P3. Card đồ họa Intel Iris Xe Graphics. Bàn phím chiclet với đèn nền LED. Touchpad Precision Touchpad. Webcam 1080p. Âm thanh DTS Audio với 2 loa. Cổng kết nối: 2x USB-A 3.2, 1x USB-C (DisplayPort), HDMI 2.1, jack tai nghe 3.5mm. Pin 65Whr với thời lượng 10-12 tiếng. Đạt chuẩn độ bền quân đội MIL-STD-810H.",
                "CPU: Intel Core i5-13500H, RAM: 16GB LPDDR5, Storage: 512GB SSD, GPU: Intel Iris Xe Graphics, Screen: 14 inch 2.8K OLED 90Hz", 4.4f, 20, 64);
        addProduct("21", "LG Gram SuperSlim", 31990000, 35000000, "LG", "Mỏng nhẹ",
                "LG Gram SuperSlim là laptop siêu mỏng với thiết kế mỏng như quyển tạp chí và màn hình OLED chống chói. Trọng lượng 1.12kg, độ dày 10.9mm. Vỏ máy magie nguyên khối màu White hoặc Dark Silver. Bộ xử lý Intel Core i5-1340P với 12 nhân 16 luồng. RAM 16GB LPDDR5 kênh đôi tốc độ 5200MHz. Ổ SSD 512GB PCIe Gen 4. Màn hình 15.6 inch FHD OLED với độ sáng 350 nits, tỷ lệ tương phản 1.000.000:1, màu sắc 99% DCI-P3. Card đồ họa Intel Iris Xe Graphics. Bàn phím chiclet với đèn nền LED. Touchpad Precision Touchpad. Webcam 1080p với AI. Âm thanh DTS:X với 2 loa. Cổng kết nối: 2x USB-C (Thunderbolt 4), HDMI 2.1. Pin 80Whr với thời lượng 20-22 tiếng. Đạt chuẩn độ bền quân đội MIL-STD-810G và xếp hạng IP52 chống bụi nước.",
                "CPU: Intel Core i5-1340P, RAM: 16GB LPDDR5, Storage: 512GB SSD, GPU: Intel Iris Xe Graphics, Screen: 15.6 inch FHD OLED", 4.6f, 20, 39);

        // Sinh viên - 7 products
        addProduct("22", "Asus Vivobook 16", 10990000, 12500000, "Asus", "Sinh viên",
                "Asus Vivobook 16 là lựa chọn hoàn hảo cho sinh viên với màn hình lớn 16 inch làm việc đa nhiệm mượt mà và giá thành phải chăng. Thiết kế vỏ nhựa ABS màu Quiet Blue hoặc Indie Black đơn giản, phù hợp môi trường học đường. Bộ xử lý Intel Core i3-1315U với 6 nhân 8 luồng, đủ mạnh cho các tác vụ học tập như soạn thảo luận văn, trình chiếu, học online. RAM 8GB DDR4 kênh đơn, có thể nâng cấp lên 32GB. Ổ SSD 256GB NVMe PCIe Gen 3 cho tốc độ khởi động nhanh. Màn hình 16 inch WUXGA (1920x1200) với tỷ lệ 16:10 hiện đại hơn, độ sáng 300 nits, góc nhìn rộng 178 độ. Card đồ họa Intel UHD Graphics xử lý tốt các tác vụ cơ bản và một số phần mềm học tập. Bàn phím full-size với hành trình phím 1.5mm thoải mái. Touchpad ASUS Precision Touchpad với driver Microsoft. Webcam HD 720p với công nghệ 3DNR giảm noise. Âm thanh SonicMaster với 2 loa. Cổng kết nối: 1x USB-A 3.2, 1x USB-C 3.2, HDMI 1.4, jack tai nghe 3.5mm, khe thẻ SD. Pin 50Whr với thời lượng 6-7 tiếng học tập. Khối lượng 1.88kg phù hợp mang theo hàng ngày.",
                "CPU: Intel Core i3-1315U, RAM: 8GB DDR4, Storage: 256GB SSD, GPU: Intel UHD Graphics, Screen: 16 inch WUXGA IPS", 4.0f, 20, 127);
        addProduct("23", "Dell Vostro 3520", 12500000, 14200000, "Dell", "Sinh viên",
                "Dell Vostro 3520 là laptop bền bỉ cho sinh viên với thiết kế chắc chắn và tản nhiệt hiệu quả, hỗ trợ đầy đủ cổng kết nối cho học tập. Vỏ máy nhựa ABS màu Black với các cạnh viền bo tròn, logo Dell tinh tế. Bộ xử lý Intel Core i5-1235U với 10 nhân 12 luồng, hiệu năng ổn định cho đa nhiệm học tập. RAM 8GB DDR4 kênh đôi, có thể nâng cấp. Ổ SSD 512GB NVMe PCIe Gen 3. Màn hình 15.6 inch FHD IPS với độ sáng 220 nits, công nghệ Anti-Glare chống chói. Card đồ họa Intel UHD Graphics. Hệ thống tản nhiệt với 2 quạt và ống dẫn nhiệt, giữ máy luôn mát mẻ. Bàn phím chiclet với hành trình phím 1.7mm, đèn nền LED. Touchpad Precision Touchpad. Webcam HD 720p. Âm thanh Waves MaxxAudio với 2 loa. Cổng kết nối: 2x USB-A 3.2, 1x USB-C 3.2, HDMI 1.4, LAN RJ45, jack tai nghe 3.5mm, khe thẻ SD. Pin 41Whr với thời lượng 5-6 tiếng. Khối lượng 1.91kg.",
                "CPU: Intel Core i5-1235U, RAM: 8GB DDR4, Storage: 512GB SSD, GPU: Intel UHD Graphics, Screen: 15.6 inch FHD 120Hz", 4.1f, 20, 98);
        addProduct("24", "HP 15s-fq5000", 9990000, 14500000, "HP", "Sinh viên",
                "HP 15s-fq5000 mang đến thiết kế bo tròn mềm mại và phím bấm êm ái cho trải nghiệm học tập thoải mái. Vỏ máy nhựa ABS màu Natural Silver hoặc Jet Black. Bộ xử lý Intel Core i3-1215U với 6 nhân 8 luồng, đủ dùng cho Word, Excel, PowerPoint và lướt web. RAM 8GB DDR4 kênh đơn. Ổ SSD 256GB PCIe Gen 3. Màn hình 15.6 inch FHD TN với độ sáng 250 nits. Card đồ họa Intel UHD Graphics. Bàn phím full-size với hành trình phím 1.5mm êm ái. Touchpad Precision Touchpad. Webcam HD 720p. Âm thanh B&O với 2 loa. Cổng kết nối: 2x USB-A 3.2, 1x USB-C 3.2, HDMI 1.4, jack tai nghe 3.5mm. Pin 41Whr với thời lượng 6-7 tiếng. Khối lượng 1.69kg, dễ dàng bỏ vào balo.",
                "CPU: Intel Core i3-1215U, RAM: 8GB DDR4, Storage: 256GB SSD, GPU: Intel UHD Graphics, Screen: 15.6 inch FHD", 3.9f, 20, 134);
        addProduct("25", "Acer Aspire 3", 8490000, 12000000, "Acer", "Sinh viên",
                "Acer Aspire 3 là laptop giá rẻ cho sinh viên với cấu hình đủ dùng cho các tác vụ học tập cơ bản như Word, Excel và lướt web. Thiết kế vỏ nhựa màu Silver hoặc Black đơn giản. Bộ xử lý AMD Ryzen 3 7320U với 4 nhân 8 luồng. RAM 8GB LPDDR5 kênh đơn. Ổ SSD 256GB PCIe Gen 3. Màn hình 15.6 inch FHD TN với độ sáng 220 nits. Card đồ họa AMD Radeon Graphics. Bàn phím chiclet với hành trình phím 1.5mm. Touchpad Precision Touchpad. Webcam HD 720p. Âm thanh DTS Audio với 2 loa. Cổng kết nối: 2x USB-A 3.2, 1x USB-C 3.2, HDMI 1.4, jack tai nghe 3.5mm. Pin 37Whr với thời lượng 5-6 tiếng. Khối lượng 1.7kg.",
                "CPU: AMD Ryzen 3 7320U, RAM: 8GB LPDDR5, Storage: 256GB SSD, GPU: AMD Radeon Graphics, Screen: 15.6 inch FHD", 3.8f, 20, 156);
        addProduct("26", "Lenovo V15 G4", 9200000, 10500000, "Lenovo", "Sinh viên",
                "Lenovo V15 G4 có thiết kế thực dụng với bản lề mở rộng tiện lợi cho việc chia sẻ nội dung trong nhóm học tập. Vỏ máy nhựa ABS màu Iron Grey. Bộ xử lý AMD Ryzen 3 7320U với 4 nhân 8 luồng. RAM 8GB LPDDR5 kênh đơn. Ổ SSD 256GB PCIe Gen 3. Màn hình 15.6 inch FHD TN với độ sáng 250 nits. Card đồ họa AMD Radeon Graphics. Bàn phím AccuType với hành trình phím 1.5mm. Touchpad Precision Touchpad. Webcam HD 720p. Âm thanh Dolby Audio với 2 loa. Cổng kết nối: 2x USB-A 3.2, 1x USB-C 3.2, HDMI 1.4, jack tai nghe 3.5mm. Pin 38Whr với thời lượng 5-6 tiếng. Khối lượng 1.7kg.",
                "CPU: AMD Ryzen 3 7320U, RAM: 8GB LPDDR5, Storage: 256GB SSD, GPU: AMD Radeon Graphics, Screen: 15.6 inch FHD TN", 3.9f, 20, 142);
        addProduct("27", "MSI Modern 15 B13M", 13500000, 15000000, "MSI", "Sinh viên",
                "MSI Modern 15 B13M mang đến cấu hình cao cấp trong tầm giá cho sinh viên vừa học vừa giải trí nhẹ. Thiết kế vỏ nhựa giả kim loại màu Classic Black tinh tế. Bộ xử lý Intel Core i5-1335U với 10 nhân 12 luồng, hiệu năng vượt trội. RAM 16GB DDR4 kênh đôi. Ổ SSD 512GB NVMe PCIe Gen 3. Màn hình 15.6 inch FHD IPS với độ sáng 300 nits, góc nhìn rộng 178 độ. Card đồ họa Intel Iris Xe Graphics xử lý tốt các tác vụ học tập và một số game nhẹ. Bàn phím chiclet với đèn nền LED. Touchpad Precision Touchpad. Webcam HD 720p. Âm thanh Nahimic với 2 loa. Cổng kết nối: 2x USB-A 3.2, 1x USB-C 3.2, HDMI 1.4, jack tai nghe 3.5mm. Pin 52.4Whr với thời lượng 8-9 tiếng. Khối lượng 1.7kg.",
                "CPU: Intel Core i5-1335U, RAM: 16GB DDR4, Storage: 512GB SSD, GPU: Intel Iris Xe Graphics, Screen: 15.6 inch FHD IPS", 4.2f, 20, 89);
        addProduct("28", "Gigabyte G5 MF", 18990000, 20900000, "Gigabyte", "Sinh viên",
                "Gigabyte G5 MF là laptop gaming giá rẻ nhất cho sinh viên vừa học tập vừa chơi game giải trí. Thiết kế gaming với vỏ nhựa màu Matte Black, logo Gigabyte phát sáng. Bộ xử lý Intel Core i5-12500H với 12 nhân 16 luồng. RAM 8GB DDR4 kênh đôi, có thể nâng cấp. Ổ SSD 512GB NVMe PCIe Gen 3. Card đồ họa NVIDIA GeForce RTX 4050 6GB với DLSS 3, chơi mượt mà các game AAA ở 1080p. Màn hình 15.6 inch FHD IPS với tần số quét 144Hz, thời gian phản hồi 3ms. Bàn phím RGB 4 vùng với anti-ghosting. Touchpad Precision Touchpad. Webcam HD 720p. Âm thanh DTS:X với 2 loa. Cổng kết nối: 3x USB-A 3.2, 1x USB-C 3.2, HDMI 2.1, LAN RJ45, jack tai nghe 3.5mm. Pin 54Whr. Khối lượng 2.1kg.",
                "CPU: Intel Core i5-12500H, RAM: 8GB DDR4, Storage: 512GB SSD, GPU: NVIDIA GeForce RTX 4050 6GB, Screen: 15.6 inch FHD 144Hz", 4.3f, 20, 76);

        // Cảm ứng - 7 products
        addProduct("29", "HP Envy x360 13", 21900000, 23500000, "HP", "Cảm ứng",
                "HP Envy x360 13 là laptop 2-in-1 xoay gập 360 độ hoàn hảo cho người dùng cần sự linh hoạt tối đa giữa laptop và tablet. Thiết kế vỏ magie nguyên khối màu Natural Silver hoặc Nightfall Black tinh tế với các cạnh viền bo tròn mềm mại, logo HP phát sáng nhẹ nhàng. Bộ xử lý Intel Core i5-1230U với 10 nhân 12 luồng, hiệu năng đủ dùng cho đa nhiệm văn phòng, chỉnh sửa ảnh cơ bản và lướt web mượt mà. RAM 8GB LPDDR4x kênh đôi tốc độ 4266MHz đảm bảo không bị lag khi mở nhiều ứng dụng cùng lúc. Ổ SSD 512GB PCIe Gen 3 cho tốc độ khởi động nhanh chóng. Màn hình 13.3 inch FHD IPS cảm ứng với độ sáng 400 nits, góc nhìn rộng 178 độ, hỗ trợ bút stylus HP Tilt Pen đi kèm (có thể nghiêng để tạo hiệu ứng đổ bóng tự nhiên). Card đồ họa Intel Iris Xe Graphics tích hợp xử lý tốt các tác vụ văn phòng và một số phần mềm thiết kế nhẹ. Bàn phím HP Premium với đèn nền LED, hành trình phím 1.3mm thoải mái cho việc nhập liệu dài. Touchpad Precision Touchpad với driver Microsoft hỗ trợ đa điểm cảm ứng mượt mà. Webcam 1080p với Windows Hello nhận diện khuôn mặt. Âm thanh Bang & Olufsen với 4 loa hướng lên trên, âm thanh sống động và bass sâu. Cổng kết nối: 2x Thunderbolt 4 USB-C (hỗ trợ sạc và DisplayPort), HDMI 2.1. Pin 66Whr với thời lượng 9-10 tiếng sử dụng văn phòng. Khối lượng 1.3kg, độ dày 15.4mm. Chế độ tablet xoay gập 360 độ tiện lợi cho việc vẽ, ghi chú hay giải trí.",
                "CPU: Intel Core i5-1230U, RAM: 8GB LPDDR4x, Storage: 512GB SSD, GPU: Intel Iris Xe Graphics, Screen: 13.3 inch FHD IPS Touch", 4.4f, 20, 67);
        addProduct("30", "Dell Inspiron 14 7430 2-in-1", 19500000, 21000000, "Dell", "Cảm ứng",
                "Dell Inspiron 14 7430 2-in-1 biến hình linh hoạt giữa laptop và tablet với màn hình cảm ứng tỷ lệ 16:10 hiện đại, mang đến trải nghiệm làm việc và giải trí đa dạng. Vỏ máy nhôm nguyên khối màu Platinum Silver tinh tế, các đường nét góc cạnh được bo tròn hợp lý tạo cảm giác cầm nắm chắc chắn. Bộ xử lý Intel Core i5-1335U với 10 nhân 12 luồng, sức mạnh vượt trội cho đa nhiệm văn phòng, chỉnh sửa video cơ bản và gaming nhẹ. RAM 8GB LPDDR5 kênh đôi tốc độ 4800MHz cho tốc độ xử lý siêu nhanh. Ổ SSD 512GB PCIe Gen 4 với tốc độ đọc lên đến 7000MB/s. Màn hình 14 inch FHD+ cảm ứng (1920x1200) với tỷ lệ 16:10 hiện đại hơn, độ sáng 300 nits, góc nhìn rộng 178 độ, viền màn hình mỏng chỉ 7.15mm ở 3 cạnh. Card đồ họa Intel Iris Xe Graphics xử lý mượt mà các tác vụ hàng ngày và một số phần mềm sáng tạo. Bàn phím chiclet với đèn nền LED trắng, hành trình phím 1.5mm thoải mái. Touchpad Precision Touchpad với driver Microsoft. Webcam HD 720p với công nghệ Temporal Noise Reduction. Âm thanh MaxxAudio với 2 loa hướng lên trên, âm thanh to rõ và sống động. Cổng kết nối: 2x USB-A 3.2, 1x USB-C 3.2 (hỗ trợ DisplayPort), HDMI 1.4, jack tai nghe 3.5mm, khe thẻ SD. Pin 54Whr với thời lượng 8-9 tiếng. Khối lượng 1.58kg. Chế độ tablet xoay gập 360 độ với bút cảm ứng tích hợp trong thân máy.",
                "CPU: Intel Core i5-1335U, RAM: 8GB LPDDR5, Storage: 512GB SSD, GPU: Intel Iris Xe Graphics, Screen: 14 inch FHD+ Touch", 4.3f, 20, 59);
        addProduct("31", "Asus Vivobook S 14 Flip", 17200000, 18900000, "Asus", "Cảm ứng",
                "Asus Vivobook S 14 Flip năng động với thiết kế xoay gập 360 độ và màn hình cảm ứng tỷ lệ 16:10, kết hợp hoàn hảo giữa hiệu năng và tính linh hoạt cho người dùng sáng tạo. Thiết kế vỏ kim loại màu Indie Black hoặc Cool Silver hiện đại, các cạnh viền được bo tròn tinh tế. Bộ xử lý Intel Core i5-13500H với 12 nhân 16 luồng, mang lại sức mạnh vượt trội cho đa nhiệm nặng, chỉnh sửa ảnh/video và gaming nhẹ. RAM 16GB DDR4 kênh đôi tốc độ 3200MHz. Ổ SSD 512GB PCIe Gen 4 với tốc độ siêu nhanh. Màn hình 14 inch WUXGA cảm ứng (1920x1200) với tỷ lệ 16:10 hiện đại, độ sáng 300 nits, góc nhìn rộng 178 độ, viền màn hình siêu mỏng. Card đồ họa Intel Iris Xe Graphics xử lý tốt các tác vụ sáng tạo và giải trí. Bàn phím ErgoSense với đèn nền LED, hành trình phím 1.4mm êm ái. Touchpad ASUS Precision Touchpad với driver Microsoft. Webcam 1080p với AI noise reduction. Âm thanh SonicMaster với 2 loa hướng lên trên. Cổng kết nối: 2x USB-A 3.2, 1x USB-C 3.2 (hỗ trợ DisplayPort), HDMI 1.4, jack tai nghe 3.5mm. Pin 75Whr dung lượng lớn với thời lượng 8-9 tiếng. Khối lượng 1.5kg. Chế độ tablet với bút ASUS Pen đi kèm, hỗ trợ 4096 mức lực nhấn.",
                "CPU: Intel Core i5-13500H, RAM: 16GB DDR4, Storage: 512GB SSD, GPU: Intel Iris Xe Graphics, Screen: 14 inch WUXGA Touch", 4.2f, 20, 71);
        addProduct("32", "Lenovo Yoga 7i", 23500000, 25000000, "Lenovo", "Cảm ứng",
                "Lenovo Yoga 7i sở hữu thiết kế bo tròn cầm nắm thoải mái như đang ôm trong lòng bàn tay, kết hợp với màn hình OLED cảm ứng rực rỡ tỷ lệ 16:10 cho trải nghiệm thị giác tuyệt đỉnh. Vỏ máy magie nguyên khối màu Arctic Grey tinh tế, logo Yoga ẩn tinh tế. Bộ xử lý Intel Core i5-1340P với 12 nhân 16 luồng, hiệu năng vượt trội cho đa nhiệm văn phòng và sáng tạo nội dung. RAM 16GB LPDDR5 kênh đôi tốc độ 5200MHz. Ổ SSD 512GB PCIe Gen 4. Màn hình 14 inch 2.8K OLED cảm ứng (2880x1800) với tỷ lệ 16:10 hiện đại, độ sáng 400 nits, tỷ lệ tương phản 1.000.000:1, màu sắc 100% DCI-P3, góc nhìn rộng 178 độ. Card đồ họa Intel Iris Xe Graphics. Bàn phím AccuType với đèn nền LED, cảm giác gõ thoải mái và yên tĩnh. Touchpad Precision Touchpad. Webcam 1080p với AI. Âm thanh Dolby Atmos với 4 loa hướng lên trên, âm thanh vòm sống động. Cổng kết nối: 2x USB-C (Thunderbolt 4), HDMI 2.1. Pin 71Whr với thời lượng 10-11 tiếng. Khối lượng 1.43kg, độ dày 16.9mm. Chế độ tablet xoay gập 360 độ với bút Lenovo Pen Plus đi kèm.",
                "CPU: Intel Core i5-1340P, RAM: 16GB LPDDR5, Storage: 512GB SSD, GPU: Intel Iris Xe Graphics, Screen: 14 inch 2.8K OLED Touch", 4.5f, 20, 54);
        addProduct("33", "MSI Summit E13 Flip Evo", 28900000, 31000000, "MSI", "Cảm ứng",
                "MSI Summit E13 Flip Evo là dòng doanh nhân cao cấp với bảo mật phần cứng TPM 2.0, bút MSI Pen và thiết kế xoay gập 360 độ cho sự linh hoạt tối đa. Thiết kế vỏ magie nguyên khối màu Star Blue tinh tế, logo MSI ẩn tinh tế. Bộ xử lý Intel Core i7-1360P với 12 nhân 16 luồng, sức mạnh vượt trội cho công việc chuyên nghiệp. RAM 16GB LPDDR5 kênh đôi tốc độ 5200MHz. Ổ SSD 1TB PCIe Gen 4. Màn hình 13.4 inch FHD+ cảm ứng với tần số quét 120Hz, độ sáng 400 nits, góc nhìn rộng 178 độ, viền màn hình siêu mỏng. Card đồ họa Intel Iris Xe Graphics. Bàn phím backlit với đèn nền LED, hành trình phím 1.5mm. Touchpad Precision Touchpad. Webcam 1080p với IR camera hỗ trợ Windows Hello. Âm thanh Nahimic với 2 loa hướng lên trên. Cổng kết nối: 2x Thunderbolt 4 USB-C, HDMI 2.1. Pin 75Whr với thời lượng 10-11 tiếng. Bảo mật vân tay và Windows Hello. Khối lượng 1.35kg. Chế độ tablet với bút cảm ứng hỗ trợ 4096 mức lực nhấn.",
                "CPU: Intel Core i7-1360P, RAM: 16GB LPDDR5, Storage: 1TB SSD, GPU: Intel Iris Xe Graphics, Screen: 13.4 inch FHD+ 120Hz Touch", 4.6f, 20, 42);
        addProduct("34", "Acer Spin 5", 24000000, 26500000, "Acer", "Cảm ứng",
                "Acer Spin 5 tích hợp bút Stylus trong thân máy với thiết kế xoay gập 360 độ, màn hình cảm ứng độ phân giải cao và viền màn hình siêu mỏng cho trải nghiệm hiện đại. Vỏ máy kim loại màu Steel Gray tinh tế, các đường nét góc cạnh được bo tròn hợp lý. Bộ xử lý Intel Core i5-1240P với 12 nhân 16 luồng, hiệu năng vượt trội cho đa nhiệm và sáng tạo. RAM 16GB LPDDR5 kênh đôi tốc độ 4800MHz. Ổ SSD 512GB PCIe Gen 4. Màn hình 14 inch WQXGA cảm ứng (2560x1600) với tỷ lệ 16:10, độ sáng 400 nits, góc nhìn rộng 178 độ, viền màn hình mỏng chỉ 7.15mm. Card đồ họa Intel Iris Xe Graphics. Bàn phím chiclet với đèn nền LED. Touchpad Precision Touchpad. Webcam 1080p. Âm thanh DTS Audio với 2 loa hướng lên trên. Cổng kết nối: 2x USB-A 3.2, 1x USB-C 3.2 (hỗ trợ DisplayPort), HDMI 2.1. Pin 56Whr với thời lượng 8-9 tiếng. Bút Acer Active Stylus đi kèm với 4096 mức lực nhấn. Khối lượng 1.5kg.",
                "CPU: Intel Core i5-1240P, RAM: 16GB LPDDR5, Storage: 512GB SSD, GPU: Intel Iris Xe Graphics, Screen: 14 inch WQXGA Touch", 4.4f, 20, 49);
        addProduct("35", "LG Gram 2-in-1 16", 34990000, 38000000, "LG", "Cảm ứng",
                "LG Gram 2-in-1 16 là máy tính lai nhẹ nhất thế giới ở kích thước 16 inch với màn hình WQXGA cảm ứng, mang đến sự cân bằng hoàn hảo giữa sức mạnh và tính di động. Vỏ máy magie nguyên khối màu Dark Silver cao cấp, các cạnh viền bo tròn tinh tế. Bộ xử lý Intel Core i7-1360P với 12 nhân 16 luồng, hiệu năng vượt trội cho công việc chuyên nghiệp. RAM 16GB LPDDR5 kênh đôi tốc độ 5200MHz. Ổ SSD 512GB PCIe Gen 4. Màn hình 16 inch WQXGA cảm ứng (2560x1600) với tỷ lệ 16:10, độ sáng 350 nits, công nghệ Dolby Vision IQ tự động điều chỉnh màu sắc theo môi trường, viền màn hình siêu mỏng. Card đồ họa Intel Iris Xe Graphics. Bàn phím chiclet với đèn nền LED. Touchpad Precision Touchpad. Webcam 1080p. Âm thanh DTS:X với 2 loa hướng lên trên. Cổng kết nối: 2x USB-C (Thunderbolt 4), HDMI 2.1. Pin 80Whr với thời lượng 20-22 tiếng. Khối lượng 1.45kg, độ dày 16.8mm. Chế độ tablet xoay gập 360 độ với bút cảm ứng tích hợp.",
                "CPU: Intel Core i7-1360P, RAM: 16GB LPDDR5, Storage: 512GB SSD, GPU: Intel Iris Xe Graphics, Screen: 16 inch WQXGA IPS Touch", 4.7f, 20, 35);

        // Laptop AI - 7 products
        addProduct("36", "Asus Zenbook 14 OLED AI", 26990000, 28990000, "Asus", "Laptop AI",
                "Asus Zenbook 14 OLED AI là laptop đầu tiên trên thế giới trang bị chip Intel Core Ultra thế hệ 14 với Neural Processing Unit (NPU) chuyên dụng cho trí tuệ nhân tạo, mang đến trải nghiệm AI thực sự. Thiết kế vỏ kim loại nguyên khối màu Ponder Blue tinh tế với các đường nét góc cạnh được bo tròn mềm mại, logo Asus ẩn tinh tế. Bộ xử lý Intel Core Ultra 5 125H với 14 nhân 18 luồng tích hợp NPU 45 TOPS, mang lại hiệu năng vượt trội cho các tác vụ AI như nhận diện giọng nói, xử lý hình ảnh thông minh, và hỗ trợ Windows Copilot. RAM 16GB LPDDR5X kênh đôi tốc độ 6400MHz cho đa nhiệm siêu mượt. Ổ SSD 512GB PCIe Gen 4 với tốc độ đọc lên đến 7000MB/s. Màn hình 14 inch 3K OLED (2880x1800) với tỷ lệ 16:10 hiện đại, độ sáng 550 nits, tỷ lệ tương phản 1.000.000:1, màu sắc 100% DCI-P3 và công nghệ Dolby Vision. Card đồ họa Intel Arc Graphics với AI acceleration, webcam 1080p với AI noise reduction. Bàn phím ZenBook chiclet với đèn nền LED, hành trình phím 1.4mm êm ái. Touchpad ASUS Precision Touchpad. Âm thanh Harman Kardon với 6 loa và công nghệ Smart Amp. Cổng kết nối: 2x Thunderbolt 4 USB-C, HDMI 2.1. Pin 75Whr với thời lượng 10-12 tiếng. Đạt chuẩn độ bền quân đội MIL-STD-810H.",
                "CPU: Intel Core Ultra 5 125H, RAM: 16GB LPDDR5X, Storage: 512GB SSD, GPU: Intel Arc Graphics (NPU AI), Screen: 14 inch 3K OLED 120Hz", 4.6f, 20, 62);
        addProduct("37", "MSI Prestige 16 AI Evo", 31900000, 34000000, "MSI", "Laptop AI",
                "MSI Prestige 16 AI Evo là laptop doanh nhân cao cấp với chip Intel Core Ultra thế hệ mới và pin dung lượng khủng 99.9Whr, mang đến trải nghiệm AI thông minh và thời lượng sử dụng vượt trội. Thiết kế vỏ magie nguyên khối màu Star Blue tinh tế với các đường nét góc cạnh hiện đại, logo MSI ẩn tinh tế. Bộ xử lý Intel Core Ultra 7 155H với 14 nhân 22 luồng và NPU 45 TOPS, hiệu năng vượt trội cho đa nhiệm văn phòng và sáng tạo nội dung với sự hỗ trợ của AI. RAM 32GB LPDDR5 kênh đôi tốc độ 6400MHz đảm bảo không bị bottleneck. Ổ SSD 1TB PCIe Gen 4 với tốc độ siêu nhanh. Màn hình 16 inch QHD+ IPS (2560x1600) với tỷ lệ 16:10, độ sáng 400 nits, góc nhìn rộng 178 độ, công nghệ Adaptive-Sync và 100% DCI-P3. Card đồ họa Intel Arc Graphics với AI acceleration. Bàn phím chiclet với đèn nền LED, hành trình phím 1.5mm thoải mái. Touchpad Precision Touchpad với driver Microsoft. Webcam 1080p với AI. Âm thanh Nahimic với 2 loa hướng lên trên. Cổng kết nối: 2x Thunderbolt 4 USB-C, HDMI 2.1, LAN RJ45. Pin 99.9Whr dung lượng cực lớn với thời lượng lên đến 20-22 tiếng sử dụng văn phòng. Bảo mật vân tay và Windows Hello. Khối lượng 1.9kg.",
                "CPU: Intel Core Ultra 7 155H, RAM: 32GB LPDDR5, Storage: 1TB SSD, GPU: Intel Arc Graphics, Screen: 16 inch QHD+ IPS", 4.7f, 20, 48);
        addProduct("38", "Acer Swift Go 14 AI", 22990000, 24500000, "Acer", "Laptop AI",
                "Acer Swift Go 14 AI là laptop mỏng nhẹ với chip Intel Core Ultra thế hệ mới và phím Copilot riêng biệt, tối ưu hóa cho trải nghiệm AI cá nhân hóa. Thiết kế vỏ kim loại nguyên khối màu Steel Gray tinh tế với các đường nét góc cạnh được bo tròn hợp lý, logo Acer ẩn tinh tế. Bộ xử lý Intel Core Ultra 5 125H với 14 nhân 18 luồng và NPU 45 TOPS, mang lại hiệu năng vượt trội cho các tác vụ AI như nhận diện khuôn mặt, xử lý ngôn ngữ tự nhiên và hỗ trợ Copilot. RAM 16GB LPDDR5X kênh đôi tốc độ 6400MHz. Ổ SSD 512GB PCIe Gen 4. Màn hình 14 inch 2.8K OLED (2880x1800) với tỷ lệ 16:10, độ sáng 400 nits, tỷ lệ tương phản 1.000.000:1, màu sắc 100% DCI-P3. Card đồ họa Intel Arc Graphics với AI acceleration. Bàn phím chiclet với phím Copilot riêng biệt và đèn nền LED. Touchpad Precision Touchpad. Webcam 1080p với AI noise reduction. Âm thanh DTS Audio với 2 loa. Cổng kết nối: 2x USB-A 3.2, 1x USB-C (DisplayPort), HDMI 2.1. Pin 65Whr với thời lượng 10-12 tiếng. Khối lượng 1.37kg, độ dày 15.9mm.",
                "CPU: Intel Core Ultra 5 125H, RAM: 16GB LPDDR5X, Storage: 512GB SSD, GPU: Intel Arc Graphics, Screen: 14 inch 2.8K OLED", 4.5f, 20, 71);
        addProduct("39", "Lenovo Yoga 9i 2-in-1", 38900000, 41900000, "Lenovo", "Laptop AI",
                "Lenovo Yoga 9i 2-in-1 là laptop 2-in-1 cao cấp với thiết kế như trang sức, chip Intel Core Ultra thế hệ mới và màn hình OLED 4K cảm ứng, mang đến trải nghiệm AI sáng tạo và linh hoạt tối đa. Thiết kế vỏ magie nguyên khối màu Storm Grey tinh tế với các đường nét bo tròn mềm mại như đang cầm trên tay, logo Yoga ẩn tinh tế. Bộ xử lý Intel Core Ultra 7 155H với 14 nhân 22 luồng và NPU 45 TOPS, hiệu năng vượt trội cho đa nhiệm văn phòng và sáng tạo nội dung với sự hỗ trợ của AI. RAM 16GB LPDDR5X kênh đôi tốc độ 6400MHz. Ổ SSD 1TB PCIe Gen 4. Màn hình 14 inch 4K OLED cảm ứng (3840x2400) với tỷ lệ 16:10, độ sáng 400 nits, tỷ lệ tương phản 1.000.000:1, màu sắc 100% DCI-P3 và công nghệ Dolby Vision. Card đồ họa Intel Arc Graphics với AI acceleration. Bàn phím AccuType với đèn nền LED, cảm giác gõ thoải mái. Touchpad Precision Touchpad. Webcam 1080p với AI. Âm thanh Dolby Atmos với 4 loa. Cổng kết nối: 2x Thunderbolt 4 USB-C. Pin 75Whr với thời lượng 10-12 tiếng. Bút Lenovo Pen Plus đi kèm với 4096 mức lực nhấn. Khối lượng 1.4kg.",
                "CPU: Intel Core Ultra 7 155H, RAM: 16GB LPDDR5X, Storage: 1TB SSD, GPU: Intel Arc Graphics, Screen: 14 inch 4K OLED Touch", 4.8f, 20, 39);
        addProduct("40", "HP Omen Transcend 14", 41500000, 45000000, "HP", "Laptop AI",
                "HP Omen Transcend 14 là laptop gaming AI nhẹ nhất thế giới với chip Intel Core Ultra thế hệ mới và hệ thống tản nhiệt AI thông minh, mang đến sức mạnh gaming và AI trong thân hình siêu mỏng. Thiết kế vỏ magie nguyên khối màu Shadow Black với các đường nét góc cạnh hầm hố, logo Omen phát sáng RGB. Bộ xử lý Intel Core Ultra 7 155H với 14 nhân 22 luồng và NPU 45 TOPS, hiệu năng vượt trội cho gaming và sáng tạo nội dung. RAM 16GB LPDDR5X kênh đôi tốc độ 6400MHz. Ổ SSD 1TB PCIe Gen 4. Card đồ họa NVIDIA GeForce RTX 4060 8GB với DLSS 3, ray tracing và AI acceleration. Màn hình 14 inch 2.8K OLED (2880x1800) với tần số quét 120Hz, độ sáng 500 nits, tỷ lệ tương phản 1.000.000:1, màu sắc 100% DCI-P3. Hệ thống tản nhiệt AI tự động điều chỉnh quạt và hiệu năng. Bàn phím chiclet với đèn nền RGB 4 vùng. Touchpad Precision Touchpad. Webcam 1080p với AI. Âm thanh Bang & Olufsen với 4 loa. Cổng kết nối: 2x Thunderbolt 4 USB-C, HDMI 2.1. Pin 73Whr với thời lượng 8-10 tiếng. Khối lượng 1.78kg, độ dày 19.9mm.",
                "CPU: Intel Core Ultra 7 155H, RAM: 16GB LPDDR5X, Storage: 1TB SSD, GPU: NVIDIA GeForce RTX 4060 8GB, Screen: 14 inch 2.8K OLED 120Hz", 4.7f, 20, 34);
        addProduct("41", "Dell XPS 14 (9440)", 49900000, 52000000, "Dell", "Laptop AI",
                "Dell XPS 14 (9440) là laptop siêu mỏng với chip Intel Core Ultra thế hệ mới và thiết kế nhôm nguyên khối InfinityEdge, mang đến trải nghiệm AI thông minh trong thân hình di động. Thiết kế vỏ nhôm nguyên khối màu Platinum Silver tinh tế với các đường nét góc cạnh được bo tròn mềm mại, viền màn hình siêu mỏng chỉ 5.7mm. Bộ xử lý Intel Core Ultra 7 155H với 14 nhân 22 luồng và NPU 45 TOPS, hiệu năng vượt trội cho đa nhiệm văn phòng và sáng tạo nội dung với sự hỗ trợ của AI. RAM 16GB LPDDR5X kênh đôi tốc độ 6400MHz. Ổ SSD 512GB PCIe Gen 4. Card đồ họa NVIDIA GeForce RTX 4050 6GB với DLSS 3 và AI acceleration. Màn hình 14.5 inch FHD+ InfinityEdge (1920x1200) với tỷ lệ 16:10, độ sáng 500 nits, góc nhìn rộng 178 độ, công nghệ Adaptive-Sync. Webcam 1080p với AI. Bàn phím backlit với haptic feedback. Touchpad vô hình với haptic feedback. Âm thanh Waves Nx với 4 loa. Cổng kết nối: 2x Thunderbolt 4 USB-C. Pin 58Whr với thời lượng 10-12 tiếng. Khối lượng 1.36kg, độ dày 15.3mm.",
                "CPU: Intel Core Ultra 7 155H, RAM: 16GB LPDDR5X, Storage: 512GB SSD, GPU: NVIDIA GeForce RTX 4050 6GB, Screen: 14.5 inch FHD+ InfinityEdge", 4.8f, 20, 28);
        addProduct("42", "Gigabyte AERO 14 OLED AI", 33500000, 36000000, "Gigabyte", "Laptop AI",
                "Gigabyte AERO 14 OLED AI là laptop sáng tạo với màn hình OLED chuẩn điện ảnh và chip Intel Core Ultra thế hệ mới, hỗ trợ AI cho các tác vụ render và chỉnh sửa video chuyên nghiệp. Thiết kế vỏ magie nguyên khối màu Matte Black với các đường nét góc cạnh tinh tế, logo Gigabyte phát sáng RGB. Bộ xử lý Intel Core Ultra 7 155H với 14 nhân 22 luồng và NPU 45 TOPS, hiệu năng vượt trội cho đa nhiệm nặng và sáng tạo nội dung. RAM 16GB LPDDR5 kênh đôi tốc độ 6400MHz. Ổ SSD 1TB PCIe Gen 4. Card đồ họa NVIDIA GeForce RTX 4050 6GB với DLSS 3 và AI acceleration. Màn hình 14 inch 2.8K OLED (2880x1800) với độ sáng 400 nits, tỷ lệ tương phản 1.000.000:1, màu sắc 100% DCI-P3 và công nghệ Dolby Vision. Hệ thống tản nhiệt Aorus Engine với AI tự động điều chỉnh. Bàn phím Aorus Fusion RGB với anti-ghosting. Touchpad Precision Touchpad. Webcam 1080p với AI. Âm thanh DTS:X với 2 loa. Cổng kết nối: 3x USB-A 3.2, 1x USB-C (Thunderbolt 4), HDMI 2.1. Pin 93Whr dung lượng lớn. Khối lượng 1.8kg.",
                "CPU: Intel Core Ultra 7 155H, RAM: 16GB LPDDR5, Storage: 1TB SSD, GPU: NVIDIA GeForce RTX 4050 6GB, Screen: 14 inch 2.8K OLED", 4.6f, 20, 44);

        // Đồ họa- Kỹ thuật - 7 products
        addProduct("43", "Dell Precision 3581", 39500000, 42000000, "Dell", "Đồ họa- Kỹ thuật",
                "Dell Precision 3581 là workstation di động bền bỉ với thiết kế chắc chắn và hiệu năng chuyên nghiệp, được tối ưu hóa cho các phần mềm CAD, Revit và AutoCAD chạy mượt mà. Thiết kế vỏ magie nguyên khối màu Black tinh tế với các đường nét góc cạnh chắc chắn, logo Dell tinh tế. Bộ xử lý Intel Core i7-13800H với 14 nhân 20 luồng, hiệu năng vượt trội cho đa nhiệm nặng và các tác vụ kỹ thuật chuyên nghiệp. RAM 16GB DDR5 kênh đôi tốc độ 4800MHz. Ổ SSD 512GB PCIe Gen 4. Card đồ họa NVIDIA RTX A1000 6GB với kiến trúc Ampere, xử lý tốt các tác vụ CAD 3D, render và simulation. Màn hình 15.6 inch FHD IPS với độ sáng 300 nits, góc nhìn rộng 178 độ, công nghệ Anti-Glare và 100% sRGB. Hệ thống tản nhiệt với 2 quạt và ống dẫn nhiệt, giữ máy luôn mát mẻ. Bàn phím chiclet với đèn nền LED. Touchpad Precision Touchpad. Webcam HD 720p. Âm thanh Waves MaxxAudio với 2 loa. Cổng kết nối: 3x USB-A 3.2, 1x USB-C (Thunderbolt 4), HDMI 2.1, LAN RJ45. Pin 68Whr với thời lượng 4-5 tiếng. Khối lượng 2.1kg. Đạt chuẩn độ bền quân đội MIL-STD-810G.",
                "CPU: Intel Core i7-13800H, RAM: 16GB DDR5, Storage: 512GB SSD, GPU: NVIDIA RTX A1000 6GB, Screen: 15.6 inch FHD IPS", 4.5f, 20, 34);
        addProduct("44", "HP ZBook Firefly 16 G10", 36500000, 38900000, "HP", "Đồ họa- Kỹ thuật",
                "HP ZBook Firefly 16 G10 là workstation mỏng nhẹ với màn hình DreamColor chuẩn màu và hiệu năng chuyên nghiệp cho kỹ sư và kiến trúc sư, mang đến sự cân bằng hoàn hảo giữa sức mạnh và tính di động. Thiết kế vỏ magie nguyên khối màu Silver tinh tế với các đường nét góc cạnh được bo tròn hợp lý, logo HP tinh tế. Bộ xử lý Intel Core i7-1360P với 12 nhân 16 luồng, hiệu năng vượt trội cho các phần mềm kỹ thuật như SolidWorks, AutoCAD và Revit. RAM 32GB DDR5 kênh đôi tốc độ 4800MHz. Ổ SSD 1TB PCIe Gen 4. Card đồ họa NVIDIA RTX A500 4GB với kiến trúc Ampere, xử lý tốt các tác vụ CAD và render cơ bản. Màn hình 16 inch WUXGA IPS (1920x1200) với tỷ lệ 16:10, độ sáng 400 nits, góc nhìn rộng 178 độ, công nghệ DreamColor cho độ chính xác màu cao và 100% Adobe RGB. Hệ thống tản nhiệt HP Sure Sense với AI tự động điều chỉnh. Bàn phím HP Premium với đèn nền LED, hành trình phím 1.5mm thoải mái. Touchpad Precision Touchpad. Webcam 1080p với Windows Hello. Âm thanh Bang & Olufsen với 2 loa. Cổng kết nối: 2x USB-A 3.2, 1x USB-C (Thunderbolt 4), HDMI 2.1. Pin 68Whr với thời lượng 6-7 tiếng. Khối lượng 1.78kg.",
                "CPU: Intel Core i7-1360P, RAM: 32GB DDR5, Storage: 1TB SSD, GPU: NVIDIA RTX A500 4GB, Screen: 16 inch WUXGA IPS", 4.6f, 20, 28);
        addProduct("45", "Lenovo ThinkPad P1 Gen 6", 58900000, 62000000, "Lenovo", "Đồ họa- Kỹ thuật",
                "Lenovo ThinkPad P1 Gen 6 là biểu tượng của sức mạnh workstation với bàn phím huyền thoại ThinkPad và hiệu năng tối thượng trong thân hình mỏng nhẹ, được thiết kế đặc biệt cho kỹ sư và nhà thiết kế chuyên nghiệp. Thiết kế vỏ magie nguyên khối màu Black cổ điển với các đường nét góc cạnh chắc chắn, logo ThinkPad đỏ nổi bật. Bộ xử lý Intel Core i7-13800H với 14 nhân 20 luồng, hiệu năng vượt trội cho đa nhiệm nặng và các phần mềm kỹ thuật chuyên nghiệp. RAM 32GB DDR5 kênh đôi tốc độ 4800MHz. Ổ SSD 1TB PCIe Gen 4. Card đồ họa NVIDIA RTX 2000 Ada 8GB với kiến trúc Ada Lovelace, xử lý tốt các tác vụ CAD 3D, render và simulation phức tạp. Màn hình 16 inch WQXGA IPS (2560x1600) với tỷ lệ 16:10, độ sáng 400 nits, góc nhìn rộng 178 độ, công nghệ Dolby Vision và 100% DCI-P3. Bàn phím ThinkPad Legend với đèn nền LED, hành trình phím 1.8mm và TrackPoint nổi tiếng. Touchpad Precision Touchpad. Webcam 1080p với ThinkShutter. Âm thanh Dolby Atmos với 2 loa. Cổng kết nối: 2x USB-A 3.2, 1x USB-C (Thunderbolt 4), HDMI 2.1, LAN RJ45. Pin 90Whr với thời lượng 5-6 tiếng. Khối lượng 1.75kg, độ dày 19.4mm.",
                "CPU: Intel Core i7-13800H, RAM: 32GB DDR5, Storage: 1TB SSD, GPU: NVIDIA RTX 2000 Ada 8GB, Screen: 16 inch WQXGA IPS", 4.7f, 20, 22);
        addProduct("46", "Asus ProArt Studiobook 16", 54900000, 58000000, "Asus", "Đồ họa- Kỹ thuật",
                "Asus ProArt Studiobook 16 là workstation sáng tạo với núm xoay vật lý Asus Dial độc đáo cho Adobe Creative Suite và màn hình OLED 3.2K chuẩn màu, mang đến trải nghiệm thiết kế đồ họa chuyên nghiệp đỉnh cao. Thiết kế vỏ magie nguyên khối màu Dark Grey tinh tế với các đường nét góc cạnh hiện đại, logo Asus tinh tế. Bộ xử lý Intel Core i9-13980HX với 24 nhân 32 luồng, hiệu năng vượt trội cho đa nhiệm nặng và render phức tạp. RAM 32GB DDR5 kênh đôi tốc độ 4800MHz. Ổ SSD 1TB PCIe Gen 4. Card đồ họa NVIDIA GeForce RTX 4070 8GB với kiến trúc Ada Lovelace, DLSS 3 và ray tracing, xử lý mượt mà các tác vụ 3D và video editing. Màn hình 16 inch 3.2K OLED (3200x2000) với tỷ lệ 16:10, độ sáng 400 nits, tỷ lệ tương phản 1.000.000:1, màu sắc 100% DCI-P3 và công nghệ Dolby Vision. Núm xoay Asus Dial tích hợp cho các phần mềm Adobe. Bàn phím chiclet với đèn nền LED. Touchpad ASUS Precision Touchpad. Webcam 1080p với AI. Âm thanh Harman Kardon với 6 loa. Cổng kết nối: 2x USB-A 3.2, 1x USB-C (Thunderbolt 4), HDMI 2.1. Pin 96Whr với thời lượng 6-7 tiếng. Khối lượng 2.0kg.",
                "CPU: Intel Core i9-13980HX, RAM: 32GB DDR5, Storage: 1TB SSD, GPU: NVIDIA GeForce RTX 4070 8GB, Screen: 16 inch 3.2K OLED 120Hz", 4.8f, 20, 19);
        addProduct("47", "MSI Creator Z17 HX Studio", 64500000, 68000000, "MSI", "Đồ họa- Kỹ thuật",
                "MSI Creator Z17 HX Studio là workstation gaming-sáng tạo với thiết kế CNC nguyên khối và đạt chuẩn NVIDIA Studio cho các tác vụ thiết kế đồ họa chuyên nghiệp, mang đến hiệu năng vượt trội trong mọi tình huống. Thiết kế vỏ magie nguyên khối màu Lunar Gray tinh tế với các đường nét góc cạnh chắc chắn, logo MSI tinh tế. Bộ xử lý Intel Core i9-13950HX với 24 nhân 32 luồng, hiệu năng vượt trội cho đa nhiệm nặng và render 3D phức tạp. RAM 64GB DDR5 kênh đôi tốc độ 4800MHz. Ổ SSD 2TB PCIe Gen 4. Card đồ họa NVIDIA GeForce RTX 4070 8GB với kiến trúc Ada Lovelace, DLSS 3 và ray tracing, xử lý mượt mà 1440p ở 60-100fps. Màn hình 17 inch QHD+ IPS cảm ứng (2560x1600) với tần số quét 165Hz, độ sáng 400 nits, góc nhìn rộng 178 độ, công nghệ G-Sync và 100% DCI-P3. Hệ thống tản nhiệt Cooler Boost Trinity với 3 quạt và 7 ống dẫn nhiệt. Bàn phím chiclet với đèn nền RGB 4 vùng. Touchpad Precision Touchpad. Webcam 1080p với AI. Âm thanh Nahimic với 2 loa. Cổng kết nối: 3x USB-A 3.2, 1x USB-C (Thunderbolt 4), HDMI 2.1, LAN RJ45. Pin 90Whr. Khối lượng 2.49kg.",
                "CPU: Intel Core i9-13950HX, RAM: 64GB DDR5, Storage: 2TB SSD, GPU: NVIDIA GeForce RTX 4070 8GB, Screen: 17 inch QHD+ 165Hz Touch", 4.7f, 20, 16);
        addProduct("48", "Gigabyte Aero 16 OLED", 45900000, 49000000, "Gigabyte", "Đồ họa- Kỹ thuật",
                "Gigabyte Aero 16 OLED là laptop sáng tạo với màn hình 4K OLED cân chỉnh màu X-Rite từng máy tại nhà máy, mang đến độ chính xác màu tuyệt đối cho các nhà thiết kế đồ họa và nhiếp ảnh gia chuyên nghiệp. Thiết kế vỏ magie nguyên khối màu Matte Black tinh tế với các đường nét góc cạnh hiện đại, logo Gigabyte tinh tế. Bộ xử lý Intel Core i9-13900H với 14 nhân 20 luồng, hiệu năng vượt trội cho đa nhiệm nặng và chỉnh sửa video 4K. RAM 32GB LPDDR5 kênh đôi tốc độ 4800MHz. Ổ SSD 1TB PCIe Gen 4. Card đồ họa NVIDIA GeForce RTX 4070 8GB với DLSS 3 và ray tracing, xử lý mượt mà các tác vụ 3D và video editing. Màn hình 16 inch 4K OLED (3840x2400) với độ sáng 400 nits, tỷ lệ tương phản 1.000.000:1, màu sắc 100% DCI-P3 và công nghệ Dolby Vision. Hệ thống tản nhiệt Aorus Engine với AI tự động điều chỉnh. Bàn phím Aorus Fusion RGB với anti-ghosting. Touchpad Precision Touchpad. Webcam 1080p với AI. Âm thanh DTS:X với 2 loa. Cổng kết nối: 3x USB-A 3.2, 1x USB-C (Thunderbolt 4), HDMI 2.1. Pin 99Whr dung lượng lớn. Khối lượng 2.0kg.",
                "CPU: Intel Core i9-13900H, RAM: 32GB LPDDR5, Storage: 1TB SSD, GPU: NVIDIA GeForce RTX 4070 8GB, Screen: 16 inch 4K OLED", 4.8f, 20, 19);
        addProduct("49", "Razer Blade 16 Studio", 84900000, 89000000, "Razer", "Đồ họa- Kỹ thuật",
                "Razer Blade 16 Studio là workstation gaming-sáng tạo cao cấp với màn hình Mini-LED chế độ kép (UHD+ 120Hz hoặc FHD+ 240Hz), mang đến sự linh hoạt tối đa cho cả gamer và creator chuyên nghiệp. Thiết kế vỏ nhôm nguyên khối màu Mercury tinh tế với các đường nét góc cạnh hầm hố, logo Razer phát sáng RGB 3 vùng. Bộ xử lý Intel Core i9-13950HX với 24 nhân 32 luồng, hiệu năng vượt trội cho đa nhiệm nặng và render 3D phức tạp. RAM 32GB DDR5 kênh đôi tốc độ 4800MHz. Ổ SSD 1TB PCIe Gen 4. Card đồ họa NVIDIA GeForce RTX 4080 12GB với kiến trúc Ada Lovelace, DLSS 3 và ray tracing, xử lý mượt mà 1440p ở 80-120fps. Màn hình 16 inch Dual Mode Mini-LED với chế độ UHD+ (3840x2400) 120Hz hoặc FHD+ (1920x1200) 240Hz, độ sáng 1000 nits, tỷ lệ tương phản 100.000:1, màu sắc 100% DCI-P3. Hệ thống tản nhiệt Vapor Chamber với quạt nén và dung dịch kim loại lỏng. Bàn phím Razer Opto-Mechanical với RGB Chroma, switch quang học 0.2ms. Touchpad Precision Touchpad. Webcam 1080p với Windows Hello. Âm thanh THX Spatial Audio với 2 loa. Cổng kết nối: 2x USB-A 3.2, 1x USB-C (Thunderbolt 4), HDMI 2.1. Pin 80.2Whr. Khối lượng 2.09kg.",
                "CPU: Intel Core i9-13950HX, RAM: 32GB DDR5, Storage: 1TB SSD, GPU: NVIDIA GeForce RTX 4080 12GB, Screen: 16 inch Dual Mode Mini-LED", 4.9f, 20, 12);

        // Macbook CTO - 7 products
        addProduct("50", "MacBook Air 13 M3 CTO 1", 31500000, 32990000, "Apple", "Macbook CTO",
                "Bản nâng cấp RAM cho đa nhiệm mượt mà hơn trên chip M3 mới nhất.",
                "CPU: Apple M3 (8-core CPU), RAM: 16GB Unified, Storage: 256GB SSD, GPU: Apple M3 (10-core GPU), Screen: 13.6 inch Liquid Retina", 4.9f, 20, 56);
        addProduct("51", "MacBook Air 15 M3 CTO 2", 40990000, 42990000, "Apple", "Macbook CTO",
                "Màn hình lớn, cấu hình Max Option cho dòng Air.",
                "CPU: Apple M3 (8-core CPU), RAM: 24GB Unified, Storage: 512GB SSD, GPU: Apple M3 (10-core GPU), Screen: 15.3 inch Liquid Retina", 5.0f, 20, 42);
        addProduct("52", "MacBook Pro 14 M3 Pro CTO 1", 52990000, 54990000, "Apple", "Macbook CTO",
                "Dòng Pro với chip M3 Pro mạnh mẽ, màu Space Black mới.",
                "CPU: Apple M3 Pro (11-core CPU), RAM: 36GB Unified, Storage: 512GB SSD, GPU: Apple M3 Pro (14-core GPU), Screen: 14.2 inch Liquid Retina XDR", 5.0f, 20, 38);
        addProduct("53", "MacBook Pro 14 M3 Max CTO", 87500000, 89990000, "Apple", "Macbook CTO",
                "Sức mạnh khủng khiếp trong thân hình 14 inch nhỏ gọn.",
                "CPU: Apple M3 Max (14-core CPU), RAM: 36GB Unified, Storage: 1TB SSD, GPU: Apple M3 Max (30-core GPU), Screen: 14.2 inch Liquid Retina XDR", 5.0f, 20, 24);
        addProduct("54", "MacBook Pro 16 M3 Pro CTO", 67990000, 69990000, "Apple", "Macbook CTO",
                "Màn hình lớn nhất, pin trâu nhất dòng Pro, RAM nâng cấp.",
                "CPU: Apple M3 Pro (12-core CPU), RAM: 36GB Unified, Storage: 1TB SSD, GPU: Apple M3 Pro (18-core GPU), Screen: 16.2 inch Liquid Retina XDR", 4.9f, 20, 31);
        addProduct("55", "MacBook Pro 16 M3 Max CTO 1", 96990000, 99990000, "Apple", "Macbook CTO",
                "Cấu hình đồ họa cao cấp cho Editor và 3D Artist chuyên nghiệp.",
                "CPU: Apple M3 Max (14-core CPU), RAM: 48GB Unified, Storage: 1TB SSD, GPU: Apple M3 Max (30-core GPU), Screen: 16.2 inch Liquid Retina XDR", 5.0f, 20, 18);
        addProduct("56", "MacBook Pro 16 M3 Max Ultimate", 145000000, 149990000, "Apple", "Macbook CTO",
                "Trùm cuối. Cấu hình cao nhất với bộ nhớ RAM khổng lồ.",
                "CPU: Apple M3 Max (16-core CPU), RAM: 128GB Unified, Storage: 2TB SSD, GPU: Apple M3 Max (40-core GPU), Screen: 16.2 inch Liquid Retina XDR", 5.0f, 20, 12);
    }
    
    private void addProduct(String id, String name, double price, double originalPrice, String brand, String category,
                           String description, String specs, float rating, int stock, int soldCount) {
        Product product = new Product(id, name, price, originalPrice, description,
                "https://via.placeholder.com/300x200?text=" + brand, brand, category, specs, rating, stock);
        product.setSoldCount(soldCount);
        product.setImageUrls(getProductImages(name));
        products.add(product);
    }
    
    private ArrayList<String> getProductImages(String productName) {
        ArrayList<String> images = new ArrayList<>();
        
        // Dell Inspiron
        if (productName.contains("Dell Inspiron")) {
            images.add("laptopdellinspiron1");
            images.add("laptopdellinspiron2");
            images.add("laptopdellinspiron3");
            images.add("laptopdellinspiron4");
            images.add("laptopdellinspiron5");
            images.add("laptopdellinspiron6");
        }
        // Asus Vivobook
        else if (productName.contains("Asus Vivobook") || productName.contains("Asus VivoBook")) {
            images.add("laptopasusvivobook1");
            images.add("laptopasusvivobook2");
            images.add("laptopasusvivobook3");
            images.add("laptopasusvivobook4");
            images.add("laptopasusvivobook5");
            images.add("laptopasusvivobook6");
            images.add("laptopasusvivobook7");
        }
        // Acer Aspire
        else if (productName.contains("Acer Aspire")) {
            images.add("laptopaceraspire1");
            images.add("laptopaceraspire2");
            images.add("laptopaceraspire3");
            images.add("laptopaceraspire4");
        }
        // Lenovo IdeaPad / Lenovo Idea
        else if (productName.contains("Lenovo IdeaPad") || productName.contains("Lenovo Idea")) {
            images.add("laptoplenovoidea1");
            images.add("laptoplenovoidea2");
            images.add("laptoplenovoidea3");
            images.add("laptoplenovoidea4");
            images.add("laptoplenovoidea5");
            images.add("laptoplenovoidea6");
        }
        // LG Gram
        else if (productName.contains("LG Gram")) {
            images.add("laptoplggram1");
            images.add("laptoplggram2");
            images.add("laptoplggram3");
            images.add("laptoplggram4");
            images.add("laptoplggram5");
            images.add("laptoplggram6");
        }
        // Asus ROG
        else if (productName.contains("Asus ROG") || productName.contains("ROG")) {
            images.add("laptopasusrog1");
            images.add("laptopasusrog2");
            images.add("laptopasusrog3");
            images.add("laptopasusrog4");
            images.add("laptopasusrog5");
            images.add("laptopasusrog6");
            images.add("laptopasusrog7");
        }
        // MSI Katana
        else if (productName.contains("MSI Katana")) {
            images.add("laptopmsikatana1");
            images.add("laptopmsikatana2");
            images.add("laptopmsikatana3");
            images.add("laptopmsikatana4");
            images.add("laptopmsikatana5");
            images.add("laptopmsikatana6");
            images.add("laptopmsikatana7");
        }
        // Lenovo Legion
        else if (productName.contains("Lenovo Legion")) {
            images.add("laptoplenovolegion1");
            images.add("laptoplenovolegion2");
            images.add("laptoplenovolegion3");
            images.add("laptoplenovolegion4");
            images.add("laptoplenovolegion5");
            images.add("laptoplenovolegion6");
            images.add("laptoplenovolegion7");
            images.add("laptoplenovolegion8");
        }
        // MacBook Air
        else if (productName.contains("MacBook Air")) {
            images.add("laptopmacbookair1");
            images.add("laptopmacbookair2");
            images.add("laptopmacbookair3");
            images.add("laptopmacbookair4");
        }
        // MacBook Pro
        else if (productName.contains("MacBook Pro")) {
            images.add("laptopmacbookpro1");
            images.add("laptopmacbookpro2");
            images.add("laptopmacbookpro3");
            images.add("laptopmacbookpro4");
            images.add("laptopmacbookpro5");
        }
        // HP Victus (for HP Gaming)
        else if (productName.contains("HP") && productName.contains("Victus")) {
            images.add("laptophpvictus1");
            images.add("laptophpvictus2");
            images.add("laptophpvictus3");
            images.add("laptophpvictus4");
            images.add("laptophpvictus5");
        }
        // Lenovo ThinkPad
        else if (productName.contains("ThinkPad")) {
            images.add("laptoplenovothinkpad1");
            images.add("laptoplenovothinkpad2");
            images.add("laptoplenovothinkpad3");
            images.add("laptoplenovothinkpad4");
        }
        // Default fallback
        else {
            images.add("laptop1");
            images.add("laptop2");
            images.add("laptop3");
            images.add("laptop4");
            images.add("laptop5");
        }
        
        return images;
    }
    
    // ==================== PRODUCT METHODS ====================
    public ArrayList<Product> getAllProducts() {
        return new ArrayList<>(products);
    }
    
    public Product getProductById(String productId) {
        for (Product p : products) {
            if (p.getId().equals(productId)) {
                return p;
            }
        }
        return null;
    }
    
    public ArrayList<Product> getProductsByCategory(String category) {
        ArrayList<Product> result = new ArrayList<>();
        for (Product p : products) {
            if (p.getCategory().equalsIgnoreCase(category)) {
                result.add(p);
            }
        }
        return result;
    }
    
    public ArrayList<Product> searchProducts(String query) {
        ArrayList<Product> result = new ArrayList<>();
        String lowerQuery = query.toLowerCase();
        for (Product p : products) {
            if (p.getName().toLowerCase().contains(lowerQuery) ||
                p.getBrand().toLowerCase().contains(lowerQuery) ||
                p.getCategory().toLowerCase().contains(lowerQuery)) {
                result.add(p);
            }
        }
        return result;
    }
    
    public ArrayList<Product> getRelatedProducts(String productId, int limit) {
        ArrayList<Product> result = new ArrayList<>();
        Product currentProduct = getProductById(productId);
        
        if (currentProduct == null) {
            return result;
        }
        
        // Get products from same category or brand
        for (Product p : products) {
            if (!p.getId().equals(productId)) {
                if (p.getCategory().equals(currentProduct.getCategory()) ||
                    p.getBrand().equals(currentProduct.getBrand())) {
                    result.add(p);
                }
            }
            
            if (result.size() >= limit) {
                break;
            }
        }
        
        // If not enough, add random products
        if (result.size() < limit) {
            for (Product p : products) {
                if (!p.getId().equals(productId) && !result.contains(p)) {
                    result.add(p);
                    if (result.size() >= limit) {
                        break;
                    }
                }
            }
        }
        
        return result;
    }
    
    public boolean addProduct(Product product) {
        if (product.getId() == null || product.getId().isEmpty()) {
            product.setId(UUID.randomUUID().toString());
        }
        return products.add(product);
    }
    
    public boolean updateProduct(Product product) {
        for (int i = 0; i < products.size(); i++) {
            if (products.get(i).getId().equals(product.getId())) {
                products.set(i, product);
                return true;
            }
        }
        return false;
    }
    
    public boolean deleteProduct(String productId) {
        return products.removeIf(p -> p.getId().equals(productId));
    }
    
    // ==================== USER METHODS ====================
    public User login(String username, String password) {
        for (User u : users) {
            if (u.getUsername().equals(username) && u.getPassword().equals(password)) {
                return u;
            }
        }
        return null;
    }
    
    public boolean register(User user) {
        // Check if username exists
        for (User u : users) {
            if (u.getUsername().equals(user.getUsername())) {
                return false;
            }
        }
        if (user.getId() == null || user.getId().isEmpty()) {
            user.setId(UUID.randomUUID().toString());
        }
        if (user.getRole() == null || user.getRole().isEmpty()) {
            user.setRole("user");
        }
        return users.add(user);
    }
    
    public boolean resetPassword(String username, String newPassword) {
        for (User u : users) {
            if (u.getUsername().equals(username)) {
                u.setPassword(newPassword);
                return true;
            }
        }
        return false;
    }
    
    public User getUserByUsername(String username) {
        for (User u : users) {
            if (u.getUsername().equals(username)) {
                return u;
            }
        }
        return null;
    }
    
    // ==================== CART METHODS ====================
    public ArrayList<CartItem> getCart(String userId) {
        if (!userCarts.containsKey(userId)) {
            userCarts.put(userId, new ArrayList<>());
        }
        return userCarts.get(userId);
    }
    
    public boolean addToCart(String userId, Product product, int quantity) {
        ArrayList<CartItem> cart = getCart(userId);
        
        // Check if product already in cart
        for (CartItem item : cart) {
            if (item.getProduct().getId().equals(product.getId())) {
                item.setQuantity(item.getQuantity() + quantity);
                return true;
            }
        }
        
        // Add new item
        CartItem newItem = new CartItem(UUID.randomUUID().toString(), userId, product, quantity);
        return cart.add(newItem);
    }
    
    public boolean updateCartItemQuantity(String userId, String cartItemId, int quantity) {
        ArrayList<CartItem> cart = getCart(userId);
        for (CartItem item : cart) {
            if (item.getId().equals(cartItemId)) {
                if (quantity <= 0) {
                    return cart.remove(item);
                }
                item.setQuantity(quantity);
                return true;
            }
        }
        return false;
    }
    
    public boolean removeFromCart(String userId, String cartItemId) {
        ArrayList<CartItem> cart = getCart(userId);
        return cart.removeIf(item -> item.getId().equals(cartItemId));
    }
    
    public void clearCart(String userId) {
        userCarts.put(userId, new ArrayList<>());
    }
    
    public double getCartTotal(String userId) {
        ArrayList<CartItem> cart = getCart(userId);
        double total = 0;
        for (CartItem item : cart) {
            total += item.getTotalPrice();
        }
        return total;
    }
    
    // ==================== WISHLIST METHODS ====================
    public ArrayList<Product> getWishlist(String userId) {
        if (!userWishlists.containsKey(userId)) {
            userWishlists.put(userId, new ArrayList<>());
        }
        return userWishlists.get(userId);
    }
    
    public boolean addToWishlist(String userId, Product product) {
        ArrayList<Product> wishlist = getWishlist(userId);
        
        // Check if already in wishlist
        for (Product p : wishlist) {
            if (p.getId().equals(product.getId())) {
                return false;
            }
        }
        
        product.setFavorite(true);
        return wishlist.add(product);
    }
    
    public boolean removeFromWishlist(String userId, String productId) {
        ArrayList<Product> wishlist = getWishlist(userId);
        boolean removed = wishlist.removeIf(p -> p.getId().equals(productId));
        
        if (removed) {
            Product product = getProductById(productId);
            if (product != null) {
                product.setFavorite(false);
            }
        }
        
        return removed;
    }
    
    public boolean isInWishlist(String userId, String productId) {
        ArrayList<Product> wishlist = getWishlist(userId);
        for (Product p : wishlist) {
            if (p.getId().equals(productId)) {
                return true;
            }
        }
        return false;
    }
    
    // ==================== ORDER METHODS ====================
    public Order checkout(String userId, String deliveryAddress, String phone) {
        ArrayList<CartItem> cart = getCart(userId);
        if (cart.isEmpty()) {
            return null;
        }
        
        double total = getCartTotal(userId);
        Order order = new Order(UUID.randomUUID().toString(), userId, 
                new ArrayList<>(cart), total, deliveryAddress, phone);
        orders.add(order);
        clearCart(userId);
        
        return order;
    }
    
    public Order checkoutSelectedItems(String userId, ArrayList<CartItem> selectedItems, 
                                       String deliveryAddress, String phone) {
        if (selectedItems == null || selectedItems.isEmpty()) {
            return null;
        }
        
        // Check stock availability before proceeding
        for (CartItem item : selectedItems) {
            Product product = getProductById(item.getProduct().getId());
            if (product == null || product.getStock() < item.getQuantity()) {
                return null; // Not enough stock
            }
        }
        
        // Calculate total from selected items
        double total = 0;
        for (CartItem item : selectedItems) {
            total += item.getProduct().getPrice() * item.getQuantity();
        }
        
        // Create order
        Order order = new Order(UUID.randomUUID().toString(), userId, 
                new ArrayList<>(selectedItems), total, deliveryAddress, phone);
        orders.add(order);
        
        // Decrease stock and update soldCount for each product
        for (CartItem item : selectedItems) {
            Product product = getProductById(item.getProduct().getId());
            if (product != null) {
                product.setStock(product.getStock() - item.getQuantity());
                product.setSoldCount(product.getSoldCount() + item.getQuantity());
            }
        }
        
        // Remove selected items from cart
        ArrayList<CartItem> cart = getCart(userId);
        for (CartItem selectedItem : selectedItems) {
            cart.removeIf(item -> item.getProduct().getId().equals(selectedItem.getProduct().getId()));
        }
        
        return order;
    }
    
    public ArrayList<Order> getUserOrders(String userId) {
        ArrayList<Order> userOrders = new ArrayList<>();
        for (Order order : orders) {
            if (order.getUserId().equals(userId)) {
                userOrders.add(order);
            }
        }
        return userOrders;
    }
    
    public ArrayList<Order> getAllOrders() {
        return new ArrayList<>(orders);
    }
    
    public Order getOrderById(String orderId) {
        for (Order order : orders) {
            if (order.getId().equals(orderId)) {
                return order;
            }
        }
        return null;
    }
    
    public boolean updateOrderStatus(String orderId, String status) {
        Order order = getOrderById(orderId);
        if (order != null) {
            order.setStatus(status);
            return true;
        }
        return false;
    }
    
    public boolean cancelOrder(String orderId, String reason) {
        Order order = getOrderById(orderId);
        if (order != null && order.canBeCancelled()) {
            order.setStatus("cancelled");
            order.setCancelReason(reason);
            
            // Return stock to products
            for (CartItem item : order.getItems()) {
                Product product = getProductById(item.getProduct().getId());
                if (product != null) {
                    product.setStock(product.getStock() + item.getQuantity());
                    // Also decrease sold count
                    product.setSoldCount(Math.max(0, product.getSoldCount() - item.getQuantity()));
                }
            }
            
            return true;
        }
        return false;
    }
    
    // Admin helper
    public User getUserById(String userId) {
        for (User user : users) {
            if (user.getId().equals(userId)) {
                return user;
            }
        }
        return null;
    }
    
    public ArrayList<Order> getOrdersByUserId(String userId) {
        ArrayList<Order> userOrders = new ArrayList<>();
        for (Order order : orders) {
            if (order.getUserId().equals(userId)) {
                userOrders.add(order);
            }
        }
        return userOrders;
    }
    
    // ==================== ADDRESS MANAGEMENT ====================
    
    public ArrayList<Address> getUserAddresses(String userId) {
        if (!userAddresses.containsKey(userId)) {
            userAddresses.put(userId, new ArrayList<>());
        }
        return userAddresses.get(userId);
    }
    
    public Address addAddress(String userId, String recipientName, String phone, 
                             String city, String district, String ward, String addressLine, 
                             boolean isDefault) {
        ArrayList<Address> addresses = getUserAddresses(userId);
        
        // If setting as default, unset all other defaults
        if (isDefault) {
            for (Address addr : addresses) {
                addr.setDefault(false);
            }
        }
        
        // Create full address string
        String fullAddress = addressLine + ", " + ward + ", " + district + ", " + city;
        
        Address newAddress = new Address(
            UUID.randomUUID().toString(),
            userId,
            recipientName,
            phone,
            fullAddress,
            isDefault
        );
        
        addresses.add(newAddress);
        return newAddress;
    }
    
    public boolean updateAddress(Address address) {
        ArrayList<Address> addresses = getUserAddresses(address.getUserId());
        for (int i = 0; i < addresses.size(); i++) {
            if (addresses.get(i).getId().equals(address.getId())) {
                // If setting as default, unset all other defaults
                if (address.isDefault()) {
                    for (Address addr : addresses) {
                        addr.setDefault(false);
                    }
                }
                addresses.set(i, address);
                return true;
            }
        }
        return false;
    }
    
    public boolean deleteAddress(String userId, String addressId) {
        ArrayList<Address> addresses = getUserAddresses(userId);
        return addresses.removeIf(addr -> addr.getId().equals(addressId));
    }
    
    public boolean setDefaultAddress(String userId, String addressId) {
        ArrayList<Address> addresses = getUserAddresses(userId);
        boolean found = false;
        
        for (Address addr : addresses) {
            if (addr.getId().equals(addressId)) {
                addr.setDefault(true);
                found = true;
            } else {
                addr.setDefault(false);
            }
        }
        
        return found;
    }
    
    public Address getDefaultAddress(String userId) {
        ArrayList<Address> addresses = getUserAddresses(userId);
        for (Address addr : addresses) {
            if (addr.isDefault()) {
                return addr;
            }
        }
        // Return first address if no default
        return addresses.isEmpty() ? null : addresses.get(0);
    }
    
    // ==================== REVIEW MANAGEMENT ====================
    
    private void initReviews() {
        // Product 1: Dell Inspiron (4.2 stars) - Văn phòng
        addMockReview("1", "user1", "Nguyễn Văn A", 5, "Máy chạy mượt, pin trâu, rất đáng tiền cho văn phòng!");
        addMockReview("1", "user2", "Trần Thị B", 4, "Bàn phím gõ tốt nhưng màn hình hơi tối một chút.");
        addMockReview("1", "user3", "Lê Văn C", 5, "Đóng gói cẩn thận, giao hàng nhanh. Máy chất lượng.");
        addMockReview("1", "user4", "Phạm Thị D", 4, "Tốt cho công việc văn phòng, thiết kế đơn giản nhưng sang.");
        addMockReview("1", "user5", "Hoàng Văn E", 3, "Ổn nhưng RAM DDR4 hơi lạc hậu so với DDR5.");
        
        // Product 8: Asus ROG Strix G16 (4.6 stars) - Gaming
        addMockReview("8", "gamer1", "Minh Gamer", 5, "Chiến game siêu mượt! RTX 4060 xử lý mọi game AAA như Cyberpunk 2077.");
        addMockReview("8", "gamer2", "Tuấn Pro", 5, "Màn hình 240Hz đỉnh cao! LED RGB đẹp mắt, tản nhiệt tốt.");
        addMockReview("8", "gamer3", "Hùng Gaming", 5, "Xứng đáng với giá tiền, hiệu năng khủng cho streamer.");
        addMockReview("8", "user6", "Nam Nguyễn", 4, "Máy mạnh nhưng hơi nặng, không tiện mang đi.");
        addMockReview("8", "user7", "Anh Đỗ", 4, "Pin yếu khi không cắm sạc, nhưng đó là đặc điểm gaming laptop.");
        addMockReview("8", "user8", "Bình Lê", 5, "Bàn phím cơ học cảm giác gõ tuyệt vời!");
        
        // Product 15: Asus Zenbook S 13 OLED (4.7 stars) - Mỏng nhẹ
        addMockReview("15", "user9", "Mai Phương", 5, "Nhẹ như lông hồng! Màn OLED đẹp xuất sắc.");
        addMockReview("15", "user10", "Linh Trần", 5, "Thiết kế sang trọng, phù hợp dân văn phòng hay đi công tác.");
        addMockReview("15", "user11", "Dũng Vũ", 4, "Pin trâu nhưng giá hơi cao so với cấu hình.");
        addMockReview("15", "user12", "Hà Nguyễn", 5, "Vỏ gốm plasma độc đáo, chạy êm không ồn.");
        
        // Product 22: MacBook Air M3 (4.9 stars) - Sinh viên
        addMockReview("22", "sv1", "Hương SV", 5, "Hoàn hảo cho sinh viên! Pin 18 tiếng, nhẹ, chạy nhanh.");
        addMockReview("22", "sv2", "Tùng Uni", 5, "M3 xử lý mượt mà, không lag dù mở nhiều tab Chrome.");
        addMockReview("22", "sv3", "Lan Student", 5, "Thiết kế đẹp, hệ sinh thái Apple tuyệt vời!");
        addMockReview("22", "sv4", "Khoa Dev", 5, "Code Swift và Xcode mượt lắm, recommend!");
        addMockReview("22", "user13", "Quân Lê", 5, "Không ồn, không nóng, màn Retina sắc nét.");
        addMockReview("22", "user14", "Thảo Võ", 4, "Tốt nhưng thiếu cổng USB-A, phải mua thêm hub.");
        
        // Product 29: iPad Pro M2 (4.8 stars) - Cảm ứng
        addMockReview("29", "user15", "Minh Họa Sĩ", 5, "Apple Pencil vẽ như trên giấy, ProCreate chạy đỉnh!");
        addMockReview("29", "user16", "Nhã Designer", 5, "Màn hình 120Hz mượt mà, màu sắc chuẩn chỉnh.");
        addMockReview("29", "user17", "Phong Editor", 4, "Chỉnh video 4K tốt nhưng LumaFusion hơi khó dùng.");
        addMockReview("29", "user18", "Tú Nguyễn", 5, "Thay thế laptop hoàn toàn cho công việc di động.");
        
        // Product 36: HP ZBook Studio G10 (4.8 stars) - Đồ họa
        addMockReview("36", "designer1", "Khang 3D", 5, "Render 3Ds Max cực nhanh! RTX A2000 xử lý tốt.");
        addMockReview("36", "designer2", "Vy Arch", 5, "AutoCAD, Revit chạy mượt, màn hình 4K sắc nét.");
        addMockReview("36", "user19", "Đạt Video", 4, "Premiere Pro tốt nhưng máy hơi nóng khi render.");
        addMockReview("36", "user20", "Sơn Design", 5, "Màn sắc màu chuẩn DCI-P3, phù hợp thiết kế đồ họa.");
        
        // Product 43: MacBook Pro M3 Max (5.0 stars) - MacBook CTO
        addMockReview("43", "pro1", "Khánh Dev", 5, "Sức mạnh M3 Max kinh khủng! Build code cực nhanh.");
        addMockReview("43", "pro2", "Hải Editor", 5, "Export video 8K không lag, fan chạy êm.");
        addMockReview("43", "pro3", "Dương ML", 5, "Train model AI nhanh gấp đôi Intel, pin 22 giờ.");
        addMockReview("43", "user21", "Bảo Phạm", 5, "Đáng từng đồng! Đầu tư lâu dài cho công việc chuyên nghiệp.");
        
        // Product 50: Lenovo Yoga Slim 7 Pro X (4.5 stars) - Laptop AI
        addMockReview("50", "ai1", "Tâm AI Dev", 5, "NPU xử lý AI tasks tốt, hỗ trợ Copilot mượt mà.");
        addMockReview("50", "ai2", "Cường Tech", 4, "OLED đẹp nhưng pin yếu khi dùng AI nhiều.");
        addMockReview("50", "user22", "Hải Trần", 5, "Màn hình 3K sắc nét, âm thanh Dolby Atmos hay.");
        addMockReview("50", "user23", "Thư Võ", 4, "Thiết kế đẹp nhưng giá hơi cao cho phân khúc.");
    }
    
    private void addMockReview(String productId, String userId, String userName, int rating, String comment) {
        ArrayList<Review> reviews = getProductReviews(productId);
        Review newReview = new Review(
            UUID.randomUUID().toString(),
            productId,
            userId,
            userName,
            rating,
            comment
        );
        reviews.add(newReview);
    }
    
    public ArrayList<Review> getProductReviews(String productId) {
        if (!productReviews.containsKey(productId)) {
            productReviews.put(productId, new ArrayList<>());
        }
        return productReviews.get(productId);
    }
    
    public Review addReview(String productId, String userId, String userName, float rating, String comment) {
        ArrayList<Review> reviews = getProductReviews(productId);
        
        Review newReview = new Review(
            UUID.randomUUID().toString(),
            productId,
            userId,
            userName,
            rating,
            comment
        );
        
        reviews.add(0, newReview); // Add to beginning
        
        // Update product rating
        updateProductRating(productId);
        
        return newReview;
    }
    
    private void updateProductRating(String productId) {
        ArrayList<Review> reviews = getProductReviews(productId);
        if (reviews.isEmpty()) return;
        
        float totalRating = 0;
        for (Review review : reviews) {
            totalRating += review.getRating();
        }
        
        float avgRating = totalRating / reviews.size();
        
        Product product = getProductById(productId);
        if (product != null) {
            product.setRating(avgRating);
        }
    }
    
    public boolean hasUserReviewedProduct(String userId, String productId) {
        ArrayList<Review> reviews = getProductReviews(productId);
        for (Review review : reviews) {
            if (review.getUserId().equals(userId)) {
                return true;
            }
        }
        return false;
    }
    
    public boolean canUserReviewProduct(String userId, String productId) {
        // Check if user has purchased this product
        ArrayList<Order> userOrders = getUserOrders(userId);
        for (Order order : userOrders) {
            if (order.getStatus().equals("delivered")) {
                for (CartItem item : order.getItems()) {
                    if (item.getProduct().getId().equals(productId)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    // ==================== ADMIN METHODS ====================
    
    public ArrayList<User> getAllUsers() {
        return new ArrayList<>(users);
    }
    
    public ArrayList<Review> getAllReviews() {
        ArrayList<Review> allReviews = new ArrayList<>();
        for (ArrayList<Review> reviews : productReviews.values()) {
            allReviews.addAll(reviews);
        }
        return allReviews;
    }
    
    public ArrayList<Product> getProductsWithReviews() {
        ArrayList<Product> productsWithReviews = new ArrayList<>();
        for (Product product : products) {
            if (productReviews.containsKey(product.getId()) && 
                !productReviews.get(product.getId()).isEmpty()) {
                productsWithReviews.add(product);
            }
        }
        return productsWithReviews;
    }
    
    public int getReviewCountForProduct(String productId) {
        ArrayList<Review> reviews = getProductReviews(productId);
        return reviews.size();
    }
    
    public boolean deleteReview(String reviewId) {
        for (ArrayList<Review> reviews : productReviews.values()) {
            for (Review review : reviews) {
                if (review.getId().equals(reviewId)) {
                    reviews.remove(review);
                    // Update product rating after deleting review
                    updateProductRating(review.getProductId());
                    return true;
                }
            }
        }
        return false;
    }
    
    // ==================== NOTIFICATION METHODS ====================
    
    public void createNotification(String userId, String title, String message, 
                                   String type, String orderId) {
        String notificationId = UUID.randomUUID().toString();
        long timestamp = System.currentTimeMillis();
        Notification notification = new Notification(notificationId, userId, title, 
                                                     message, type, orderId, timestamp, false);
        notifications.add(notification);
    }
    
    public ArrayList<Notification> getNotifications(String userId) {
        ArrayList<Notification> userNotifications = new ArrayList<>();
        for (Notification notification : notifications) {
            if (notification.getUserId().equals(userId)) {
                userNotifications.add(notification);
            }
        }
        // Sort by timestamp descending (newest first)
        userNotifications.sort((n1, n2) -> Long.compare(n2.getTimestamp(), n1.getTimestamp()));
        return userNotifications;
    }
    
    public int getUnreadNotificationCount(String userId) {
        int count = 0;
        for (Notification notification : notifications) {
            if (notification.getUserId().equals(userId) && !notification.isRead()) {
                count++;
            }
        }
        return count;
    }
    
    public void markNotificationAsRead(String notificationId) {
        for (Notification notification : notifications) {
            if (notification.getId().equals(notificationId)) {
                notification.setRead(true);
                break;
            }
        }
    }
    
    public void markAllNotificationsAsRead(String userId) {
        for (Notification notification : notifications) {
            if (notification.getUserId().equals(userId)) {
                notification.setRead(true);
            }
        }
    }
    
    // ==================== BANNER METHODS ====================
    
    private void initBanners() {
        banners.add(new Banner("1", "banner1", "Laptop Gaming RTX 4060", "Gaming", true, 1));
        banners.add(new Banner("2", "banner2", "Laptop Văn Phòng Giá Tốt", "Văn phòng", true, 2));
        banners.add(new Banner("3", "banner3", "Macbook Pro M3 Mới Nhất", "Macbook CTO", true, 3));
        banners.add(new Banner("4", "banner4", "Laptop AI - Tương Lai Của Công Nghệ", "Laptop AI", true, 4));
        banners.add(new Banner("5", "banner5", "Laptop Sinh Viên - Ưu Đãi Khủng", "Sinh viên", true, 5));
    }
    
    public ArrayList<Banner> getAllBanners() {
        return new ArrayList<>(banners);
    }
    
    public ArrayList<Banner> getActiveBanners() {
        ArrayList<Banner> activeBanners = new ArrayList<>();
        for (Banner banner : banners) {
            if (banner.isActive()) {
                activeBanners.add(banner);
            }
        }
        // Sort by order ascending
        activeBanners.sort((b1, b2) -> Integer.compare(b1.getOrder(), b2.getOrder()));
        return activeBanners;
    }
    
    public Banner getBannerById(String id) {
        for (Banner banner : banners) {
            if (banner.getId().equals(id)) {
                return banner;
            }
        }
        return null;
    }
    
    public boolean addBanner(Banner banner) {
        if (banner == null || banner.getId() == null || banner.getId().isEmpty()) {
            return false;
        }
        
        // Check if banner with same ID already exists
        for (Banner b : banners) {
            if (b.getId().equals(banner.getId())) {
                return false;
            }
        }
        
        banners.add(banner);
        return true;
    }
    
    public boolean updateBanner(Banner updatedBanner) {
        if (updatedBanner == null || updatedBanner.getId() == null) {
            return false;
        }
        
        for (int i = 0; i < banners.size(); i++) {
            if (banners.get(i).getId().equals(updatedBanner.getId())) {
                banners.set(i, updatedBanner);
                return true;
            }
        }
        return false;
    }
    
    public boolean deleteBanner(String id) {
        if (id == null || id.isEmpty()) {
            return false;
        }
        
        for (int i = 0; i < banners.size(); i++) {
            if (banners.get(i).getId().equals(id)) {
                banners.remove(i);
                return true;
            }
        }
        return false;
    }
    
    public String generateBannerId() {
        int maxId = 0;
        for (Banner banner : banners) {
            try {
                int id = Integer.parseInt(banner.getId());
                if (id > maxId) {
                    maxId = id;
                }
            } catch (NumberFormatException e) {
                // Ignore non-numeric IDs
            }
        }
        return String.valueOf(maxId + 1);
    }
}
