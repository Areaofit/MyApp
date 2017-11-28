package com.areaofit.app.zookeeper;

import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;

/**
 * 
 * @Description zookeeper的基本操作--基于原生zookeeper的api
 * @Author Huangjinwen
 * @Date 2017年11月28日-下午4:34:26
 */
public class ZookeeperBase {
	
	/**
	 * zookeeper通信地址
	 */
	public static final String CONNECTION_ADDR = "";
	
	/**
	 * session超时
	 */
	public static final int SESSION_TIMEOUT = 2000;
	
	/**
	 * 使用CountDownLatch进行同步控制，使只有在zookeeper连接成功的情况继续下一步操作
	 */
	static final CountDownLatch countDownLatch = new CountDownLatch(1);
	
	
	public static void main(String[] args) throws Exception {
		// 创建zookeeper实例
		ZooKeeper zooKeeper = new ZooKeeper(CONNECTION_ADDR, SESSION_TIMEOUT, new Watcher(){
			@Override
			public void process(WatchedEvent evevt) {
				// 获取事件状态
				KeeperState keeperState = evevt.getState();
				// 获取事件类型
				EventType eventType = evevt.getType();
				// 判断是否连接上
				if (keeperState == KeeperState.SyncConnected) {
					if (eventType == EventType.None) {
						System.out.println("zookeeper 连接成功！");
						countDownLatch.countDown();
					}
				}
			}
		});
		
		// 等待zookeeper连接成功
		countDownLatch.await();
		
		// 创建节点
		String firstNode = zooKeeper.create("/Test", "第一个节点".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
		System.out.println("新建的节点为-------> " + firstNode);
		
		// 获取节点的内容
		byte[] result = zooKeeper.getData("/Test", false, null);
		System.out.println("节点的内容为-------> " + new String(result));
		
		// 修改节点
		zooKeeper.setData("/Test", "修改第一个节点的内容".getBytes(), -1);
		result = zooKeeper.getData("/Test", false, null);
		System.out.println("修改的内容为-------> " + new String(result));
		
		// 删除节点
		zooKeeper.delete("/Test", -1);
		// 判断是否存在该节点
		System.out.println(zooKeeper.exists("/Test", false));
		
		// 创建父节点
		zooKeeper.create("/Parent", null, Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
		// 创建子节点，只能一级一级的创建子节点，zookeeper原生api不支持递归创建
		zooKeeper.create("/Parent/Child", "子节点内容".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
		result = zooKeeper.getData("/Parent/Child", false, null);
		System.out.println("子节点的内容为-------> " + new String(result));
		
		// 关闭连接
		zooKeeper.close();
	}

}
