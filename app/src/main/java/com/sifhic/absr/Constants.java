package com.sifhic.absr;


public final class Constants {

    // Notification Channel constants

    // Name of Notification Channel for verbose notifications of background work
    public static final CharSequence VERBOSE_NOTIFICATION_CHANNEL_NAME =
            "Verbose WorkManager Notifications";
    public static String VERBOSE_NOTIFICATION_CHANNEL_DESCRIPTION =
            "Shows notifications whenever work starts";
    public static final CharSequence NOTIFICATION_TITLE = "Refresh Starting";
    public static final String CHANNEL_ID = "VERBOSE_NOTIFICATION" ;
    public static final int NOTIFICATION_ID = 1;

    // The name of the image manipulation work
  public   static final String SCRAPE_WORK_NAME = "image_manipulation_work";

    // Other keys

    public static final String KEY_PRODUCT_ID = "KEY_PRODUCT_ID";
    public static final String TAG_OUTPUT = "OUTPUT";

    public static final long DELAY_TIME_MILLIS = 10000;

    public static final String DEFAULT_CATEGORY =  "Antitheft Remote Starters";
    public static final int GROUP_PRODUCTS_COUNT = 10;

    // Ensures this class is never instantiated
    private Constants() {}
}