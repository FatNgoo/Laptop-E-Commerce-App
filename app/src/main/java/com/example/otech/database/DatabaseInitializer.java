package com.example.otech.database;

import android.content.Context;
import android.util.Log;

import com.example.otech.model.Product;
import com.example.otech.model.Review;
import com.example.otech.model.User;
import com.example.otech.repository.DataRepository;

import java.util.ArrayList;
import java.util.Date;

/**
 * DatabaseInitializer - Seed initial mock data vào Room Database
 * Chạy lần đầu tiên khi database trống
 */
public class DatabaseInitializer {
    private static final String TAG = "DatabaseInitializer";
    
    public interface InitCallback {
        void onComplete();
        void onError(Exception e);
    }
    
    public static void initializeDatabase(Context context, InitCallback callback) {
        DataRepository repository = DataRepository.getInstance(context);
        
        // Check xem database đã có data chưa
        repository.isDatabaseEmpty(new DataRepository.DataCallback<Boolean>() {
            @Override
            public void onSuccess(Boolean isEmpty) {
                if (isEmpty) {
                    Log.d(TAG, "Database is empty, initializing with mock data...");
                    seedInitialData(repository, context, callback);
                } else {
                    Log.d(TAG, "Database already has data, skipping initialization");
                    callback.onComplete();
                }
            }
            
            @Override
            public void onError(Exception e) {
                Log.e(TAG, "Error checking database: " + e.getMessage());
                callback.onError(e);
            }
        });
    }
    
    private static void seedInitialData(DataRepository repository, Context context, InitCallback callback) {
        // Seed users trước
        seedUsers(repository, new DataRepository.VoidCallback() {
            @Override
            public void onSuccess() {
                // Sau đó seed products
                seedProducts(repository, context, new DataRepository.VoidCallback() {
                    @Override
                    public void onSuccess() {
                        // Seed banners
                        seedBanners(repository, context, new DataRepository.VoidCallback() {
                            @Override
                            public void onSuccess() {
                                // Cuối cùng seed reviews
                                seedReviews(repository, callback);
                            }
                            
                            @Override
                            public void onError(Exception e) {
                                Log.e(TAG, "Error seeding banners: " + e.getMessage());
                                callback.onError(e);
                            }
                        });
                    }
                    
                    @Override
                    public void onError(Exception e) {
                        Log.e(TAG, "Error seeding products: " + e.getMessage());
                        callback.onError(e);
                    }
                });
            }
            
            @Override
            public void onError(Exception e) {
                Log.e(TAG, "Error seeding users: " + e.getMessage());
                callback.onError(e);
            }
        });
    }
    
    private static void seedUsers(DataRepository repository, DataRepository.VoidCallback callback) {
        ArrayList<User> users = new ArrayList<>();
        
        users.add(new User("1", "admin", "admin", "Admin User",
                "0123456789", "123 Admin St", "admin@otech.com", "admin"));
        users.add(new User("2", "user", "user", "Test User",
                "0987654321", "456 User Ave", "user@otech.com", "user"));
        
        // Use repository method to insert
        repository.insertAllUsers(users, new DataRepository.VoidCallback() {
            @Override
            public void onSuccess() {
                Log.d(TAG, "Seeded " + users.size() + " users");
                callback.onSuccess();
            }
            
            @Override
            public void onError(Exception e) {
                Log.e(TAG, "Error seeding users: " + e.getMessage());
                callback.onError(e);
            }
        });
    }
    
