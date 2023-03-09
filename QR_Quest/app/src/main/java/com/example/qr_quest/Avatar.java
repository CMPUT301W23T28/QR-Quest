package com.example.qr_quest;
import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class is used to create an avatar and avatar name for a given QR code
 */
public class Avatar implements Serializable {
    private String qrCode, avatarFigure, avatarName;
    private Map<Integer, List<String>> avatarNameDict = new HashMap<Integer, List<String>>() {{
        put(0, Arrays.asList("Huge", "Fat", "Enormous", "Chubby", "Huge", "Tiny", "large", "Big", "Small", "Crazy", "Cool", "Red", "Green", "Blue", "Black", "White"));
        put(1, Arrays.asList("Long", "Short", "One", "Three", "Four", "Cool", "Hot", "Flat", "Round", "Ten", "Nine", "Six", "Seven", "Five", "Two", "Eight"));
        put(2, Arrays.asList("Legged", "Armed", "Winged", "Headed", "Leaved", "Toothed", "Eyed", "Tongued", "Glow", "Antennaed", "Mouthed", "Toed", "Fingered", "Brained", "Necked", "Footed"));
        put(3, Arrays.asList("Explosive", "Fast", "Dangerous", "Slimy", "Tricky", "Weak", "Strong", "Flying", "Angry", "Fierce", "Timid", "Friendly", "Calm", "Cute", "Nice", "Buggy"));
        put(4, Arrays.asList("Aerial", "Sea", "Desert", "Mountain", "Wilderness", "Land", "Cave", "Medieval", "Hairy", "Bald", "Frozen", "Forest", "Swampy", "Rocky", "Tree", "House"));
        put(5, Arrays.asList("Monster", "Ghost", "Beast", "Shark", "Turtle", "Robocop", "Kong", "Cannon", "Fly", "Trojan", "Bug", "Virus", "Algae", "Dragon", "Dino", "Chimp"));
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
     * @param qrCode
     */
    public Avatar(String qrCode) {
        this.qrCode = qrCode.substring(0, 6);
        generateAvatarName();
        generateAvatarFigure();
    }

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
     * this is the function to generate the avatar name
     */
    private Void generateAvatarName() {
        this.avatarName = "";
        for(int i = 0; i < 6; i++) {
            this.avatarName += avatarNameDict.get(i).get(Character.getNumericValue(this.qrCode.charAt(i)));
        }
        return null;
    }

    public String getAvatarName() {
        return this.avatarName;
    }

    public String getAvatarFigure() {
        return this.avatarFigure;
    }
}