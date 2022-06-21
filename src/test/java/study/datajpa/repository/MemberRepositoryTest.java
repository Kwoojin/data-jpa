package study.datajpa.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;
import study.datajpa.entity.Team;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;


@SpringBootTest
@Transactional
@Rollback(value = false)
class MemberRepositoryTest {

    @Autowired MemberRepository memberRepository;
    @Autowired TeamRepository teamRepository;

    /**
     * Rollback false 로 인한
     * 실행되는 Query 확인용
     */
    @AfterEach
    void afterEach() {
        memberRepository.deleteAll();
    }

    @Test
    public void testMember() {
        Member member = new Member("memberA");
        Member savedMember = memberRepository.save(member);

        Member findMember = memberRepository.findById(savedMember.getId()).orElseThrow();

        assertThat(findMember.getId()).isEqualTo(member.getId());
        assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
        assertThat(findMember).isEqualTo(member);
    }

    @Test
    public void basicCRUD() {
        Member member1 = new Member("member1");
        Member member2 = new Member("member2");
        memberRepository.save(member1);
        memberRepository.save(member2);

        //단일 조회 검증
        Member findMember1 = memberRepository.findById(member1.getId()).orElseThrow();
        Member findMember2 = memberRepository.findById(member2.getId()).orElseThrow();

        assertThat(findMember1).isEqualTo(member1);
        assertThat(findMember2).isEqualTo(member2);

        //리스트 조회 검증
        List<Member> all = memberRepository.findAll();
        assertThat(all.size()).isEqualTo(2);

        //카운트 검증
        long count = memberRepository.count();
        assertThat(count).isEqualTo(2);

        //삭제 검증
        memberRepository.delete(member1);
        memberRepository.delete(member2);

        long deleteCount = memberRepository.count();
        assertThat(deleteCount).isEqualTo(0);
    }

    @Test
    @DisplayName("NamedQuery")
    public void findByUsernameAndAgeGreaterThanEqual() {
        Member member1 = new Member("AAA", 10, null);
        Member member2 = new Member("AAA", 20, null);
        memberRepository.save(member1);
        memberRepository.save(member2);

        List<Member> result = memberRepository.findByUsernameAndAgeGreaterThanEqual("AAA", 15);

        assertThat(result.get(0).getUsername()).isEqualTo("AAA");
        assertThat(result.get(0).getAge()).isEqualTo(20);
        assertThat(result.size()).isEqualTo(1);
    }

    @Test
    public void findHelloBy() {
        List<Member> helloBy = memberRepository.findTop3HelloBy();
//        helloBy.stream().forEach(member -> System.out.println("member = " + member));
    }

    @Test
    public void namedQuery() {
        Member member1 = new Member("AAA", 10, null);
        Member member2 = new Member("BBB", 20, null);
        memberRepository.save(member1);
        memberRepository.save(member2);

        List<Member> result = memberRepository.findByUsername("BBB");
        assertThat(result.get(0).getUsername()).isEqualTo("BBB");
    }

    @Test
    public void testQuery() {
        Member member1 = new Member("AAA", 10, null);
        Member member2 = new Member("BBB", 20, null);
        memberRepository.save(member1);
        memberRepository.save(member2);

        List<Member> result = memberRepository.findUser("AAA", 10);
        assertThat(result.get(0)).isEqualTo(member1);
    }


    @Test
    public void findUsernameList() {
        Member member1 = new Member("AAA", 10, null);
        Member member2 = new Member("BBB", 20, null);
        memberRepository.save(member1);
        memberRepository.save(member2);

        List<String> result = memberRepository.findUsernameList();
        assertThat(result.get(0)).isEqualTo(member1.getUsername());
        assertThat(result.get(1)).isEqualTo(member2.getUsername());
    }

    @Test
    public void findDto() {
        Team team = new Team("Q");
        teamRepository.save(team);

        Member member = new Member("AAA", 10, team);
        memberRepository.save(member);

        List<MemberDto> result = memberRepository.findMemberDto();
        assertThat(result.get(0).getTeamName()).isEqualTo(team.getName());
    }

    @Test
    public void findByNames() {
        Member member1 = new Member("AAA", 10, null);
        Member member2 = new Member("BBB", 20, null);
        memberRepository.save(member1);
        memberRepository.save(member2);

        List<Member> result = memberRepository.finByNames(Arrays.asList(member1.getUsername(), member2.getUsername()));
        assertThat(result).containsExactly(member1, member2);
    }

    @Test
    public void returnType() {
        Member member1 = new Member("AAA", 10, null);
        Member member2 = new Member("AAA", 20, null);
        memberRepository.save(member1);
        memberRepository.save(member2);

        List<Member> listMember = memberRepository.findListByUsername("AAA");
        listMember.stream().forEach(m -> System.out.println("*** m = " + m));

        Member oneMember = memberRepository.findOneByUsername("CCC");
        assertThat(oneMember).isNull();

        assertThatThrownBy(() -> memberRepository.findOptionalByUsername("AAA"))
                .isInstanceOf(IncorrectResultSizeDataAccessException.class);
    }


    @Test
    public void paging() {
        for(int i=1; i<=10; i++) {
            memberRepository.save(new Member("member"+i, 10, null));
        }

        int age = 10;
        //Page 는 0부터 시작
        PageRequest pageRequest = PageRequest.of(1, 3, Sort.by(Sort.Direction.DESC, "username"));

        //when
        Page<Member> page = memberRepository.findPageByAge(age, pageRequest);

        Page<MemberDto> toMap = page.map(
                member -> new MemberDto(member.getId(), member.getUsername(), null)
        );

        //then
        List<Member> content = page.getContent();

        assertThat(content.size()).isEqualTo(3);
        assertThat(page.getTotalElements()).isEqualTo(10);
        assertThat(page.getNumber()).isEqualTo(1);
        assertThat(page.getTotalPages()).isEqualTo(4);
//        assertThat(page.isFirst()).isTrue();
        assertThat(page.hasNext()).isTrue();

    }


    @Test
    public void paging_slice() {
        for(int i=1; i<=10; i++) {
            memberRepository.save(new Member("member"+i, 10, null));
        }

        int age = 10;
        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "username"));

        //when
        Slice<Member> page = memberRepository.findSliceByAge(age, pageRequest);

        //then
        List<Member> content = page.getContent();

        assertThat(content.size()).isEqualTo(3);
        assertThat(page.getNumber()).isEqualTo(0);
        assertThat(page.isFirst()).isTrue();
        assertThat(page.hasNext()).isTrue();

    }
}