    private static void seedProducts(DataRepository repository, Context context, DataRepository.VoidCallback callback) {
        ArrayList<Product> products = new ArrayList<>();
        
        // Văn phòng (7 products - ID 1-7)
        products.add(createProductWithImages("1", "Dell Inspiron 15 3530", 16990000, 18500000, "Dell", "Văn phòng",
                "Dell Inspiron 15 3530 là người bạn đồng hành lý tưởng cho mọi công việc văn phòng. Với thiết kế thanh lịch, vỏ nhựa chắc chắn cùng bàn phím full-size có NumPad tiện lợi, laptop này mang đến trải nghiệm nhập liệu thoải mái cho các tác vụ Excel, Word hay nhập liệu chuyên nghiệp. Màn hình 15.6 inch FHD 120Hz cho hình ảnh sắc nét, mượt mà khi cuộn tài liệu. Chip Intel Core i5-1335U thế hệ 13 kết hợp RAM 16GB DDR4 đảm bảo đa nhiệm mượt mà, từ xử lý bảng tính phức tạp đến họp video Teams, Zoom. Ổ SSD NVMe 512GB khởi động nhanh chóng và lưu trữ đủ dùng. Cổng kết nối phong phú với HDMI, USB-A, USB-C giúp dễ dàng mở rộng thiết bị ngoại vi.",
                "CPU: Intel Core i5-1335U, RAM: 16GB DDR4 3200MHz, Storage: 512GB SSD NVMe, GPU: Intel UHD Graphics, Screen: 15.6 inch FHD 120Hz",
                4.2f, 20, context, new String[]{"laptopdellinspiron1", "laptopdellinspiron2", "laptopdellinspiron3", "laptopdellinspiron4", "laptopdellinspiron5", "laptopdellinspiron6"}));
        
        products.add(createProductWithImages("2", "HP Pavilion 15-eg3000", 17500000, 19200000, "HP", "Văn phòng",
                "HP Pavilion 15-eg3000 nổi bật với thiết kế vỏ kim loại sang trọng, mỏng nhẹ chỉ 1.75kg dễ dàng mang theo. Laptop tích hợp công nghệ âm thanh B&O chất lượng cao, tạo trải nghiệm giải trí đỉnh cao khi xem phim hay nghe nhạc giữa giờ nghỉ. Chip Intel Core i5-13500H thế hệ 13 mạnh mẽ cùng Intel Iris Xe Graphics xử lý tốt các tác vụ đồ họa cơ bản như chỉnh sửa ảnh, làm slide thuyết trình. RAM 16GB DDR4 đảm bảo mở nhiều tab Chrome, chạy đồng thời Office và phần mềm công việc không giật lag. Màn hình IPS 15.6 inch FHD với góc nhìn rộng 178 độ, màu sắc chính xác phù hợp cho công việc thiết kế đồ họa nhẹ. Pin 41Wh kết hợp sạc nhanh HP Fast Charge cho thời gian sử dụng linh hoạt cả ngày dài.",
                "CPU: Intel Core i5-13500H, RAM: 16GB DDR4, Storage: 512GB SSD, GPU: Intel Iris Xe Graphics, Screen: 15.6 inch FHD IPS",
                4.3f, 20, context, new String[]{"laptophpvictus1", "laptophpvictus2", "laptophpvictus3", "laptophpvictus4", "laptophpvictus5"}));
        
        products.add(createProductWithImages("3", "Asus Vivobook Go 15", 12990000, 14500000, "Asus", "Văn phòng",
                "Asus Vivobook Go 15 là lựa chọn hoàn hảo cho những ai cần một chiếc laptop văn phòng giá rẻ nhưng vẫn đảm bảo chất lượng. Thiết kế mỏng nhẹ chỉ 1.63kg với bản lề ErgoLift độc quyền nâng bàn phím lên 3 độ, tạo góc gõ thoải mái và cải thiện tản nhiệt. Laptop đạt chuẩn độ bền quân đội MIL-STD-810H, chịu được va đập, rơi từ độ cao 122cm, phù hợp cho người hay di chuyển. Màn hình 15.6 inch FHD OLED với độ phủ màu 100% DCI-P3 mang đến hình ảnh sống động, màu đen sâu thẳm tuyệt đẹp. Chip AMD Ryzen 5 7520U tiết kiệm điện nhưng vẫn mạnh mẽ cho văn phòng, kết hợp RAM LPDDR5 16GB tốc độ cao. Tản nhiệt IceCool Plus giữ máy mát mẻ ngay cả khi làm việc liên tục nhiều giờ.",
                "CPU: AMD Ryzen 5 7520U, RAM: 16GB LPDDR5, Storage: 512GB SSD, GPU: AMD Radeon Graphics, Screen: 15.6 inch FHD OLED",
                4.1f, 20, context, new String[]{"laptopasusvivobook1", "laptopasusvivobook2", "laptopasusvivobook3", "laptopasusvivobook4", "laptopasusvivobook5", "laptopasusvivobook6", "laptopasusvivobook7"}));
        
        products.add(createProductWithImages("4", "Acer Aspire 5 Slim", 13990000, 15900000, "Acer", "Văn phòng",
                "Acer Aspire 5 Slim kết hợp hoàn hảo giữa hiệu năng và tính di động với thiết kế slim chỉ 17.9mm, nặng 1.7kg. Laptop trang bị chip Intel Core i5-13420H - CPU thế hệ 13 với 8 nhân 12 luồng, xung nhịp Turbo lên đến 4.6GHz, xử lý mượt mà Excel với hàng ngàn dòng dữ liệu hay render PowerPoint nặng. RAM DDR5 16GB băng thông cao gấp đôi DDR4, đảm bảo tốc độ đa nhiệm vượt trội. Hệ thống tản nhiệt dual-fan với thiết kế Acer TwinAir, cùng lớp phủ nhiệt ở bề mặt giúp máy luôn mát ngay cả khi tải nặng. Màn hình IPS 14 inch viền mỏng 3 cạnh mang lại tỷ lệ màn hình/thân máy 82%, trải nghiệm làm việc nhập vai hơn. Cổng USB Type-C hỗ trợ Power Delivery sạc nhanh và xuất hình HDMI tiện lợi.",
                "CPU: Intel Core i5-13420H, RAM: 16GB DDR5, Storage: 512GB SSD, GPU: Intel UHD Graphics, Screen: 14 inch FHD IPS",
                4.2f, 20, context, new String[]{"laptopaceraspire1", "laptopaceraspire2", "laptopaceraspire3", "laptopaceraspire4"}));
        
        products.add(createProductWithImages("5", "Lenovo IdeaPad Slim 5", 18990000, 20500000, "Lenovo", "Văn phòng",
                "Lenovo IdeaPad Slim 5 là biểu tượng của sự thanh lịch và hiệu suất cao cấp trong phân khúc văn phòng. Thiết kế tối giản với vỏ nhôm cao cấp Storm Grey sang trọng, mỏng chỉ 16.9mm và nhẹ 1.89kg. Màn hình 16 inch WUXGA (1920x1200) tỷ lệ 16:10 cho diện tích hiển thị lớn hơn 11% so với 16:9 thông thường, giúp làm việc với nhiều cửa sổ hiệu quả hơn. Chip Intel Core i5-1340P với 12 nhân 16 luồng, kết hợp RAM LPDDR5 16GB hàn onboard tốc độ 4800MHz mang đến hiệu năng mạnh mẽ. Webcam 1080p FHD với nắp đóng/mở Privacy Shutter bảo vệ quyền riêng tư tuyệt đối. Pin 57Wh kết hợp công nghệ Rapid Charge Boost sạc nhanh 80% chỉ trong 1 giờ, làm việc suốt ngày dài không lo hết pin. Bàn phím có đèn nền trắng tiện lợi cho môi trường thiếu sáng.",
                "CPU: Intel Core i5-1340P, RAM: 16GB LPDDR5, Storage: 512GB SSD, GPU: Intel Iris Xe Graphics, Screen: 16 inch WUXGA IPS",
                4.4f, 20, context, new String[]{"laptoplenovoidea1", "laptoplenovoidea2", "laptoplenovoidea3", "laptoplenovoidea4", "laptoplenovoidea5", "laptoplenovoidea6"}));
        
        products.add(createProductWithImages("6", "MSI Modern 14 C13M", 11990000, 13500000, "MSI", "Văn phòng",
                "MSI Modern 14 C13M là laptop siêu di động dành cho dân văn phòng hiện đại với trọng lượng chỉ 1.4kg - nhẹ nhất phân khúc. Thiết kế thanh lịch với vỏ nhựa giả kim loại cao cấp, màu Carbon Grey trẻ trung phù hợp mọi lứa tuổi. Màn hình 14 inch FHD IPS có viền mỏng giúp máy gọn gàng dễ cho vào balo. Chip Intel Core i3-1315U thế hệ 13 với kiến trúc hybrid (2P-core + 4E-core) tiết kiệm điện nhưng vẫn đủ mạnh cho Word, Excel, PowerPoint, lướt web đa tab. RAM 8GB DDR4 đủ dùng cho văn phòng cơ bản, có thể nâng cấp lên 32GB nếu cần. Ổ SSD 512GB PCIe Gen3 đảm bảo khởi động Windows chỉ 8 giây. Bàn phím full-size với NumPad ảo trên touchpad - tính năng độc đáo giúp nhập số nhanh chóng. Pin 3-cell 39.3Wh cho thời gian sử dụng 6-7 tiếng.",
                "CPU: Intel Core i3-1315U, RAM: 8GB DDR4, Storage: 512GB SSD, GPU: Intel UHD Graphics, Screen: 14 inch FHD IPS",
                4.0f, 20, context, new String[]{"laptop1", "laptop2", "laptop3", "laptop4", "laptop5"}));
        
        products.add(createProductWithImages("7", "LG Gram 14 2023", 24990000, 28900000, "LG", "Văn phòng",
                "LG Gram 14 2023 là chiếc laptop siêu nhẹ kỷ lục thế giới với trọng lượng chỉ 999g - nhẹ hơn cả 1 chai nước. Dù nhẹ như vậy nhưng laptop vẫn đạt chuẩn độ bền quân đội MIL-STD-810G, chịu được sốc, rung lắc, nhiệt độ khắc nghiệt. Vỏ máy bằng hợp kim Carbon-Magnesium siêu bền siêu nhẹ, màu Obsidian Black hoặc Snow White thanh lịch. Màn hình IPS 14 inch WUXGA (1920x1200) tỷ lệ 16:10, độ sáng 350 nits rõ ràng ngay cả ngoài trời nắng. Chip Intel Core i5-1340P với 12 nhân cùng RAM LPDDR5 16GB hàn liền cho hiệu năng tốt, Intel Iris Xe Graphics xử lý nhẹ nhàng Photoshop, Illustrator. Thời lượng pin là điểm cực mạnh: 72Wh cho 20.5 giờ sử dụng liên tục - đi công tác cả ngày không cần mang sạc. Cổng kết nối đầy đủ: 2x Thunderbolt 4, 2x USB-A, HDMI, Jack tai nghe, khe thẻ SD.",
                "CPU: Intel Core i5-1340P, RAM: 16GB LPDDR5, Storage: 512GB SSD, GPU: Intel Iris Xe Graphics, Screen: 14 inch WUXGA IPS",
                4.5f, 20, context, new String[]{"laptoplggram1", "laptoplggram2", "laptoplggram3", "laptoplggram4", "laptoplggram5", "laptoplggram6"}));
        
        // Gaming (7 products - ID 8-14)
        products.add(createProductWithImages("8", "Asus ROG Strix G16", 39990000, 45000000, "Asus", "Gaming",
                "Asus ROG Strix G16 là chiến thần gaming chân chính với sức mạnh vô song từ chip Intel Core i9-13980HX - CPU gaming mạnh nhất hiện tại với 24 nhân 32 luồng, xung nhịp Turbo 5.6GHz. Kết hợp cùng RTX 4060 8GB GDDR6 với công nghệ DLSS 3.0 và Ray Tracing thế hệ mới, laptop này chiến mượt mọi tựa game AAA ở setting Ultra, từ Cyberpunk 2077, Elden Ring đến Call of Duty. Màn hình 16 inch QHD+ (2560x1600) tần số quét 240Hz với độ trễ chỉ 3ms mang đến trải nghiệm gaming siêu mượt, hỗ trợ G-Sync chống xé hình. Hệ thống tản nhiệt ROG Intelligent Cooling với 3 quạt Arc Flow, 7 ống đồng và kim loại lỏng Conductonaut Extreme trên CPU giúp nhiệt độ luôn ổn định ngay cả khi overclock. LED RGB Aura Sync 4 vùng trên bàn phím với hiệu ứng đèn tùy chỉnh theo từng game. Âm thanh Dolby Atmos 6 loa cho trải nghiệm âm thanh vòm sống động.",
                "CPU: Intel Core i9-13980HX, RAM: 16GB DDR5, Storage: 1TB SSD NVMe, GPU: NVIDIA GeForce RTX 4060 8GB, Screen: 16 inch QHD+ 240Hz",
                4.6f, 20, context, new String[]{"laptopasusrog1", "laptopasusrog2", "laptopasusrog3", "laptopasusrog4", "laptopasusrog5", "laptopasusrog6", "laptopasusrog7"}));
        
        products.add(createProductWithImages("9", "MSI Katana 15", 28500000, 32000000, "MSI", "Gaming",
                "MSI Katana 15 mang đến hiệu năng gaming đỉnh cao với mức giá cực kỳ hợp lý cho game thủ. Thiết kế lấy cảm hứng từ thanh kiếm samurai Nhật Bản với những đường cắt góc cạnh độc đáo trên nắp máy. Chip Intel Core i7-13620H thế hệ 13 với 10 nhân 16 luồng kết hợp RTX 4050 6GB cho khả năng chiến game 1080p ở 60-144 FPS tùy tựa game, đủ sức với Valorant, League of Legends, PUBG, Apex Legends ở setting cao. RAM DDR5 16GB 4800MHz có thể nâng cấp lên 64GB cho tương lai. Màn hình 15.6 inch FHD 144Hz IPS-Level với độ phủ màu 45% NTSC mang đến hình ảnh rõ nét, mượt mà. Bàn phím gaming với đèn nền đỏ đặc trưng MSI, phím WASD được bao viền nổi bật, hành trình phím 1.5mm cho cảm giác gõ tốt. Tản nhiệt Cooler Boost 5 với 2 quạt và 6 ống đồng đảm bảo nhiệt độ ổn định khi chơi game marathon.",
                "CPU: Intel Core i7-13620H, RAM: 16GB DDR5, Storage: 1TB SSD, GPU: NVIDIA GeForce RTX 4050 6GB, Screen: 15.6 inch FHD 144Hz",
                4.5f, 20, context, new String[]{"laptopmsikatana1", "laptopmsikatana2", "laptopmsikatana3", "laptopmsikatana4", "laptopmsikatana5", "laptopmsikatana6", "laptopmsikatana7"}));
        
        products.add(createProductWithImages("10", "Acer Predator Helios Neo 16", 35990000, 38900000, "Acer", "Gaming",
                "Acer Predator Helios Neo 16 là dòng laptop gaming cao cấp với công nghệ tản nhiệt AeroBlade 3D thế hệ mới - quạt kim loại siêu mỏng với 89 cánh có cấu trúc 3 chiều giúp tăng luồng gió 10% so với thế hệ trước. Chip Intel Core i7-13700HX với 16 nhân 24 luồng là con chip HX mạnh mẽ nhất của Intel, kết hợp RTX 4060 8GB với TGP 140W cho hiệu năng gaming tối đa. Màn hình 16 inch WQXGA (2560x1600) tỷ lệ 16:10 cho diện tích hiển thị rộng hơn, tần số quét 165Hz với Adaptive Sync chống giật lag. Phím Turbo độc quyền giúp boost hiệu năng lên mức tối đa chỉ với 1 nút bấm, CPU và GPU sẽ chạy ở công suất cao nhất. Bàn phím RGB 4 vùng với keycap POM chống bóng, độ nảy tốt cho gaming lâu không mỏi tay. Cổng kết nối đầy đủ: Thunderbolt 4, HDMI 2.1, USB 3.2, RJ-45 Killer Ethernet cho kết nối mạng ổn định nhất.",
                "CPU: Intel Core i7-13700HX, RAM: 16GB DDR5, Storage: 512GB SSD, GPU: NVIDIA GeForce RTX 4060 8GB, Screen: 16 inch WQXGA 165Hz",
                4.6f, 20, context, new String[]{"laptopaceraspire1", "laptopaceraspire2", "laptopaceraspire3", "laptopaceraspire4"}));
        
        products.add(createProductWithImages("11", "Lenovo Legion Pro 5", 38990000, 42000000, "Lenovo", "Gaming",
                "Lenovo Legion Pro 5 là laptop gaming với thiết kế tối giản, không có LED RGB lòe loẹt nhưng ẩn chứa hiệu năng khủng bên trong. Chip AMD Ryzen 7 7745HX với 8 nhân 16 luồng kiến trúc Zen 4 mạnh mẽ, tiết kiệm điện hơn Intel, kết hợp RTX 4060 8GB với TGP 140W - cao nhất phân khúc giá này. Màn hình 16 inch WQXGA 240Hz với độ phủ màu 100% sRGB, độ sáng 500 nits chuẩn màu Pantone X-Rite từ nhà máy - phù hợp cả cho content creator, designer. Hệ thống tản nhiệt Legion Coldfront 5.0 với vapor chamber lớn, 4 khe tản nhiệt và phần mềm AI Engine++ tự động điều chỉnh fan curve thông minh. Bàn phím TrueStrike với hành trình phím 1.5mm, lực nhấn 70g cho cảm giác gõ chắc chắn như bàn phím cơ. RAM DDR5 16GB có thể nâng cấp lên 32GB, 2 khe M.2 cho SSD mở rộng. Pin 80Wh cho thời gian chơi game lên đến 4-5 giờ.",
                "CPU: AMD Ryzen 7 7745HX, RAM: 16GB DDR5, Storage: 1TB SSD, GPU: NVIDIA GeForce RTX 4060 8GB, Screen: 16 inch WQXGA 240Hz",
                4.5f, 20, context, new String[]{"laptoplenovolegion1", "laptoplenovolegion2", "laptoplenovolegion3", "laptoplenovolegion4", "laptoplenovolegion5", "laptoplenovolegion6", "laptoplenovolegion7", "laptoplenovolegion8"}));
        
        products.add(createProductWithImages("12", "Gigabyte Aorus 15", 33990000, 36500000, "Gigabyte", "Gaming",
                "Gigabyte Aorus 15 là laptop gaming siêu mỏng với độ dày chỉ 23mm - mỏng nhất trong số các laptop gaming có RTX 4070. Thiết kế với logo Aorus Falcon phát sáng RGB ở nắp máy cực ngầu. Chip Intel Core i7-13700H thế hệ 13 với 14 nhân kết hợp RTX 4070 8GB cho hiệu năng gaming cao, có thể chơi game 1440p ở setting Ultra mượt mà. Màn hình 15.6 inch QHD (2560x1440) 165Hz với công nghệ Pantone Calibrated Factory cho màu sắc chuẩn xác. Tính năng độc đáo là AI Engine giúp tối ưu hiệu năng dựa trên tựa game đang chơi - tự động điều chỉnh CPU, GPU, fan để đạt hiệu suất tốt nhất. Bàn phím RGB per-key 16.7 triệu màu với phần mềm Aorus Control Center tùy chỉnh từng phím, tạo hiệu ứng đèn đồng bộ với in-game. Âm thanh DTS:X Ultra 3D cho trải nghiệm âm thanh vòm sống động. Cân nặng chỉ 2.2kg rất nhẹ cho gaming laptop.",
                "CPU: Intel Core i7-13700H, RAM: 16GB DDR5, Storage: 1TB SSD, GPU: NVIDIA GeForce RTX 4070 8GB, Screen: 15.6 inch QHD 165Hz",
                4.4f, 20, context, new String[]{"laptop1", "laptop2", "laptop3", "laptop4", "laptop5"}));
        
        products.add(createProductWithImages("13", "Razer Blade 15", 59990000, 65000000, "Razer", "Gaming",
                "Razer Blade 15 là biểu tượng của laptop gaming cao cấp với thiết kế vỏ nhôm CNC nguyên khối màu đen tuyền (Anodized Black) sang trọng như MacBook. Độ dày chỉ 16.8mm với trọng lượng 2.01kg - gọn nhẹ nhất phân khúc high-end. Chip Intel Core i7-13800H với 14 nhân kết hợp RTX 4070 8GB TGP 140W cho hiệu năng gaming đỉnh cao. Màn hình QHD (2560x1440) 240Hz với độ phủ màu 100% DCI-P3, chuẩn màu Calman từ nhà máy - đẹp nhất trong các laptop gaming. Bàn phím Razer Chroma RGB với đèn nền per-key 16.7 triệu màu, có thể đồng bộ với chuột, tai nghe Razer tạo hệ sinh thái đồng bộ. Tản nhiệt vapor chamber lớn kết hợp fan siêu mỏng đảm bảo nhiệt độ luôn ổn định. THX Spatial Audio cho âm thanh 360 độ cực chất. Cổng Thunderbolt 4, HDMI 2.1 hỗ trợ xuất hình 4K 120Hz ra TV, màn hình ngoài.",
                "CPU: Intel Core i7-13800H, RAM: 16GB DDR5, Storage: 1TB SSD, GPU: NVIDIA GeForce RTX 4070 8GB, Screen: 15.6 inch QHD 240Hz",
                4.7f, 20, context, new String[]{"laptop1", "laptop2", "laptop3", "laptop4", "laptop5"}));
        
        products.add(createProductWithImages("14", "Dell Alienware M16", 49990000, 55000000, "Dell", "Gaming",
                "Dell Alienware M16 là biểu tượng gaming laptop với thiết kế Legend 3.0 độc đáo - viền LED AlienFX RGB 360 độ bao quanh máy với 13 vùng đèn có thể tùy chỉnh màu sắc, hiệu ứng theo ý thích hoặc đồng bộ với game. Logo Alienware phát sáng ở nắp máy cực kỳ nổi bật. Chip Intel Core i9-13900HX với 24 nhân 32 luồng - CPU gaming mạnh nhất hiện tại, kết hợp RTX 4070 8GB và RAM DDR5 32GB cho hiệu năng khủng. Màn hình 16 inch QHD+ (2560x1600) 165Hz với công nghệ ComfortView Plus giảm ánh sáng xanh bảo vệ mắt khi chơi game lâu. Hệ thống tản nhiệt Cryo-Tech với vapor chamber lớn, 4 quạt và Element 31 (gallium-silicone) trên CPU giúp giảm nhiệt độ đến 25%. Bàn phím Cherry MX Ultra-Low-Profile mechanical với hành trình 3.5mm, cảm giác gõ như bàn phím cơ thật sự. Âm thanh Dolby Atmos 6 loa tổng công suất 12W cực đỉnh.",
                "CPU: Intel Core i9-13900HX, RAM: 32GB DDR5, Storage: 1TB SSD, GPU: NVIDIA GeForce RTX 4070 8GB, Screen: 16 inch QHD+ 165Hz",
                4.8f, 20, context, new String[]{"laptopdellinspiron1", "laptopdellinspiron2", "laptopdellinspiron3", "laptopdellinspiron4", "laptopdellinspiron5", "laptopdellinspiron6"}));
        
        // Mỏng nhẹ (7 products - ID 15-21)
        products.add(createProductWithImages("15", "Asus Zenbook S 13 OLED", 36990000, 39900000, "Asus", "Mỏng nhẹ",
                "Laptop OLED mỏng nhất thế giới, vỏ gốm plasma độc đáo.",
                "CPU: Intel Core i7-1355U, RAM: 32GB LPDDR5, Storage: 1TB SSD, GPU: Intel Iris Xe Graphics, Screen: 13.3 inch 2.8K OLED",
                4.6f, 20, context, new String[]{"laptopasusvivobook1", "laptopasusvivobook2", "laptopasusvivobook3"}));
        
        products.add(createProductWithImages("16", "Dell XPS 13 Plus", 44990000, 48000000, "Dell", "Mỏng nhẹ",
                "Thiết kế tương lai với bàn phím tràn viền và trackpad vô hình.",
                "CPU: Intel Core i7-1360P, RAM: 16GB LPDDR5, Storage: 512GB SSD, GPU: Intel Iris Xe Graphics, Screen: 13.4 inch 3.5K OLED Touch",
                4.7f, 20, context, new String[]{"laptopdellinspiron1", "laptopdellinspiron2", "laptopdellinspiron3"}));
        
        products.add(createProductWithImages("17", "HP Spectre x360 14", 39900000, 42500000, "HP", "Mỏng nhẹ",
                "Xoay gập 360 độ, thiết kế vát cắt kim cương, bảo mật vân tay.",
                "CPU: Intel Core i7-1355U, RAM: 16GB LPDDR4x, Storage: 1TB SSD, GPU: Intel Iris Xe Graphics, Screen: 13.5 inch 3K2K OLED",
                4.6f, 20, context, new String[]{"laptophpvictus1", "laptophpvictus2", "laptophpvictus3", "laptophpvictus4", "laptophpvictus5"}));
        
        products.add(createProductWithImages("18", "Lenovo Yoga Slim 7i Carbon", 26990000, 29900000, "Lenovo", "Mỏng nhẹ",
                "Vỏ Carbon siêu bền siêu nhẹ, màu trắng tinh khôi.",
                "CPU: Intel Core i5-1340P, RAM: 16GB LPDDR5, Storage: 512GB SSD, GPU: Intel Iris Xe Graphics, Screen: 13.3 inch 2.5K 90Hz",
                4.5f, 20, context, new String[]{"laptoplenovoidea1", "laptoplenovoidea2", "laptoplenovoidea3", "laptoplenovoidea4", "laptoplenovoidea5", "laptoplenovoidea6"}));
        
        products.add(createProductWithImages("19", "MacBook Air M2 13 inch", 24990000, 27990000, "Apple", "Mỏng nhẹ",
                "Thiết kế phẳng mới, chip M2 mạnh mẽ, pin cả ngày dài.",
                "CPU: Apple M2 (8-core CPU), RAM: 8GB Unified, Storage: 256GB SSD, GPU: Apple M2 (8-core GPU), Screen: 13.6 inch Liquid Retina",
                4.8f, 20, context, new String[]{"laptopmacbookair1", "laptopmacbookair2", "laptopmacbookair3"}));
        
        products.add(createProductWithImages("20", "Acer Swift Go 14", 18990000, 21000000, "Acer", "Mỏng nhẹ",
                "Màn hình OLED rực rỡ, trọng lượng nhẹ, đầy đủ cổng kết nối.",
                "CPU: Intel Core i5-13500H, RAM: 16GB LPDDR5, Storage: 512GB SSD, GPU: Intel Iris Xe Graphics, Screen: 14 inch 2.8K OLED 90Hz",
                4.4f, 20, context, new String[]{"laptopaceraspire2", "laptopaceraspire3", "laptopaceraspire4"}));
        
        products.add(createProductWithImages("21", "LG Gram SuperSlim", 31990000, 35000000, "LG", "Mỏng nhẹ",
                "Mỏng như quyển tạp chí (10.9mm), màn hình OLED chống chói.",
                "CPU: Intel Core i5-1340P, RAM: 16GB LPDDR5, Storage: 512GB SSD, GPU: Intel Iris Xe Graphics, Screen: 15.6 inch FHD OLED",
                4.6f, 20, context, new String[]{"laptoplggram4", "laptoplggram5", "laptoplggram6"}));
        
        // Sinh viên (7 products - ID 22-28)
        products.add(createProductWithImages("22", "Asus Vivobook 16", 10990000, 12500000, "Asus", "Sinh viên",
                "Màn hình lớn 16 inch làm việc đa nhiệm, giá rẻ cho sinh viên.",
                "CPU: Intel Core i3-1315U, RAM: 8GB DDR4, Storage: 256GB SSD, GPU: Intel UHD Graphics, Screen: 16 inch WUXGA IPS",
                4.0f, 20, context, new String[]{"laptopasusvivobook4", "laptopasusvivobook5", "laptopasusvivobook6"}));
        
        products.add(createProductWithImages("23", "Dell Vostro 3520", 12500000, 14200000, "Dell", "Sinh viên",
                "Bền bỉ, tản nhiệt tốt, hỗ trợ đầy đủ cổng kết nối cho học tập.",
                "CPU: Intel Core i5-1235U, RAM: 8GB DDR4, Storage: 512GB SSD, GPU: Intel UHD Graphics, Screen: 15.6 inch FHD 120Hz",
                4.1f, 20, context, new String[]{"laptopdellinspiron1", "laptopdellinspiron2", "laptopdellinspiron3"}));
        
        products.add(createProductWithImages("24", "HP 15s-fq5000", 9990000, 11900000, "HP", "Sinh viên",
                "Thiết kế bo tròn mềm mại, phím bấm êm, pin khá.",
                "CPU: Intel Core i3-1215U, RAM: 8GB DDR4, Storage: 256GB SSD, GPU: Intel UHD Graphics, Screen: 15.6 inch FHD",
                3.9f, 20, context, new String[]{"laptophpvictus2", "laptophpvictus3", "laptophpvictus4"}));
        
        products.add(createProductWithImages("25", "Acer Aspire 3", 8490000, 9500000, "Acer", "Sinh viên",
                "Giá cực tốt, đủ dùng cho Word, Excel và lướt web.",
                "CPU: AMD Ryzen 3 7320U, RAM: 8GB LPDDR5, Storage: 256GB SSD, GPU: AMD Radeon Graphics, Screen: 15.6 inch FHD",
                3.8f, 20, context, new String[]{"laptopaceraspire1", "laptopaceraspire2", "laptopaceraspire3"}));
        
        products.add(createProductWithImages("26", "Lenovo V15 G4", 9200000, 10500000, "Lenovo", "Sinh viên",
                "Thiết kế thực dụng, bản lề mở rộng, phù hợp học nhóm.",
                "CPU: AMD Ryzen 3 7320U, RAM: 8GB LPDDR5, Storage: 256GB SSD, GPU: AMD Radeon Graphics, Screen: 15.6 inch FHD TN",
                3.8f, 20, context, new String[]{"laptoplenovoidea1", "laptoplenovoidea2", "laptoplenovoidea3"}));
        
        products.add(createProductWithImages("27", "MSI Modern 15 B13M", 13500000, 15000000, "MSI", "Sinh viên",
                "Cấu hình cao trong tầm giá, vỏ nhựa giả kim loại đẹp mắt.",
                "CPU: Intel Core i5-1335U, RAM: 16GB DDR4, Storage: 512GB SSD, GPU: Intel Iris Xe Graphics, Screen: 15.6 inch FHD IPS",
                4.2f, 20, context, new String[]{"laptop1", "laptop2", "laptop3"}));
        
        products.add(createProductWithImages("28", "Gigabyte G5 MF (Gaming Budget)", 18990000, 20900000, "Gigabyte", "Sinh viên",
                "Laptop Gaming giá rẻ nhất cho sinh viên vừa học vừa chơi.",
                "CPU: Intel Core i5-12500H, RAM: 8GB DDR4, Storage: 512GB SSD, GPU: NVIDIA GeForce RTX 4050 6GB, Screen: 15.6 inch FHD 144Hz",
                4.3f, 20, context, new String[]{"laptop1", "laptop2", "laptop3"}));
        
        // Cảm ứng (7 products - ID 29-35)
        products.add(createProductWithImages("29", "HP Envy x360 13", 21900000, 23500000, "HP", "Cảm ứng",
                "HP Envy x360 13 là chiếc laptop 2-in-1 linh hoạt với bản lề xoay 360 độ cho phép chuyển đổi giữa 4 chế độ: laptop, tablet, tent và stand một cách mượt mà. Vỏ nhôm nguyên khối cao cấp với tone màu bạc ánh kim sang trọng, trọng lượng chỉ 1.3kg giúp mang theo dễ dàng suốt cả ngày. Màn hình cảm ứng IPS 13.3 inch với độ sáng 400 nits hỗ trợ bút HP Active Pen đi kèm, lý tưởng cho ghi chú, vẽ phác họa và chỉnh sửa ảnh. Bàn phím có đèn nền cùng touchpad rộng hỗ trợ cử chỉ đa điểm giúp thao tác nhanh chóng. Trang bị chip Intel Core i5-1230U tiết kiệm điện với 10 nhân, RAM 8GB LPDDR4x dual-channel và SSD 512GB NVMe PCIe 4.0 đáp ứng tốt công việc văn phòng di động và sáng tạo nội dung nhẹ. Pin 51Wh cho thời lượng sử dụng lên đến 10 giờ, sạc nhanh 50% chỉ trong 45 phút.",
                "CPU: Intel Core i5-1230U, RAM: 8GB LPDDR4x, Storage: 512GB SSD, GPU: Intel Iris Xe Graphics, Screen: 13.3 inch FHD IPS Touch",
                4.3f, 20, context, new String[]{"laptophpvictus1", "laptophpvictus2", "laptophpvictus3"}));
        
        products.add(createProductWithImages("30", "Dell Inspiron 14 7430 2-in-1", 19500000, 21000000, "Dell", "Cảm ứng",
                "Dell Inspiron 14 7430 2-in-1 kết hợp hoàn hảo giữa hiệu năng và tính linh hoạt với thiết kế xoay gập 360 độ giúp biến hình thành máy tính bảng chỉ trong tích tắc. Viền màn hình mỏng 4 cạnh tạo nên tỷ lệ screen-to-body ratio lên đến 84%, mang lại trải nghiệm hình ảnh rộng rãi và hiện đại. Màn hình cảm ứng 14 inch FHD+ với tỷ lệ 16:10 cung cấp thêm 11% diện tích hiển thị so với 16:9 truyền thống, rất hữu ích cho đọc tài liệu và lập trình. Hệ thống loa stereo kép được tinh chỉnh bởi Waves MaxxAudio Pro cho âm thanh trong trẻo, bass sâu phù hợp xem phim và nghe nhạc. Trang bị chip Intel Core i5-1335U thế hệ 13 với 10 nhân và RAM 8GB LPDDR5-4800MHz giúp đa nhiệm mượt mà. Cổng kết nối đầy đủ bao gồm 2x USB-C Thunderbolt 4, 1x USB-A 3.2, HDMI 1.4 và jack 3.5mm. Trọng lượng 1.6kg và pin 54Wh cho thời lượng 8-9 giờ sử dụng hỗn hợp.",
                "CPU: Intel Core i5-1335U, RAM: 8GB LPDDR5, Storage: 512GB SSD, GPU: Intel Iris Xe Graphics, Screen: 14 inch FHD+ Touch",
                4.2f, 20, context, new String[]{"laptopdellinspiron1", "laptopdellinspiron2", "laptopdellinspiron3", "laptopdellinspiron4", "laptopdellinspiron5", "laptopdellinspiron6"}));
        
        products.add(createProductWithImages("31", "Asus Vivobook S 14 Flip", 17200000, 18900000, "Asus", "Cảm ứng",
                "Asus Vivobook S 14 Flip là laptop 2-in-1 năng động với thiết kế vỏ kim loại màu bạc thời thượng và trọng lượng chỉ 1.5kg, dễ dàng mang theo cho các bạn trẻ năng động. Bản lề ErgoLift xoay 360 độ không chỉ cho phép chuyển đổi linh hoạt giữa các chế độ sử dụng mà còn nâng bàn phím lên một góc thoải mái khi ở chế độ laptop. Màn hình cảm ứng WUXGA 14 inch với tỷ lệ 16:10 cung cấp độ phân giải 1920x1200, màu sắc 100% sRGB rực rỡ và hỗ trợ Asus Pen tùy chọn cho ghi chú và vẽ. Công nghệ tản nhiệt IceCool Plus với ống dẫn nhiệt kép giúp giữ nhiệt độ bề mặt bàn phím luôn mát mẻ ngay cả khi xử lý tác vụ nặng. Trang bị chip Intel Core i5-13500H với 12 nhân mạnh mẽ, RAM 16GB DDR4 nâng cấp được và SSD 512GB NVMe đáp ứng tốt cho sinh viên, freelancer làm đồ họa và lập trình. Cổng kết nối đầy đủ: USB-C Thunderbolt 4, 2x USB-A 3.2, HDMI 2.0, jack 3.5mm và khe microSD. Pin 50Wh cho thời lượng 7-8 giờ sử dụng văn phòng.",
                "CPU: Intel Core i5-13500H, RAM: 16GB DDR4, Storage: 512GB SSD, GPU: Intel Iris Xe Graphics, Screen: 14 inch WUXGA Touch",
                4.2f, 20, context, new String[]{"laptopasusvivobook1", "laptopasusvivobook2", "laptopasusvivobook3", "laptopasusvivobook4", "laptopasusvivobook5", "laptopasusvivobook6", "laptopasusvivobook7"}));
        
        products.add(createProductWithImages("32", "Lenovo Yoga 7i", 23500000, 25000000, "Lenovo", "Cảm ứng",
                "Lenovo Yoga 7i nổi bật với thiết kế vỏ nhôm bo tròn mềm mại tại các cạnh giúp cầm nắm thoải mái hơn so với thiết kế vuông góc truyền thống. Bản lề xoay 360 độ với cơ chế khóa đa cấp cho phép giữ chắc ở mọi góc độ từ 0 đến 360, hoàn hảo cho chế độ trình chiếu tent và xem phim stand mode. Màn hình OLED 14 inch độ phân giải 2.8K (2880x1800) với dải màu DCI-P3 100% mang đến hình ảnh sống động như thật, độ tương phản vô cực và màu đen sâu thẳm, lý tưởng cho chỉnh sửa video và xem phim HDR. Loa Bose 4 kêu stereo tích hợp với công nghệ Dolby Atmos tạo không gian âm thanh 3D bao trùm cực kỳ ấn tượng. Trang bị chip Intel Core i5-1340P với 12 nhân kết hợp hiệu năng và tiết kiệm pin, RAM 16GB LPDDR5 hàn chặt và SSD 512GB PCIe 4.0 đáp ứng tốt đa nhiệm và xử lý sáng tạo nội dung. Trọng lượng 1.49kg và pin 71Wh cho thời lượng 11-12 giờ sử dụng văn phòng nhẹ nhàng. Tích hợp webcam 1080p IR với Windows Hello, cảm biến vân tay trên nút nguồn và privacy shutter bảo mật. Cổng kết nối: 2x USB-C Thunderbolt 4, 1x USB-A 3.2, jack 3.5mm.",
                "CPU: Intel Core i5-1340P, RAM: 16GB LPDDR5, Storage: 512GB SSD, GPU: Intel Iris Xe Graphics, Screen: 14 inch 2.8K OLED Touch",
                4.5f, 20, context, new String[]{"laptoplenovoidea1", "laptoplenovoidea2", "laptoplenovoidea3", "laptoplenovoidea4", "laptoplenovoidea5", "laptoplenovoidea6"}));
        
        products.add(createProductWithImages("33", "MSI Summit E13 Flip Evo", 28900000, 31000000, "MSI", "Cảm ứng",
                "MSI Summit E13 Flip Evo là laptop 2-in-1 cao cấp dành cho doanh nhân và chuyên gia với thiết kế vỏ magiê-nhôm siêu bền và siêu nhẹ chỉ 1.35kg, thoải mái mang theo suốt ngày dài. Đạt chuẩn Intel Evo Platform đảm bảo hiệu năng ổn định, đánh thức nhanh dưới 1 giây, kết nối Thunderbolt 4 tốc độ cao và thời lượng pin thực tế trên 9 giờ. Màn hình cảm ứng 13.4 inch FHD+ với tần số quét 120Hz mang đến trải nghiệm vuốt chạm cực kỳ mượt mà, hỗ trợ bút MSI Pen đi kèm với 4096 mức lực nhấn cho ghi chú và ký điện tử chuyên nghiệp. Tích hợp chip bảo mật TPM 2.0, cảm biến vân tay Windows Hello và webcam IR nhận diện khuôn mặt bảo vệ dữ liệu doanh nghiệp quan trọng. Trang bị chip Intel Core i7-1360P với 12 nhân hiệu năng cao, RAM 16GB LPDDR5 và SSD 1TB NVMe PCIe 4.0 đáp ứng tốt mọi tác vụ từ văn phòng đến sáng tạo nội dung. Bàn phím có đèn nền trắng thanh lịch, touchpad lớn và hỗ trợ MSI Center Pro để quản lý hệ thống. Cổng kết nối: 2x USB-C Thunderbolt 4, 1x USB-A 3.2 Gen2, jack 3.5mm và khe microSD. Pin 4-cell 56Wh với sạc nhanh 65W.",
                "CPU: Intel Core i7-1360P, RAM: 16GB LPDDR5, Storage: 1TB SSD, GPU: Intel Iris Xe Graphics, Screen: 13.4 inch FHD+ 120Hz Touch",
                4.6f, 20, context, new String[]{"laptop1", "laptop2", "laptop3"}));
        
        products.add(createProductWithImages("34", "Acer Spin 5", 24000000, 26500000, "Acer", "Cảm ứng",
                "Acer Spin 5 là laptop 2-in-1 đa năng với thiết kế vỏ nhôm nguyên khối và trọng lượng chỉ 1.5kg, dễ dàng xoay gập 360 độ để chuyển đổi giữa laptop, tablet, tent và stand mode phù hợp mọi tình huống. Điểm nổi bật là chiếc bút Acer Active Stylus được tích hợp ngay trong thân máy với cơ chế đẩy ra như bút bi, luôn sẵn sàng khi cần ghi chú hoặc vẽ mà không lo thất lạc. Màn hình cảm ứng IPS 14 inch với độ phân giải WQXGA (2560x1600) mang lại hình ảnh sắc nét, dải màu 100% sRGB chính xác cho chỉnh sửa ảnh và thiết kế đồ họa cơ bản. Viền màn hình mỏng 3 cạnh tạo cảm giác hiện đại và tối đa hóa diện tích hiển thị trong khung máy nhỏ gọn. Trang bị chip Intel Core i5-1240P với 12 nhân cân bằng hiệu năng và pin, RAM 16GB LPDDR5 và SSD 512GB NVMe PCIe 4.0 đáp ứng tốt đa nhiệm và khởi động nhanh. Hệ thống loa stereo kép DTS Audio mang lại âm thanh trong trẻo. Cổng kết nối: 2x USB-C Thunderbolt 4, 1x USB-A 3.2, HDMI 2.1 và jack 3.5mm. Pin 56Wh cho thời lượng 8-9 giờ, webcam 1080p QHD với privacy shutter bảo mật.",
                "CPU: Intel Core i5-1240P, RAM: 16GB LPDDR5, Storage: 512GB SSD, GPU: Intel Iris Xe Graphics, Screen: 14 inch WQXGA Touch",
                4.4f, 20, context, new String[]{"laptopaceraspire1", "laptopaceraspire2", "laptopaceraspire3", "laptopaceraspire4"}));
        
        products.add(createProductWithImages("35", "LG Gram 2-in-1 16", 34990000, 38000000, "LG", "Cảm ứng",
                "LG Gram 2-in-1 16 là chiếc laptop 2-in-1 nhẹ nhất thế giới ở kích thước 16 inch với trọng lượng chỉ 1.48kg, thách thức mọi định nghĩa về trọng lượng và kích thước màn hình lớn. Vỏ hợp kim magiê-nhôm-carbon đạt chuẩn MIL-STD-810H chịu được va đập, rung lắc, nhiệt độ khắc nghiệt và áp suất cao, đảm bảo độ bền vượt trội mặc dù siêu nhẹ. Bản lề xoay 360 độ chắc chắn cho phép sử dụng ở mọi tư thế từ laptop đến tablet mode với màn hình 16 inch, mang lại không gian làm việc và giải trí rộng rãi. Màn hình cảm ứng IPS WQXGA (2560x1600) với tỷ lệ 16:10 cung cấp thêm 11% diện tích hiển thị, dải màu 99% DCI-P3 và độ sáng 350 nits phù hợp cho chuyên gia sáng tạo di động. Trang bị chip Intel Core i7-1360P với 12 nhân mạnh mẽ, RAM 16GB LPDDR5 và SSD 512GB NVMe PCIe 4.0 xử lý tốt đa nhiệm nặng và rendering. Pin dung lượng 80Wh cực lớn cho thời lượng sử dụng hỗn hợp lên đến 15 giờ liên tục. Hỗ trợ LG Stylus Pen tùy chọn với 4096 mức lực nhấn. Cổng kết nối đầy đủ: 2x USB-C Thunderbolt 4, 2x USB-A 3.2, HDMI 2.0, jack 3.5mm và khe microSD. Webcam 1080p IR với Windows Hello và nút tắt webcam vật lý.",
                "CPU: Intel Core i7-1360P, RAM: 16GB LPDDR5, Storage: 512GB SSD, GPU: Intel Iris Xe Graphics, Screen: 16 inch WQXGA IPS Touch",
                4.7f, 20, context, new String[]{"laptoplggram1", "laptoplggram2", "laptoplggram3"}));
        
        // Laptop AI (7 products - ID 36-42)
        products.add(createProductWithImages("36", "Asus Zenbook 14 OLED AI", 26990000, 28990000, "Asus", "Laptop AI",
                "Asus Zenbook 14 OLED AI là laptop thế hệ mới trang bị chip Intel Core Ultra 5 125H với NPU (Neural Processing Unit) chuyên biệt có khả năng xử lý 34 TOPS cho các tác vụ AI như nhận diện hình ảnh, xử lý ngôn ngữ tự nhiên và tăng cường video call mà không tốn tài nguyên CPU/GPU. Thiết kế vỏ nhôm nguyên khối mỏng nhẹ với trọng lượng 1.2kg và độ dày chỉ 14.9mm, dễ dàng bỏ vào balo hàng ngày. Màn hình OLED 14 inch độ phân giải 3K (2880x1800) với tần số quét 120Hz mang lại hình ảnh mượt mà, màu sắc 100% DCI-P3 chuẩn điện ảnh và chứng nhận Pantone Validated cho độ chính xác màu sắc. Công nghệ OLED cho độ tương phản vô cực, màu đen sâu thẳm và độ sáng đỉnh 550 nits phù hợp cho chỉnh sửa video HDR. Tích hợp phím Copilot riêng biệt để gọi trợ lý AI Windows Copilot chỉ bằng một chạm. RAM 16GB LPDDR5X-7467MHz tốc độ cực cao và SSD 512GB PCIe 4.0 đáp ứng tốt đa nhiệm và khởi động siêu nhanh. Pin 75Wh cho thời lượng 12-13 giờ sử dụng hỗn hợp. Cổng kết nối: 2x USB-C Thunderbolt 4, 1x USB-A 3.2, HDMI 2.1 và jack 3.5mm. Webcam 1080p IR với Windows Hello và tính năng AI Noise Cancellation khử tiếng ồn cuộc gọi.",
                "CPU: Intel Core Ultra 5 125H, RAM: 16GB LPDDR5X, Storage: 512GB SSD, GPU: Intel Arc Graphics (NPU AI), Screen: 14 inch 3K OLED 120Hz",
                4.5f, 20, context, new String[]{"laptopasusvivobook1", "laptopasusvivobook2", "laptopasusvivobook3", "laptopasusvivobook4", "laptopasusvivobook5", "laptopasusvivobook6", "laptopasusvivobook7"}));
        
        products.add(createProductWithImages("37", "MSI Prestige 16 AI Evo", 31900000, 34000000, "MSI", "Laptop AI",
                "MSI Prestige 16 AI Evo là laptop cao cấp dành cho chuyên gia sáng tạo nội dung với chip Intel Core Ultra 7 155H tích hợp NPU mạnh mẽ 34 TOPS, đạt chuẩn Intel Evo đảm bảo hiệu năng ổn định và thời lượng pin dài. Thiết kế vỏ nhôm màu xám không gian sang trọng với trọng lượng 1.9kg, vừa phải cho màn hình 16 inch lớn. Pin dung lượng khủng 99.9Whr (giới hạn tối đa cho phép mang lên máy bay) mang lại thời lượng sử dụng thực tế 12-14 giờ, lý tưởng cho dân di động và làm việc ngoại cảnh cả ngày. Màn hình QHD+ 16 inch (2560x1600) với tỷ lệ 16:10 cung cấp không gian làm việc rộng rãi, dải màu 100% sRGB và độ sáng 300 nits phù hợp cho chỉnh sửa ảnh và video. Trang bị RAM 32GB LPDDR5 khủng cho phép xử lý đồng thời nhiều ứng dụng nặng và SSD 1TB PCIe 4.0 lưu trữ dự án lớn. Tính năng AI Creator tích hợp giúp tối ưu hiệu năng tự động cho từng ứng dụng như Photoshop, Premiere Pro, DaVinci Resolve. Cổng kết nối: 2x USB-C Thunderbolt 4, 2x USB-A 3.2 Gen2, HDMI 2.1, jack 3.5mm và khe microSD UHS-III. Webcam 1080p IR với Windows Hello và AI tự động làm đẹp, khử nhiễu. Bàn phím có đèn nền trắng, touchpad lớn hỗ trợ cử chỉ đa điểm.",
                "CPU: Intel Core Ultra 7 155H, RAM: 32GB LPDDR5, Storage: 1TB SSD, GPU: Intel Arc Graphics, Screen: 16 inch QHD+ IPS",
                4.6f, 20, context, new String[]{"laptop1", "laptop2", "laptop3"}));
        
        products.add(createProductWithImages("38", "Acer Swift Go 14 AI", 22990000, 24500000, "Acer", "Laptop AI",
                "Acer Swift Go 14 AI là laptop tầm trung tích hợp công nghệ AI với chip Intel Core Ultra 5 125H có NPU xử lý 34 TOPS, mang trải nghiệm AI hiện đại đến tay người dùng với mức giá hợp lý. Điểm nổi bật là bàn phím tích hợp phím Copilot riêng biệt cho phép gọi trợ lý AI Windows Copilot nhanh chóng để hỗ trợ viết lách, tìm kiếm thông tin và tóm tắt tài liệu. Tính năng AI PurifiedVoice 2.0 sử dụng NPU để khử tiếng ồn xung quanh trong cuộc gọi video, tự động lọc tiếng chó sủa, xe cộ và tăng cường giọng nói rõ ràng. AI PurifiedView tối ưu hóa hình ảnh webcam với tự động lấy nét, điều chỉnh ánh sáng và làm mờ phông nền thời gian thực. Màn hình OLED 14 inch 2.8K (2880x1800) với dải màu 100% DCI-P3 và độ sáng 400 nits mang lại hình ảnh sống động, tỷ lệ 16:10 tăng diện tích làm việc. Thiết kế vỏ nhôm mỏng 15.9mm, trọng lượng 1.3kg dễ dàng mang theo. RAM 16GB LPDDR5X-7467MHz và SSD 512GB PCIe 4.0 đáp ứng tốt đa nhiệm. Pin 65Wh cho 10-11 giờ sử dụng. Cổng kết nối: 2x USB-C Thunderbolt 4, 2x USB-A 3.2, HDMI 2.1 và jack 3.5mm.",
                "CPU: Intel Core Ultra 5 125H, RAM: 16GB LPDDR5X, Storage: 512GB SSD, GPU: Intel Arc Graphics, Screen: 14 inch 2.8K OLED",
                4.4f, 20, context, new String[]{"laptopaceraspire1", "laptopaceraspire2", "laptopaceraspire3", "laptopaceraspire4"}));
        
        products.add(createProductWithImages("39", "Lenovo Yoga 9i 2-in-1 (AI)", 38900000, 41900000, "Lenovo", "Laptop AI",
                "Lenovo Yoga 9i 2-in-1 AI là đỉnh cao của thiết kế và công nghệ với vỏ nhôm gia công CNC tỉ mỉ, viền cạnh kim cương cắt chính xác tạo cảm giác cao cấp như một món trang sức sang trọng. Chip Intel Core Ultra 7 155H với NPU 34 TOPS mang lại khả năng xử lý AI mạnh mẽ cho chỉnh sửa ảnh thông minh, tăng cường video và render 3D nhanh chóng. Màn hình OLED cảm ứng 14 inch 4K (3840x2400) với độ phân giải cực nét, dải màu 100% DCI-P3, độ sáng 500 nits và chứng nhận VESA DisplayHDR 500 True Black cho trải nghiệm hình ảnh đỉnh cao. Bản lề xoay 360 độ chắc chắn giúp chuyển đổi linh hoạt giữa laptop và tablet mode. Hệ thống âm thanh Bowers & Wilkins 4 loa stereo với soundbar xoay theo chế độ sử dụng mang đến chất lượng âm thanh audiophile với bass sâu và treble trong trẻo. Trang bị RAM 16GB LPDDR5X và SSD 1TB PCIe 4.0 cho hiệu năng cao. Trọng lượng 1.4kg nhẹ nhàng. Pin 75Wh cho 12 giờ sử dụng. Webcam 1080p IR với Windows Hello, AI tự động làm đẹp và theo dõi chuyển động. Tính năng Lenovo AI Engine+ tự động tối ưu hiệu năng, nhiệt độ và pin dựa trên hành vi sử dụng. Cổng kết nối: 2x USB-C Thunderbolt 4, 1x USB-A 3.2 và jack 3.5mm.",
                "CPU: Intel Core Ultra 7 155H, RAM: 16GB LPDDR5X, Storage: 1TB SSD, GPU: Intel Arc Graphics, Screen: 14 inch 4K OLED Touch",
                4.7f, 20, context, new String[]{"laptoplenovolegion1", "laptoplenovolegion2", "laptoplenovolegion3", "laptoplenovolegion4", "laptoplenovolegion5", "laptoplenovolegion6", "laptoplenovolegion7", "laptoplenovolegion8"}));
        
        products.add(createProductWithImages("40", "HP Omen Transcend 14", 41500000, 45000000, "HP", "Laptop AI",
                "HP Omen Transcend 14 là laptop gaming AI nhẹ nhất thế giới ở phân khúc RTX 4060 với trọng lượng chỉ 1.63kg, phá vỡ mọi định nghĩa về laptop gaming phải nặng và cồng kềnh. Chip Intel Core Ultra 7 155H với NPU AI giúp tối ưu hóa hiệu năng game thông minh, tự động phân bổ tài nguyên cho GPU khi chơi game và chuyển sang tiết kiệm pin khi làm việc văn phòng. Tính năng Omen AI Engine sử dụng machine learning để điều chỉnh cooling fan speed tự động dựa trên nhiệt độ và tác vụ, giảm tiếng ồn khi không cần thiết. Card đồ họa NVIDIA GeForce RTX 4060 8GB với kiến trúc Ada Lovelace hỗ trợ DLSS 3, ray tracing thời gian thực và AI Frame Generation tăng FPS lên gấp đôi. Màn hình OLED 14 inch 2.8K (2880x1800) với tần số quét 120Hz, response time 0.2ms, 100% DCI-P3 và 400 nits mang lại hình ảnh mượt mà và màu sắc rực rỡ. RAM 16GB LPDDR5X-7467MHz và SSD 1TB PCIe 4.0 đáp ứng tốt game AAA và streaming. Thiết kế vỏ nhôm CNC mỏng 17.9mm với hệ thống tản nhiệt Vapor Chamber tiên tiến. Pin 71Wh cho 7-8 giờ sử dụng hỗn hợp. Bàn phím RGB per-key, touchpad lớn. Cổng kết nối: 2x USB-C Thunderbolt 4, 1x USB-A 3.2, HDMI 2.1, jack 3.5mm.",
                "CPU: Intel Core Ultra 7 155H, RAM: 16GB LPDDR5X, Storage: 1TB SSD, GPU: NVIDIA GeForce RTX 4060 8GB, Screen: 14 inch 2.8K OLED 120Hz",
                4.7f, 20, context, new String[]{"laptophpvictus1", "laptophpvictus2", "laptophpvictus3", "laptophpvictus4", "laptophpvictus5"}));
        
        products.add(createProductWithImages("41", "Dell XPS 14 (9440)", 49900000, 52000000, "Dell", "Laptop AI",
                "Dell XPS 14 9440 là kiệt tác thiết kế với vỏ nhôm nguyên khối gia công CNC tỉ mỉ, viền cắt kim cương chính xác tạo cảm giác cực kỳ cao cấp và chắc chắn trong tay. Chip Intel Core Ultra 7 155H với NPU AI 34 TOPS mang đến khả năng xử lý AI thông minh, tự động tối ưu hiệu năng khi cần và chuyển sang chế độ tiết kiệm pin khi rảnh rỗi để kéo dài thời lượng pin lên 30-40%. Công nghệ Dell Optimizer tích hợp AI học hỏi thói quen sử dụng để tự động điều chỉnh cài đặt hiệu năng, độ sáng màn hình, âm lượng và kết nối WiFi tối ưu nhất. Màn hình InfinityEdge 14.5 inch FHD+ (1920x1200) với viền cực mỏng 4 cạnh mang lại tỷ lệ screen-to-body 93%, tấm nền IPS 500 nits chống lóa và dải màu 100% sRGB. Card đồ họa NVIDIA GeForce RTX 4050 6GB hỗ trợ content creation và chơi game nhẹ. Bàn phím full-size không viền với phím bấm hành trình 1mm êm ái, touchpad cảm ứng haptic phản hồi rung chính xác. RAM 16GB LPDDR5X hàn chặt và SSD 512GB PCIe 4.0. Trọng lượng 1.68kg, độ dày 18mm. Pin 69.5Wh cho 11-12 giờ. Cổng kết nối: 3x USB-C Thunderbolt 4, jack 3.5mm. Webcam 1080p IR với Windows Hello, AI tự động làm đẹp và theo dõi người dùng.",
                "CPU: Intel Core Ultra 7 155H, RAM: 16GB LPDDR5X, Storage: 512GB SSD, GPU: NVIDIA GeForce RTX 4050 6GB, Screen: 14.5 inch FHD+ InfinityEdge",
                4.8f, 20, context, new String[]{"laptopdellinspiron1", "laptopdellinspiron2", "laptopdellinspiron3", "laptopdellinspiron4", "laptopdellinspiron5", "laptopdellinspiron6"}));
        
        products.add(createProductWithImages("42", "Gigabyte AERO 14 OLED AI", 33500000, 36000000, "Gigabyte", "Laptop AI",
                "Gigabyte AERO 14 OLED AI là laptop content creator tích hợp phần mềm AI Creator độc quyền giúp tăng tốc render video trong Adobe Premiere Pro, DaVinci Resolve lên 40-50% nhờ tối ưu hóa thông minh phân bổ tài nguyên CPU/GPU. Tuy chip Intel Core i7-13700H không có NPU phần cứng nhưng phần mềm AI được tối ưu sâu với kiến trúc 14 nhân này mang lại hiệu năng AI ấn tượng. Màn hình OLED 14 inch 2.8K (2880x1800) với chứng nhận X-Rite Pantone và chuẩn màu điện ảnh 100% DCI-P3, Delta E < 1 cho độ chính xác màu sắc tuyệt đối phù hợp cho grading màu video và chỉnh sửa ảnh chuyên nghiệp. Card đồ họa NVIDIA GeForce RTX 4050 6GB với Tensor Cores hỗ trợ AI rendering, DLSS 3 và OptiX ray tracing tăng tốc render 3D. Thiết kế vỏ nhôm mỏng 16.9mm, trọng lượng 1.49kg dễ mang theo. RAM 16GB LPDDR5 và SSD 1TB PCIe 4.0. Bàn phím RGB per-key, touchpad lớn. Pin 73Wh cho 9-10 giờ. Tính năng AI Boost tự động overclock an toàn khi phát hiện tác vụ nặng. Hệ thống tản nhiệt WINDFORCE với 2 quạt và 4 ống dẫn nhiệt. Cổng kết nối: 2x USB-C Thunderbolt 4, 2x USB-A 3.2, HDMI 2.1, jack 3.5mm và SD card reader UHS-II.",
                "CPU: Intel Core i7-13700H (AI Software), RAM: 16GB LPDDR5, Storage: 1TB SSD, GPU: NVIDIA GeForce RTX 4050 6GB, Screen: 14 inch 2.8K OLED",
                4.5f, 20, context, new String[]{"laptop1", "laptop2", "laptop3"}));
        
        // Đồ họa - Kỹ thuật (7 products - ID 43-49)
        products.add(createProductWithImages("43", "Dell Precision 3581", 39500000, 42000000, "Dell", "Đồ hoạ- Kỹ thuật",
                "Dell Precision 3581 là workstation di động chuyên nghiệp được thiết kế để chạy các phần mềm CAD, Revit, SolidWorks, AutoCAD một cách mượt mà và ổn định trong môi trường làm việc kỹ thuật. Vỏ nhựa ABS gia cố sợi carbon đạt chuẩn MIL-STD-810H chịu được va đập, rung lắc, nhiệt độ khắc nghiệt và bụi bẩn thường gặp tại công trường xây dựng. Card đồ họa NVIDIA RTX A1000 6GB là GPU chuyên nghiệp với driver được chứng nhận ISV cho hơn 100 ứng dụng kỹ thuật, đảm bảo hiệu năng ổn định và không bị lỗi viewport như card gaming thường gặp. Chip Intel Core i7-13800H với 14 nhân mạnh mẽ xử lý tốt tính toán simulation, rendering và compile code. RAM 16GB DDR5-4800MHz có thể nâng cấp lên 64GB và SSD 512GB PCIe 4.0 có khe M.2 thứ hai để mở rộng lưu trữ. Màn hình 15.6 inch FHD IPS chống lóa với độ sáng 300 nits và dải màu 72% NTSC phù hợp xem bản vẽ kỹ thuật cả ngày. Bàn phím full-size có đèn nền, touchpad chính xác, bảo mật vân tay và TPM 2.0. Cổng kết nối đầy đủ: 2x USB-C Thunderbolt 4, 2x USB-A 3.2, HDMI 2.0, RJ45 Gigabit Ethernet và SD card reader. Trọng lượng 2.06kg, pin 64Wh cho 6-7 giờ. Bảo hành ProSupport.",
                "CPU: Intel Core i7-13800H, RAM: 16GB DDR5, Storage: 512GB SSD, GPU: NVIDIA RTX A1000 6GB, Screen: 15.6 inch FHD IPS",
                4.6f, 20, context, new String[]{"laptopdellinspiron1", "laptopdellinspiron2", "laptopdellinspiron3"}));
        
        products.add(createProductWithImages("44", "HP ZBook Firefly 16 G10", 36500000, 38900000, "HP", "Đồ hoạ- Kỹ thuật",
                "HP ZBook Firefly 16 G10 là workstation di động mỏng nhẹ chỉ 1.8kg với thiết kế vỏ nhôm cao cấp, phá vỡ định kiến máy trạm phải nặng nề và cồng kềnh. Màn hình 16 inch WUXGA (1920x1200) IPS với tùy chọn HP DreamColor đạt chuẩn màu Pantone Validated và DCI-P3 100%, độ chính xác Delta E < 2 lý tưởng cho designer và photographer cần màu sắc chính xác tuyệt đối. Card đồ họa NVIDIA RTX A500 4GB là GPU chuyên nghiệp thế hệ Ampere với driver ISV certified cho Adobe Creative Suite, AutoCAD, SolidWorks, Revit đảm bảo tương thích và ổn định tối đa. Chip Intel Core i7-1360P với 12 nhân kết hợp P-core hiệu năng cao và E-core tiết kiệm pin, phù hợp làm việc di động cả ngày. RAM 32GB DDR5-5200MHz có thể nâng cấp lên 64GB cho xử lý file 3D phức tạp và SSD 1TB PCIe 4.0 đủ lưu trữ dự án lớn. Bàn phím có đèn nền trắng, touchpad lớn, bảo mật vân tay tích hợp nút nguồn và TPM 2.0. Cổng kết nối phong phú: 2x USB-C Thunderbolt 4, 2x USB-A 3.2, HDMI 2.1, RJ45 Ethernet và SD card reader UHS-II tốc độ cao. Pin 83Wh cho thời lượng 9-10 giờ. Webcam 5MP với Windows Hello IR và privacy shutter. Chứng nhận MIL-STD-810H về độ bền.",
                "CPU: Intel Core i7-1360P, RAM: 32GB DDR5, Storage: 1TB SSD, GPU: NVIDIA RTX A500 4GB, Screen: 16 inch WUXGA IPS",
                4.5f, 20, context, new String[]{"laptophpvictus2", "laptophpvictus3", "laptophpvictus4"}));
        
        products.add(createProductWithImages("45", "Lenovo ThinkPad P1 Gen 6", 58900000, 62000000, "Lenovo", "Đồ hoạ- Kỹ thuật",
                "Lenovo ThinkPad P1 Gen 6 là đỉnh cao của dòng workstation di động với sức mạnh tối thượng được đóng gói trong thân hình mỏng chỉ 17.8mm và trọng lượng 1.81kg, đủ nhẹ để mang theo suốt ngày dài. Vỏ nhôm-carbon fiber kết hợp chắc chắn đạt chuẩn MIL-STD-810H về độ bền với 12 bài test khắc nghiệt. Bàn phím ThinkPad huyền thoại với phím bấm cong ergonomic, hành trình 1.5mm sâu, feedback rõ ràng và độ bền 5 triệu lần nhấn mang lại trải nghiệm gõ thoải mái nhất trong ngành. TrackPoint đỏ đặc trưng và touchpad thủy tinh lớn cung cấp đa dạng phương thức điều khiển. Card đồ họa NVIDIA RTX 2000 Ada 8GB thế hệ mới nhất với kiến trúc Ada Lovelace mang lại hiệu năng vượt trội cho rendering 3D, simulation FEA và AI training. Chip Intel Core i7-13800H với 14 nhân xử lý tốt workload đa nhiệm nặng. RAM 32GB DDR5-5600MHz có thể nâng cấp lên 64GB và SSD 1TB PCIe 4.0 với khe M.2 thứ hai mở rộng. Màn hình 16 inch WQXGA (2560x1600) IPS với tỷ lệ 16:10, 100% sRGB, 400 nits và Dolby Vision HDR. Pin 90Wh cho 8-9 giờ, sạc nhanh 80% trong 60 phút. Bảo mật cấp doanh nghiệp: vân tay, IR webcam, TPM 2.0, Kensington Lock và PrivacyGuard. Cổng kết nối: 2x USB-C Thunderbolt 4, 2x USB-A 3.2, HDMI 2.1, jack 3.5mm và SD card.",
                "CPU: Intel Core i7-13800H, RAM: 32GB DDR5, Storage: 1TB SSD, GPU: NVIDIA RTX 2000 Ada 8GB, Screen: 16 inch WQXGA IPS",
                4.8f, 20, context, new String[]{"laptoplenovothinkpad1", "laptoplenovothinkpad2", "laptoplenovothinkpad3"}));
        
        products.add(createProductWithImages("46", "Asus ProArt Studiobook 16", 54900000, 58000000, "Asus", "Đồ hoạ- Kỹ thuật",
                "Asus ProArt Studiobook 16 là laptop sáng tạo nội dung cao cấp với tính năng độc đáo Asus Dial - núm xoay vật lý tích hợp trên touchpad cho phép điều chỉnh các thông số như brush size, zoom, timeline scrubbing trực tiếp trong Adobe Photoshop, Premiere Pro, Lightroom một cách trực quan và nhanh chóng hơn rất nhiều so với chuột thường. Màn hình OLED 16 inch 3.2K (3200x2000) với tần số quét 120Hz mang lại hình ảnh sắc nét cực kỳ mượt mà, dải màu 100% DCI-P3 và chứng nhận Pantone Validated đảm bảo độ chính xác màu sắc Delta E < 1 cho công việc grading màu chuyên nghiệp. Chip Intel Core i9-13980HX là CPU desktop-class mạnh nhất với 24 nhân (8P+16E) và TDP 55W, vượt trội trong rendering, encoding video 4K/8K và compile code. Card đồ họa NVIDIA GeForce RTX 4070 8GB với kiến trúc Ada Lovelace hỗ trợ CUDA, OptiX ray tracing và DLSS 3 tăng tốc render 3D đáng kể. RAM 32GB DDR5-4800MHz có thể nâng cấp lên 64GB và SSD 1TB PCIe 4.0 với 2 khe M.2. Hệ thống tản nhiệt IceCool Pro với 2 quạt ArcFlow và 6 ống dẫn nhiệt đồng giữ nhiệt độ ổn định. Bàn phím full-size có NumPad và đèn nền LED trắng. Cổng kết nối: 2x USB-C Thunderbolt 4, 3x USB-A 3.2, HDMI 2.1, jack 3.5mm và SD card UHS-II. Trọng lượng 2.4kg, pin 90Wh cho 6-7 giờ.",
                "CPU: Intel Core i9-13980HX, RAM: 32GB DDR5, Storage: 1TB SSD, GPU: NVIDIA GeForce RTX 4070 8GB, Screen: 16 inch 3.2K OLED 120Hz",
                4.8f, 20, context, new String[]{"laptopasusrog1", "laptopasusrog2", "laptopasusrog3", "laptopasusrog4", "laptopasusrog5", "laptopasusrog6", "laptopasusrog7"}));
        
        products.add(createProductWithImages("47", "MSI Creator Z17 HX Studio", 64500000, 68000000, "MSI", "Đồ hoạ- Kỹ thuật",
                "MSI Creator Z17 HX Studio là laptop workstation cao cấp với thiết kế vỏ nhôm CNC nguyên khối màu xám không gian sang trọng, gia công tỉ mỉ tạo cảm giác cực kỳ chắc chắn và đẳng cấp. Đạt chứng nhận NVIDIA Studio đảm bảo driver được tối ưu cho hơn 70 ứng dụng sáng tạo nội dung như Adobe Creative Cloud, DaVinci Resolve, Blender, Autodesk Maya với hiệu năng ổn định và không xung đột. Chip Intel Core i9-13950HX là CPU mạnh nhất dòng HX với 24 nhân (8P+16E) base clock 2.2GHz, boost lên 5.5GHz mang lại sức mạnh vượt trội cho rendering, simulation và compile. RAM 64GB DDR5-5600MHz khủng đủ mở đồng thời nhiều project 4K/8K và xử lý file raw nặng hàng GB mà không giật lag. SSD 2TB PCIe 4.0 với 2 khe M.2 cho phép mở rộng thêm lưu trữ. Card đồ họa NVIDIA GeForce RTX 4070 8GB Ada Lovelace với Tensor Cores thế hệ 4 tăng tốc AI rendering và RT Cores thế hệ 3 cho ray tracing realtime. Màn hình cảm ứng 17 inch QHD+ (2560x1600) với tần số quét 165Hz cực mượt, dải màu 100% DCI-P3 và độ sáng 400 nits. Hệ thống tản nhiệt Cooler Boost 5 với 3 quạt và 7 ống dẫn nhiệt. Cổng kết nối: 2x USB-C Thunderbolt 4, 3x USB-A 3.2, HDMI 2.1, SD card UHS-III và jack 3.5mm. Trọng lượng 2.6kg, pin 90Wh.",
                "CPU: Intel Core i9-13950HX, RAM: 64GB DDR5, Storage: 2TB SSD, GPU: NVIDIA GeForce RTX 4070 8GB, Screen: 17 inch QHD+ 165Hz Touch",
                4.9f, 20, context, new String[]{"laptopmsikatana1", "laptopmsikatana2", "laptopmsikatana3", "laptopmsikatana4", "laptopmsikatana5", "laptopmsikatana6", "laptopmsikatana7"}));
        
        products.add(createProductWithImages("48", "Gigabyte Aero 16 OLED", 45900000, 49000000, "Gigabyte", "Đồ hoạ- Kỹ thuật",
                "Gigabyte Aero 16 OLED nổi bật với màn hình Samsung AMOLED 16 inch 4K (3840x2400) được cân chỉnh màu X-Rite Pantone từng máy tại nhà máy, kèm theo giấy chứng nhận độ chính xác màu sắc Delta E < 1 đảm bảo màu sắc hiển thị giống 100% với bản in thực tế. Dải màu 100% Adobe RGB và DCI-P3 phù hợp cho photographer chuyên nghiệp và colorist cần màu sắc tuyệt đối chính xác. Độ tương phản vô cực của OLED mang lại màu đen sâu thẳm và màu sắc rực rỡ chân thực. Chip Intel Core i9-13900H với 14 nhân (6P+8E) mạnh mẽ xử lý tốt rendering video 4K/8K và modeling 3D phức tạp. Card đồ họa NVIDIA GeForce RTX 4070 8GB Ada Lovelace với 5888 CUDA cores hỗ trợ DLSS 3, ray tracing và AI-accelerated rendering trong Blender, V-Ray, OctaneRender. RAM 32GB LPDDR5-5200MHz hàn chặt và SSD 1TB PCIe 4.0 với khe M.2 thứ hai. Thiết kế vỏ nhôm mỏng 19mm, trọng lượng 2.1kg dễ mang theo. Hệ thống tản nhiệt WINDFORCE Infinity với 2 quạt 71 cánh và 5 ống dẫn nhiệt. Bàn phím RGB per-key, touchpad lớn. Pin 99Wh cho 7-8 giờ. Cổng kết nối: 2x USB-C Thunderbolt 4, 2x USB-A 3.2, HDMI 2.1, jack 3.5mm, SD card UHS-II và RJ45 2.5G Ethernet. Tặng kèm balo Aero cao cấp.",
                "CPU: Intel Core i9-13900H, RAM: 32GB LPDDR5, Storage: 1TB SSD, GPU: NVIDIA GeForce RTX 4070 8GB, Screen: 16 inch 4K OLED",
                4.7f, 20, context, new String[]{"laptop1", "laptop2", "laptop3", "laptop4", "laptop5"}));
        
        products.add(createProductWithImages("49", "Razer Blade 16 Studio Edition", 84900000, 89000000, "Razer", "Đồ hoạ- Kỹ thuật",
                "Razer Blade 16 Studio Edition là đỉnh cao của laptop sáng tạo nội dung với màn hình Mini-LED 16 inch chế độ kép (Dual Mode) độc quyền của Razer: chuyển đổi linh hoạt giữa UHD+ (3840x2400) 120Hz cho chỉnh sửa nội dung với màu sắc chuẩn 100% Adobe RGB hoặc FHD+ (1920x1200) 240Hz cho gaming với response time cực nhanh. Công nghệ Mini-LED với 512 vùng Local Dimming mang lại độ tương phản 1,000,000:1, độ sáng đỉnh 1000 nits đạt chuẩn HDR1000 cho hình ảnh chân thực với vùng tối sâu và vùng sáng rực rỡ. Thiết kế vỏ nhôm CNC unibody nguyên khối màu đen với logo Razer phát sáng tinh tế, độ dày chỉ 19.9mm mỏng hơn nhiều workstation cùng cấu hình. Chip Intel Core i9-13950HX với 24 nhân desktop-class mang lại sức mạnh tuyệt đối cho rendering 3D, simulation FEA và compile code lớn. Card đồ họa NVIDIA GeForce RTX 4080 12GB với 9728 CUDA cores là GPU mạnh thứ 2 dòng laptop Ada Lovelace, xử lý mượt mà ray tracing, AI rendering và game 4K Ultra. RAM 32GB DDR5-5600MHz dual-channel nâng cấp được và SSD 1TB PCIe 4.0. Hệ thống tản nhiệt Vapor Chamber tiên tiến giữ nhiệt độ ổn định. Bàn phím Razer Chroma RGB per-key với 16.8 triệu màu. Pin 95.2Wh cho 6-7 giờ. Cổng kết nối: 3x USB-C Thunderbolt 4, 3x USB-A 3.2, HDMI 2.1, SD card UHS-III và jack 3.5mm. Trọng lượng 2.45kg.",
                "CPU: Intel Core i9-13950HX, RAM: 32GB DDR5, Storage: 1TB SSD, GPU: NVIDIA GeForce RTX 4080 12GB, Screen: 16 inch Dual Mode Mini-LED",
                4.9f, 20, context, new String[]{"laptop1", "laptop2", "laptop3", "laptop4", "laptop5"}));
        
        // Macbook CTO (7 products - ID 50-56)
        products.add(createProductWithImages("50", "MacBook Air 13 M3 CTO 1", 31500000, 32990000, "Apple", "Macbook CTO",
                "MacBook Air 13 M3 CTO 1 là bản nâng cấp RAM lên 16GB thay vì 8GB cơ bản, mang lại khả năng đa nhiệm mượt mà hơn rất nhiều khi mở đồng thời nhiều tab Chrome, chạy Xcode compile code và chỉnh sửa ảnh trong Photoshop cùng lúc mà không bị giật lag. Chip Apple M3 thế hệ 3 sản xuất trên tiến trình 3nm tiên tiến với 8 nhân CPU (4 Performance + 4 Efficiency) và 10 nhân GPU mang lại hiệu năng tăng 20% so với M2 và tiết kiệm pin hơn 15%. Kiến trúc Unified Memory cho phép CPU và GPU chia sẻ cùng một pool RAM giúp truyền dữ liệu nhanh chóng mà không qua bus riêng biệt như laptop Windows. Thiết kế vỏ nhôm nguyên khối siêu mỏng 11.3mm, trọng lượng chỉ 1.24kg với 4 màu sắc: Silver, Starlight, Space Gray và Midnight. Màn hình Liquid Retina 13.6 inch độ phân giải 2560x1664 với dải màu P3 rộng, độ sáng 500 nits và True Tone tự động điều chỉnh nhiệt độ màu. Bàn phím Magic Keyboard với cơ chế scissor switch êm ái, TouchID tích hợp, và loa stereo 4 kêu hỗ trợ Spatial Audio. Không có quạt tản nhiệt nhưng vẫn hoạt động êm ái nhờ hiệu suất nhiệt tối ưu của M3. Pin 52.6Wh cho thời lượng 18 giờ xem video liên tục. Cổng kết nối: 2x USB-C Thunderbolt 3, jack 3.5mm và MagSafe 3 sạc nam châm. SSD 256GB có thể nâng cấp CTO lên 512GB, 1TB hoặc 2TB.",
                "CPU: Apple M3 (8-core CPU), RAM: 16GB Unified, Storage: 256GB SSD, GPU: Apple M3 (10-core GPU), Screen: 13.6 inch Liquid Retina",
                4.7f, 20, context, new String[]{"laptopmacbookair1", "laptopmacbookair2", "laptopmacbookair3"}));
        
        products.add(createProductWithImages("51", "MacBook Air 15 M3 CTO 2", 40990000, 42990000, "Apple", "Macbook CTO",
                "MacBook Air 15 M3 CTO 2 là bản cấu hình Max Option cho dòng Air với màn hình 15.3 inch lớn nhất, RAM 24GB Unified Memory khủng và SSD 512GB, mang lại trải nghiệm cao cấp cho người dùng cần không gian làm việc rộng rãi và hiệu năng mạnh mẽ nhưng vẫn siêu nhẹ chỉ 1.51kg. RAM 24GB là mức tối đa cho dòng Air, đủ mở đồng thời Logic Pro X với hàng trăm track, Final Cut Pro chỉnh sửa video 4K và nhiều ứng dụng nặng khác mà vẫn dư dả. Màn hình Liquid Retina 15.3 inch với độ phân giải 2880x1864 cung cấp không gian làm việc rộng rãi cho đa nhiệm split-view, dải màu P3 rộng và độ sáng 500 nits lý tưởng cho chỉnh sửa ảnh và làm việc ngoài trời. Chip M3 với 8 nhân CPU và 10 nhân GPU xử lý tốt workload chuyên nghiệp. Thiết kế vỏ nhôm mỏng chỉ 11.5mm, không có quạt tản nhiệt nhưng vẫn chạy êm ái ngay cả khi export video. Loa stereo 6 kêu với Spatial Audio và Dolby Atmos mang lại âm thanh vòm sống động. Bàn phím Magic Keyboard full-size có TouchID, Force Touch trackpad lớn nhất trong dòng Air. Pin 66.5Wh cho thời lượng 18 giờ xem video. Cổng kết nối: 2x USB-C Thunderbolt 3, jack 3.5mm và MagSafe 3. SSD 512GB đủ lưu trữ dự án trung bình, có thể CTO lên 1TB hoặc 2TB nếu cần.",
                "CPU: Apple M3 (8-core CPU), RAM: 24GB Unified, Storage: 512GB SSD, GPU: Apple M3 (10-core GPU), Screen: 15.3 inch Liquid Retina",
                4.8f, 20, context, new String[]{"laptopmacbookair4", "laptopmacbookair1", "laptopmacbookair2"}));
        
        products.add(createProductWithImages("52", "MacBook Pro 14 M3 Pro CTO 1", 52990000, 54990000, "Apple", "Macbook CTO",
                "MacBook Pro 14 M3 Pro CTO 1 là bản nâng cấp dòng Pro với chip M3 Pro mạnh mẽ 11 nhân CPU (5 Performance + 6 Efficiency) và 14 nhân GPU, RAM tùy chỉnh 36GB Unified Memory mang lại hiệu năng vượt trội cho chuyên gia sáng tạo nội dung và lập trình viên. Màu Space Black mới với lớp phủ anodized đặc biệt chống bám vân tay tốt hơn, tạo vẻ ngoài sang trọng và chuyên nghiệp. Chip M3 Pro với băng thông bộ nhớ 150GB/s gấp đôi M3 cơ bản, rất phù hợp cho chỉnh sửa video multicam 4K/8K trong Final Cut Pro và xử lý file 3D lớn trong Blender. Màn hình Liquid Retina XDR 14.2 inch mini-LED với 1000 vùng Local Dimming mang lại độ tương phản 1,000,000:1, độ sáng SDR 600 nits và đỉnh HDR 1600 nits, dải màu P3 rộng và ProMotion 120Hz cực kỳ mượt mà. Thiết kế vỏ nhôm nguyên khối chắc chắn với trọng lượng 1.55kg. Loa stereo 6 kêu với bass sâu, micro 3 kêu studio-quality và webcam 1080p. Bàn phím Magic Keyboard với TouchID và Touch Bar thay bằng phím function full-size. Hệ thống tản nhiệt chủ động với quạt êm cho phép duy trì hiệu năng cao liên tục. Pin 70Wh cho 18 giờ xem video. Cổng kết nối: 3x USB-C Thunderbolt 4, HDMI 2.1, jack 3.5mm, khe SDXC card và MagSafe 3 sạc nhanh. SSD 512GB PCIe 4.0 với tốc độ đọc 5000MB/s.",
                "CPU: Apple M3 Pro (11-core CPU), RAM: 36GB Unified, Storage: 512GB SSD, GPU: Apple M3 Pro (14-core GPU), Screen: 14.2 inch Liquid Retina XDR",
                4.8f, 20, context, new String[]{"laptopmacbookpro1", "laptopmacbookpro2", "laptopmacbookpro3"}));
        
        products.add(createProductWithImages("53", "MacBook Pro 14 M3 Max CTO", 87500000, 89990000, "Apple", "Macbook CTO",
                "MacBook Pro 14 M3 Max CTO đem sức mạnh khủng khiếp vào thân hình 14 inch nhỏ gọn với chip M3 Max 14 nhân CPU (10 Performance + 4 Efficiency) và 30 nhân GPU, băng thông bộ nhớ 300GB/s gấp đôi M3 Pro mang lại hiệu năng workstation-class cho rendering 3D, simulation, machine learning và chỉnh sửa video 8K ProRes. RAM 36GB Unified Memory đủ mở file Photoshop hàng chục GB, Premiere Pro timeline phức tạp với nhiều lớp hiệu ứng và Xcode compile project iOS lớn cùng lúc mà vẫn dư dả. GPU 30 nhân với 120 GPU cores hỗ trợ hardware ray tracing, mesh shading và Dynamic Caching thông minh phân bổ bộ nhớ tự động cho hiệu suất tối ưu. Màn hình Liquid Retina XDR 14.2 inch mini-LED với chất lượng hình ảnh tương đương Pro Display XDR, hỗ trợ reference modes cho Rec. 709, P3-D65, P3-ST 2084 phù hợp grading màu chuyên nghiệp. Thiết kế vỏ nhôm nguyên khối Space Black sang trọng, trọng lượng 1.61kg. Media Engine chuyên biệt với 2 video encode engines và 2 ProRes encode/decode engines tăng tốc export video lên 2.7x so với M1 Max. Pin 70Wh cho 18 giờ. Hệ thống tản nhiệt chủ động duy trì hiệu năng cao liên tục. Cổng kết nối: 3x USB-C Thunderbolt 4, HDMI 2.1 hỗ trợ 8K 60Hz hoặc 4K 240Hz, jack 3.5mm, SDXC card và MagSafe 3.",
                "CPU: Apple M3 Max (14-core CPU), RAM: 36GB Unified, Storage: 1TB SSD, GPU: Apple M3 Max (30-core GPU), Screen: 14.2 inch Liquid Retina XDR",
                4.9f, 20, context, new String[]{"laptopmacbookpro1", "laptopmacbookpro2", "laptopmacbookpro3", "laptopmacbookpro4", "laptopmacbookpro5"}));
        
        products.add(createProductWithImages("54", "MacBook Pro 16 M3 Pro CTO", 67990000, 69990000, "Apple", "Macbook CTO",
                "MacBook Pro 16 M3 Pro CTO mang đến màn hình lớn nhất 16.2 inch trong dòng MacBook Pro và pin trâu nhất 100Wh (giới hạn tối đa cho phép mang lên máy bay) cho thời lượng 22 giờ xem video liên tục, lý tưởng cho chuyên gia di động cần làm việc cả ngày dài mà không cần sạc. Chip M3 Pro 12 nhân CPU (6 Performance + 6 Efficiency) và 18 nhân GPU với băng thông 150GB/s mang lại hiệu năng cao cho video editing, 3D modeling và software development. RAM 36GB Unified Memory đủ dư dả cho mọi tác vụ nặng và SSD 1TB lưu trữ nhiều project lớn. Màn hình Liquid Retina XDR 16.2 inch mini-LED với độ phân giải 3456x2234, 1000 vùng Local Dimming, độ sáng SDR 600 nits và đỉnh HDR 1600 nits, dải màu P3 rộng và ProMotion 120Hz mang lại không gian làm việc rộng rãi với chất lượng hình ảnh tuyệt đối. Thiết kế vỏ nhôm nguyên khối Space Black chống bám vân tay, trọng lượng 2.15kg vừa phải cho màn hình lớn. Loa stereo 6 kêu với bass cực sâu, micro 3 kêu studio-quality và webcam 1080p. Bàn phím Magic Keyboard full-size với TouchID, Force Touch trackpad lớn nhất. Media Engine với video encode/decode engines tăng tốc export video. Hệ thống tản nhiệt chủ động với 2 quạt lớn duy trì hiệu năng cao. Cổng kết nối: 3x USB-C Thunderbolt 4, HDMI 2.1, jack 3.5mm, SDXC card và MagSafe 3 sạc nhanh 140W.",
                "CPU: Apple M3 Pro (12-core CPU), RAM: 36GB Unified, Storage: 1TB SSD, GPU: Apple M3 Pro (18-core GPU), Screen: 16.2 inch Liquid Retina XDR",
                4.8f, 20, context, new String[]{"laptopmacbookpro1", "laptopmacbookpro2", "laptopmacbookpro3", "laptopmacbookpro4", "laptopmacbookpro5"}));
        
        products.add(createProductWithImages("55", "MacBook Pro 16 M3 Max CTO 1", 96990000, 99990000, "Apple", "Macbook CTO",
                "MacBook Pro 16 M3 Max CTO 1 là cấu hình cao cấp dành riêng cho Video Editor chuyên nghiệp xử lý video 8K RAW và 3D Artist rendering scene phức tạp với chip M3 Max 14 nhân CPU và 30 nhân GPU, băng thông bộ nhớ 300GB/s cực khủng. RAM 48GB Unified Memory cho phép mở timeline Premiere Pro hoặc DaVinci Resolve với hàng trăm clip 4K/8K, nhiều lớp color grading và hiệu ứng nặng mà vẫn scrub realtime không giật lag. GPU 30 nhân với 120 GPU cores hỗ trợ hardware ray tracing, mesh shading và Dynamic Caching giúp render Blender Cycles, Octane, Redshift nhanh gấp 2-3 lần so với GPU rời RTX 3060 Laptop. Media Engine với 2 video encode engines và 2 ProRes encode/decode engines cho phép playback đồng thời 12 stream video 4K ProRes 422 hoặc 7 stream 8K ProRes 422 mà không cần proxy. Màn hình Liquid Retina XDR 16.2 inch với 1000 vùng Local Dimming và độ sáng đỉnh 1600 nits lý tưởng cho color grading HDR chuyên nghiệp với reference modes chính xác. Thiết kế vỏ nhôm nguyên khối Space Black sang trọng, trọng lượng 2.15kg. Pin 100Wh cho 22 giờ, sạc nhanh 140W. Hệ thống tản nhiệt chủ động với 2 quạt lớn duy trì turbo boost liên tục. SSD 1TB PCIe 4.0 với tốc độ đọc 7400MB/s. Cổng kết nối: 3x USB-C Thunderbolt 4, HDMI 2.1 hỗ trợ 8K 60Hz, jack 3.5mm, SDXC card và MagSafe 3.",
                "CPU: Apple M3 Max (14-core CPU), RAM: 48GB Unified, Storage: 1TB SSD, GPU: Apple M3 Max (30-core GPU), Screen: 16.2 inch Liquid Retina XDR",
                4.9f, 20, context, new String[]{"laptopmacbookpro1", "laptopmacbookpro2", "laptopmacbookpro3", "laptopmacbookpro4", "laptopmacbookpro5"}));
        
        products.add(createProductWithImages("56", "MacBook Pro 16 M3 Max Ultimate", 145000000, 149990000, "Apple", "Macbook CTO",
                "MacBook Pro 16 M3 Max Ultimate là trùm cuối, cấu hình cao nhất tuyệt đối trong dòng MacBook với chip M3 Max 16 nhân CPU (12 Performance + 4 Efficiency) và 40 nhân GPU đi kèm RAM 128GB Unified Memory khổng lồ - mức tối đa mà Apple từng cung cấp trong laptop, dành cho những công việc cực kỳ nặng như machine learning training models lớn, simulation FEA với millions elements, chỉnh sửa video 8K RAW multicam với hàng trăm track và render 3D scene với billions polygons. Băng thông bộ nhớ 400GB/s cực khủng đảm bảo dữ liệu được truyền tải nhanh chóng giữa CPU, GPU và RAM. GPU 40 nhân với 160 GPU cores mạnh ngang GPU workstation desktop, hỗ trợ hardware ray tracing realtime và Dynamic Caching thông minh. Media Engine với 2 video encode engines và 2 ProRes encode/decode engines tăng tốc export video 8K ProRes lên 6.5x so với Mac Intel. SSD 2TB PCIe 4.0 với tốc độ đọc 7400MB/s, ghi 6000MB/s đủ lưu trữ thư viện footage khổng lồ. Màn hình Liquid Retina XDR 16.2 inch với chất lượng reference monitor chuyên nghiệp. Pin 100Wh cho 22 giờ, sạc nhanh 140W. Hệ thống tản nhiệt chủ động với 2 quạt lớn và vapor chamber duy trì turbo boost liên tục ngay cả khi render nhiều giờ. Thiết kế vỏ nhôm Space Black cao cấp, trọng lượng 2.15kg. Cổng kết nối: 3x USB-C Thunderbolt 4, HDMI 2.1, jack 3.5mm, SDXC card và MagSafe 3. Đây là laptop mạnh nhất thế giới cho creative professionals.",
                "CPU: Apple M3 Max (16-core CPU), RAM: 128GB Unified, Storage: 2TB SSD, GPU: Apple M3 Max (40-core GPU), Screen: 16.2 inch Liquid Retina XDR",
                5.0f, 20, context, new String[]{"laptopmacbookpro3", "laptopmacbookpro4", "laptopmacbookpro5"}));
        
        // Use repository method to insert products
        repository.insertAllProducts(products, new DataRepository.VoidCallback() {
            @Override
            public void onSuccess() {
                Log.d(TAG, "Seeded " + products.size() + " products");
                callback.onSuccess();
            }
            
            @Override
            public void onError(Exception e) {
                Log.e(TAG, "Error seeding products: " + e.getMessage());
                callback.onError(e);
            }
        });
    }
    
