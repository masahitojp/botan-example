package com.github.masahitojp.botan;

import com.github.masahitojp.botan.adapter.ComandLineAdapter;
import com.github.masahitojp.botan.adapter.SlackAdapter;
import com.github.masahitojp.botan.brain.MapDBBrain;
import com.github.masahitojp.botan.brain.RedisBrain;
import com.github.masahitojp.botan.exception.BotanException;
import com.github.masahitojp.botan.utils.BotanUtils;

public class SlackBot {


	static public void main(String[] Args) {

        final String team = BotanUtils.envToOpt("SLACK_TEAM").orElse("");
        final String user = BotanUtils.envToOpt("SLACK_USERNAME").orElse("");
        final String pswd = BotanUtils.envToOpt("SLACK_PASSWORD").orElse("");
        final String channel = BotanUtils.envToOpt("SLACK_ROOM").orElse("");
        final String mapDBPath = BotanUtils.envToOpt("MAPDB_PATH").orElse("botanDB");
        final String mapDBName = BotanUtils.envToOpt("MAPDB_NAME").orElse(team);

        final Botan botan = new Botan.BotanBuilder(new ComandLineAdapter())
                .setBrain(new RedisBrain("localhost", 6379))
                .build();

        try {
            botan.start();
        } catch (final BotanException ex) {
            ex.printStackTrace();
        }

	}
}
