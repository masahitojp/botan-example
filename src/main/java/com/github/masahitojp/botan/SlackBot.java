package com.github.masahitojp.botan;

import com.github.masahitojp.botan.adapter.SlackRTMAdapter;
import com.github.masahitojp.botan.brain.mapdb.MapDBBrain;
import com.github.masahitojp.botan.exception.BotanException;
import com.github.masahitojp.botan.utils.BotanUtils;

public class SlackBot {
    static public void main(String[] Args) {

        final String apiToken = BotanUtils.envToOpt("SLACK_API_TOKEN").orElse("");

        final Botan botan = new Botan.BotanBuilder()
                .setAdapter(new SlackRTMAdapter(apiToken))
                .setBrain(new MapDBBrain("botan_map_db", "botan"))
                .build();

        try {
            botan.start();
        } catch (final BotanException ex) {
            ex.printStackTrace();
        } finally {
            botan.stop();
        }

    }
}
