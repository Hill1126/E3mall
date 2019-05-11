package cn.gdou.e3mall.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.gdou.e3mall.pojo.TbItem;
import cn.gdou.e3mall.service.ProductService;

@Controller
public class ProductController {

	@Autowired
	private ProductService productService;
	
	@RequestMapping("/item/{itemId}")
	@ResponseBody
	public TbItem getTbitemById(@PathVariable Long itemId){
		return productService.getTbitemById(itemId);
		
	}
}
