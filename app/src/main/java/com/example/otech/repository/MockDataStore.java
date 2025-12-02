package com.example.otech.repository;

import com.example.otech.model.Address;
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
    private HashMap<String, ArrayList<CartItem>> userCarts; // userId -> CartItems
    private HashMap<String, ArrayList<Product>> userWishlists; // userId -> Products
    private HashMap<String, ArrayList<Address>> userAddresses; // userId -> Addresses
    private HashMap<String, ArrayList<Review>> productReviews; // productId -> Reviews
    
    private MockDataStore() {
        products = new ArrayList<>();
        users = new ArrayList<>();
        orders = new ArrayList<>();
        notifications = new ArrayList<>();
        userCarts = new HashMap<>();
        userWishlists = new HashMap<>();
        userAddresses = new HashMap<>();
        productReviews = new HashMap<>();
        
        initUsers();
        initProducts();
        initReviews();
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
                "Laptop văn phòng bền bỉ, thiết kế thanh lịch, bàn phím rộng rãi phù hợp nhập liệu.",
                "CPU: Intel Core i5-1335U, RAM: 16GB DDR4 3200MHz, Storage: 512GB SSD NVMe, GPU: Intel UHD Graphics, Screen: 15.6 inch FHD 120Hz", 4.2f, 20, 45);
        addProduct("2", "HP Pavilion 15-eg3000", 17500000, 21000000, "HP", "Văn phòng",
                "Thiết kế vỏ kim loại sang trọng, âm thanh B&O chất lượng cao, hiệu năng ổn định.",
                "CPU: Intel Core i5-13500H, RAM: 16GB DDR4, Storage: 512GB SSD, GPU: Intel Iris Xe Graphics, Screen: 15.6 inch FHD IPS", 4.3f, 20, 38);
        addProduct("3", "Asus Vivobook Go 15", 12990000, 14500000, "Asus", "Văn phòng",
                "Mỏng nhẹ, bản lề 180 độ, đạt độ bền chuẩn quân đội, giá thành hợp lý.",
                "CPU: AMD Ryzen 5 7520U, RAM: 16GB LPDDR5, Storage: 512GB SSD, GPU: AMD Radeon Graphics, Screen: 15.6 inch FHD OLED", 4.1f, 20, 52);
        addProduct("4", "Acer Aspire 5 Slim", 13990000, 17000000, "Acer", "Văn phòng",
                "Hiệu năng đa nhiệm tốt, tản nhiệt hiệu quả, phù hợp môi trường công sở.",
                "CPU: Intel Core i5-13420H, RAM: 16GB DDR5, Storage: 512GB SSD, GPU: Intel UHD Graphics, Screen: 14 inch FHD IPS", 4.2f, 20, 41);
        addProduct("5", "Lenovo IdeaPad Slim 5", 18990000, 20500000, "Lenovo", "Văn phòng",
                "Thiết kế tối giản, pin trâu, webcam có nắp che bảo mật.",
                "CPU: Intel Core i5-1340P, RAM: 16GB LPDDR5, Storage: 512GB SSD, GPU: Intel Iris Xe Graphics, Screen: 16 inch WUXGA IPS", 4.4f, 20, 36);
        addProduct("6", "MSI Modern 14 C13M", 11990000, 15000000, "MSI", "Văn phòng",
                "Siêu nhẹ chỉ 1.4kg, thiết kế trẻ trung, phù hợp di chuyển nhiều.",
                "CPU: Intel Core i3-1315U, RAM: 8GB DDR4, Storage: 512GB SSD, GPU: Intel UHD Graphics, Screen: 14 inch FHD IPS", 4.0f, 20, 29);
        addProduct("7", "LG Gram 14 2023", 24990000, 28900000, "LG", "Văn phòng",
                "Siêu nhẹ vô địch (999g), thời lượng pin lên đến 20 giờ.",
                "CPU: Intel Core i5-1340P, RAM: 16GB LPDDR5, Storage: 512GB SSD, GPU: Intel Iris Xe Graphics, Screen: 14 inch WUXGA IPS", 4.5f, 20, 33);

        // Gaming - 8 products
        addProduct("8", "Asus ROG Strix G16", 39990000, 45000000, "Asus", "Gaming",
                "Chiến thần gaming, tản nhiệt 3 quạt, LED RGB rực rỡ.",
                "CPU: Intel Core i9-13980HX, RAM: 16GB DDR5, Storage: 1TB SSD NVMe, GPU: NVIDIA GeForce RTX 4060 8GB, Screen: 16 inch QHD+ 240Hz", 4.6f, 20, 78);
        addProduct("9", "MSI Katana 15", 28500000, 38000000, "MSI", "Gaming",
                "Bàn phím cắt gọt độc đáo, hiệu năng mạnh mẽ cho mọi tựa game AAA.",
                "CPU: Intel Core i7-13620H, RAM: 16GB DDR5, Storage: 1TB SSD, GPU: NVIDIA GeForce RTX 4050 6GB, Screen: 15.6 inch FHD 144Hz", 4.5f, 20, 92);
        addProduct("10", "Acer Predator Helios Neo 16", 35990000, 38900000, "Acer", "Gaming",
                "Công nghệ tản nhiệt Aeroblade 3D độc quyền, phím nóng Turbo.",
                "CPU: Intel Core i7-13700HX, RAM: 16GB DDR5, Storage: 512GB SSD, GPU: NVIDIA GeForce RTX 4060 8GB, Screen: 16 inch WQXGA 165Hz", 4.6f, 20, 65);
        addProduct("11", "Lenovo Legion Pro 5", 38990000, 42000000, "Lenovo", "Gaming",
                "Thiết kế tối giản nhưng hiệu năng khủng, màn hình chuẩn màu 100% sRGB.",
                "CPU: AMD Ryzen 7 7745HX, RAM: 16GB DDR5, Storage: 1TB SSD, GPU: NVIDIA GeForce RTX 4060 8GB, Screen: 16 inch WQXGA 240Hz", 4.7f, 20, 71);
        addProduct("12", "Gigabyte Aorus 15", 33990000, 36500000, "Gigabyte", "Gaming",
                "Siêu mỏng trong phân khúc gaming, hỗ trợ AI cho game thủ.",
                "CPU: Intel Core i7-13700H, RAM: 16GB DDR5, Storage: 1TB SSD, GPU: NVIDIA GeForce RTX 4070 8GB, Screen: 15.6 inch QHD 165Hz", 4.6f, 20, 58);
        addProduct("13", "Razer Blade 15", 59990000, 65000000, "Razer", "Gaming",
                "Vỏ nhôm nguyên khối cao cấp, thiết kế gaming đẳng cấp nhất.",
                "CPU: Intel Core i7-13800H, RAM: 16GB DDR5, Storage: 1TB SSD, GPU: NVIDIA GeForce RTX 4070 8GB, Screen: 15.6 inch QHD 240Hz", 4.7f, 20, 44);
        addProduct("14", "Dell Alienware M16", 49990000, 55000000, "Dell", "Gaming",
                "Biểu tượng của sức mạnh, hệ thống tản nhiệt Cryo-Tech, thiết kế Legend 3.",
                "CPU: Intel Core i9-13900HX, RAM: 32GB DDR5, Storage: 1TB SSD, GPU: NVIDIA GeForce RTX 4070 8GB, Screen: 16 inch QHD+ 165Hz", 4.8f, 20, 37);

        // Mỏng nhẹ - 7 products
        addProduct("15", "Asus Zenbook S 13 OLED", 36990000, 39900000, "Asus", "Mỏng nhẹ",
                "Laptop OLED mỏng nhất thế giới, vỏ gốm plasma độc đáo.",
                "CPU: Intel Core i7-1355U, RAM: 32GB LPDDR5, Storage: 1TB SSD, GPU: Intel Iris Xe Graphics, Screen: 13.3 inch 2.8K OLED", 4.7f, 20, 52);
        addProduct("16", "Dell XPS 13 Plus", 44990000, 48000000, "Dell", "Mỏng nhẹ",
                "Thiết kế tương lai với bàn phím tràn viền và trackpad vô hình.",
                "CPU: Intel Core i7-1360P, RAM: 16GB LPDDR5, Storage: 512GB SSD, GPU: Intel Iris Xe Graphics, Screen: 13.4 inch 3.5K OLED Touch", 4.8f, 20, 46);
        addProduct("17", "HP Spectre x360 14", 39900000, 42500000, "HP", "Mỏng nhẹ",
                "Xoay gập 360 độ, thiết kế vát cắt kim cương, bảo mật vân tay.",
                "CPU: Intel Core i7-1355U, RAM: 16GB LPDDR4x, Storage: 1TB SSD, GPU: Intel Iris Xe Graphics, Screen: 13.5 inch 3K2K OLED", 4.7f, 20, 41);
        addProduct("18", "Lenovo Yoga Slim 7i Carbon", 26990000, 36000000, "Lenovo", "Mỏng nhẹ",
                "Vỏ Carbon siêu bền siêu nhẹ, màu trắng tinh khôi.",
                "CPU: Intel Core i5-1340P, RAM: 16GB LPDDR5, Storage: 512GB SSD, GPU: Intel Iris Xe Graphics, Screen: 13.3 inch 2.5K 90Hz", 4.5f, 20, 55);
        addProduct("19", "MacBook Air M2 13 inch", 24990000, 33000000, "Apple", "Mỏng nhẹ",
                "Thiết kế phẳng mới, chip M2 mạnh mẽ, pin cả ngày dài.",
                "CPU: Apple M2 (8-core CPU), RAM: 8GB Unified, Storage: 256GB SSD, GPU: Apple M2 (8-core GPU), Screen: 13.6 inch Liquid Retina", 4.8f, 20, 187);
        addProduct("20", "Acer Swift Go 14", 18990000, 25000000, "Acer", "Mỏng nhẹ",
                "Màn hình OLED rực rỡ, trọng lượng nhẹ, đầy đủ cổng kết nối.",
                "CPU: Intel Core i5-13500H, RAM: 16GB LPDDR5, Storage: 512GB SSD, GPU: Intel Iris Xe Graphics, Screen: 14 inch 2.8K OLED 90Hz", 4.4f, 20, 64);
        addProduct("21", "LG Gram SuperSlim", 31990000, 35000000, "LG", "Mỏng nhẹ",
                "Mỏng như quyển tạp chí (10.9mm), màn hình OLED chống chói.",
                "CPU: Intel Core i5-1340P, RAM: 16GB LPDDR5, Storage: 512GB SSD, GPU: Intel Iris Xe Graphics, Screen: 15.6 inch FHD OLED", 4.6f, 20, 39);

        // Sinh viên - 7 products
        addProduct("22", "Asus Vivobook 16", 10990000, 12500000, "Asus", "Sinh viên",
                "Màn hình lớn 16 inch làm việc đa nhiệm, giá rẻ cho sinh viên.",
                "CPU: Intel Core i3-1315U, RAM: 8GB DDR4, Storage: 256GB SSD, GPU: Intel UHD Graphics, Screen: 16 inch WUXGA IPS", 4.0f, 20, 127);
        addProduct("23", "Dell Vostro 3520", 12500000, 14200000, "Dell", "Sinh viên",
                "Bền bỉ, tản nhiệt tốt, hỗ trợ đầy đủ cổng kết nối cho học tập.",
                "CPU: Intel Core i5-1235U, RAM: 8GB DDR4, Storage: 512GB SSD, GPU: Intel UHD Graphics, Screen: 15.6 inch FHD 120Hz", 4.1f, 20, 98);
        addProduct("24", "HP 15s-fq5000", 9990000, 14500000, "HP", "Sinh viên",
                "Thiết kế bo tròn mềm mại, phím bấm êm, pin khá.",
                "CPU: Intel Core i3-1215U, RAM: 8GB DDR4, Storage: 256GB SSD, GPU: Intel UHD Graphics, Screen: 15.6 inch FHD", 3.9f, 20, 134);
        addProduct("25", "Acer Aspire 3", 8490000, 12000000, "Acer", "Sinh viên",
                "Giá cực tốt, đủ dùng cho Word, Excel và lướt web.",
                "CPU: AMD Ryzen 3 7320U, RAM: 8GB LPDDR5, Storage: 256GB SSD, GPU: AMD Radeon Graphics, Screen: 15.6 inch FHD", 3.8f, 20, 156);
        addProduct("26", "Lenovo V15 G4", 9200000, 10500000, "Lenovo", "Sinh viên",
                "Thiết kế thực dụng, bản lề mở rộng, phù hợp học nhóm.",
                "CPU: AMD Ryzen 3 7320U, RAM: 8GB LPDDR5, Storage: 256GB SSD, GPU: AMD Radeon Graphics, Screen: 15.6 inch FHD TN", 3.9f, 20, 142);
        addProduct("27", "MSI Modern 15 B13M", 13500000, 15000000, "MSI", "Sinh viên",
                "Cấu hình cao trong tầm giá, vỏ nhựa giả kim loại đẹp mắt.",
                "CPU: Intel Core i5-1335U, RAM: 16GB DDR4, Storage: 512GB SSD, GPU: Intel Iris Xe Graphics, Screen: 15.6 inch FHD IPS", 4.2f, 20, 89);
        addProduct("28", "Gigabyte G5 MF", 18990000, 20900000, "Gigabyte", "Sinh viên",
                "Laptop Gaming giá rẻ nhất cho sinh viên vừa học vừa chơi.",
                "CPU: Intel Core i5-12500H, RAM: 8GB DDR4, Storage: 512GB SSD, GPU: NVIDIA GeForce RTX 4050 6GB, Screen: 15.6 inch FHD 144Hz", 4.3f, 20, 76);

        // Cảm ứng - 7 products
        addProduct("29", "HP Envy x360 13", 21900000, 23500000, "HP", "Cảm ứng",
                "Xoay gập linh hoạt, màn hình cảm ứng nhạy, bút cảm ứng đi kèm.",
                "CPU: Intel Core i5-1230U, RAM: 8GB LPDDR4x, Storage: 512GB SSD, GPU: Intel Iris Xe Graphics, Screen: 13.3 inch FHD IPS Touch", 4.4f, 20, 67);
        addProduct("30", "Dell Inspiron 14 7430 2-in-1", 19500000, 21000000, "Dell", "Cảm ứng",
                "Biến hình thành máy tính bảng, âm thanh to rõ, viền mỏng.",
                "CPU: Intel Core i5-1335U, RAM: 8GB LPDDR5, Storage: 512GB SSD, GPU: Intel Iris Xe Graphics, Screen: 14 inch FHD+ Touch", 4.3f, 20, 59);
        addProduct("31", "Asus Vivobook S 14 Flip", 17200000, 18900000, "Asus", "Cảm ứng",
                "Năng động, màn hình cảm ứng tỷ lệ 16:10, tản nhiệt IceCool.",
                "CPU: Intel Core i5-13500H, RAM: 16GB DDR4, Storage: 512GB SSD, GPU: Intel Iris Xe Graphics, Screen: 14 inch WUXGA Touch", 4.2f, 20, 71);
        addProduct("32", "Lenovo Yoga 7i", 23500000, 25000000, "Lenovo", "Cảm ứng",
                "Thiết kế bo tròn cầm nắm thoải mái, màn hình OLED rực rỡ cảm ứng.",
                "CPU: Intel Core i5-1340P, RAM: 16GB LPDDR5, Storage: 512GB SSD, GPU: Intel Iris Xe Graphics, Screen: 14 inch 2.8K OLED Touch", 4.5f, 20, 54);
        addProduct("33", "MSI Summit E13 Flip Evo", 28900000, 31000000, "MSI", "Cảm ứng",
                "Dòng doanh nhân cao cấp, bảo mật phần cứng, bút MSI Pen.",
                "CPU: Intel Core i7-1360P, RAM: 16GB LPDDR5, Storage: 1TB SSD, GPU: Intel Iris Xe Graphics, Screen: 13.4 inch FHD+ 120Hz Touch", 4.6f, 20, 42);
        addProduct("34", "Acer Spin 5", 24000000, 26500000, "Acer", "Cảm ứng",
                "Nhẹ, tích hợp bút Stylus trong thân máy, màn hình độ phân giải cao.",
                "CPU: Intel Core i5-1240P, RAM: 16GB LPDDR5, Storage: 512GB SSD, GPU: Intel Iris Xe Graphics, Screen: 14 inch WQXGA Touch", 4.4f, 20, 49);
        addProduct("35", "LG Gram 2-in-1 16", 34990000, 38000000, "LG", "Cảm ứng",
                "Máy tính lai nhẹ nhất thế giới ở kích thước 16 inch.",
                "CPU: Intel Core i7-1360P, RAM: 16GB LPDDR5, Storage: 512GB SSD, GPU: Intel Iris Xe Graphics, Screen: 16 inch WQXGA IPS Touch", 4.7f, 20, 35);

        // Laptop AI - 7 products
        addProduct("36", "Asus Zenbook 14 OLED AI", 26990000, 28990000, "Asus", "Laptop AI",
                "Trang bị chip Intel Core Ultra tích hợp NPU xử lý tác vụ AI chuyên biệt.",
                "CPU: Intel Core Ultra 5 125H, RAM: 16GB LPDDR5X, Storage: 512GB SSD, GPU: Intel Arc Graphics (NPU AI), Screen: 14 inch 3K OLED 120Hz", 4.6f, 20, 62);
        addProduct("37", "MSI Prestige 16 AI Evo", 31900000, 34000000, "MSI", "Laptop AI",
                "Hiệu năng AI vượt trội, pin dung lượng cực lớn 99.9Whr.",
                "CPU: Intel Core Ultra 7 155H, RAM: 32GB LPDDR5, Storage: 1TB SSD, GPU: Intel Arc Graphics, Screen: 16 inch QHD+ IPS", 4.7f, 20, 48);
        addProduct("38", "Acer Swift Go 14 AI", 22990000, 24500000, "Acer", "Laptop AI",
                "Tích hợp phím Copilot riêng biệt, tối ưu hóa cuộc gọi video bằng AI.",
                "CPU: Intel Core Ultra 5 125H, RAM: 16GB LPDDR5X, Storage: 512GB SSD, GPU: Intel Arc Graphics, Screen: 14 inch 2.8K OLED", 4.5f, 20, 71);
        addProduct("39", "Lenovo Yoga 9i 2-in-1", 38900000, 41900000, "Lenovo", "Laptop AI",
                "Thiết kế sang trọng như trang sức, chip AI hỗ trợ sáng tạo nội dung.",
                "CPU: Intel Core Ultra 7 155H, RAM: 16GB LPDDR5X, Storage: 1TB SSD, GPU: Intel Arc Graphics, Screen: 14 inch 4K OLED Touch", 4.8f, 20, 39);
        addProduct("40", "HP Omen Transcend 14", 41500000, 45000000, "HP", "Laptop AI",
                "Laptop Gaming AI nhẹ nhất thế giới, làm mát bằng AI.",
                "CPU: Intel Core Ultra 7 155H, RAM: 16GB LPDDR5X, Storage: 1TB SSD, GPU: NVIDIA GeForce RTX 4060 8GB, Screen: 14 inch 2.8K OLED 120Hz", 4.7f, 20, 34);
        addProduct("41", "Dell XPS 14 (9440)", 49900000, 52000000, "Dell", "Laptop AI",
                "Thiết kế nhôm nguyên khối, chip AI tối ưu hiệu năng và pin.",
                "CPU: Intel Core Ultra 7 155H, RAM: 16GB LPDDR5X, Storage: 512GB SSD, GPU: NVIDIA GeForce RTX 4050 6GB, Screen: 14.5 inch FHD+ InfinityEdge", 4.8f, 20, 28);
        addProduct("42", "Gigabyte AERO 14 OLED AI", 33500000, 36000000, "Gigabyte", "Laptop AI",
                "Màn hình chuẩn màu điện ảnh, AI hỗ trợ render nhanh chóng.",
                "CPU: Intel Core i7-13700H, RAM: 16GB LPDDR5, Storage: 1TB SSD, GPU: NVIDIA GeForce RTX 4050 6GB, Screen: 14 inch 2.8K OLED", 4.6f, 20, 44);

        // Đồ họa- Kỹ thuật - 7 products
        addProduct("43", "Dell Precision 3581", 39500000, 42000000, "Dell", "Đồ họa- Kỹ thuật",
                "Workstation di động bền bỉ, chuyên chạy CAD, Revit mượt mà.",
                "CPU: Intel Core i7-13800H, RAM: 16GB DDR5, Storage: 512GB SSD, GPU: NVIDIA RTX A1000 6GB, Screen: 15.6 inch FHD IPS", 4.5f, 20, 34);
        addProduct("44", "HP ZBook Firefly 16 G10", 36500000, 38900000, "HP", "Đồ họa- Kỹ thuật",
                "Máy trạm mỏng nhẹ, màn hình DreamColor chuẩn màu.",
                "CPU: Intel Core i7-1360P, RAM: 32GB DDR5, Storage: 1TB SSD, GPU: NVIDIA RTX A500 4GB, Screen: 16 inch WUXGA IPS", 4.6f, 20, 28);
        addProduct("45", "Lenovo ThinkPad P1 Gen 6", 58900000, 62000000, "Lenovo", "Đồ họa- Kỹ thuật",
                "Sức mạnh tối thượng trong thân hình mỏng nhẹ, bàn phím huyền thoại.",
                "CPU: Intel Core i7-13800H, RAM: 32GB DDR5, Storage: 1TB SSD, GPU: NVIDIA RTX 2000 Ada 8GB, Screen: 16 inch WQXGA IPS", 4.7f, 20, 22);
        addProduct("46", "Asus ProArt Studiobook 16", 54900000, 58000000, "Asus", "Đồ họa- Kỹ thuật",
                "Có núm xoay vật lý Asus Dial độc đáo cho Adobe, màn hình OLED 3.2K.",
                "CPU: Intel Core i9-13980HX, RAM: 32GB DDR5, Storage: 1TB SSD, GPU: NVIDIA GeForce RTX 4070 8GB, Screen: 16 inch 3.2K OLED 120Hz", 4.8f, 20, 19);
        addProduct("47", "MSI Creator Z17 HX Studio", 64500000, 68000000, "MSI", "Đồ họa- Kỹ thuật",
                "Thiết kế CNC nguyên khối, đạt chuẩn NVIDIA Studio cho sáng tạo.",
                "CPU: Intel Core i9-13950HX, RAM: 64GB DDR5, Storage: 2TB SSD, GPU: NVIDIA GeForce RTX 4070 8GB, Screen: 17 inch QHD+ 165Hz Touch", 4.7f, 20, 16);
        addProduct("48", "Gigabyte Aero 16 OLED", 45900000, 49000000, "Gigabyte", "Đồ họa- Kỹ thuật",
                "Màn hình 4K OLED cân chỉnh màu X-Rite từng máy tại nhà máy.",
                "CPU: Intel Core i9-13900H, RAM: 32GB LPDDR5, Storage: 1TB SSD, GPU: NVIDIA GeForce RTX 4070 8GB, Screen: 16 inch 4K OLED", 4.8f, 20, 19);
        addProduct("49", "Razer Blade 16 Studio", 84900000, 89000000, "Razer", "Đồ họa- Kỹ thuật",
                "Màn hình Mini-LED chế độ kép (UHD+ 120Hz hoặc FHD+ 240Hz).",
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
}
