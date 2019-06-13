package org.common.eureka.controller;

import java.util.HashMap;

import org.common.eureka.ZuulApplication;
import org.common.eureka.service.AggregationService;
import org.common.eureka.vo.UserVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import com.google.common.collect.Maps;

import rx.Observable;
import rx.Observer;
 
@RestController
public class AggregationController {
  public static final Logger LOGGER = LoggerFactory.getLogger(ZuulApplication.class);
 
  @Autowired
  private AggregationService aggregationService;
 
  @GetMapping("/aggregate/{id}")
  public DeferredResult<HashMap<String, UserVo>> aggregate(@PathVariable String id) {
	
    Observable<HashMap<String, UserVo>> result = this.aggregateObservable(id);
    return this.toDeferredResult(result);
  }
  
  public Observable<HashMap<String, UserVo>> aggregateObservable(String id) {
    // 合并两个或者多个Observables发射出的数据项，根据指定的函数变换它们
    return Observable.zip(
            this.aggregationService.getUserById(id),
            this.aggregationService.getArchiveUserByUserId(id),
            (UserVo, movieUserVo) -> {
              HashMap<String, UserVo> map = Maps.newHashMap();
              map.put("userVo", UserVo);
              map.put("archiveUserVo", movieUserVo);
              return map;
            }
    );
  }
  
  public DeferredResult<HashMap<String, UserVo>> toDeferredResult(Observable<HashMap<String, UserVo>> details) {
    DeferredResult<HashMap<String, UserVo>> result = new DeferredResult<>();
    // 订阅
    details.subscribe(new Observer<HashMap<String, UserVo>>() {
      @Override
      public void onCompleted() {
        LOGGER.info("完成...");
      }
 
      @Override
      public void onError(Throwable throwable) {
        LOGGER.error("发生错误...", throwable);
      }
 
      @Override
      public void onNext(HashMap<String, UserVo> movieDetails) {
        result.setResult(movieDetails);
      }
    });
    return result;
  }
}

