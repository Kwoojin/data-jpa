package study.datajpa.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.entity.Member;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@Transactional
@SpringBootTest
@Rollback(value = false)
class MemberJpaRepositoryTest {

    @Autowired MemberJpaRepository memberJpaRepository;

    @Test
    public void testMember() {
        Member member = new Member("memberA");
        Member savedMember = memberJpaRepository.save(member);

        Member findMember = memberJpaRepository.find(savedMember.getId());

        assertThat(findMember.getId()).isEqualTo(member.getId());
        assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
        assertThat(findMember).isEqualTo(member);
    }

    @Test
    public void basicCRUD() {
        Member member1 = new Member("member1");
        Member member2 = new Member("member2");
        memberJpaRepository.save(member1);
        memberJpaRepository.save(member2);

        //단일 조회 검증
        Member findMember1 = memberJpaRepository.findById(member1.getId()).orElseThrow();
        Member findMember2 = memberJpaRepository.findById(member2.getId()).orElseThrow();

        assertThat(findMember1).isEqualTo(member1);
        assertThat(findMember2).isEqualTo(member2);

        //리스트 조회 검증
        List<Member> all = memberJpaRepository.findAll();
        assertThat(all.size()).isEqualTo(2);

        //카운트 검증
        long count = memberJpaRepository.count();
        assertThat(count).isEqualTo(2);

        //삭제 검증
        memberJpaRepository.delete(member1);
        memberJpaRepository.delete(member2);

        long deleteCount = memberJpaRepository.count();
        assertThat(deleteCount).isEqualTo(0);
    }


    @Test
    public void findByUsernameAndAgeGreaterThanEqual() {
        Member member1 = new Member("AAA", 10, null);
        Member member2 = new Member("AAA", 20, null);
        memberJpaRepository.save(member1);
        memberJpaRepository.save(member2);

        List<Member> result = memberJpaRepository.findByUsernameAndAgeGreaterThanEqual("AAA", 15);

        assertThat(result.get(0).getUsername()).isEqualTo("AAA");
        assertThat(result.get(0).getAge()).isEqualTo(20);
        assertThat(result.size()).isEqualTo(1);
    }

    @Test
    public void testNamedQuery() {
        Member member1 = new Member("AAA", 10, null);
        Member member2 = new Member("BBB", 20, null);
        memberJpaRepository.save(member1);
        memberJpaRepository.save(member2);

        List<Member> result = memberJpaRepository.findByUsername("BBB");
        assertThat(result.get(0).getUsername()).isEqualTo("BBB");
    }

    @Test
    public void paging() {
        for(int i=1; i<=10; i++) {
            memberJpaRepository.save(new Member("member"+i, 10+i*5, null));
        }

        int age = 10;
        int offset = 1;
        int limit = 3;

        List<Member> members = memberJpaRepository.findByPage(age, offset, limit);
        long totalCount = memberJpaRepository.totalCount(age);

        //페이지 계산 공식 적용
        // totalPage = totalCount / size ...
        // 마지막 페이지...
        // 최초 페이지..

        assertThat(members.size()).isEqualTo(3);
        assertThat(totalCount).isEqualTo(10);
    }

    @Test
    public void bulkUpdate() {
        //given
        for(int i=1; i<=5; i++) {
            memberJpaRepository.save(new Member("member"+i, 10+i*5, null));
        }

        //when
        int resultCount = memberJpaRepository.bulkAgePlus(20);

        //then
        assertThat(resultCount).isEqualTo(4);
    }




}