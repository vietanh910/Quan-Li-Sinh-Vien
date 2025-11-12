package com.example.studentapp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;

/**
 * Lớp tiện ích quản lý kết nối và khởi tạo CSDL.
 */
public class Database {
    // URL kết nối SQLite - file DB đặt cùng thư mục chạy (có thể chỉnh đường dẫn tuyệt đối nếu muốn)
    private static final String URL = "jdbc:sqlite:students.db";

    static {
        // Đảm bảo driver được nạp (một số môi trường cần gọi tường minh)
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        // Khối static: tạo bảng nếu chưa có khi lớp được nạp.
        try (Connection conn = getConnection(); Statement st = conn.createStatement()) {
            String sql = "CREATE TABLE IF NOT EXISTS SinhVien(" +
                    "MaSV TEXT PRIMARY KEY," +
                    "HoTen TEXT NOT NULL," +
                    "Lop TEXT NOT NULL," +
                    "GPA REAL NOT NULL CHECK(GPA >= 0.0 AND GPA <= 4.0)" +
                    ")";
            st.execute(sql);

            // Tuỳ chọn: nếu bảng rỗng, thêm vài dòng mẫu để demo
            try (ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM SinhVien")) {
                if (rs.next() && rs.getInt(1) == 0) {
                    st.executeUpdate("INSERT INTO SinhVien(MaSV, HoTen, Lop, GPA) VALUES" +
                            "('SV001','Nguyen Van A','KTPM1',3.2)," +
                            "('SV002','Tran Thi B','KTPM2',3.8)," +
                            "('SV003','Le Van C','CNTT1',2.75)");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Trả về một kết nối mới. Người gọi chịu trách nhiệm đóng (nên dùng try-with-resources)
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }
}
