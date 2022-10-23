package hello.jdbc.connection;

import com.zaxxer.hikari.HikariDataSource;
import com.zaxxer.hikari.pool.HikariPool;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static hello.jdbc.connection.ConnectionConst.*;

@Slf4j
public class ConnectionTest {

    @Test
    void driverManager() throws SQLException {
        // 서로 다른 connection을 두개 가져온다.
        Connection con1 = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        Connection con2 = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        log.info("connection={}, class={}", con1, con1.getClass());
        log.info("connection={}, class={}", con1, con1.getClass());
    }

    @Test
    void dataSourceDriverManager() throws SQLException {
        // 얘도 항상 새로운 커넥션을 획득하긴 한다.
        DriverManagerDataSource dataSource = new DriverManagerDataSource(URL, USERNAME, PASSWORD);
        useDataSource(dataSource);
    }

    private void useDataSource(DataSource dataSource) throws SQLException {
        // 하지만 이렇게 커넥션을 가지고 올때, dataSource 라는 인터페이스에서 가져온단게 차이점.
        // 설정과 사용의 분리
        Connection con1 = dataSource.getConnection();
        Connection con2 = dataSource.getConnection();
        log.info("connection={}, class={}", con1, con1.getClass());
        log.info("connection={}, class={}", con1, con1.getClass());
    }

    @SneakyThrows
    @Test
    void dataSourceConnectionPool() throws SQLException {
        // 커넥션 풀링
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(URL);
        dataSource.setUsername(USERNAME);
        dataSource.setPassword(PASSWORD);
        dataSource.setMaximumPoolSize(10);
        dataSource.setPoolName("Mypool");

        useDataSource(dataSource);
       // Thread.sleep(1000);  쓰레드 풀에 커넥션이 생성되는 로그를 확인하기 위해.

    }
}
