package com.smona.image.loader.glide;

import com.bumptech.glide.load.model.ModelLoaderFactory;

import java.util.ArrayList;
import java.util.List;

public class GlideConfig {

    private static List<ModelLoaderFactory> sFactories = new ArrayList<>();
    public static void addModelLoaderFactory(ModelLoaderFactory factory) {
        sFactories.add(factory);
    }

    public static List<ModelLoaderFactory> getModelLoaderFactories() {
        return sFactories;
    }
}
