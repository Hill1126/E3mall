package com.gdou.e3mall;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import cn.e3mall.common.utils.JedisClientCluster;
import cn.e3mall.common.utils.JedisClientPool;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;

public class JedisClientTest {

	
	/*
	 * 
	 */
	@Test
	public void SingleTest(){
		Jedis jedis = new Jedis("192.168.25.128", 6379);
		jedis.set("single", "single_test");
		String str = jedis.get("single");
		System.out.println(str);
		
		jedis.close();
	}
	
	@Test
	public void JedisPool(){
		JedisPool pool = new JedisPool("192.168.25.128", 6379);
		Jedis jedis = pool.getResource();
		String str = jedis.get("single");
		System.out.println(str);
		
		pool.close();
	}
	
	@Test
	public void JedisCluster(){
		
		Set<HostAndPort> nodes = new HashSet<HostAndPort>();
		nodes.add(new HostAndPort("192.168.25.128", 7001));
		nodes.add(new HostAndPort("192.168.25.128", 7002));
		nodes.add(new HostAndPort("192.168.25.128", 7003));
		nodes.add(new HostAndPort("192.168.25.128", 7004));
		nodes.add(new HostAndPort("192.168.25.128", 7005));
		nodes.add(new HostAndPort("192.168.25.128", 7006));
		JedisCluster cluster = new JedisCluster(nodes);
		cluster.set("single", "cluster test");
		String str = cluster.get("single");
		System.out.println(str);
		
		cluster.close();
	}
	
	@Test
	public void SpringJedis(){
		ApplicationContext ac = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-redis.xml");
		JedisClientPool pool = ac.getBean(JedisClientPool.class);
		String str = pool.get("single");
		System.out.println(str);
	}
	
	@Test
	public void SpringJedisCluster(){
		ApplicationContext ac = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-redis.xml");
		JedisClientCluster cluster = ac.getBean(JedisClientCluster.class);
		
		String str = cluster.get("single");
		System.out.println(str);
		
	}
	
	
	
}
