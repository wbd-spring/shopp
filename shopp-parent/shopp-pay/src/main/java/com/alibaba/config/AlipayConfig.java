package com.alibaba.config;

import java.io.FileWriter;
import java.io.IOException;

/* *
 *类名：AlipayConfig
 *功能：基础配置类
 *详细：设置帐户有关信息及返回路径
 *修改日期：2017-04-05
 *说明：
 *以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己网站的需要，按照技术文档编写,并非一定要使用该代码。
 *该代码仅供学习和研究支付宝接口使用，只是提供一个参考。
 */

public class AlipayConfig {
	
//↓↓↓↓↓↓↓↓↓↓请在这里配置您的基本信息↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓

	// 应用ID,您的APPID，收款账号既是您的APPID对应支付宝账号
	public static String app_id ="2016091900550742";
	

	// 商户私钥，您的PKCS8格式RSA2私钥
    public static String merchant_private_key = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQDExByesZHPWVgAVL77TWC0kuEqK+o0rqX9zCRVLCN5v9B/WTthSZ1PZzmNwdBEC4W1iMYdCZUZ3rFP+fN/f7gQLu95iG0JiflQ8Yg+ec0JgA+ARV05sqGuVdhZQgoeb0jNzycodhRibDLMLE7KCHAqt2hgG91Kfp+ssP9LEjZjh72w0VVe2OVnHsMNm4AiJqltsC9Zqf893EZ6Ng6ljbv01K0X+z7OU/3phM3seX9aKc2w6GgMACy+XAKw/q8UVM9eXheCxzhSQscsUNSRYBnwJguCzRpW7ykhqikx7MaQntWvXc6ox2OZVfr+uCzauYpX0/nWYWL+Q0g9517fvHKvAgMBAAECggEAGOSChmUKgEPaSThvbNVuLYoDaFVj7MRHhaMk2Y2a81Ub6HB+faFVgG0br95cHa+je8LQ5W2H6lzs66RAdNnQjGOTsjlbKgDFhML5rIW2NcR4sY6U6664m5iKpUgghZcoi7jwSEMf8w+jIYdddFu67PyE/NuplDoOs0c1FOc58D9LyttWpVmS4BIBtPd0Sc5o9mvKnjhxupGg3d3M2wA87HEE0W0ok6prQPZxFtcCGTggDnbN42vsBdT9hVkEwkAy4wXfHo+sgu/5PhuBh+9aOcJ7P/AW0k8sEdJyXZiL89pmNAVDpDqOjvnLVJrtvKTkiPR+IuKKXqohgY2/6GGaKQKBgQD73UfelghzDWhk5Et4Gl5l8VWxKAzD6Thgpp1ncZxGq+ERsGFo0P8FIjgDctBQfTO1KklbCN2LcPVjAicM35SJiTIRVzSQ0lzfi12BmHGnGk1KKm6vv4kxVcAeLIoFZUVkRlXoLf5j8hWQng0avPK0e/AcH/JTfXKbo8oS+LuwrQKBgQDH/zlC49sHzi76zOyRs8aSkQxvX18rvMbeJ2AgTt1r4J7XkecmxNPQ/1HqNTwY10XnadkuWi7X9kQXsN0QjTbtpD1XdKJGltMYYXm2GrbdguumzxxY6uO3uizLAxKcuOit0sUIDvUBNitw0YvILe/NrmhbF4VIti+NtDp1+8RwSwKBgQC+3PGZvBirF34E9vw6B8GBf2Fw3ujNje8rTxf5TB8hbx8gSImI5NhdC5FnC5rRZA/LOkBcvFsnUHKklP0CxGvLziwi9/LdzIyU2wgBLYTzHPt1OLKofH165YCxXQ87B4OOeb6gNDqTguDUtvE3UfiazVEn/w2kN2yihXyc4+FYAQKBgQC59l0+DzR4m4UfezB1gl4fpnOTk3Cg8JVRt/cv02Ubi+7mrynp6Jaz/NQkHH/W8UR0mv1wljZ8ZOaHyNKlM0lhypenHDJPxKpgPzK22zwx2CxaHLZVD4PSMHC6DA9gXqcsCZFfnti92aqm6pE4/029SHCMUuCB7bVl47GARywu5QKBgDHgijWQYIzl0EyL9HX3j5WZP0L96DDBDnI/z0bvcQUkoBaZ6Zldg3cfI+bvsStoOCOqVSCrPSWl+PuY2JtcvE4QixX9DaSAjGxTCDQ5xZaMqDCUVAlB4YfXBxIS6hV9BQEWhrBvvFzmyV5jUKDXNQp00KVK12gAJvtk3onLI4+W";
	
	// 支付宝公钥,查看地址：https://openhome.alipay.com/platform/keyManage.htm 对应APPID下的支付宝公钥。
    public static String alipay_public_key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAogBH7aVtgdYrlH6dXtvZYieKD3RKJ/b0mX4oBdfHiJ/nicul7nEb6WwluvnAD0xbvKxisEE8GzRIy8yx+N5S2NT5/9io/kfa2r0XOJduGycwM36Zf28bdNbnuIA5iBt8Iyh9eBSvo0Uu71peG8D9aClr66E4H3VDHv+DxR7mlnajD1DOxGorWMG7p4bnOzrqtWN0qxkL1PfIXSdHD4xDVh2YVkEerr2RLjnRWA/CLArp9dKfxF3NZoX5WaU8BZxAx1fdpsfV00ZicMGrwsPKDtYaVm+ODx/iKBTiXyXn5mTBuhLQJ131rRI6u5F7q7udQohudieja370qk0Pa6QowwIDAQAB";

	// 服务器异步通知页面路径  需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
	public static String notify_url ="http://zgh.tunnel.qydev.com/alibaba/callBack/notifyUrl";

	// 页面跳转同步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
	public static String return_url ="http://zgh.tunnel.qydev.com/alibaba/callBack/returnUrl";

	// 签名方式
	public static String sign_type = "RSA2";
	
	// 字符编码格式
	public static String charset = "utf-8";
	
	// 支付宝网关
	public static String gatewayUrl = "https://openapi.alipaydev.com/gateway.do";
	
	// 支付宝网关
	public static String log_path = "C:\\";


//↑↑↑↑↑↑↑↑↑↑请在这里配置您的基本信息↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑

    /** 
     * 写日志，方便测试（看网站需求，也可以改成把记录存入数据库）
     * @param sWord 要写入日志里的文本内容
     */
    public static void logResult(String sWord) {
        FileWriter writer = null;
        try {
            writer = new FileWriter(log_path + "alipay_log_" + System.currentTimeMillis()+".txt");
            writer.write(sWord);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

