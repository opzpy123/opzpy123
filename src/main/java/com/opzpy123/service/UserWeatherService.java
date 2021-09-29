package com.opzpy123.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.opzpy123.mapper.AuthUserMapper;
import com.opzpy123.mapper.UserWeatherMapper;
import com.opzpy123.model.AuthUser;
import com.opzpy123.model.UserWeather;
import com.opzpy123.model.vo.ApiResponse;
import com.opzpy123.model.vo.WeatherTaskVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

@Service
@Slf4j
public class UserWeatherService extends ServiceImpl<UserWeatherMapper, UserWeather> {

    @Resource
    private UserWeatherMapper userWeatherMapper;

    //存放定时任务Future
    @Resource(name = "scheduledFutureMap")
    private ConcurrentHashMap<Long, ScheduledFuture<?>> scheduledFutureMap;

    @Resource
    private ThreadPoolTaskScheduler threadPoolTaskScheduler;

    public List<UserWeather> findByUserId(Long userId) {
        return userWeatherMapper.selectList(new QueryWrapper<UserWeather>().lambda()
                .eq(UserWeather::getUserId, userId));
    }

    public void addUserWeather(UserWeather userWeather) {
        userWeatherMapper.insert(userWeather);
    }

    public ApiResponse<String> openUserWeather(Long userWeatherId) {
        UserWeather userWeather = baseMapper.selectById(userWeatherId);
        if (userWeather == null || userWeather.getEnabled() == 1) return ApiResponse.ofSuccess();
        //获取定时任务的future

        ScheduledFuture<?> scheduledFuture = scheduledFutureMap.get(userWeather.getId());
        if (scheduledFuture == null) {
            //构建任务
            WeatherTaskVo taskVo = new WeatherTaskVo(userWeather);
            //开启任务
            ScheduledFuture<?> schedule = threadPoolTaskScheduler
                    .schedule(taskVo, new CronTrigger(userWeather.getCronExpression()));
            //任务future暂存
            if (schedule != null) scheduledFutureMap.put(userWeather.getId(), schedule);
        }
        userWeather.setEnabled(1);
        userWeatherMapper.updateById(userWeather);
        return ApiResponse.ofSuccess();
    }

    public ApiResponse<String> stopUserWeather(Long userWeatherId) {
        UserWeather userWeather = baseMapper.selectById(userWeatherId);
        if (userWeather == null || userWeather.getEnabled() == 0) return ApiResponse.ofSuccess();

        ScheduledFuture<?> scheduledFuture = scheduledFutureMap.get(userWeather.getId());
        if (scheduledFuture == null) return ApiResponse.ofSuccess();
        // 查看任务是否在正常执行之前结束,正常true
        boolean cancelled = scheduledFuture.isCancelled();
        while (!cancelled) {
            scheduledFuture.cancel(true);
            cancelled = scheduledFuture.isCancelled();
        }
        //清理map
        scheduledFutureMap.remove(userWeather.getId());
        //更新数据库
        userWeather.setEnabled(0);
        userWeatherMapper.updateById(userWeather);
        return ApiResponse.ofSuccess();
    }
}
