package cn.gdou.e3mall.content.service;

import java.util.List;

import cn.e3mall.common.pojo.E3Result;
import cn.e3mall.common.pojo.EasyUITreeNode;

public interface ContentCategoryService {

	List<EasyUITreeNode> getCategoryList(Long parent_id);
	
	E3Result addCategory(Long parentId,String name);
}
