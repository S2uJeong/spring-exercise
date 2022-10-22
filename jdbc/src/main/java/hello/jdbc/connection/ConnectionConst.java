package hello.jdbc.connection;

public abstract class ConnectionConst { // 규약대로 서버를 불러온다.
    public static final String URL = "jdbc:h2:tcp://localhost/~/test";
    public static final String USERNAME = "sa";
    public static final String PASSWORD = "";
}
