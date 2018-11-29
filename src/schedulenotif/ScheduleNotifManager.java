package schedulenotif;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import schedule.model.NotifTable;
import schedule.model.Schedule;
import schedule.model.ScheduleTable;
import schedule.model.SendMail;
import schedule.model.TargetTable;

public class ScheduleNotifManager {

	public ScheduleNotifManager() {

	}

	public static void main(String[] args) throws InterruptedException {

		ArrayList<String> firstSendURL = new ArrayList<>();
		ArrayList<String> reSendURL = new ArrayList<>();
		ArrayList<String> decideURL = new ArrayList<>();

		ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();

		System.out.println("----------監視開始----------");

		// 60秒ごとに更新
		// debug用に最初の実行だけ5秒で
		//////////////////////////////////ここから////////////////////////////////////
		service.scheduleWithFixedDelay(() -> {

			//現在の時間を取得することで、現在の時間よりも前のnotifを実行するため
			LocalDateTime ldt = LocalDateTime.now();
			String nowTime = ldt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

			// 現在の時間を入れてnotiflistを取得
			ArrayList<HashMap<String, String>> notifList = new NotifTable().getNotifList(nowTime);
			HashMap<String, String> notifHM = new HashMap<>();
			for (int i = 0; i < notifList.size(); i++) {
				notifHM = notifList.get(i);
				// 初回の送信
				if (Integer.parseInt(notifHM.get("type")) == 0) {
					firstSendURL.add(notifHM.get("randomURL"));
				// 再送
				} else if(Integer.parseInt(notifHM.get("type")) == 1) {
					reSendURL.add(notifHM.get("randomURL"));
				// 決定
				} else {
					decideURL.add(notifHM.get("randomURL"));
				}
			}

			// 初回と再送で本文やタイトルを変えるため
			Schedule schedule;
			HashMap<String, String> targetHM = new HashMap<>();
			HashMap<String, String> scheduleHM = new HashMap<>();
			for(int i = 0 ; i < firstSendURL.size(); i++) {
				targetHM = new TargetTable().getTarget(firstSendURL.get(i));
				scheduleHM = new ScheduleTable().getSchedule(targetHM.get("id"), targetHM.get("senderEmail"));
				schedule = new Schedule(scheduleHM.get("id"), scheduleHM.get("eventName"), scheduleHM.get("eventContent")
						,  scheduleHM.get("eventDeadline")
						, targetHM.get("senderEmail"), null, null);
				try {
					System.out.println("-------メール送信-------");
					System.out.println(nowTime);
					new SendMail().send(schedule, firstSendURL.get(i), targetHM.get("targetEmail"), 0);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			for(int i = 0 ; i < reSendURL.size(); i++) {
				targetHM = new TargetTable().getTarget(reSendURL.get(i));
				// 再送する前に未入力か確認
				if(Integer.parseInt(targetHM.get("isInput")) == 0) {
					scheduleHM = new ScheduleTable().getSchedule(targetHM.get("id"), targetHM.get("senderEmail"));
					schedule = new Schedule(scheduleHM.get("id"), scheduleHM.get("eventName"), scheduleHM.get("eventContent")
							, scheduleHM.get("eventDeadline")
							, targetHM.get("senderEmail"), null, null);
					try {
						System.out.println("-------メール送信-------");
						System.out.println(nowTime);
						new SendMail().send(schedule, reSendURL.get(i), targetHM.get("targetEmail"), 1);
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					// 入力してあった場合、その対象の通知を全て削除
					new NotifTable().delete(reSendURL.get(i));
				}
			}
			for(int i = 0 ; i < decideURL.size(); i++) {
				targetHM = new TargetTable().getTarget(decideURL.get(i));
				scheduleHM = new ScheduleTable().getSchedule(targetHM.get("id"), targetHM.get("senderEmail"));
				schedule = new Schedule(scheduleHM.get("id"), scheduleHM.get("eventName"), scheduleHM.get("eventContent")
						, scheduleHM.get("eventDeadline")
						, targetHM.get("senderEmail"), scheduleHM.get("decideDate"), scheduleHM.get("note"));
				try {
					System.out.println("-------メール送信-------");
					System.out.println(nowTime);
					new SendMail().send(schedule, decideURL.get(i), targetHM.get("targetEmail"), 2);
				} catch (Exception e) {
					e.printStackTrace();
				}
				// その対象の通知を全て削除
				new NotifTable().delete(decideURL.get(i));
			}

			// 結合し、実行したもの全て削除
			firstSendURL.addAll(reSendURL);
			firstSendURL.addAll(decideURL);
			new NotifTable().delete(firstSendURL, nowTime);


			// URLの配列の初期化 (ループさせるため)
			firstSendURL.clear();
			reSendURL.clear();
			decideURL.clear();

			// 第二引数が最初のループ、第三引数が2回目以降のループ
			// テスト用に最初だけ短くしている
		}, 5, 10, TimeUnit.SECONDS);
		//////////////////////////////////ここまで////////////////////////////////////
	}

}
