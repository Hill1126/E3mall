package cn.gdou.e3mall.protal.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.gdou.e3mall.content.service.ContentService;
import cn.gdou.e3mall.pojo.TbContent;

/**
 * 首頁controller
 * @author HILL
 *
 */
@Controller
public class IndexController {

	@Autowired
	private ContentService contentService;
	
	@Value("${INDEX_LUNBO}")
	private Long INDEX_LUNBO;
	
	@RequestMapping("/index.html")
	public String toIndex(Model model){
		List<TbContent> list = contentService.getContenList(INDEX_LUNBO);
		model.addAttribute("ad1List", list);
		return "index";
	}
}
