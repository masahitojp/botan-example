package com.github.masahitojp.botan.listener;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.junit.Test;

import java.lang.reflect.Type;
import java.util.Collection;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.is;

public class MisawaMessageListenerTest {
    @Test
    public void test() {
        final String json = "[{\n" +
                "\n" +
                "    \"id\": 1,\n" +
                "    \"title\": \"ドラム\",\n" +
                "    \"image\": \"http://livedoor.blogimg.jp/jigokuno_misawa/imgs/6/b/6bb141f8.gif\",\n" +
                "    \"character\": \"KAZ(32)\",\n" +
                "    \"cid\": 1,\n" +
                "    \"eid\": 1,\n" +
                "    \"body\": \"この世に存在するドラムは\\n\\n全て俺が叩く\\n\"\n" +
                "\n" +
                "}]";

        Type collectionType = new TypeToken<Collection<Meigen>>(){}.getType();
        Gson gson = new Gson();
        Collection<Meigen> meigens =  gson.fromJson(json, collectionType);
        assertThat(meigens.size(), is(1));
    }
}