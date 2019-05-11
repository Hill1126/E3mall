package cn.gdou.e3mall.content.service;

import java.util.List;

import cn.e3mall.common.pojo.E3Result;
import cn.e3mall.common.pojo.EasyUIDataGridResult;
import cn.gdou.e3mall.pojo.TbContent;

public interface ContentService {
	
	EasyUIDataGridResult getContentList(Long categoryId,int page, int rows);
	
	E3Result addContent(TbContent tbContent);
	
	List<TbContent> getContenList(Long categoryId);
}
