package hello.jdbc.repository;

import hello.jdbc.connection.DBConnectionUtil;
import hello.jdbc.domain.Member;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;

/**
* JDBC - DriverManager 사용 (low level)
*/
@Slf4j
public class MemberRepositoryV0 {
    public Member save(Member member) throws SQLException {
        String sql = "insert into member(member_id, money) values (?,?)";

        Connection con = null;
        PreparedStatement pstmt  = null;

        try {
            con = getConnection();  // DBConnectionUtil에서 설정했던 manager을 통해 DB를 불러와라. (밑 메서드와 연결)
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, member.getMemberId());
            pstmt.setInt(2, member.getMoney());
            pstmt.executeUpdate();
            return member;
        } catch (SQLException e) {
            log.error("db error", e);
            throw e;
        } finally {
            /*pstmt.close();   외부리소스를 가져오는건데, 안 닫아주면 연결이 안끊어질 수 있다.
            con.close();     그런데 close에서 exception이 터지면?? -> 따로 try-catch 만들어준다. */
            close(con, pstmt, null);
        }

    }

    // * PreparedStatement는 Statement의 자식으로 (상속관계) value를 바인딩 할때 쓰는 클래스이다.
    // 따라서 파라미터를 Statemen 객체로 하여도 pstmt를 가져올 수 있다.
    private void close (Connection con, Statement stmt, ResultSet rs) {

        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                log.info("error", e);
            }
        }

        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                log.info("error", e);
            }
        }

       if (con != null){
           try {
               con.close();
           } catch (SQLException e) {
               log.info("error", e);
           }
       }

    }

    private static Connection getConnection() {
        return DBConnectionUtil.getConnection();
    }

}
