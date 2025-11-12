package com.example.studentapp;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO thực hiện CRUD với bảng SinhVien.
 */
public class StudentDAO {

    public List<Student> findAll() {
        List<Student> list = new ArrayList<>();
        String sql = "SELECT MaSV, HoTen, Lop, GPA FROM SinhVien ORDER BY MaSV";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Student s = new Student(
                        rs.getString("MaSV"),
                        rs.getString("HoTen"),
                        rs.getString("Lop"),
                        rs.getDouble("GPA")
                );
                list.add(s);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean existsById(String maSV) {
        String sql = "SELECT 1 FROM SinhVien WHERE MaSV = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maSV);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean insert(Student s) {
        if (existsById(s.getMaSV())) {
            return false; // khoá trùng
        }
        String sql = "INSERT INTO SinhVien(MaSV, HoTen, Lop, GPA) VALUES(?,?,?,?)";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, s.getMaSV());
            ps.setString(2, s.getHoTen());
            ps.setString(3, s.getLop());
            ps.setDouble(4, s.getGpa());
            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean update(Student s) {
        String sql = "UPDATE SinhVien SET HoTen = ?, Lop = ?, GPA = ? WHERE MaSV = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, s.getHoTen());
            ps.setString(2, s.getLop());
            ps.setDouble(3, s.getGpa());
            ps.setString(4, s.getMaSV());
            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteById(String maSV) {
        String sql = "DELETE FROM SinhVien WHERE MaSV = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maSV);
            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}

