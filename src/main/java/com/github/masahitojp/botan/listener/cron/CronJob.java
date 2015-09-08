package com.github.masahitojp.botan.listener.cron;

import org.slf4j.Marker;
import org.slf4j.MarkerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created with IntelliJ IDEA.
 * User: Nakamura
 * Date: 2015/09/08
 * Time: 15:53
 */
public class CronJob {
	public String schedule;
	public String to;
	public String message;

	public CronJob(final String schedule, final String to, final String message) {
		this.schedule = schedule;
		this.to = to;
		this.message = message;
	}
}
