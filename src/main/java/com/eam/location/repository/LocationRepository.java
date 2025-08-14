package com.eam.location.repository;

import com.eam.location.model.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LocationRepository extends JpaRepository<Location, Long> {

    @Query(value = """
        WITH RECURSIVE location_tree AS (
            SELECT l.id, l.name, l.client_id, l.parent_id
            FROM locations l
            WHERE l.id = :parentId AND l.client_id = :clientId
            UNION ALL
            SELECT child.id, child.name, child.client_id, child.parent_id
            FROM locations child
            INNER JOIN location_tree lt ON lt.id = child.parent_id
        )
        SELECT * FROM location_tree
        """, nativeQuery = true)
    List<Object[]> findLocationTree(Long clientId, Long parentId);

    @Query(value = """
        WITH RECURSIVE location_tree AS (
            SELECT l.*
            FROM locations l
            WHERE l.id = :parentId
            UNION ALL
            SELECT child.*
            FROM locations child
            INNER JOIN location_tree lt ON child.parent_id = lt.id
        )
        SELECT * FROM location_tree;
        """, nativeQuery = true)
    List<Location> findLocationTree(@Param("parentId") Long parentId);

    List<Location> findByClientId(Long clientId);
}