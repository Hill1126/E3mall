package cn.gdou.e3mall.service;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import cn.e3mall.common.pojo.E3Result;
import cn.e3mall.common.pojo.EasyUIDataGridResult;
import cn.e3mall.common.utils.IDUtils;
import cn.e3mall.common.utils.JedisClient;
import cn.e3mall.common.utils.JsonUtils;
import cn.gdou.e3mall.mapper.TbItemDescMapper;
import cn.gdou.e3mall.mapper.TbItemMapper;
import cn.gdou.e3mall.pojo.TbItem;
import cn.gdou.e3mall.pojo.TbItemDesc;
import cn.gdou.e3mall.pojo.TbItemExample;

@Service
public class ProductServiceImpl implements ProductService {

	@Autowired
	private TbItemMapper tbItemMapper;
	@Autowired
	private TbItemDescMapper tbItemDescMapper;
	@Autowired
	private JmsTemplate jmsTemplate;
	@Resource(name="topicDestination")
	private Destination destination;
	@Autowired
	private JedisClient jedisClient;
	
	@Value("${ITEM_INFO}")
	private String ITEM_INFO;
	@Value("${LIFE_TIME}")
	private Integer LIFE_TIME;
	
	@Override
	public TbItem getTbitemById(Long id) {
		//查询redis中是否有缓存
		String item_str = jedisClient.get(ITEM_INFO+":"+id+":"+"INFO");
		if(item_str!=null &&"".equals(item_str) ){
			TbItem tbItem = JsonUtils.jsonToPojo(item_str, TbItem.class);
			return tbItem;
		}
		//redis没有缓存，查询数据库
		TbItem result = tbItemMapper.selectByPrimaryKey(id);
		//添加到redis中并设置失效时间
		if(result!=null){
			item_str = JsonUtils.objectToJson(result);
			jedisClient.set( ITEM_INFO+":"+id+":"+"INFO", item_str);
			jedisClient.expire(ITEM_INFO+":"+id+":"+"INFO", LIFE_TIME);//设置缓存过期时间，单位秒
			return result;
		}
		return null;
		
	}

	@Override
	public EasyUIDataGridResult getItemList(int page, int rows) {
		//设置当前页数和每页显示条数
		PageHelper.startPage(page, rows);
		//执行查询
		TbItemExample tbItemExample = new TbItemExample();
		//获取结果
		List<TbItem> list = tbItemMapper.selectByExample(tbItemExample);
		//包装查询结果
		PageInfo<TbItem> pageInfo = new PageInfo<TbItem>(list);
		//封装到返回对象
		EasyUIDataGridResult result = new EasyUIDataGridResult();
		result.setTotal(pageInfo.getTotal());
		result.setRows(list);
		return result;
	}

	@Override
	public E3Result addItem(TbItem item, String desc) {

		//生成新增商品的id
		final long itemId = IDUtils.genItemId();
		Date date = new Date(); //生成日期
		//添加完整商品的信息
		item.setId(itemId);
		item.setStatus((byte)1);	//'商品状态，1-正常，2-下架，3-删除'
		item.setCreated(date);
		item.setUpdated(date);
		//创建商品描述对象
		TbItemDesc tbItemDesc = new TbItemDesc();
		//添加完整商品描述信息
		tbItemDesc.setItemDesc(desc);
		tbItemDesc.setCreated(date);
		tbItemDesc.setCreated(date);
		//描述信息指定好商品id
		tbItemDesc.setItemId(itemId);
		//存入数据库，返回成功信息
		tbItemMapper.insert(item);
		tbItemDescMapper.insert(tbItemDesc);
		
		//activemq 同步更新索引库
		jmsTemplate.send(destination, new MessageCreator() {
			
			@Override
			public Message createMessage(Session session) throws JMSException {
				TextMessage textMessage = session.createTextMessage(itemId+"");
				return textMessage;
			}
		});
		
		return E3Result.ok();
	}

	@Override
	public TbItemDesc getTbItemDescById(Long id) {
		//查询redis中是否有缓存
		String itemDesc_str = jedisClient.get(ITEM_INFO+":"+id+":"+"DESC");
		if(itemDesc_str!=null &&"".equals(itemDesc_str) ){
			TbItemDesc tbItemDesc = JsonUtils.jsonToPojo(itemDesc_str, TbItemDesc.class);
			return tbItemDesc;
		}
		 TbItemDesc result = tbItemDescMapper.selectByPrimaryKey(id);
		
		if(result!=null){
			itemDesc_str = JsonUtils.objectToJson(result);
			jedisClient.set( ITEM_INFO+":"+id+":"+"DESC", itemDesc_str);
			jedisClient.expire(ITEM_INFO+":"+id+":"+"DESC", LIFE_TIME);//设置缓存过期时间，单位秒
			return result;
		}
		return null;
	}

	@Override
	public E3Result updataItem(TbItem item, String desc) {
		//修改更改时间
		Date date = new Date();
		item.setUpdated(date);
		item.setStatus((byte)1);	//'商品状态，1-正常，2-下架，3-删除'
		//修改描述信息
		TbItemDesc itemDesc = tbItemDescMapper.selectByPrimaryKey(item.getId());
		itemDesc.setItemDesc(desc);
		itemDesc.setUpdated(date);
		//保存
		tbItemDescMapper.updateByPrimaryKey(itemDesc);
		tbItemMapper.updateByPrimaryKeySelective(item);//item对象不为空的才能插入
		
		//activemq 同步更新索引库
		jmsTemplate.send(destination, new MessageCreator() {
			
			@Override
			public Message createMessage(Session session) throws JMSException {
				TextMessage textMessage = session.createTextMessage(item.getId()+"");
				return textMessage;
			}
		});
		
		return E3Result.ok();
	}
	
	

}
