package com.yuzhi.aiagent.advisor;

import org.springframework.ai.chat.client.advisor.api.*;
import reactor.core.publisher.Flux;

import java.util.HashMap;
import java.util.Map;


//re-reaing, 让ai重读输入，提高精确度
public class ReReadingAdvisor implements CallAroundAdvisor, StreamAroundAdvisor {
	/*
	* 执行请求前，改写prompt
	* @param advisedRequest
	* @return
	* */

	private AdvisedRequest before(AdvisedRequest advisedRequest) { 

		Map<String, Object> advisedUserParams = new HashMap<>(advisedRequest.userParams());
		advisedUserParams.put("re2_input_query", advisedRequest.userText());

		return AdvisedRequest.from(advisedRequest)
			.userText("""
			    {re2_input_query}
			    Read the question again: {re2_input_query}
			    """)
			.userParams(advisedUserParams)
			.build();

	}

	@Override
	public AdvisedResponse aroundCall(AdvisedRequest advisedRequest, CallAroundAdvisorChain chain) { 
		return chain.nextAroundCall(this.before(advisedRequest));
	}

	@Override
	public Flux<AdvisedResponse> aroundStream(AdvisedRequest advisedRequest, StreamAroundAdvisorChain chain) {
		return chain.nextAroundStream(this.before(advisedRequest));
	}

	@Override
	public int getOrder() { 
		return 0;
	}

    @Override
    public String getName() { 
		return this.getClass().getSimpleName();
	}
}