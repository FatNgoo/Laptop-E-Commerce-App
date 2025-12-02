1. Danh sách chức năng chi tiết (Feature List)

A. Phía Khách Hàng (User Client)

Authentication (Xác thực):

Đăng nhập (Login): Username/Password.

Đăng ký (Register): Thêm user mới.

Quên mật khẩu (Forgot Password): Xác thực thông tin và đặt lại mật khẩu mới.

Lưu trạng thái đăng nhập (SharedPreferences).

Trang chủ (Home):

Banner quảng cáo (Slider chạy ảnh).

Danh mục (Categories): Laptop Gaming, Work Station, Đồ Họa, Văn phòng, Ultrabook, Phụ kiện.

Top sản phẩm bán chạy / Khuyến mãi (Hiển thị dạng lưới 2 cột).

Thanh tìm kiếm (Search) & Bộ lọc (Filter theo hãng).

Chi tiết sản phẩm (Product Detail):

Ảnh lớn, Tên, Giá gốc, Giá giảm.

Thông số kỹ thuật.

Sản phẩm liên quan.

Comment & Đánh giá.

Nút: "Thêm vào giỏ", "Mua ngay", "Yêu thích" (Thả tim).

Giỏ hàng (Cart):

List sản phẩm, tăng giảm số lượng, tổng tiền.

Thanh toán (Checkout):

Form địa chỉ, xác nhận đơn hàng.

Cá nhân (Profile):

Thông tin user.

Sổ địa chỉ.

Trang Yêu thích (Wishlist): Xem và quản lý các sản phẩm đã thả tim.

Lịch sử đơn hàng (Order History):

Xem danh sách đơn, hủy đơn.

B. Phía Quản Trị (Admin Panel)

Dashboard: Thống kê cơ bản.

Quản lý sản phẩm: Thêm / Sửa / Xóa.

Quản lý đơn hàng: Duyệt đơn, xem lý do hủy.

2. Các bước thực hiện

Bước 1: Setup Font & Theme (Phong cách Material Design)

"Tôi đang làm app Android bán laptop tên 'OTech'. Tôi muốn giao diện hiện đại sử dụng Material Design.

Dependencies: Hãy chắc chắn thêm thư viện com.google.android.material:material:x.x.x vào build.gradle.

Theme: Sử dụng Theme.Material3.DayNight.NoActionBar làm theme gốc trong themes.xml.

Màu sắc (colors.xml):

colorPrimary: Xanh Navy (#0A5688)

colorOnPrimary: Trắng (#FFFFFF)

colorSecondary: Cam Nhạt (#FF9800) - dùng cho nút hành động/giá.

backgroundColor: Xám rất nhạt (#F5F5F5).

Font chữ: Hướng dẫn tôi import và áp dụng font Open Sans.

Styles: Tạo style cho MaterialButton (bo góc 8dp) và MaterialCardView (bo góc 12dp, elevation 4dp). Hãy đưa tôi code XML cho phần resource này."

Bước 2: Tạo Model & Mock Database (Dữ liệu cứng)

"Bây giờ hãy viết code Java cho phần dữ liệu (Model & Data).

Tạo các class POJO: Product (id, name, price, oldPrice, description, imageUrl, brand, category, specs, rating, isFavorite), User (id, username, password, fullName, phone, address, role), Order, CartItem. Tất cả implements Serializable.

Tạo class MockDataStore (Singleton).

Khởi tạo ArrayList chứa 10 laptop mẫu.

Tạo sẵn 1 Admin (admin/admin) và 1 User thường.

Viết các hàm: getAllProducts(), getProductById(), login(), addToCart(), checkout().

Thêm hàm cho Wishlist: addToWishlist(Product p), removeFromWishlist(Product p), getWishlist().

Thêm hàm cho Quên mật khẩu: resetPassword(String username, String newPass). Hãy viết code đầy đủ và chi tiết cho MockDataStore."

Bước 3: Login, Register & Forgot Password

"Viết code cho LoginActivity, RegisterActivity và ForgotPasswordActivity.

Giao diện:

Dùng TextInputLayout style OutlinedBox cho các ô nhập liệu.

LoginActivity: Có nút 'Quên mật khẩu?' dưới ô pass.

ForgotPasswordActivity:

Ô nhập Username/Email.

Ô nhập Mật khẩu mới.

Ô nhập Xác nhận mật khẩu mới.

Nút 'Đặt lại mật khẩu'.

Logic:

Forgot Password: Kiểm tra username có tồn tại trong MockDataStore không. Nếu có, cập nhật password mới và thông báo thành công, sau đó finish về trang Login. Nếu không tồn tại thì báo lỗi."

Bước 4: Trang chủ (Home) - Grid Layout 2 Cột

"Viết code cho MainActivity (Trang chủ khách hàng).

Giao diện:

Toolbar: MaterialToolbar, icon Menu, Logo, icon Giỏ hàng.

Search: CardView bo tròn chứa SearchView.

Danh sách sản phẩm: RecyclerView, GridLayoutManager (spanCount = 2).

Item Layout: MaterialCardView. Ảnh ở trên, Tên/Giá ở dưới.

Logic:

Load list từ MockDataStore.

Xử lý click vào item để mở ProductDetailActivity."

Bước 5: Chi tiết sản phẩm (Product Detail)

"Viết code cho ProductDetailActivity.

Giao diện:

Ảnh lớn, Tên (Bold), Giá (Đỏ), RatingBar.

Nút Icon Trái Tim (Yêu thích) nằm cạnh tên sản phẩm hoặc trên ảnh. Dùng ToggleButton hoặc CheckBox custom background drawable (trái tim rỗng/đầy).

Nút 'Thêm vào giỏ' to ở đáy màn hình (Bottom Sticky).

Logic:

Khi nhấn trái tim: Gọi addToWishlist hoặc removeFromWishlist trong MockDataStore và đổi trạng thái icon.

Nút thêm giỏ hàng hiện Snackbar."

Bước 6: Giỏ hàng & Thanh toán

"Viết code cho CartActivity và CheckoutActivity.

CartActivity: RecyclerView hiển thị list item. Mỗi item là MaterialCardView. Tổng tiền ở dưới.

CheckoutActivity: Form nhập thông tin (TextInputLayout). Logic tạo đơn hàng và xóa giỏ."

Bước 7: Trang Cá nhân, Yêu thích & Lịch sử

"Viết code cho ProfileActivity, WishlistActivity và OrderHistoryActivity.

ProfileActivity:

Header: Avatar & Tên.

Menu List: 'Sản phẩm yêu thích' (dẫn tới WishlistActivity), 'Lịch sử đơn hàng', 'Đổi mật khẩu', 'Đăng xuất'.

WishlistActivity (Mới):

Hiển thị danh sách các sản phẩm user đã thả tim bằng RecyclerView (dùng lại adapter của trang Home hoặc tạo mới).

Cho phép click vào để xem chi tiết hoặc nhấn icon thùng rác để bỏ thích.

OrderHistoryActivity:

Dùng TabLayout (Tất cả, Chờ xử lý...).

Hiển thị list đơn hàng, cho phép hủy đơn pending."

Bước 8: Trang Admin (Quản lý)

"Viết code cho AdminActivity và các trang con.

AdminActivity: Dashboard Grid 2 cột.

ManageProductActivity: List sản phẩm, FAB thêm mới.

ManageOrderActivity: List đơn hàng, click để đổi trạng thái."

3. Code Mẫu: MockDataStore (Phần lõi)

Đây là file quan trọng nhất để app chạy được mà không cần backend. Hãy tạo file này đầu tiên.