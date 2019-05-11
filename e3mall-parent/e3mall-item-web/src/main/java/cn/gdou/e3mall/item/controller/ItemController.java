package cn.gdou.e3mall.item.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.gdou.e3mall.item.pojo.Item;
import cn.gdou.e3mall.pojo.TbItem;
import cn.gdou.e3mall.pojo.TbItemDesc;
import cn.gdou.e3mall.service.ProductService;

@Controller
public class ItemController {

	@Autowired
	private ProductService productService;
	
	@RequestMapping("/item/{itemId}")
	public String showItemInfo(@PathVariable Long itemId,Model model){
		//查询数据库获得信息并且封装到对象中
		TbItem tbItem = productService.getTbitemById(itemId);
		Item item = new Item(tbItem);
		TbItemDesc itemDesc = productService.getTbItemDescById(itemId);
		
		model.addAttribute("item", item);
		model.addAttribute("itemDesc", itemDesc);
		return "item";
		
	}
}
