/*
 * Copyright 2015-2020 msun.com All right reserved.
 */
package com.uuzu.mktgo.elasticsearch;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * @author zxc Aug 28, 2015 11:46:11 AM
 */
public interface ConversationPersonaSummaryRepository extends ElasticsearchRepository<ConversationPersonaSummary, String> {

}
