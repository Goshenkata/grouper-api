package com.example.grouperapi.repositories;

import com.example.grouperapi.model.dto.ObjectSearchReturnDTO;
import com.example.grouperapi.model.entities.GroupEntity;
import org.hibernate.annotations.Cache;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GroupRepository extends JpaRepository<GroupEntity, Long> {
    Optional<GroupEntity> findByName(String name);
    @Query("select new com.example.grouperapi.model.dto.ObjectSearchReturnDTO(g.iconUrl, g.name) from GroupEntity g")
    List<ObjectSearchReturnDTO> getQueryResult(String query);
}
