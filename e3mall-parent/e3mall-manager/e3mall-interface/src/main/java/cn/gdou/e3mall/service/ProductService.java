package cn.gdou.e3mall.service;

import cn.e3mall.common.pojo.E3Result;
import cn.e3mall.common.pojo.EasyUIDataGridResult;
import cn.gdou.e3mall.pojo.TbItem;
import cn.gdou.e3mall.pojo.TbItemDesc;

public interface ProductService {

	public TbItem getTbitemById(Long id);
	
	public EasyUIDataGridResult getItemList(int page,int rows);
	
	//新增商品
	public E3Result addItem(TbItem item ,String desc);
	
	public E3Result updataItem(TbItem item ,String desc);
	
	public TbItemDesc getTbItemDescById(Long id);
	
}
