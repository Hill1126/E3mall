package cn.gdou.e3mall.service;

import java.util.List;

import cn.e3mall.common.pojo.EasyUITreeNode;

public interface ItemCatService {

	public List<EasyUITreeNode> getItemCatList(long parent_id);
}
