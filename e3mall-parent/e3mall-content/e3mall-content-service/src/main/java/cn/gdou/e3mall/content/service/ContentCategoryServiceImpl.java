package cn.gdou.e3mall.content.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.e3mall.common.pojo.E3Result;
import cn.e3mall.common.pojo.EasyUIDataGridResult;
import cn.e3mall.common.pojo.EasyUITreeNode;
import cn.gdou.e3mall.mapper.TbContentCategoryMapper;
import cn.gdou.e3mall.mapper.TbContentMapper;
import cn.gdou.e3mall.pojo.TbContentCategory;
import cn.gdou.e3mall.pojo.TbContentCategoryExample;
import cn.gdou.e3mall.pojo.TbContentCategoryExample.Criteria;
import cn.gdou.e3mall.pojo.TbContentExample;

@Service
public class ContentCategoryServiceImpl implements ContentCategoryService {

	@Autowired
	private TbContentCategoryMapper tbContentCategoryMapper;
	@Autowired
	private TbContentMapper tbContentMapper;
	
	@Override
	public List<EasyUITreeNode> getCategoryList(Long parent_id) {

		TbContentCategoryExample example = new TbContentCategoryExample();
		Criteria criteria = example.createCriteria();
		criteria.andParentIdEqualTo(parent_id);
		List<TbContentCategory> list = tbContentCategoryMapper.selectByExample(example);
		List<EasyUITreeNode> result = new ArrayList<EasyUITreeNode>();
		//把结果封装到EasyUITreeNode中
		for (TbContentCategory tbContentCategory : list) {
			EasyUITreeNode node = new EasyUITreeNode();
			node.setId(tbContentCategory.getId());
			node.setText(tbContentCategory.getName());
			node.setState(tbContentCategory.getIsParent()?"closed":"open");
			
			result.add(node);
		}
		
		return result;
	}

	@Override
	public E3Result addCategory(Long parentId, String name) {
		TbContentCategory tbContentCategory = new TbContentCategory();
		Date date = new Date();
		//把获得的参数设置入类别对象
		tbContentCategory.setParentId(parentId);
		tbContentCategory.setName(name);
		tbContentCategory.setCreated(date);
		tbContentCategory.setUpdated(date);
		tbContentCategory.setStatus(1);//'状态。可选值:1(正常),2(删除)'
		tbContentCategory.setSortOrder(1);
		tbContentCategory.setIsParent(false);
		//插入数据库
		tbContentCategoryMapper.insert(tbContentCategory);
		//更新父对象的Isparent的值
		TbContentCategory parentCategory = tbContentCategoryMapper.selectByPrimaryKey(parentId);
		if(!parentCategory.getIsParent()){
			parentCategory.setIsParent(true);
			tbContentCategoryMapper.updateByPrimaryKey(parentCategory);
		}
		
		return E3Result.ok(tbContentCategory);
	}


}
