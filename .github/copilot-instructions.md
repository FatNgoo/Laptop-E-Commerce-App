# OTech - Android Laptop E-Commerce App

## Project Overview
OTech là ứng dụng Android bán laptop sử dụng Java với Material Design 3. App có 2 phần: Client (khách hàng) và Admin (quản trị).

## Architecture & Structure

### Package Organization
```
com.example.otech/
├── activity/          # Tất cả Activity classes
├── adapter/           # RecyclerView Adapters
├── model/             # POJO classes (Product, User, Order, CartItem)
├── repository/        # MockDataStore (singleton data layer)
└── util/              # Helper classes, Constants
```

### Data Layer Pattern
- **MockDataStore**: Singleton pattern quản lý toàn bộ dữ liệu in-memory
- Không sử dụng database, tất cả data là mock data trong ArrayList
- SharedPreferences chỉ dùng cho lưu trạng thái đăng nhập
- Tất cả model classes implement `Serializable` để truyền qua Intent

### Key Features
- **User flow**: Login → Home (Grid 2 columns) → Product Detail → Cart → Checkout
- **Admin flow**: Dashboard → Manage Products/Orders
- **Wishlist**: Thả tim sản phẩm, lưu trong MockDataStore
- **Order Management**: User có thể hủy đơn pending

## Material Design 3 Standards

### Color Scheme (colors.xml)
```xml
<color name="colorPrimary">#0A5688</color>      <!-- Xanh Navy -->
<color name="colorOnPrimary">#FFFFFF</color>    <!-- Trắng -->
<color name="colorSecondary">#FF9800</color>    <!-- Cam cho CTA/giá -->
<color name="backgroundColor">#F5F5F5</color>   <!-- Xám nhạt -->
```

### Theme Configuration
- Base theme: `Theme.Material3.DayNight.NoActionBar`
- Custom `MaterialButton`: cornerRadius 8dp
- Custom `MaterialCardView`: cornerRadius 12dp, elevation 4dp
- Font: Open Sans (download từ Google Fonts, đặt trong `res/font/`)

### UI Patterns
- **RecyclerView**: GridLayoutManager với spanCount=2 cho product list
- **TextInputLayout**: Style OutlinedBox cho tất cả form input
- **MaterialCardView**: Dùng cho mọi item card (product, cart, order)
- **MaterialToolbar**: Thay thế ActionBar, custom title/icons
- **FAB**: FloatingActionButton cho Admin add actions

## Developer Workflows

### Build Commands
```bash
# Build debug APK
.\gradlew assembleDebug

# Install and run
.\gradlew installDebug

# Clean build
.\gradlew clean build
```

### Adding New Activity Checklist
1. Tạo class trong `activity/` package
2. Tạo layout XML trong `res/layout/`
3. Đăng ký trong `AndroidManifest.xml`
4. Sử dụng Material Design components (MaterialButton, MaterialCardView, etc.)
5. Implement data flow qua MockDataStore

### Adding New Product Feature
1. Update `Product` model nếu cần field mới
2. Update `MockDataStore.initProducts()` với data mẫu
3. Update adapter để hiển thị field mới
4. Update layout XML với Material Design style

## Critical Conventions

### Intent Data Passing
```java
// Truyền Product object qua Intent (vì implement Serializable)
intent.putExtra("product", productObject);
Product p = (Product) getIntent().getSerializableExtra("product");
```

### MockDataStore Access Pattern
```java
MockDataStore store = MockDataStore.getInstance();
ArrayList<Product> products = store.getAllProducts();
store.addToCart(userId, product, quantity);
```

### Wishlist Implementation
- `Product` class có field `isFavorite` (boolean)
- `MockDataStore` có methods: `addToWishlist()`, `removeFromWishlist()`, `getWishlist()`
- ProductDetailActivity dùng ToggleButton/CheckBox với custom drawable (heart icon)

### Forgot Password Flow
1. ForgotPasswordActivity: Input username, new password, confirm password
2. Validate username tồn tại trong MockDataStore
3. Call `store.resetPassword(username, newPass)`
4. Finish về LoginActivity

## Testing Strategy
- Unit tests: Test MockDataStore logic (login, cart operations, wishlist)
- Instrumented tests: Test Activity navigation flows
- Manual testing: Verify Material Design UI consistency

## Common Pitfalls
- **Đừng quên** đăng ký Activity trong AndroidManifest.xml
- **Luôn check null** khi getSerializableExtra từ Intent
- **SharedPreferences key**: Dùng constants trong util/Constants.java
- **RecyclerView Adapter**: Luôn notify adapter sau khi update data
- **Material Theme**: Phải extend từ Material3 theme, không phải AppCompat

## Dependencies (build.gradle.kts)
```kotlin
implementation("com.google.android.material:material:1.11.0")
implementation("androidx.recyclerview:recyclerview:1.3.2")
implementation("androidx.cardview:cardview:1.0.0")
// Glide cho load ảnh (if needed later)
```

## Admin vs User Differentiation
- `User.role` field: "admin" hoặc "user"
- LoginActivity check role → route đến AdminActivity hoặc MainActivity
- Admin có thêm quyền: CRUD products, approve/reject orders
