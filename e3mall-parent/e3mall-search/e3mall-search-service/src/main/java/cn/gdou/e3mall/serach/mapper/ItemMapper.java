package cn.gdou.e3mall.serach.mapper;

import java.util.List;

import cn.e3mall.common.pojo.SearchItem;


public interface ItemMapper {

	List<SearchItem> getItemList();
	SearchItem getItemById(Long itemId);
}
