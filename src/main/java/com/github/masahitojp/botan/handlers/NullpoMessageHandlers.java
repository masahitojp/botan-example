package com.github.masahitojp.botan.handlers;

import com.github.masahitojp.botan.Robot;
import com.github.masahitojp.botan.handler.BotanMessageHandlers;

@SuppressWarnings("unused")
public class NullpoMessageHandlers implements BotanMessageHandlers {

    @Override
    public void register(final Robot robot) {
        robot.respond(
                "ぬるぽ|ヌルポ|nullpo",
                "rich: Use rich style ｶﾞｯ",
                message -> {
                    final String ga =
                            "　 Λ＿Λ　　　　＼＼\n" +
                            " （　・∀・）　　　|　|　ｶﾞｯ\n" +
                            "と　　　　）　　　|　|\n" +
                            "　 Ｙ　/ノ　　　 人\n" +
                            "　　 /　）　 　 < 　>_Λ∩\n" +
                            " ＿/し´　／／. Ｖ｀Д´）/\n" +
                            "（＿フ彡　　　　　　 /　>>@%s";
                    message.reply(String.format(ga, message.getFromName()));
                });
    }
}
