package hello.jdbc.repository;

import hello.jdbc.connection.DBConnectionUtil;
import hello.jdbc.domain.Member;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.util.NoSuchElementException;

/**
* JDBC - DriverManager 사용 (low level)
*/
@Slf4j
public class MemberRepositoryV0 {
    // 1) 회원 등록 메서드 - connection 불러오고, insert문과 setString으로 데이터 넣기
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
            /*pstmt.close();   외부리소스를 가져오는건데, 안 닫아주면 연결이 안끊어질 수 있다. ( 리소스 정리 )
            con.close();     그런데 close에서 exception이 터지면?? -> 따로 try-catch 만들어준다.( 밑이 따로 메소드화 ) */
            close(con, pstmt, null);
        }

    }

    // 2) 조회하는 메서드
    public Member findById(String memberId) throws SQLException {
        String sql = "select * from member where member_id = ?";

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con  = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, memberId);

            rs = pstmt.executeQuery();
            if (rs.next()) {  // 위 코드 실행 후, next()해줘야 데이터가 있는곳까지 가서 확인 할 수 있기 때문에.
                Member member = new Member();
                member.setMemberId(rs.getString("member_id"));
                member.setMoney(rs.getInt("money"));
                return member;
            } else {
                throw new NoSuchElementException("member not found memberId=" + memberId);
            }

        } catch (SQLException e) {
            log.error("db error", e);
            throw e;
        } finally {
            close(con, pstmt, rs);
        }

    }
    // 3) 수정 하는 메서드
    public void update(String memberId, int money) throws SQLException {
        String sql = "update member set money = ? where member_id=?";

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, money);
            pstmt.setString(2, memberId);
            int resultSize = pstmt.executeUpdate();
            log.info("resultSize={};",resultSize);
        } catch (SQLException e) {
            log.error("db error", e);
            throw e;
        } finally {
            close(con, pstmt, null);
        }
    }

    // 4) 삭제하는 메서드
    public void delete(String memberId) throws SQLException {
        String sql = "delete from member where member_id =?";

        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, memberId);
             pstmt.executeUpdate();
        } catch (SQLException e) {
            log.error("db error", e);
            throw e;
        } finally {
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
