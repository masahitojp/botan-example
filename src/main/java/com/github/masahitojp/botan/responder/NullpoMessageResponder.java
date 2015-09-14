package com.github.masahitojp.botan.responder;

import com.github.masahitojp.botan.Robot;

@SuppressWarnings("unused")
public class NullpoMessageResponder implements BotanMessageResponderRegister {

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
