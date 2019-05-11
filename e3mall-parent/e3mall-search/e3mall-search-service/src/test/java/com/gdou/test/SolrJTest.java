package com.gdou.test;

import java.io.IOException;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.CloudSolrServer;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;

public class SolrJTest {

	
	/*
	 * 更新和添加文档
	 */
	@Test
	public void addDocument(){
		try {
			//创建对象连接服务器
			SolrServer solrj  = new HttpSolrServer("http://192.168.25.128:9001/solr");
			//创建文档对象，添加对象信息
			SolrInputDocument doc = new SolrInputDocument();
			doc.addField("id", "doc1");
			doc.addField("item_price", "399");
			doc.addField("item_title", "倩碧y");
			//把文档对象添加到索引库
			solrj.add(doc);
			solrj.commit();
			//提交并关闭
		} catch (SolrServerException | IOException e) {
			e.printStackTrace();
		}
		
	}
	@Test
	public void delDocument(){
		try {
			//创建对象连接服务器
			SolrServer solrj  = new HttpSolrServer("http://192.168.25.128:9001/solr");
			//创建文档对象，添加对象信息
			//把文档对象添加到索引库
			solrj.deleteByQuery("id:doc1");
			solrj.commit();
			//提交并关闭
		} catch (SolrServerException | IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * solr集群测试
	 * @throws IOException 
	 * @throws SolrServerException 
	 */
	@Test
	public void SolrCloudTest() throws SolrServerException, IOException{
		
		//zookeeper的服务地址
		CloudSolrServer solrj = new CloudSolrServer("192.168.25.131:2181,192.168.25.131:2182,192.168.25.131:2183");
		solrj.setDefaultCollection("collection2");
		SolrInputDocument doc = new SolrInputDocument();
		doc.addField("id", "solrcloud");
		doc.addField("item_title", "手机");
		solrj.add(doc);
		solrj.commit();
		
	}
	
	@Test
	public void SolrCloudDelTest() throws SolrServerException, IOException{
		
		//zookeeper的服务地址
		CloudSolrServer solrj = new CloudSolrServer("192.168.25.131:2181,192.168.25.131:2182,192.168.25.131:2183");
		solrj.setDefaultCollection("collection2");
		solrj.deleteById("solrcloud");
		solrj.commit();
		
	}
	
	
}
