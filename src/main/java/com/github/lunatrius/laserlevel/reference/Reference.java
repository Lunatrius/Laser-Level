package com.github.lunatrius.laserlevel.reference;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Locale;

public class Reference {
    public static final String MODID = "LaserLevel";
    public static final String MODID_LOWER = MODID.toLowerCase(Locale.ENGLISH);
    public static final String NAME = "Laser Level";
    public static final String VERSION = "${version}";
    public static final String FORGE = "${forgeversion}";
    public static final String MINECRAFT = "${mcversion}";
    public static final String PROXY_SERVER = "com.github.lunatrius.laserlevel.proxy.ServerProxy";
    public static final String PROXY_CLIENT = "com.github.lunatrius.laserlevel.proxy.ClientProxy";

    public static Logger logger = LogManager.getLogger(Reference.MODID);
}
