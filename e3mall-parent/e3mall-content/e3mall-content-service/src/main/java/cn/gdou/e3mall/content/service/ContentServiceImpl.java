package cn.gdou.e3mall.content.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import cn.e3mall.common.pojo.E3Result;
import cn.e3mall.common.pojo.EasyUIDataGridResult;
import cn.e3mall.common.utils.JedisClient;
import cn.e3mall.common.utils.JedisClientCluster;
import cn.e3mall.common.utils.JsonUtils;
import cn.gdou.e3mall.mapper.TbContentMapper;
import cn.gdou.e3mall.pojo.TbContent;
import cn.gdou.e3mall.pojo.TbContentExample;
import cn.gdou.e3mall.pojo.TbContentExample.Criteria;

@Service
public class ContentServiceImpl implements ContentService {

	@Autowired
	private TbContentMapper tbContentMapper;
	@Autowired
	private JedisClient jedisClient;
	@Value(value = "${CONTENT_TYPE}")
	String CONTENT_TYPE;
	
	
	@Override
	public EasyUIDataGridResult getContentList(Long categoryId,int page, int rows) {
		PageHelper.startPage(page, rows);//設置当前页面及显示条数
		//查询结果
		TbContentExample example = new TbContentExample();
		Criteria criteria = example.createCriteria();
		criteria.andCategoryIdEqualTo(categoryId);
		
		List<TbContent> list = tbContentMapper.selectByExampleWithBLOBs(example);//查询包括大文本的二进制内容
		PageInfo<TbContent> pageInfo = new PageInfo<TbContent>(list);
		
		//封装到返回对象
		EasyUIDataGridResult result = new EasyUIDataGridResult();
		result.setTotal(pageInfo.getTotal());
		result.setRows(list);
		
		return result;
	}

	@Override
	public E3Result addContent(TbContent tbContent) {
		tbContent.setCreated(new Date());
		tbContent.setUpdated(new Date());
		tbContentMapper.insert(tbContent);
		
		//添加内容成功，删除redis里面旧的缓存
		jedisClient.hdel(CONTENT_TYPE, tbContent.getCategoryId()+"");
		
		return E3Result.ok();
	}

	@Override
	public List<TbContent> getContenList(Long categoryId) {
		//查询redis中是否有缓存
		try {
			String Content_json = jedisClient.hget(CONTENT_TYPE, categoryId+"");
			if(Content_json!=null){
				List<TbContent> list = JsonUtils.jsonToList(Content_json, TbContent.class);
				return list;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//查询结果
		TbContentExample example = new TbContentExample();
		Criteria criteria = example.createCriteria();
		criteria.andCategoryIdEqualTo(categoryId);
		
		List<TbContent> list = tbContentMapper.selectByExampleWithBLOBs(example);//查询包括大文本的二进制内容
		String Content_json = JsonUtils.objectToJson(list);
		jedisClient.hset(CONTENT_TYPE, categoryId+"", Content_json);
		
		return list;
	}

}
