package com.github.masahitojp.botan;

import com.github.masahitojp.botan.handlers.cron.CronJob;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.is;
public class GsonTest {
	@Test
	public void test() {
		ConcurrentHashMap<String, CronJob> hashMap = new ConcurrentHashMap<>();
		hashMap.put("id1", new CronJob("* * * * *", "to", "message"));
		hashMap.put("id2", new CronJob("* * * * *", "to", "message"));
		final Gson gson = new Gson();
		final List<CronJob> jobs = hashMap.values().stream().map(data -> data).collect(Collectors.toList());
		String json = gson.toJson(jobs);
		assertThat(json, is("[{\"schedule\":\"* * * * *\",\"to\":\"to\",\"message\":\"message\"},{\"schedule\":\"* * * * *\",\"to\":\"to\",\"message\":\"message\"}]"));
		final List<CronJob> list = gson.fromJson(json, new TypeToken<List<CronJob>>() {}.getType());
		assertThat(list.size(), is(2));
		assertThat(list.get(1).message, is("message"));
		assertThat(list.get(0).message, is("message"));

	}

}
