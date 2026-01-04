package com.tms.finalproject_autoshop.repository;

import com.tms.finalproject_autoshop.model.SpareParts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SparePartsRepository extends JpaRepository<SpareParts, Long> {
    @Query(nativeQuery = true, value = "SELECT * FROM spare_parts WHERE category = :category")
    List<SpareParts> findByCategory(String category);
    @Query(nativeQuery = true, value = """
            SELECT * FROM spare_parts WHERE
                    (:category IS NULL OR category = :category )AND
                     (:specKeys)::jsonb <@ specifications""")
    List<SpareParts> findOilByCategoryAndSpec(
            @Param("category") String category,
            @Param("specKeys") String specKeysJson
    );

}
