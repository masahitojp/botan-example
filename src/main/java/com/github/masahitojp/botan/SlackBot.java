package com.github.masahitojp.botan;

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
        final String channel = BotanUtils.envToOpt("SLACK_CHANNEL").orElse("");

        final Botan botan = new Botan.BotanBuilder()
                .setAdapter(new SlackAdapter(team, user, pswd, channel))
                .setBrain(new MapDBBrain("botan_map_db", "botan"))
                .build();

        try {
            botan.start();
        } catch (final BotanException ex) {
            ex.printStackTrace();
        }

    }
}
