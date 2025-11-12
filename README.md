# Ứng dụng Quản Lý Sinh Viên (Swing + SQLite JDBC)

Ứng dụng Java SE đơn giản minh họa CRUD với SQLite thông qua JDBC và giao diện Swing thuần code.

## Chức năng
- Tự động tạo file CSDL `students.db` và bảng `SinhVien` nếu chưa tồn tại.
- Hiển thị toàn bộ sinh viên (ORDER BY MaSV).
- Thêm / Cập nhật / Xoá bản ghi với kiểm tra ràng buộc.
- Reset form nhập liệu.

## Cấu trúc bảng
```sql
CREATE TABLE IF NOT EXISTS SinhVien(
  MaSV TEXT PRIMARY KEY,
  HoTen TEXT NOT NULL,
  Lop  TEXT NOT NULL,
  GPA  REAL NOT NULL CHECK(GPA >= 0.0 AND GPA <= 4.0)
);
```

## Yêu cầu môi trường
- JDK 17+
- Thư viện `sqlite-jdbc` (nếu dùng Maven đã khai báo trong `pom.xml`).

## Chạy bằng Maven (nếu đã cài Maven)
```bash
mvn -q exec:java -Dexec.mainClass=com.example.studentapp.Main
```

## Chạy thủ công không Maven
1. Tải file JAR driver (ví dụ `sqlite-jdbc-3.45.3.0.jar`).
2. Biên dịch:
```bash
javac -cp sqlite-jdbc-3.45.3.0.jar -d out $(find src/main/java -name "*.java")
```
3. Chạy:
```bash
java -cp out;sqlite-jdbc-3.45.3.0.jar com.example.studentapp.Main
```
(Trên Windows thay `:` bằng `;`)  

## Ghi chú
- Sử dụng `try-with-resources` cho mọi thao tác JDBC.
- Không sửa được cột khóa `MaSV` khi cập nhật/xoá (disabled edit khi chọn trên bảng).
- GPA được validate là số 0.0 .. 4.0.

## Mở rộng gợi ý
- Thêm tìm kiếm theo tên / lớp.
- Phân trang nếu dữ liệu lớn.
- Xuất CSV.

