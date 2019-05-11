package cn.gdou.e3mall.sso;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import cn.e3mall.common.pojo.E3Result;
import cn.e3mall.common.utils.JedisClient;
import cn.e3mall.common.utils.JsonUtils;
import cn.gdou.e3mall.mapper.TbUserMapper;
import cn.gdou.e3mall.pojo.TbUser;
import cn.gdou.e3mall.pojo.TbUserExample;
import cn.gdou.e3mall.pojo.TbUserExample.Criteria;

/**
 * 用户登陆注册服务
 * @author HILL
 *
 */

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private TbUserMapper tbUserMapper;
	@Autowired
	private JedisClient jedisClient;
	@Value("${USER_TOKEN}")
	private String USER_TOKEN;
	@Value("${SESSION_EXPIRE}")
	private int SESSION_EXPIRE;
	
	
	/**
	 * @param param 要校验的内容 
	 * @param type  1、2、3分别代表username、phone、email
	 */
	@Override
	public E3Result checkParam(String param, int type) {
		TbUserExample example = new TbUserExample();
		Criteria criteria = example.createCriteria();
		
		//判断类型，返回相应结果
		if(1 == type && param!=null){
			criteria.andUsernameEqualTo(param);
		}else if( 2== type && param!=null){
			criteria.andPhoneEqualTo(param);
		}else {
			return E3Result.build(400, "非法参数");
		}
		List<TbUser> list = tbUserMapper.selectByExample(example);
		if( list!=null && list.size()>0){
			//用户存在不允许注册
			return E3Result.ok(false);
		}
		
		return E3Result.ok(true);
	}


	@Override
	public E3Result registUser(TbUser user) {
		//校验username、phone、email的值
		//1、2、3分别代表username、phone、email
		if(StringUtils.isBlank(user.getUsername())){
			return E3Result.build(400, "username is not allow balank");
		}
		if(StringUtils.isBlank(user.getPassword())){
			return E3Result.build(400, "password is not allow balank");
		}
		//检查用户名
		E3Result result = checkParam(user.getUsername(),1);
		if(!(Boolean)result.getData()) return E3Result.build(400, "用户名不可用");
		//检查电话
		result = checkParam(user.getPhone(), 2);
		if(!(Boolean)result.getData()) return E3Result.build(400, "手机号码不可用");
		
		//经过校验
		//md5加密用户的密码
		String hex_password = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
		//补充user的数据
		user.setPassword(hex_password);
		user.setCreated(new Date());
		user.setUpdated(new Date());
		//插入数据库
		tbUserMapper.insert(user);
		
		return E3Result.ok();
	}


	@Override
	public E3Result login(String username, String password) {

		//根据账号密码查询用户是否存在
		TbUserExample example = new TbUserExample();
		Criteria criteria = example.createCriteria();
		criteria.andUsernameEqualTo(username);
		//密码转换为md5加密
		criteria.andPasswordEqualTo(DigestUtils.md5DigestAsHex(password.getBytes()));
		List<TbUser> list = tbUserMapper.selectByExample(example);
		//若不存在则直接返回失败
		if(list==null || list.size()<1){
			return E3Result.build(400, "用户名或密码错误！");
		}
		//生成token放入redis中
		TbUser tbUser = list.get(0);
		tbUser.setPassword(null);
		String token = UUID.randomUUID().toString();
		jedisClient.set(USER_TOKEN+":"+token, JsonUtils.objectToJson(tbUser));
		jedisClient.expire(USER_TOKEN+":"+token, SESSION_EXPIRE);		//模拟session设置过期时间 3600s
		//
		return E3Result.ok(token);
	}


	@Override
	public E3Result getUserByToken(String token) {
		//根据token从redi中查询用户是否存在
		String json_obj = jedisClient.get(USER_TOKEN+":"+token);
		//检查该字符串，根据判断结果判断用户是否登录
		if(json_obj==null || "".equals(json_obj)){
			return E3Result.build(400, "您的信息已过期，请重新登录！");
		}
		TbUser tbUser = JsonUtils.jsonToPojo(json_obj, TbUser.class);
		return E3Result.ok(tbUser);
	}
	
}
