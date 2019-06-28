package org.common.eureka.service;

import java.util.Date;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.common.eureka.vo.UserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

import rx.Observable;
import rx.Observer;
import rx.observables.SyncOnSubscribe;

@Service
public class AggregationService {

	@Autowired
	private RestTemplate restTemplate;

	@HystrixCommand(fallbackMethod = "fallback")
	public Observable<UserVo> getUserById(String id) {

		// 创建一个被观察者
//		return Observable.create(observer -> {
//			// 请求用户微服务的/{id}端点
//			UserVo user = restTemplate.getForObject("http://microservice-user/findById/{id}", UserVo.class, id);
//			observer.onNext(user);
//			observer.onCompleted();
//		});

		// 请求用户微服务的/{id}端点
		UserVo user = restTemplate.getForObject("http://microservice-user/findById/{id}", UserVo.class, id);
		Observable<UserVo> observer = Observable.just(user);
		return observer.asObservable();
	}

	@HystrixCommand(fallbackMethod = "fallback")
	public Observable<UserVo> getArchiveUserByUserId(String id) {
		// 请求档案微服务的/user/{id}端点
		UserVo movieUser = restTemplate.getForObject("http://microservice-archive-ribbon/findById/{id}", UserVo.class,
				id);
		Observable<UserVo> observer = Observable.just(movieUser);
		return observer.asObservable();
	}

	public UserVo fallback(String id) {
		UserVo userVo = new UserVo();
		userVo.setUserId("-1");
		userVo.setUserName("samphin");
		userVo.setCreateTime(DateFormatUtils.format(new Date(), DateFormatUtils.ISO_DATETIME_TIME_ZONE_FORMAT.getPattern()));
		return userVo;
	}
}
