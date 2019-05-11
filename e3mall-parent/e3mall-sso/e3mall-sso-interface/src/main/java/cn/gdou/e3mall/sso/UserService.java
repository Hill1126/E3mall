package cn.gdou.e3mall.sso;

import cn.e3mall.common.pojo.E3Result;
import cn.gdou.e3mall.pojo.TbUser;

public interface UserService {

	E3Result registUser(TbUser user);
	
	E3Result checkParam(String param,int type);
	
	E3Result login(String username,String password);
	
	E3Result getUserByToken(String token);
}
