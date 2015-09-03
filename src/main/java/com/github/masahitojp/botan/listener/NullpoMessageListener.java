package com.github.masahitojp.botan.listener;

import com.github.masahitojp.botan.exception.BotanException;
import com.github.masahitojp.botan.utils.BotanUtils;

@SuppressWarnings("unused")
public class NullpoMessageListener implements BotanMessageListenerRegister {

    @Override
    public void register() {
        BotanUtils.respond(
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
                    message.reply(String.format(ga, message.getFrom()));
                });
    }
}
