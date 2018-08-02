package schedulenotif;

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

public class Start {

	public Start() {

	}

	public static void main(String[] args) throws InterruptedException {

		ArrayList<String> firstSendURL = new ArrayList<>();
		ArrayList<String> reSendURL = new ArrayList<>();

		ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();

		// 60秒ごとに更新
		// debug用に最初の実行だけ5秒で
		//////////////////////////////////ここから////////////////////////////////////
		service.scheduleWithFixedDelay(() -> {
			LocalDateTime ldt = LocalDateTime.now();
			String nowTime = ldt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

			ArrayList<HashMap<String, String>> notifList = new NotifTable().getNotifList(nowTime);
			HashMap<String, String> notifHM = new HashMap<>();
			for (int i = 0; i < notifList.size(); i++) {
				notifHM = notifList.get(i);
				// 初回の送信
				if (Integer.parseInt(notifHM.get("isFirst")) == 1) {
					firstSendURL.add(notifHM.get("randomURL"));
				} else {
					reSendURL.add(notifHM.get("randomURL"));
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
						, scheduleHM.get("eventStartDate"), scheduleHM.get("eventEndDate"), scheduleHM.get("eventDeadlineDate")
						, targetHM.get("senderEmail"));
				new SendMail().send(schedule, firstSendURL.get(i), targetHM.get("targetEmail"), 1);
			}
			for(int i = 0 ; i < reSendURL.size(); i++) {
				targetHM = new TargetTable().getTarget(reSendURL.get(i));
				// 再送する前に未入力か確認
				if(Integer.parseInt(targetHM.get("isInput")) == 0) {
					scheduleHM = new ScheduleTable().getSchedule(targetHM.get("id"), targetHM.get("senderEmail"));
					schedule = new Schedule(scheduleHM.get("id"), scheduleHM.get("eventName"), scheduleHM.get("eventContent")
							, scheduleHM.get("eventStartDate"), scheduleHM.get("eventEndDate"), scheduleHM.get("eventDeadlineDate")
							, targetHM.get("senderEmail"));
					new SendMail().send(schedule, reSendURL.get(i), targetHM.get("targetEmail"), 0);
				} else {
					// 入力してあった場合、その対象の通知を全て削除
					new NotifTable().delete(reSendURL.get(i));
				}
			}

			firstSendURL.addAll(reSendURL);
			new NotifTable().delete(firstSendURL, nowTime);
			System.out.println(nowTime);
			firstSendURL.clear();
			reSendURL.clear();
		}, 5, 60, TimeUnit.SECONDS);
		//////////////////////////////////ここまで////////////////////////////////////
	}

}
