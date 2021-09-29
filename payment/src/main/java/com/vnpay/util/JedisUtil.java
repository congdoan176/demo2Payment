package com.vnpay.util;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.vnpay.controller.PaymentController;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Component
public class JedisUtil {
	@Autowired
    private JedisPool jedisPool;
	
	private static Logger logger = LogManager.getLogger(PaymentController.class);
	public String save(String key, String value) {
        Jedis jedis = jedisPool.getResource();
        try {
            return jedis.set(key, value);
        } catch (Exception e) {
        	logger.error("Hset redis Ex: ", e);
            return "-1L";
        } finally {
            // The business operation is completed and the connection is returned to the connection pool
//             jedisPool.returnResource(jedis);
             jedis.close();
        }
    }
	public Long expire(String key, Long time) {
		Jedis jedis = jedisPool.getResource();
        try {
            return jedis.pexpireAt(key, time);
        } catch (Exception e) {
        	logger.error("Hset redis Ex: ", e);
            return -1L;
        } finally {
            // The business operation is completed and the connection is returned to the connection pool
//             jedisPool.returnResource(jedis);
             jedis.close();
        }
	}
    /**
     * Get the specified Value according to the passed in key
     * @param key
     * @return
     * @author hw
     * @date 2018 14 December 2003
     */
    public String get(String key, String field) {
        Jedis jedis = jedisPool.getResource();
        try {
            return jedis.hget(key, field);
        } catch (Exception e) {
            return "-1";
        } finally {
            jedis.close();
        }
    }
    
	/* Determines whether a hash field exists or not. */
    public Boolean exits(String key) {
    	Jedis jedis = jedisPool.getResource();
        try {
            return jedis.exists(key);
        } catch (Exception e) {
            return false;
        } finally {
            jedis.close();
        }
    }
}
