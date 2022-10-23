package hello.jdbc.domain;

import lombok.Data;

@Data
public class Member {

    private String memberId;
    private int money; // 회원이 가지고 있는 금액

    public Member() {
    }

    public Member(String memberID, int money) {
        this.memberId = memberID;
        this.money = money;
    }
}
