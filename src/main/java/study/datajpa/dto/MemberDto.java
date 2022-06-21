package study.datajpa.dto;

import lombok.Getter;
import org.springframework.util.Assert;
import study.datajpa.entity.Member;

@Getter
public class MemberDto {

    private Long id;
    private String username;
    private String teamName;

    public MemberDto(Long id, String username, String teamName) {
        this.id = id;
        this.username = username;
        this.teamName = teamName;
    }

    public MemberDto(Member member) {
        Assert.notNull(member.getId(), "id 필수값");
        this.id = member.getId();
        this.username = member.getUsername();
        this.teamName = null;
    }
}
