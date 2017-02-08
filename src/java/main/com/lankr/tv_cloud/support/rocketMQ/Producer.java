package com.lankr.tv_cloud.support.rocketMQ;

import java.util.List;
import java.util.concurrent.TimeUnit;

import com.alibaba.rocketmq.client.exception.MQBrokerException;
import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.client.producer.DefaultMQProducer;
import com.alibaba.rocketmq.client.producer.SendResult;
import com.alibaba.rocketmq.common.message.Message;
import com.alibaba.rocketmq.remoting.exception.RemotingException;
import com.lankr.tv_cloud.Config;

public class Producer {
	
	/** 
     * 一个应用创建一个Producer，由应用来维护此对象，可以设置为全局对象或者单例<br> 
     * 注意：ProducerGroupName需要由应用来保证唯一<br> 
     * ProducerGroup这个概念发送普通的消息时，作用不大，但是发送分布式事务消息时，比较关键， 
     * 因为服务器会回查这个Group下的任意一个Producer 
     */  
	private static Producer pro ;
	
	final DefaultMQProducer producer ;
	
	/**
	 * 定义消息生产者
	 * 指定域名及端口号
	 */
	private Producer() {
		String messageServer = Config.messageServer;
		producer = new DefaultMQProducer("ProducerGroupNama") ;
		producer.setNamesrvAddr(messageServer) ;
		producer.setInstanceName("Producer") ;
		
		//producer生效前需要初始化一次，只需要一次就够了，再次使用不需要初始化
		try {
			producer.start() ;
		} catch (MQClientException e) {
			e.printStackTrace();
		}
	}
	
	public static Producer instance() {
		if (pro == null) {
			pro = new Producer() ;
		}
		return pro ;
	}
	
	/**
	 * 发送消息
	 * @param msgs
	 */
	
	public void send(List<Message> msgs) throws Exception{
		for (Message msg : msgs) {
			SendResult sendResult = producer.send(msg);
			 
		}
		
		//关联容器的勾子函数，容器关闭时producer关闭
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {

			@Override
			public void run() {
				producer.shutdown() ;
			}
        	
        }));
        System.out.println("over");
	}
	
	/**
	 * 发送消息
	 * @param msgs
	 */
	
	public void send(Message msg) throws Exception{
		SendResult sendResult = producer.send(msg);
			 
		//关联容器的勾子函数，容器关闭时producer关闭
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {

			@Override
			public void run() {
				producer.shutdown() ;
			}
        	
        }));
        System.out.println("over");
	}
}
