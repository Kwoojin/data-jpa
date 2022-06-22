package study.datajpa.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import study.datajpa.entity.Item;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ItemRepositoryTest {

    @Autowired
    private ItemRepository itemRepository;

    /**
     * 식별자가 없을 경우 save
     * 식별자가 있을 경우 merge ( select 실행 후 갈아끼움 )
     */
    @Test
    public void save() {
//        Item item = new Item();
        Item item = new Item("A");
        itemRepository.save(item);
    }

}