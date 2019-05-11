package com.e3mall.SolrJ;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocumentList;
import org.junit.Test;

public class SolrJTest {

	@Test
	public void queryTest() throws SolrServerException{
		
		SolrServer server = new HttpSolrServer("http://192.168.25.128:9001/solr");	
		SolrQuery query = new SolrQuery();
		query.setQuery("item_title:手机");
		QueryResponse queryResponse = server.query(query);
		SolrDocumentList solrDocumentList = queryResponse.getResults();
		long numFound = solrDocumentList.getNumFound();
		System.out.println(numFound);
	}
}
