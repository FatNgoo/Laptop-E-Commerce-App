# 🛒 OTech - Android Laptop E-Commerce App

[![Android](https://img.shields.io/badge/Android-SDK_24+-green.svg)](https://developer.android.com)
[![Java](https://img.shields.io/badge/Java-11-orange.svg)](https://www.oracle.com/java/)
[![Material Design 3](https://img.shields.io/badge/Material_Design-3-blue.svg)](https://m3.material.io/)

Ứng dụng Android bán laptop với giao diện Material Design 3. Hỗ trợ 2 vai trò: **Client** (khách hàng) và **Admin** (quản trị viên).

## 📱 Screenshots

### User Flow
- **Login/Register** - Đăng nhập, đăng ký, quên mật khẩu
- **Home** - Grid 2 cột sản phẩm, tìm kiếm, lọc theo danh mục
- **Product Detail** - Chi tiết laptop, thêm giỏ hàng, yêu thích
- **Cart & Checkout** - Quản lý giỏ hàng, đặt hàng
- **Profile** - Wishlist, lịch sử đơn hàng, đăng xuất

### Admin Flow
- **Admin Dashboard** - Thống kê sản phẩm, đơn hàng
- **Manage Products** - Xem, xóa sản phẩm (CRUD)
- **Manage Orders** - Xử lý đơn hàng, cập nhật trạng thái

## ✨ Features

### 👤 User Features
- ✅ Đăng ký, đăng nhập, quên mật khẩu
- ✅ Tìm kiếm sản phẩm theo tên
- ✅ Lọc theo danh mục (Gaming, Workstation, Ultrabook, Office)
- ✅ Xem chi tiết laptop (specs, rating, giá khuyến mãi)
- ✅ Thêm vào yêu thích (wishlist)
- ✅ Giỏ hàng: thêm, sửa số lượng, xóa
- ✅ Đặt hàng với địa chỉ giao hàng
- ✅ Xem lịch sử đơn hàng
- ✅ Hủy đơn hàng đang chờ xử lý

### 🔧 Admin Features
- ✅ Dashboard với thống kê
- ✅ Quản lý sản phẩm (xem, xóa)
- ✅ Quản lý đơn hàng
- ✅ Cập nhật trạng thái đơn (Pending → Processing → Shipping → Completed)
- ✅ Hủy đơn hàng

## 🏗️ Architecture

### Tech Stack
- **Language**: Java 11
- **Min SDK**: 24 (Android 7.0)
- **Target SDK**: 36
- **UI**: Material Design 3
- **Build**: Gradle (Kotlin DSL)

### Project Structure
```
com.example.otech/
├── activity/          # All Activity classes
│   ├── LoginActivity
│   ├── RegisterActivity
│   ├── ForgotPasswordActivity
│   ├── MainActivity (Home)
│   ├── ProductDetailActivity
│   ├── CartActivity
│   ├── CheckoutActivity
│   ├── ProfileActivity
│   ├── WishlistActivity
│   ├── OrderHistoryActivity
│   ├── AdminActivity
│   ├── ManageProductsActivity
│   └── ManageOrdersActivity
├── adapter/           # RecyclerView Adapters
│   ├── ProductAdapter
│   ├── CartAdapter
│   └── OrderAdapter
├── model/             # POJO classes
│   ├── Product
│   ├── User
│   ├── Order
│   └── CartItem
├── repository/        # Data Layer
│   └── MockDataStore (Singleton)
└── util/              # Helper classes
    ├── Constants
    └── FormatUtils
```

### Data Layer
- **MockDataStore**: Singleton quản lý toàn bộ data in-memory
- **No Database**: Tất cả data là mock data trong ArrayList
- **SharedPreferences**: Chỉ lưu trạng thái đăng nhập
- **Serializable Models**: Truyền object qua Intent

## 🎨 Design System

### Colors
```xml
<color name="colorPrimary">#0A5688</color>      <!-- Navy Blue -->
<color name="colorSecondary">#FF9800</color>    <!-- Orange -->
<color name="backgroundColor">#F5F5F5</color>   <!-- Light Gray -->
<color name="colorPriceRed">#E53935</color>     <!-- Price Red -->
```

### Components
- **MaterialButton**: 8dp corner radius
- **MaterialCardView**: 12dp corner radius, 4dp elevation
- **TextInputLayout**: OutlinedBox style
- **RecyclerView**: GridLayoutManager (2 columns for products)
- **FloatingActionButton**: Admin add actions

## 🚀 Getting Started

### Prerequisites
- Android Studio Hedgehog or later
- JDK 11
- Android SDK 24+

### Installation

1. **Clone repository**
```bash
git clone <your-repo-url>
cd OTech
```

2. **Open in Android Studio**
- File → Open → Select `OTech` folder

3. **Sync Gradle**
```bash
.\gradlew clean build
```

4. **Run on device/emulator**
```bash
.\gradlew installDebug
```

### Login Credentials

#### User Account
```
Username: user
Password: user
```

#### Admin Account
```
Username: admin
Password: admin
```

## 📦 Sample Data

MockDataStore chứa **10 laptop mẫu**:

| Brand | Model | Category | Price |
|-------|-------|----------|-------|
| ASUS | ROG Strix G16 | Gaming | 35.990.000₫ |
| MSI | Titan GT77 HX | Gaming | 89.990.000₫ |
| Lenovo | Legion Pro 5 | Gaming | 32.490.000₫ |
| Dell | Precision 7770 | Workstation | 79.990.000₫ |
| HP | ZBook Fury G10 | Workstation | 85.990.000₫ |
| MacBook | Air M2 15" | Ultrabook | 34.990.000₫ |
| Dell | XPS 13 Plus | Ultrabook | 42.990.000₫ |
| Lenovo | ThinkPad X1 Carbon | Ultrabook | 45.990.000₫ |
| HP | ProBook 450 G10 | Office | 18.990.000₫ |
| Acer | Aspire 5 | Office | 15.490.000₫ |

## 📝 Development Notes

### Adding New Activity
1. Tạo class trong `activity/` package
2. Tạo layout XML trong `res/layout/`
3. Đăng ký trong `AndroidManifest.xml`
4. Sử dụng Material Design components
5. Access data qua `MockDataStore.getInstance()`

### Intent Data Passing
```java
// Send
intent.putExtra(Constants.EXTRA_PRODUCT, productObject);

// Receive
Product p = (Product) getIntent().getSerializableExtra(Constants.EXTRA_PRODUCT);
```

### MockDataStore Usage
```java
MockDataStore store = MockDataStore.getInstance();

// Products
ArrayList<Product> products = store.getAllProducts();
Product product = store.getProductById(id);

// Cart
store.addToCart(userId, product, quantity);
ArrayList<CartItem> cart = store.getCart(userId);

// Wishlist
store.addToWishlist(userId, product);
ArrayList<Product> wishlist = store.getWishlist(userId);

// Orders
Order order = store.checkout(userId, address, phone);
ArrayList<Order> orders = store.getUserOrders(userId);
store.cancelOrder(orderId, reason);
```

## 🧪 Testing

### Manual Testing Checklist

**User Flow:**
- [ ] Đăng nhập với user/user
- [ ] Tìm kiếm "ASUS"
- [ ] Lọc Gaming laptops
- [ ] Thả tim 2-3 sản phẩm
- [ ] Xem chi tiết → Thêm vào giỏ
- [ ] Mở giỏ hàng → Tăng/giảm số lượng
- [ ] Checkout → Điền địa chỉ → Đặt hàng
- [ ] Vào Profile → Xem Wishlist
- [ ] Vào Order History → Hủy đơn pending

**Admin Flow:**
- [ ] Đăng nhập với admin/admin
- [ ] Xem Dashboard statistics
- [ ] Manage Products → Click sản phẩm → Xóa
- [ ] Manage Orders → View Details → Cập nhật trạng thái
- [ ] Logout

## 🐛 Known Issues

- Font Open Sans chưa được download (dùng system font)
- Add/Edit Product chưa implement (show toast "coming soon")
- Product images dùng placeholder URLs
- Không có backend API (pure mock data)
- Không có image upload cho admin

## 🔮 Future Improvements

- [ ] Backend integration (REST API)
- [ ] Firebase Authentication
- [ ] Room Database cho offline support
- [ ] Image upload với Cloudinary/Firebase Storage
- [ ] Push notifications cho order status
- [ ] Payment gateway integration
- [ ] Product reviews & ratings
- [ ] Advanced search filters
- [ ] Order tracking map
- [ ] Multi-language support (i18n)

## 📄 License

This project is for educational purposes.

## 👥 Contributors

- **Developer**: [Your Name]
- **Project**: OTech Android App
- **Course**: Mobile Development (LTDD)
- **Year**: 2025

## 📞 Contact

For questions or support, please contact:
- Email: your.email@example.com
- GitHub: [@yourusername](https://github.com/yourusername)

---

**⭐ Nếu project hữu ích, đừng quên star repo này!**
#   O t e c h  
 #   L a p t o p - E - C o m m e r c e - A p p  
 