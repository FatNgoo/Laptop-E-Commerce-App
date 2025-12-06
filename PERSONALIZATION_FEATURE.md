# Tính năng Cá nhân hóa (Personalization) - OTech App

## 📱 Tổng quan

Đã thêm 3 tính năng cá nhân hóa chính vào trang Main để nâng cao trải nghiệm người dùng:

### 1. 🔄 **Tiếp tục xem (Resume Browsing)**
- Card nổi bật với gradient background (xanh nhạt)
- Hiển thị sản phẩm người dùng đang xem dở (trong 24 giờ gần nhất)
- Text: "Tiếp tục xem" + Tên sản phẩm
- Click vào để xem tiếp chi tiết sản phẩm

### 2. 🕐 **Vừa xem gần đây (Recently Viewed)**
- Horizontal RecyclerView hiển thị tối đa 10 sản phẩm gần nhất
- Card nhỏ gọn (140dp width) với hình ảnh, tên, brand, giá
- Icon lịch sử (⏱️) ở tiêu đề section
- Tự động ẩn nếu chưa xem sản phẩm nào

### 3. ⭐ **Dành cho bạn (Recommended For You)**
- Grid 2 columns hiển thị sản phẩm được đề xuất
- Dựa trên category mà user xem nhiều nhất
- Text phụ: "Dựa trên sở thích của bạn (Gaming/Văn phòng/...)"
- Ưu tiên sản phẩm rating cao + bán chạy trong category đó

---

## 🏗️ Kiến trúc Implementation

### **A. Database Models**

#### **1. ViewHistory Entity**
```java
@Entity(tableName = "view_history")
public class ViewHistory {
    String id;           // UUID
    String userId;       // User ID
    String productId;    // Product ID
    long timestamp;      // Thời gian xem (milliseconds)
    int viewCount;       // Số lần xem
}
```

#### **2. UserPreference Entity**
```java
@Entity(tableName = "user_preferences")
public class UserPreference {
    String userId;
    Map<String, Integer> categoryViewCounts;  // {"gaming": 15, "office": 5}
    String lastViewedProductId;
    long lastViewedTimestamp;
}
```

### **B. Repository Methods**

```java
// Track khi user xem sản phẩm
trackProductView(userId, productId, category, callback)

// Lấy danh sách sản phẩm xem gần đây
getRecentlyViewed(userId, limit, callback)

// Lấy sản phẩm được đề xuất
getRecommendedProducts(userId, limit, callback)

// Lấy user preferences
getUserPreference(userId, callback)
```

### **C. Adapter**

**RecentlyViewedAdapter** - Horizontal scrolling adapter cho Recently Viewed section
- Hỗ trợ file:// URI, content:// URI, và drawable resources
- Card nhỏ gọn, tối ưu cho horizontal scroll

### **D. UI Components**

1. **item_recently_viewed.xml** - Card item cho Recently Viewed
2. **card_resume_browsing.xml** - Card "Tiếp tục xem"
3. **gradient_resume_card.xml** - Gradient background
4. **rounded_corner_8dp.xml** - Shape với góc bo tròn
5. **ic_arrow_forward.xml** - Icon mũi tên
6. **ic_history.xml** - Icon lịch sử

---

## 🎨 Material Design 3 Compliance

