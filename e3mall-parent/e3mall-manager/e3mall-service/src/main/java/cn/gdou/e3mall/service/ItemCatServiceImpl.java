package cn.gdou.e3mall.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.e3mall.common.pojo.EasyUITreeNode;
import cn.gdou.e3mall.mapper.TbItemCatMapper;
import cn.gdou.e3mall.pojo.TbItemCat;
import cn.gdou.e3mall.pojo.TbItemCatExample;
import cn.gdou.e3mall.pojo.TbItemCatExample.Criteria;

@Service
public class ItemCatServiceImpl implements ItemCatService {

	@Autowired
	private TbItemCatMapper tbItemCatMapper;
	
	
	@Override
	public List<EasyUITreeNode> getItemCatList(long parent_id) {
		//根据parent_id查节点
		TbItemCatExample example = new TbItemCatExample();
		Criteria criteria = example.createCriteria();
		criteria.andParentIdEqualTo(parent_id);
		List<TbItemCat> list = tbItemCatMapper.selectByExample(example);
		List<EasyUITreeNode> resultList = new ArrayList<>();
		//根据查询的结果集合封装到EasyUITreeNode对象
		for (TbItemCat tbItemCat : list) {
			EasyUITreeNode node = new EasyUITreeNode();
			node.setId(tbItemCat.getId());
			node.setText(tbItemCat.getName());
			node.setState(tbItemCat.getIsParent()?"closed":"open");
			resultList.add(node);
		}
		
		return resultList;
	}

}
