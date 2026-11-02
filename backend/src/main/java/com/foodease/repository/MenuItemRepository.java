package com.foodease.repository;

import com.foodease.model.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MenuItemRepository extends JpaRepository<MenuItem, String> {
    List<MenuItem> findAllByOrderBySeqAsc();
    List<MenuItem> findByCategoryIdOrderBySeqAsc(String categoryId);
    List<MenuItem> findByVegOrderBySeqAsc(boolean veg);
    List<MenuItem> findByBestSellerTrueOrderBySeqAsc();
    List<MenuItem> findByPopularTrueOrderBySeqAsc();
}
