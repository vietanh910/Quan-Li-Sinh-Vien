package com.example.studentapp;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

/**
 * JFrame quản lý sinh viên: CRUD + hiển thị.
 */
public class StudentManagerFrame extends JFrame {
    private final JTextField txtMaSV = new JTextField();
    private final JTextField txtHoTen = new JTextField();
    private final JTextField txtLop = new JTextField();
    private final JTextField txtGPA = new JTextField();

    private final DefaultTableModel tableModel = new DefaultTableModel(new Object[]{"MaSV","HoTen","Lop","GPA"}, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false; // không cho sửa trực tiếp trên bảng
        }
    };
    private final JTable table = new JTable(tableModel);

    private final StudentDAO dao = new StudentDAO();

    private final JButton btnShow = new JButton("Hiển thị");
    private final JButton btnAdd = new JButton("Thêm");
    private final JButton btnUpdate = new JButton("Cập nhật");
    private final JButton btnDelete = new JButton("Xoá");
    private final JButton btnReset = new JButton("Reset");

    public StudentManagerFrame() {
        setTitle("Quản Lý Sinh Viên - SQLite JDBC");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 500);
        setLocationRelativeTo(null);
        initUI();
        initEvents();
    }

    private void initUI() {
        // Panel form phía NORTH
        JPanel formPanel = new JPanel(new GridLayout(4,2,5,5));
        formPanel.setBorder(BorderFactory.createTitledBorder("Thông tin sinh viên"));
        formPanel.add(new JLabel("Mã SV:"));
        formPanel.add(txtMaSV);
        formPanel.add(new JLabel("Họ tên:"));
        formPanel.add(txtHoTen);
        formPanel.add(new JLabel("Lớp:"));
        formPanel.add(txtLop);
        formPanel.add(new JLabel("GPA:"));
        formPanel.add(txtGPA);

        // Bảng ở CENTER
        JScrollPane scrollPane = new JScrollPane(table);

        // Panel nút phía SOUTH
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.setBorder(BorderFactory.createTitledBorder("Chức năng"));
        buttonPanel.add(btnShow);
        buttonPanel.add(btnAdd);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnReset);

        getContentPane().setLayout(new BorderLayout(5,5));
        getContentPane().add(formPanel, BorderLayout.NORTH);
        getContentPane().add(scrollPane, BorderLayout.CENTER);
        getContentPane().add(buttonPanel, BorderLayout.SOUTH);
    }

    private void initEvents() {
        btnShow.addActionListener(e -> onShow());
        btnAdd.addActionListener(e -> onAdd());
        btnUpdate.addActionListener(e -> onUpdate());
        btnDelete.addActionListener(e -> onDelete());
        btnReset.addActionListener(e -> onReset());

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (table.getSelectedRow() >= 0) {
                    fillFormFromSelectedRow();
                }
            }
        });
    }

    // Nút Hiển thị: tải toàn bộ dữ liệu
    private void onShow() {
        List<Student> list = dao.findAll();
        loadTableData(list);
    }

    // Nút Thêm: validate -> insert
    private void onAdd() {
        Student s = validateForm();
        if (s == null) return; // lỗi đã thông báo
        if (!dao.insert(s)) {
            JOptionPane.showMessageDialog(this, "Thêm thất bại: Mã SV đã tồn tại hoặc lỗi hệ thống.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        JOptionPane.showMessageDialog(this, "Thêm thành công.");
        onShow();
        onReset();
    }

    // Nút Cập nhật: phải chọn hàng, validate và update theo MaSV
    private void onUpdate() {
        if (txtMaSV.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Hãy chọn sinh viên cần cập nhật từ bảng.", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        Student s = validateForm();
        if (s == null) return;
        if (!dao.update(s)) {
            JOptionPane.showMessageDialog(this, "Cập nhật thất bại: kiểm tra Mã SV hoặc dữ liệu.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        JOptionPane.showMessageDialog(this, "Cập nhật thành công.");
        onShow();
    }

    // Nút Xoá: hỏi xác nhận và xoá theo MaSV
    private void onDelete() {
        String ma = txtMaSV.getText().trim();
        if (ma.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Hãy chọn sinh viên cần xoá từ bảng.", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn xoá Mã SV: " + ma + "?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            if (!dao.deleteById(ma)) {
                JOptionPane.showMessageDialog(this, "Xoá thất bại.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Xoá thành công.");
                onShow();
                onReset();
            }
        }
    }

    // Nút Reset: xoá nội dung form + clear selection
    private void onReset() {
        txtMaSV.setText("");
        txtHoTen.setText("");
        txtLop.setText("");
        txtGPA.setText("");
        table.clearSelection();
        txtMaSV.setEditable(true); // có thể thêm mới lại
    }

    // Đổ dữ liệu lên form khi chọn hàng
    private void fillFormFromSelectedRow() {
        int row = table.getSelectedRow();
        if (row < 0) return;
        txtMaSV.setText(String.valueOf(tableModel.getValueAt(row,0)));
        txtHoTen.setText(String.valueOf(tableModel.getValueAt(row,1)));
        txtLop.setText(String.valueOf(tableModel.getValueAt(row,2)));
        txtGPA.setText(String.valueOf(tableModel.getValueAt(row,3)));
        txtMaSV.setEditable(false); // khoá lại khi cập nhật/xoá
    }

    // Validate form và trả về Student; nếu lỗi -> JOptionPane và return null
    private Student validateForm() {
        String ma = txtMaSV.getText().trim();
        String ten = txtHoTen.getText().trim();
        String lop = txtLop.getText().trim();
        String gpaStr = txtGPA.getText().trim();

        if (ma.isEmpty() || ten.isEmpty() || lop.isEmpty() || gpaStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Không được để trống trường nào.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return null;
        }
        double gpa;
        try {
            gpa = Double.parseDouble(gpaStr);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "GPA phải là số thực.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return null;
        }
        if (gpa < 0.0 || gpa > 4.0) {
            JOptionPane.showMessageDialog(this, "GPA phải nằm trong khoảng 0.0 - 4.0", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return null;
        }
        return new Student(ma, ten, lop, gpa);
    }

    // Nạp list sinh viên vào bảng
    private void loadTableData(List<Student> list) {
        tableModel.setRowCount(0); // xoá dữ liệu cũ
        for (Student s : list) {
            tableModel.addRow(new Object[]{s.getMaSV(), s.getHoTen(), s.getLop(), s.getGpa()});
        }
    }
}

