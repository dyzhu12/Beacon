package com.example.davidzhu.beacon;

import java.util.ArrayList;

public final class SearchIconLoader {

    private final static int imgFood = R.drawable.ic_local_pizza_black_24dp;
    private final static int imgFree = R.drawable.ic_attach_money_black_24dp;
    private final static int imgFun = R.drawable.ic_insert_emoticon_black_24dp;
    private final static int imgArts = R.drawable.ic_palette_black_24dp;
    private final static int imgSports = R.drawable.ic_directions_bike_black_24dp;
    private final static int imgServe = R.drawable.ic_nature_people_black_24dp;
    private final static int imgFabulous = R.drawable.ic_thumb_up_black_24dp;
    private final static int imgFire = R.drawable.ic_whatshot_black_24dp;
    private final static int imgDefault = R.drawable.ic_pin_drop_black_24dp;

    private static ArrayList<String> mAllTags;

    public static void SearchIconLoader() {
        mAllTags = new ArrayList<String>();
        mAllTags.add("Food");
        mAllTags.add("Free");
        mAllTags.add("Fun");
        mAllTags.add("Arts");
        mAllTags.add("Sports");
        mAllTags.add("Serve");
        mAllTags.add("Fabulous");
        mAllTags.add("Fire");
    }

    public static Integer getIcon(String itemName) {

        switch (itemName) {
            case "Food":
                return imgFood;
            case "Free":
                return imgFree;
            case "Fun":
                return imgFun;
            case "Arts":
                return imgArts;
            case "Sports":
                return imgSports;
            case "Serve":
                return imgServe;
            case "Fabulous":
                return imgFabulous;
            case "Fire":
                return imgFire;
            default:
                return imgDefault;
        }
    }

    public static ArrayList<String> getAllTags() {
        return mAllTags;
    }

}
