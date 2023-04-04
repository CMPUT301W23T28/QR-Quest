package com.example.qr_quest;
import com.github.javafaker.Faker;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * This class is used to create an avatar and avatar name based on a given QR code hash.
 * It uses dictionaries to map characters in the QR code hash to parts of the avatar's name and figure.
 */
public class Avatar implements Serializable {

    private String qrCode, avatarFigure, avatarName;

    private Map<Integer, List<String>> avatarNameDict = new HashMap<Integer, List<String>>() {{
        put(0, Arrays.asList("Ein", "Der", "Mi", "Mon", "Xi", "La", "Tis", "A", "The", "Ze", "De", "Una", "Ti", "El", "Un", "Une"));
        put(1, Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "0"));
        put(2, Arrays.asList("Legged", "Armed", "Winged", "Headed", "Leaved", "Toothed", "Eyed", "Tongued", "Tailed", "Antennaed", "Mouthed", "Toed", "Fingered", "Brained", "Necked", "Footed"));
        put(3, Arrays.asList("Slow", "Fast", "Sick", "Slimy", "Tricky", "Weak", "Strong", "Flying", "Angry", "Fierce", "Timid", "Nice", "Calm", "Cute", "Nice", "Buggy"));
        put(4, Arrays.asList("Air", "Sea", "Dry", "Mud", "New", "Land", "Cave", "Old", "Hairy", "Bald", "Icy", "Bush", "Swampy", "Rocky", "Tree", "House"));
        put(5, Arrays.asList("Star", "Hulk", "Beast", "Shark", "Bat", "Cop", "Kong", "Cannon", "Fly", "Rat", "Bug", "Virus", "Algae", "Dragon", "Dino", "Chimp"));
    }};

    private Map<Integer, List<String>> avatarFigureDict = new HashMap<Integer, List<String>>() {{
        put(0, Arrays.asList("/", "|", ":", "&", "}", ")", "|", "(", "?", "!", "{", "\\", "||", "()", "[", "]"));
        put(1, Arrays.asList("-", "=", "~", "#", "M", "M", "N", "W", "U", "V", "H", "F", "S", "\\", "=|", "C"));
        put(2, Arrays.asList("+", "-", "@", "'", ".", "o", "*","^", "e", "a", "c", "x", "=", "..", "m", " ", "q"));
        put(3, Arrays.asList("T", "Y", "J", "L", "V", "7", "?", "v", "^", "X", "P", "R", "M", "$", "%", "&"));
        put(4, Arrays.asList("<>", "=", "()", "o", "L", "n", "u", "~", "-", "x", "-.-", "C", "{}", "~~", "-v-", " "));
        put(5, Arrays.asList("2", "3", "c", "b", "p", "k", "e", "6", "8", "r", "d", "f", "R", "Q", "5", "6"));
    }};

    /**
     * Constructor for the avatar. Saves only the first 6 characters of the QR code as that is what is used in generating
     * avatars and avatar names.
     *
     * @param qrHash
     *     A the sha_256 hash value for a certain QR object
     */
    public Avatar(String qrHash) {
        this.qrCode = qrHash.substring(0, 6);
        generateAvatarName();
        generateAvatarFigure();
    }

    /**
     Generates an avatar figure based on a QR code.
     */
    private void generateAvatarFigure() {
        String eye = avatarFigureDict.get(2).get(Character.getNumericValue(this.qrCode.charAt(0)));
        String side = avatarFigureDict.get(0).get(Character.getNumericValue(this.qrCode.charAt(1)));
        String hair = avatarFigureDict.get(1).get(Character.getNumericValue(this.qrCode.charAt(2)));
        String nose = avatarFigureDict.get(3).get(Character.getNumericValue(this.qrCode.charAt(3)));
        String mouth = avatarFigureDict.get(4).get(Character.getNumericValue(this.qrCode.charAt(4)));
        String ear = avatarFigureDict.get(5).get(Character.getNumericValue(this.qrCode.charAt(5)));

        this.avatarFigure = (
                side + hair + hair + hair + hair + side + "\n"
                + ear + side + " " + eye + " " + eye + " " + side + ear + "\n"
                + side + " " + " " + nose + " " + " " + side + "\n"
                + side + " " + " " + mouth + " " + " " + side + "\n"
                + side + hair + hair + hair + hair + side
        );
    }

    /**
     * This method uses the Java Faker library (version 1.0.2) by DiUS
     * to generate a unique avatar name based on the QR code hash.
     * Java Faker GitHub repository: https://github.com/DiUS/java-faker
     */
    private Void generateAvatarName() {
        long seed = this.qrCode.hashCode();
        Faker faker = new Faker(new Random(seed));
        this.avatarName = faker.name().firstName() + faker.name().lastName();
        return null;
    }

    /**
     * Returns the name of the avatar.
     *
     * @return the name of the avatar as a String
     */
    public String getAvatarName() {
        return this.avatarName;
    }

    /**
     * Returns the figure of the avatar.
     *
     * @return the figure of the avatar as a String
     */
    public String getAvatarFigure() {
        return this.avatarFigure;
    }
}