package com.github.masahitojp.botan.utils;

import com.github.masahitojp.botan.message.BotanMessage;
import mockit.Mock;
import mockit.MockUp;

public class BotanMessageMock extends MockUp<BotanMessage> {

    final private StringBuilder result = new StringBuilder();

    public StringBuilder getResult() {
        return result;
    }

    @Mock
    public void reply(String parameter) {
        result.append(parameter);
    }
}
