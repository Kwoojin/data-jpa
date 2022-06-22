package study.datajpa.repository;

import org.springframework.beans.factory.annotation.Value;

public interface UsernameOnly {

    /**
     * Close Projection
     * 필요한 값만 조회하여 할당
     *
     * @Value 사용 시 Open Projection
     * 모든 데이터를 가져온 후 매칭 시켜줌
     */

    @Value("#{target.username + ' ' + target.age}")
    String getUsername();
}
