package com.areaofit.app.zookeeper.origin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.ACL;

/**
 * 
 * @Description zookeeper的授权操作
 * @Author Huangjinwen
 * @Date 2017年11月28日-下午5:49:27
 */
public class ZookeeperAuth implements Watcher {

	/**
	 * 测试路径
	 */
	public static final String TEST_PATH = "/TestAuth";

	/**
	 * 测试删除节点
	 */
	public static final String TEST_DELETE_PATH = "/TestAuth/DeleteNode";

	/**
	 * 认证类型
	 */
	public static final String AUTHEN_TYPE = "digest";

	/**
	 * 正确认证密码
	 */
	public static final String CORRECT_AUTHEN_PASS = "123456";

	/**
	 * 错误认证密码
	 */
	public static final String ERROR_AUTHEN_PASS = "654321";

	static ZooKeeper zooKeeper = null;

	private CountDownLatch countDownLatch = new CountDownLatch(1);

	@Override
	public void process(WatchedEvent event) {
		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		if (event == null) {
			return;
		}
		// 获取连接状态
		KeeperState keeperState = event.getState();
		// 获取事件类型
		EventType eventType = event.getType();
		String logPrefix = "【Watcher】";
		System.out.println(logPrefix + "收到Watcher通知");
		System.out.println(logPrefix + "连接状态:\t" + keeperState.toString());
		System.out.println(logPrefix + "事件类型:\t" + eventType.toString());
		if (KeeperState.SyncConnected == keeperState) {
			if (EventType.None == eventType) {
				System.out.println(logPrefix + "成功连接上ZK服务器");
				countDownLatch.countDown();
			}
		} else if (KeeperState.Disconnected == keeperState) {
			System.out.println(logPrefix + "与ZK服务器断开连接");
		} else if (KeeperState.AuthFailed == keeperState) {
			System.out.println(logPrefix + "权限检查失败");
		} else if (KeeperState.Expired == keeperState) {
			System.out.println(logPrefix + "会话失效");
		}
	}
	
	/**
	 * 连接ZK服务器
	 */
	public void connection(String zkUrls, int sessionTimeOut) {
		this.disConnection();
		try {
			zooKeeper = new ZooKeeper(zkUrls, sessionTimeOut, this);
			zooKeeper.addAuthInfo(AUTHEN_TYPE, CORRECT_AUTHEN_PASS.getBytes());
			System.out.println("【Main】"+"开始连接ZK服务器");
			countDownLatch.countDown();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 断开ZK服务器
	 */
	public void disConnection() {
		if (zooKeeper != null) {
			try {
				zooKeeper.close();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 使用正确的认证信息获取节点数据
	 */
	public static void getDataByCorrectAuthen() {
		String prefix = "【使用正确的授权信息】";
		try {
			System.out.println(prefix + "获取数据：" + TEST_PATH);
			System.out.println(prefix + "成功获取数据：" + zooKeeper.getData(TEST_PATH, false, null));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 使用错误的认证信息获取节点数据
	 */
	public static void getDataByErrorAuthen() {
		String prefix = "【使用错误的授权信息】";
		try {
			ZooKeeper zooKeeper1 = new ZooKeeper(ZookeeperBase.CONNECTION_ADDR, ZookeeperBase.SESSION_TIMEOUT, null);
			zooKeeper1.addAuthInfo(AUTHEN_TYPE, ERROR_AUTHEN_PASS.getBytes());
			Thread.sleep(200);
			System.out.println(prefix + "获取数据：" + TEST_PATH);
			System.out.println(prefix + "成功获取数据：" + zooKeeper1.getData(TEST_PATH, false, null));
		} catch (Exception e) {
			System.err.println(prefix + "获取数据失败，原因：" + e.getMessage());
		}
	}
	
	/**
	 * 不使用认证信息获取节点的数据
	 */
	public static void getDataByNoAuthen() {
		String prefix = "【不使用的授权信息】";
		try {
			ZooKeeper zooKeeper2 = new ZooKeeper(ZookeeperBase.CONNECTION_ADDR, ZookeeperBase.SESSION_TIMEOUT, null);
			Thread.sleep(200);
			System.out.println(prefix + "获取数据：" + TEST_PATH);
			System.out.println(prefix + "成功获取数据：" + zooKeeper2.getData(TEST_PATH, false, null));
		} catch (Exception e) {
			System.err.println(prefix + "获取数据失败，原因：" + e.getMessage());
		}
	}
	
	
	

	public static void main(String[] args) {
		ZookeeperAuth zookeeperAuth = new ZookeeperAuth();
		zookeeperAuth.connection(ZookeeperBase.CONNECTION_ADDR, ZookeeperBase.SESSION_TIMEOUT);
		List<ACL> acls = new ArrayList<ACL>(1);
		for (ACL ids_acl : Ids.CREATOR_ALL_ACL) {
			acls.add(ids_acl);
		}
		try {
			zooKeeper.create(TEST_PATH, "init data".getBytes(), acls, CreateMode.PERSISTENT);
			System.out.println("使用授权key：" + CORRECT_AUTHEN_PASS + "创建节点："+ TEST_PATH + ", 初始内容是: init data");
			zooKeeper.create(TEST_DELETE_PATH, "will be deleted".getBytes(), acls, CreateMode.PERSISTENT);
			System.out.println("使用授权key：" + CORRECT_AUTHEN_PASS + "创建节点："+ TEST_DELETE_PATH + ", 初始内容是: will be deleted");
		} catch (KeeperException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
