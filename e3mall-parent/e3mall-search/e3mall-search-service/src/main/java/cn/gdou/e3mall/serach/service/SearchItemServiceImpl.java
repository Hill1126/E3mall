package cn.gdou.e3mall.serach.service;

import java.io.IOException;
import java.util.List;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.e3mall.common.pojo.E3Result;
import cn.e3mall.common.pojo.SearchItem;
import cn.gdou.e3mall.serach.mapper.ItemMapper;

@Service
public class SearchItemServiceImpl implements SearchItemService {

	@Autowired
	private ItemMapper itemMapper;
	@Autowired
	private SolrServer solrServer ;
	
	
	@Override
	public E3Result importItmes() {
		
		try {
			List<SearchItem> items = itemMapper.getItemList();
			for (SearchItem searchItem : items) {
				SolrInputDocument document = new SolrInputDocument();
				document.addField("id", searchItem.getId());
				document.addField("item_title", searchItem.getTitle());
				document.addField("item_sell_point", searchItem.getSell_point());
				document.addField("item_price", searchItem.getPrice());
				document.addField("item_image", searchItem.getImage());
				document.addField("item_category_name", searchItem.getCategory_name());
				//写入索引库
				solrServer.add(document);
			}
			solrServer.commit();
			return E3Result.ok();
			
		} catch (SolrServerException | IOException e) {
			e.printStackTrace();
			return E3Result.build(500, "添加索引庫失敗");
		}
		
	}



}
