package study.datajpa.entity;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Persistable;

import javax.persistence.*;


/**
 * Id를 직접 할당해야 하는 경우,
 * 식별자 생성 전략에 따라 식별자에 값이 있기에 merge 가 호출됨
 *
 * merge 는 DB를 호출해서 값을 확인하기 때문에 매우 비효율적
 * Persistable 을 사용하여 해결 가능
 */
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Item extends BaseTimeEntity implements Persistable<String> {

    @Id //@GeneratedValue
    private String id;

    public Item(String id) {
        this.id = id;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public boolean isNew() {
        return getCreatedDate() == null;
    }
}
