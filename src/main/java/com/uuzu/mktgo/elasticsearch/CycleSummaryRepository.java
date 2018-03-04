/*
 * Copyright 2015-2020 msun.com All right reserved.
 */
package com.uuzu.mktgo.elasticsearch;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface CycleSummaryRepository extends ElasticsearchRepository<CycleSummary, String> {

}
