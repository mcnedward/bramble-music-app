package com.mcnedward.bramble.enums;

/**
 * Created by Edward on 3/20/2016.
 */
public enum FragmentCode {
    GALLERY(1, "Gallery"),
    FOLDER(2, "Folder");

    public int id;
    public String title;

    FragmentCode(int id, String title) {
        this.id = id;
        this.title = title;
    }
}
