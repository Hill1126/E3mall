package cn.gdou.e3mall.serach.service;

import cn.e3mall.common.pojo.SearchResult;

public interface SearchService {


	SearchResult searchByQuery(String keyWord, int page, int rows) throws Exception;
	
	
}
