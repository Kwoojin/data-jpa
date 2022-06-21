package study.datajpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import study.datajpa.entity.Member;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {
    List<Member> findByUsernameAndAgeGreaterThanEqual(String username, int age);

    List<Member> findTop3HelloBy();

//    @Query(name = "Member.findByUsername") 관례상 Domain.Method 명을 찾고, 찾지 못하면 Method 이름으로 Query 생성
    List<Member> findByUsername(@Param("username") String username);
}
