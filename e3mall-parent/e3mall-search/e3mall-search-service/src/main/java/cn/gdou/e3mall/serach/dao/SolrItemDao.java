package cn.gdou.e3mall.serach.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import cn.e3mall.common.pojo.SearchItem;
import cn.e3mall.common.pojo.SearchResult;

/**
 * @see solr索引库访问对象
 * @author HILL
 *
 */
@Repository
public class SolrItemDao {
	
	@Autowired
	private SolrServer solrServer;
	
	public SearchResult searchByQuery(SolrQuery query) throws Exception{
		QueryResponse queryResponse = solrServer.query(query);
		SolrDocumentList solrDocumentList = queryResponse.getResults();
		
		SearchResult result = new SearchResult();
		result.setRecourdCount((int)solrDocumentList.getNumFound());
		
		 List<SearchItem> itemList = new ArrayList<>();
		//取出result中的结果放入result的list中
		for (SolrDocument solrDocument : solrDocumentList) {
			
			SearchItem searchItem = new SearchItem();
			searchItem.setCategory_name((String) solrDocument.get("item_category_name"));
			searchItem.setId((String) solrDocument.get("id"));
			String images = (String) solrDocument.get("item_image");
			if(images!=null && !"".equals(images)){
				String[] split = images.split(",");
				searchItem.setImage( split[0]);
			}
			searchItem.setPrice((long) solrDocument.get("item_price"));
			searchItem.setSell_point((String) solrDocument.get("item_sell_point"));
			//取出高亮结果
			List<String> list = queryResponse.getHighlighting().get(solrDocument.get("id")).get("item_title");
			//判断list是否为空来为title赋值
			if(list != null && list.size()>0 ){
				searchItem.setTitle(list.get(0));
			}else{
				searchItem.setTitle((String) solrDocument.get("item_title"));
			}
			itemList.add(searchItem);
			
		}
		result.setItemList(itemList);
		return result;
		
	}
	
}
