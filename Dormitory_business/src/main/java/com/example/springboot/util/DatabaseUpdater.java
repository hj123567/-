package com.example.springboot.util;

import java.sql.*;

public class DatabaseUpdater {

    public static void main(String[] args) {
        String url = "jdbc:mysql://127.0.0.1:3306/dormitory?serverTimezone=UTC&useUnicode=true&characterEncoding=utf-8&useSSL=false";
        String username = "root";
        String password = "123456";

        try (Connection conn = DriverManager.getConnection(url, username, password)) {
            System.out.println("数据库连接成功！");
            
            // 首先检查表是否存在这些字段
            addMissingColumns(conn);
            
            // 更新学生数据
            updateStudentData(conn);
            
            System.out.println("数据库更新完成！");
            
        } catch (SQLException e) {
            System.err.println("数据库操作失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void addMissingColumns(Connection conn) throws SQLException {
        String[] sqls = {
            "ALTER TABLE `student` ADD COLUMN IF NOT EXISTS `college` varchar(255) NULL DEFAULT NULL COMMENT '学院' AFTER `avatar`",
            "ALTER TABLE `student` ADD COLUMN IF NOT EXISTS `class_name` varchar(255) NULL DEFAULT NULL COMMENT '班级' AFTER `college`",
            "ALTER TABLE `student` ADD COLUMN IF NOT EXISTS `dorm_build_id` int(11) NULL DEFAULT NULL COMMENT '宿舍楼号' AFTER `class_name`",
            "ALTER TABLE `student` ADD COLUMN IF NOT EXISTS `dorm_room_id` int(11) NULL DEFAULT NULL COMMENT '房间号' AFTER `dorm_build_id`"
        };
        
        for (String sql : sqls) {
            try (Statement stmt = conn.createStatement()) {
                stmt.execute(sql);
                System.out.println("执行SQL成功: " + sql);
            } catch (SQLException e) {
                System.err.println("执行SQL失败: " + sql);
                System.err.println("错误信息: " + e.getMessage());
            }
        }
    }

    private static void updateStudentData(Connection conn) throws SQLException {
        Object[][] studentData = {
            {"Stest", "计算机学院", "计科2001", 1, 1101},
            {"stu1", "计算机学院", "计科2001", 1, 1101},
            {"stu10", "外国语学院", "英语2002", 2, 2101},
            {"stu11", "外国语学院", "英语2002", 2, 2101},
            {"stu12", "文学院", "中文2001", 2, 2201},
            {"stu13", "文学院", "中文2001", 2, 2201},
            {"stu14", "理学院", "数学2001", 4, 4101},
            {"stu15", "理学院", "物理2001", 3, 3101},
            {"stu16", "计算机学院", "计科2002", 1, 1201},
            {"stu17", "计算机学院", "计科2002", 1, 1201},
            {"stu18", "外国语学院", "英语2001", 2, 2102},
            {"stu19", "理学院", "物理2001", 3, 3102},
            {"stu2", "计算机学院", "计科2001", 1, 1101},
            {"stu20", "文学院", "中文2002", 2, 2202},
            {"stu21", "理学院", "数学2001", 4, 4102},
            {"stu22", "计算机学院", "计科2001", 1, 1101},
            {"stu3", "外国语学院", "英语2001", 2, 2102},
            {"stu4", "计算机学院", "计科2001", 1, 1101},
            {"stu5", "理学院", "物理2002", 3, 3201},
            {"stu6", "文学院", "中文2001", 2, 2201},
            {"newstu2024002", "信息工程学院", "软件工程2024-1班", 1, 1102}
        };

        String sql = "UPDATE `student` SET `college` = ?, `class_name` = ?, `dorm_build_id` = ?, `dorm_room_id` = ? WHERE `username` = ?";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            for (Object[] data : studentData) {
                pstmt.setString(1, (String) data[1]);
                pstmt.setString(2, (String) data[2]);
                pstmt.setInt(3, (Integer) data[3]);
                pstmt.setInt(4, (Integer) data[4]);
                pstmt.setString(5, (String) data[0]);
                pstmt.addBatch();
            }
            int[] results = pstmt.executeBatch();
            System.out.println("批量更新完成，影响行数: " + results.length);
        }
    }
}
