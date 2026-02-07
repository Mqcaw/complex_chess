package me.jdfunk.core;

import me.jdfunk.api.Engine;

public class Core implements Engine {


    @Override
    public void start() {
        System.out.println("Engine is running.");
    }
}
