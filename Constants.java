public class Constants {
    public static final int FRAMEWIDTH = 1280;
    public static final int FRAMEHEIGHT = 720;

    public static final int PLAYERWIDTH = 40;
    public static final int PLAYERHEIGHT = 40;

    public static final int PLAYERMAXLEVEL = 100;
    public static final int PLAYERESTUSNUM = 5;

    private static final int PLAYERBASEHP = 100;
    public static int playerHPLevel = 10;
    public static int playerHPLevelTemp = 10;
    public static double playerActualHP = 10;

    private static final int PLAYERBASESTR = 1;
    public static int playerSTRLevel = 10;
    public static int playerSTRLevelTemp = 10;
    public static double playerActualSTR = 10;

    private static final int PLAYERBASEDEX = 1;
    public static int playerDEXLevel = 10;
    public static int playerDEXLevelTemp = 10;
    public static double playerActualDEX = 10;
    public static double playerActualEnergy = 10;
    public static double playerActualSpeed = 10;

    public static int levelUpCost = 0;
    public static float currentVolume = 10.0f;


    public static final int TRIANGLEWIDTH = 50;
    public static final int TRIANGLEHEIGHT = (int) (TRIANGLEWIDTH * 1.732 / 2);

    public static final int TRIANGLEATTACKSTARTTIME = 50;
    public static final int TRIANGLEATTACKENDTIME = 15;
    public static final int TRIANGLEATTACKCOOLDOWN = 100;

    public static final int SMALLTRIANGLEDETECTZONE = 1000;
    public static final int TRIANGLEDETECTZONE = 1000;
    public static final int BIGTRIANGLEDETECTZONE = 10000;

    private static final double HPK = 0.2;
    private static final double STRK = 0.1;
    private static final double DEXK = 0.1;

    public static double getActualHP() {
        playerActualHP = PLAYERBASEHP;
        double k = HPK;
        for (int a = 0; a < playerHPLevel - 10; a++) {
            double extra = PLAYERBASEHP * k;
            k -= 0.001;
            playerActualHP += extra;
        }
        return playerActualHP;
    }

    public static double getActualSTR() {
        playerActualSTR = PLAYERBASESTR;
        double k = STRK;
        for (int a = 0; a < playerSTRLevel - 10; a++) {
            double extra = PLAYERBASESTR * k;
            k -= 0.001;
            playerActualSTR += extra / 2;
        }
        return playerActualSTR;
    }

    public static double getActualDEX() {
        playerActualDEX = PLAYERBASEDEX;
        double k = DEXK;
        for (int a = 0; a < playerDEXLevel - 10; a++) {
            double extra = PLAYERBASEDEX * k;
            k -= 0.001;
            playerActualDEX += extra / 2;
        }
        return playerActualDEX;
    }

    public static double getActualEnergy() {
        playerActualEnergy = playerActualDEX * 100;
        playerActualSpeed = Math.min(playerActualDEX * 3, 5);
        return playerActualEnergy;
    }

    public static double levelUpCost() {
        int totalLevel = playerHPLevelTemp + playerSTRLevelTemp + playerDEXLevelTemp;
        int baseCost = 10;
        levelUpCost = 0;
        for (int a = playerHPLevel + playerSTRLevel + playerDEXLevel; a < totalLevel; a++) {
            levelUpCost += (int) (baseCost * Math.pow(a - 30 + 1, 1.5));
        }
        return levelUpCost;
    }

    public static double nextLevelUpCost() {
        int totalLevel = playerHPLevelTemp + playerSTRLevelTemp + playerDEXLevelTemp;
        int baseCost = 10;
        int cost = (int) (baseCost * Math.pow(totalLevel - 30 + 1, 1.5));
        return cost;
    }
}