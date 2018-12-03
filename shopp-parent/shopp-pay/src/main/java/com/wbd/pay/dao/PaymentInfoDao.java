package com.wbd.pay.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.wbd.pay.api.pojo.PaymentInfo;

@Mapper
public interface PaymentInfoDao {

	@Select("select * from payment_info where  id=#{id}")
	public PaymentInfo getPaymentInfo(@Param("id") Long id);

	@Insert("insert into payment_info ( id,userid,typeid,orderid,platformorderid,price,source,state,created,updated,paymessage) value(null,#{userId},#{typeId},#{orderId},#{platformorderId},#{price},#{source},#{state},#{created},#{updated},#{payMessage})")
	@Options(useGeneratedKeys = true, keyProperty = "id") // 添加该行，product中的id将被自动添加
	public Integer savePaymentType(PaymentInfo paymentInfo);

	/**
	 * 根据订单id查询支付信息
	 * <p>Title: getByOrderIdPayInfo</p>  
	 * <p>Description: </p>  
	 * @param orderId
	 * @return
	 */
	@Select("select * from payment_info where  orderId=#{orderId}")
	public PaymentInfo getByOrderIdPayInfo(@Param("orderId") String orderId);

	/**
	 * 根据订单id更新支付信息状态
	 * <p>Title: updatePayInfo</p>  
	 * <p>Description: </p>  
	 * @param paymentInfo
	 */
	@Update("update payment_info set state =#{state},payMessage=#{payMessage},platformorderId=#{platformorderId},updated=#{updated} where orderId=#{orderId} ")
	public void updatePayInfo(PaymentInfo paymentInfo);
}
