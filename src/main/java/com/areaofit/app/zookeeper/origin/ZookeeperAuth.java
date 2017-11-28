package com.areaofit.app.zookeeper.origin;

import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooKeeper;

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

}
