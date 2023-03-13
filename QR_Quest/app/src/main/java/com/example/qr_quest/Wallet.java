package com.example.qr_quest;

/**
 * The Wallet class represents a wallet item containing a string identifier d, name and points.
 */
public class Wallet {
    String d;
    String name;
    String points;

    /**
     * Creates a new Wallet object with the specified identifier, name, and points.
     * @param d
     *       The identifier for the wallet item
     * @param name
     *       The name of the wallet item
     * @param points
     *       The number of points associated with the wallet item
     */
    public Wallet(String d, String name, String points) {
        this.d = d;
        this.name = name;
        this.points = points;
    }

    /**
     * Returns the identifier of the wallet item.
     * @return Returns the identifier of the wallet item
     */
    public String getD() {
        return d;
    }


    /**
     * Sets the identifier of the wallet item.
     * @param d
     *      The new identifier for the wallet item
     */
    public void setD(String d) {
        this.d = d;
    }


    /**
     * Returns the name of the wallet item.
     * @return Returns the name of the wallet item
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the wallet item.
     * @param name
     *      The new name for the wallet item
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the number of points associated with the wallet item.
     * @return Returns the number of points associated with the wallet item
     */
    public String getPoints() {
        return points;
    }

    /**
     * Sets the number of points associated with the wallet item.
     * @param points
     *      The new number of points for the wallet item
     */
    public void setPoints(String points) {
        this.points = points;
    }
}

