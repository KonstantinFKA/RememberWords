package com.fka.rememberwords.objects;

import java.util.UUID;

/**
 * Created by 074FrantsuzovKA on 03.10.2017.
 */

public class Dictionary {

    private UUID id;
    private String title;

    public Dictionary(String title) {
        this(UUID.randomUUID());
        this.title = title;
    }

    public Dictionary(UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
