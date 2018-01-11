package com.uuzu.mktgo.elasticsearch;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface FullAppInfoMonthlySummaryRepository extends ElasticsearchRepository<FullAppInfoMonthlySummary, String> {
}