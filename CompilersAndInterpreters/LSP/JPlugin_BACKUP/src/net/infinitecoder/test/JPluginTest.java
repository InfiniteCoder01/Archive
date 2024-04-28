package net.infinitecoder.test;

import net.infinitecoder.jplugin.*;

public class JPluginTest {

    public static void main(String[] args) {
        JPlugin jp = new JPlugin("plugin.cjp");
        jp.parse();
        jp.printErrors();
    }
}
