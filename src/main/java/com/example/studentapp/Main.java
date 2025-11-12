package com.example.studentapp;

import javax.swing.*;

/**
 * Điểm vào chương trình: chạy trên EDT, mở JFrame quản lý sinh viên.
 */
public class Main {
    public static void main(String[] args) {
        // Thiết lập Look and Feel hệ thống nếu có
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) { }

        SwingUtilities.invokeLater(() -> {
            // Tạo frame và hiển thị
            StudentManagerFrame frame = new StudentManagerFrame();
            frame.setVisible(true);
        });
    }
}

