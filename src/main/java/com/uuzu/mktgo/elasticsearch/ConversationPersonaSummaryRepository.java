package com.uuzu.mktgo.elasticsearch;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ConversationPersonaSummaryRepository extends ElasticsearchRepository<ConversationPersonaSummary, String> {
}