    private static Product createProduct(String id, String name, double price, double oldPrice,
                                        String brand, String category, String description,
                                        String specs, float rating, int stock, Context context) {
        Product product = new Product(id, name, price, oldPrice, description, 
                "placeholder_" + id, brand, category, specs, rating, stock);
        
        // Copy images from drawable to internal storage and get file:// URIs
        ArrayList<String> imageUrls = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            String imageUri = com.example.otech.util.ImageStorageHelper.copyDrawableToStorage(context, "laptop" + i);
            if (imageUri != null) {
                imageUrls.add(imageUri);
            }
        }
        
        // Fallback to drawable names if copy failed
        if (imageUrls.isEmpty()) {
            imageUrls.add("laptop1");
            imageUrls.add("laptop2");
            imageUrls.add("laptop3");
        }
        
        product.setImageUrls(imageUrls);
        
        return product;
    }
    
    private static Product createProductWithImages(String id, String name, double price, double oldPrice,
                                                   String brand, String category, String description,
                                                   String specs, float rating, int stock, Context context,
                                                   String[] imageNames) {
        Product product = new Product(id, name, price, oldPrice, description, 
                "placeholder_" + id, brand, category, specs, rating, stock);
        
        // Copy specific images from drawable to internal storage and get file:// URIs
        ArrayList<String> imageUrls = new ArrayList<>();
        for (String imageName : imageNames) {
            String imageUri = com.example.otech.util.ImageStorageHelper.copyDrawableToStorage(context, imageName);
            if (imageUri != null) {
                imageUrls.add(imageUri);
            }
        }
        
        // Fallback to drawable names if copy failed
        if (imageUrls.isEmpty()) {
            for (String imageName : imageNames) {
                imageUrls.add(imageName);
            }
        }
        
        product.setImageUrls(imageUrls);
        
        return product;
    }
    
    private static void seedBanners(DataRepository repository, Context context, DataRepository.VoidCallback callback) {
        ArrayList<com.example.otech.model.Banner> banners = new ArrayList<>();
        
        // Copy banner images from drawable to internal storage
        String[] bannerNames = {"banner1.jpg", "banner2.jpg", "banner3.jpg", "banner4.jpg", "banner5.jpg"};
        
        for (int i = 0; i < bannerNames.length; i++) {
            String bannerName = bannerNames[i];
            String imageUri = com.example.otech.util.ImageStorageHelper.copyDrawableToStorage(context, bannerName);
            
            if (imageUri != null) {
                // Create banner with file:// URI
                String bannerId = String.valueOf(i + 1);
                String title = "Banner " + (i + 1);
                String link = "";
                boolean isActive = true;
                int order = i + 1;
                
                banners.add(new com.example.otech.model.Banner(bannerId, imageUri, title, link, isActive, order));
                Log.d(TAG, "Copied banner " + bannerName + " to internal storage: " + imageUri);
            } else {
                // Fallback to drawable name if copy failed
                String bannerId = String.valueOf(i + 1);
                String title = "Banner " + (i + 1);
                banners.add(new com.example.otech.model.Banner(bannerId, bannerName, title, "", true, i + 1));
                Log.w(TAG, "Failed to copy banner " + bannerName + ", using drawable name");
            }
        }
        
        // Insert all banners into database
        repository.insertAllBanners(banners, new DataRepository.VoidCallback() {
            @Override
            public void onSuccess() {
                Log.d(TAG, "Seeded " + banners.size() + " banners to internal storage");
                callback.onSuccess();
            }
            
            @Override
            public void onError(Exception e) {
                Log.e(TAG, "Error inserting banners: " + e.getMessage());
                callback.onError(e);
            }
        });
    }
    
    private static void seedReviews(DataRepository repository, InitCallback callback) {
        ArrayList<Review> reviews = new ArrayList<>();
        
        // Sample reviews cho products
        reviews.add(new Review("r1", "1", "2", "Test User", 4.5f, 
                "Laptop rất tốt cho văn phòng, pin trâu!"));
        reviews.add(new Review("r2", "8", "2", "Test User", 5.0f, 
                "Chơi game siêu mượt, RTX 4060 quá đỉnh!"));
        
        // Use repository method to insert reviews
        repository.insertAllReviews(reviews, new DataRepository.VoidCallback() {
            @Override
            public void onSuccess() {
                Log.d(TAG, "Seeded " + reviews.size() + " reviews");
                Log.d(TAG, "Database initialization complete!");
                callback.onComplete();
            }
            
            @Override
            public void onError(Exception e) {
                Log.e(TAG, "Error seeding reviews: " + e.getMessage());
                callback.onError(e);
            }
        });
    }
}
