package org.example.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisConnectionUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Component
public class DemoMain {
    @Autowired
    @Qualifier("redisTemplate")
    RedisTemplate redisTemplate;


    @PostConstruct
    public void test() throws JsonProcessingException {
        //准备数据
        Map<String, Object> map = new HashMap<>();
        map.put("张三", 3);
        //pojo -> String
        String jsonUser = new ObjectMapper().writeValueAsString(map);

        //清空当前数据库
        redisTemplate.getConnectionFactory().getConnection().flushDb();

        //执行命令
        redisTemplate.opsForValue().set("map", jsonUser);
        System.out.println(redisTemplate.opsForValue().get("map"));//打印结果： {"name":"张三","age":3}

        redisTemplate.opsForValue().set("map", map);
        System.out.println(redisTemplate.opsForValue().get("map"));//打印结果：User(name=张三, age=3)

        //关闭连接
        RedisConnectionUtils.unbindConnection(redisTemplate.getConnectionFactory());
    }
}
