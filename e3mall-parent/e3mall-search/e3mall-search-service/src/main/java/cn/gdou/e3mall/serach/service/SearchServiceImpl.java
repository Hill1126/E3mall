package cn.gdou.e3mall.serach.service;

import org.apache.solr.client.solrj.SolrQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.e3mall.common.pojo.SearchItem;
import cn.e3mall.common.pojo.SearchResult;
import cn.gdou.e3mall.serach.dao.SolrItemDao;

@Service
public class SearchServiceImpl implements SearchService {

	@Autowired
	private SolrItemDao dao ;

	@Override
	public SearchResult searchByQuery(String keyWord, int page, int rows) throws Exception {
		SolrQuery query = new SolrQuery();
		//设置查询关键字
		query.setQuery(keyWord);
		//设置高亮及前缀和后缀
		query.setHighlight(true);
		query.setHighlightSimplePre("<em style=\"color:red\">");
		query.setHighlightSimplePost("</em>");
		//设置起始查询区间
		int start = (page-1)*rows; //开始位置
		query.setStart(start);
		query.setRows(rows);
		//设置默认查询域
		query.set("df", "item_title");
		
		SearchResult searchResult = dao.searchByQuery(query);
		//设置总页数 总数/page
		int totlePage = searchResult.getRecourdCount()/rows;
		if(searchResult.getRecourdCount()%rows>0){
			totlePage++;
		}
		searchResult.setTotalPages(totlePage);
		return searchResult;
	}



}
