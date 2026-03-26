package com.example.springboot.util;

import java.sql.*;

public class SimpleDatabaseUpdater {

    public static void main(String[] args) {
        String url = "jdbc:mysql://127.0.0.1:3306/dormitory?serverTimezone=UTC&useUnicode=true&characterEncoding=utf-8&useSSL=false";
        String username = "root";
        String password = "123456";

        try (Connection conn = DriverManager.getConnection(url, username, password)) {
            System.out.println("Database connected!");
            
            addColumns(conn);
            updateData(conn);
            
            System.out.println("Database update completed!");
            
        } catch (SQLException e) {
            System.err.println("Database operation failed: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void addColumns(Connection conn) throws SQLException {
        String[] sqls = {
            "ALTER TABLE student ADD COLUMN IF NOT EXISTS college varchar(255) NULL",
            "ALTER TABLE student ADD COLUMN IF NOT EXISTS class_name varchar(255) NULL",
            "ALTER TABLE student ADD COLUMN IF NOT EXISTS dorm_build_id int(11) NULL",
            "ALTER TABLE student ADD COLUMN IF NOT EXISTS dorm_room_id int(11) NULL"
        };
        
        for (String sql : sqls) {
            try (Statement stmt = conn.createStatement()) {
                stmt.execute(sql);
                System.out.println("Executed: " + sql);
            } catch (SQLException e) {
                System.err.println("Failed to execute: " + sql);
                System.err.println("Error: " + e.getMessage());
            }
        }
    }

    private static void updateData(Connection conn) throws SQLException {
        Object[][] data = {
            {"Stest", "Jisuanji Xueyuan", "Jike 2001", 1, 1101},
            {"stu1", "Jisuanji Xueyuan", "Jike 2001", 1, 1101},
            {"stu10", "Waiguoyu Xueyuan", "Yingyu 2002", 2, 2101},
            {"stu11", "Waiguoyu Xueyuan", "Yingyu 2002", 2, 2101},
            {"stu12", "Wenxue Xueyuan", "Zhongwen 2001", 2, 2201},
            {"stu13", "Wenxue Xueyuan", "Zhongwen 2001", 2, 2201},
            {"stu14", "Lixue Xueyuan", "Shuxue 2001", 4, 4101},
            {"stu15", "Lixue Xueyuan", "Wuli 2001", 3, 3101},
            {"stu16", "Jisuanji Xueyuan", "Jike 2002", 1, 1201},
            {"stu17", "Jisuanji Xueyuan", "Jike 2002", 1, 1201},
            {"stu18", "Waiguoyu Xueyuan", "Yingyu 2001", 2, 2102},
            {"stu19", "Lixue Xueyuan", "Wuli 2001", 3, 3102},
            {"stu2", "Jisuanji Xueyuan", "Jike 2001", 1, 1101},
            {"stu20", "Wenxue Xueyuan", "Zhongwen 2002", 2, 2202},
            {"stu21", "Lixue Xueyuan", "Shuxue 2001", 4, 4102},
            {"stu22", "Jisuanji Xueyuan", "Jike 2001", 1, 1101},
            {"stu3", "Waiguoyu Xueyuan", "Yingyu 2001", 2, 2102},
            {"stu4", "Jisuanji Xueyuan", "Jike 2001", 1, 1101},
            {"stu5", "Lixue Xueyuan", "Wuli 2002", 3, 3201},
            {"stu6", "Wenxue Xueyuan", "Zhongwen 2001", 2, 2201},
            {"newstu2024002", "Xinxigongcheng Xueyuan", "Ruanjiangongcheng 2024-1", 1, 1102}
        };

        String sql = "UPDATE student SET college = ?, class_name = ?, dorm_build_id = ?, dorm_room_id = ? WHERE username = ?";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            for (Object[] row : data) {
                pstmt.setString(1, (String) row[1]);
                pstmt.setString(2, (String) row[2]);
                pstmt.setInt(3, (Integer) row[3]);
                pstmt.setInt(4, (Integer) row[4]);
                pstmt.setString(5, (String) row[0]);
                pstmt.addBatch();
            }
            int[] results = pstmt.executeBatch();
            System.out.println("Batch update completed, rows affected: " + results.length);
        }
    }
}
