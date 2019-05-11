package cn.gdou.e3mall.search.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import cn.e3mall.common.pojo.SearchResult;
import cn.gdou.e3mall.serach.service.SearchService;

/**
 * @see 查找controller
 * @author HILL
 *
 */
@Controller
public class SearchController {

	@Autowired
	private SearchService searchService ;
	
	@Value("${ITEM_ROWS}")
	private Integer ITEM_ROWS;
	@RequestMapping("/search")
	public String searchByQuery(String keyword, @RequestParam(defaultValue="1")int page,Model model) throws Exception{
			keyword = new String(keyword.getBytes("iso8859-1"), "utf-8");
			SearchResult searchResult = searchService.searchByQuery(keyword, page, ITEM_ROWS);
			//把结果传递给jsp页面
			model.addAttribute("query", keyword);
			model.addAttribute("totalPages", searchResult.getTotalPages());
			model.addAttribute("recourdCount", searchResult.getRecourdCount());
			model.addAttribute("page", page);
			model.addAttribute("itemList", searchResult.getItemList());
		
			return "search";
		
		
		
	}
}