### **Colors**
- Primary: `?attr/colorPrimary` (#0A5688 - Xanh Navy)
- Secondary: `@color/colorSecondary` (#FF9800 - Cam)
- Surface: `?attr/colorSurface`
- OnSurface: `?attr/colorOnSurface`
- SurfaceVariant: `?attr/colorOnSurfaceVariant`

### **Typography**
- Tiêu đề section: 18sp, Bold, Open Sans Bold
- Product name: 13-15sp, Bold
- Brand/Category: 11-12sp, Regular
- Price: 14sp, Bold, Secondary color

### **Spacing**
- Section padding: 16dp horizontal
- Card elevation: 2dp
- Card corner radius: 12-16dp
- Item spacing: 12dp

---

## 🔄 Logic Flow

### **1. Khi mở MainActivity:**
```
onCreate() → initViews() → setupPersonalization()
           ↓
    setupResumeBrowsing()     (Nếu có lastViewed trong 24h)
    setupRecentlyViewed()     (Nếu có lịch sử xem)
    setupRecommended()        (Dựa vào category preferences)
```

### **2. Khi xem ProductDetailActivity:**
```
onCreate() → trackProductView()
           ↓
    Update ViewHistory (viewCount++)
    Update UserPreference (categoryViewCounts++)
    Update lastViewedProductId & timestamp
```

### **3. Khi quay lại MainActivity:**
```
onResume() → setupPersonalization()
           ↓
    Refresh Resume Browsing
    Refresh Recently Viewed
    Refresh Recommended
```

---

## 📊 Recommendation Algorithm

```java
1. Lấy UserPreference của user
2. Tìm category có viewCount cao nhất
3. Load products thuộc category đó
4. Sort theo rating (cao → thấp) và soldCount
5. Lấy top N products
6. Nếu không đủ → Fill bằng Best Sellers
```

**Ví dụ:**
- User xem Gaming: 15 lần, Office: 5 lần, Student: 3 lần
- → Most viewed category: "Gaming"
- → Recommend top Gaming laptops (high rating + best sellers)

---

## 🎯 UX Improvements

### **Responsive Behavior**
- Tự động ẩn sections nếu không có data
- Hiển thị category name trong Recommended section
- Resume card chỉ hiện nếu xem trong 24h gần đây
- Smooth scroll với horizontal RecyclerView

### **Performance Optimizations**
- Async database queries với ExecutorService
- Sync favorite status trước khi render
- Reuse adapter instances (updateProducts)
- Image loading với URI caching

### **User Experience**
- Visual hierarchy rõ ràng với icons
- Gradient background cho Resume card
- Clean, flat design theo Material 3
- Consistent spacing và typography

---

## 📱 Visual Preview

```
┌─────────────────────────────────┐
│  Banner Slider                  │
└─────────────────────────────────┘

┌─────────────────────────────────┐
│  🔄 Tiếp tục xem                │
│  Dell Inspiron 15 3520     →   │
└─────────────────────────────────┘

┌─────────────────────────────────┐
│  ⏱️  Vừa xem gần đây             │
│  ┌──────┐ ┌──────┐ ┌──────┐ →  │
│  │ Pro1 │ │ Pro2 │ │ Pro3 │     │
│  └──────┘ └──────┘ └──────┘     │
└─────────────────────────────────┘

┌─────────────────────────────────┐
│  ⭐ Dành cho bạn                │
│  (Dựa trên Gaming)              │
│  ┌──────┐ ┌──────┐              │
│  │ Pro1 │ │ Pro2 │              │
│  └──────┘ └──────┘              │
│  ┌──────┐ ┌──────┐              │
│  │ Pro3 │ │ Pro4 │              │
│  └──────┘ └──────┘              │
└─────────────────────────────────┘
```

---

## 🚀 Future Enhancements

### **Potential Improvements:**
1. **Machine Learning Integration**
   - Collaborative filtering (users who viewed X also viewed Y)
   - Deep learning recommendations

2. **More Granular Tracking**
   - Time spent on each product
   - Scroll depth tracking
   - Click-through rate analysis

3. **A/B Testing**
   - Test different recommendation algorithms
   - Optimize section ordering

4. **Advanced Features**
   - "Because you viewed X" section
   - Price drop notifications for viewed products
   - Comparison between recently viewed items

---

## ✅ Checklist hoàn thành

- [x] ViewHistory model & DAO
- [x] UserPreference model & DAO
- [x] Converters cho Map<String, Integer>
- [x] Update AppDatabase version 4
- [x] Repository tracking methods
- [x] RecentlyViewedAdapter
- [x] UI layouts (card_resume_browsing, item_recently_viewed)
- [x] Drawables (gradients, icons)
- [x] MainActivity personalization sections
- [x] ProductDetailActivity tracking
- [x] onResume refresh logic
- [x] Material Design 3 compliance

---

## 🎨 Design Philosophy

**"Less is More"**
- Chỉ hiển thị khi có data
- Clean, không quá nhiều information
- Focus vào visual hierarchy
- Smooth, natural animations

**"User-Centric"**
- Dựa trên behavior thực tế của user
- Không invasive (không ép buộc)
- Transparent (user hiểu tại sao recommend)
- Helpful (giúp tìm lại sản phẩm quan tâm)

---

**Version:** 1.0  
**Database Version:** 4  
**Date:** December 5, 2025
