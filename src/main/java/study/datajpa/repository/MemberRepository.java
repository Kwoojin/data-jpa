package study.datajpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {
    List<Member> findByUsernameAndAgeGreaterThanEqual(String username, int age);

    List<Member> findTop3HelloBy();

//    @Query(name = "Member.findByUsername") 관례상 Named Query - Domain.Method 명을 찾고, 찾지 못하면 Method 이름으로 Query 생성
    List<Member> findByUsername(@Param("username") String username);

    /**
     * 문자열로 작성된 Query 이지만
     * 어플리케이션 로딩 시점에 문법 오류 발생
     */
    @Query("select m from Member m where m.username = :username and m.age = :age")
    List<Member> findUser(@Param("username") String username, @Param("age") int age);

    @Query("select m.username from Member m")
    List<String> findUsernameList();

    @Query("select new study.datajpa.dto.MemberDto(m.id, m.username, t.name) from Member m join m.team t")
    List<MemberDto> findMemberDto();
}
