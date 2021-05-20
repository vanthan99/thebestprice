package dtu.thebestprice.repositories;

import dtu.thebestprice.entities.Search;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Set;

public interface SearchRepository extends JpaRepository<Search, Long> {
    // update số lượt tìm kiếm từ khóa lên 1
    @Modifying
    @Transactional
    @Query("UPDATE Search s SET s.numberOfSearch = (s.numberOfSearch + 1)" +
            " WHERE s.id =:searchId ")
    void updateNumberOfSearch(Long searchId);

    // lấy danh sách các từ khóa đã được tìm kiếm
    @Query("SELECT s.keyword FROM Search s")
    Set<String> findAllKeyword();

    Search findByKeyword(String keyword);

    boolean existsByKeyword(String keyword);

    Page<Search> findByOrderByNumberOfSearchDesc(Pageable pageable);

    List<Search> findTop20ByOrderByNumberOfSearchDesc();
}
