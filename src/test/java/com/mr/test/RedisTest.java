package com.mr.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.concurrent.TimeUnit;

/**
 * Created by niudd on 2018/11/7.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring-redis.xml")
public class RedisTest {
    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    public void test1(){
        ValueOperations valueOperations = redisTemplate.opsForValue();
        valueOperations.set("key2","values2");
        System.out.println(valueOperations.get("key2"));
    }

    @Test
    public void test2(){
        redisTemplate.expire("key2",100,TimeUnit.SECONDS);

    }
    @Test
    public void test3(){

        System.out.println(redisTemplate.hasKey("key1"));
    }
    @Test
    public void test4(){
        System.out.println(redisTemplate.getExpire("key2"));
    }


}
