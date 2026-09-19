package com.docmind.app.repository;

import com.docmind.app.entity.OutboxEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OutboxEventRepository extends JpaRepository<OutboxEvent,Long> {
    List<OutboxEvent> findTop100ByPublishedFalseOrderByIdAsc();
}
