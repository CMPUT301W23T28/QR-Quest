package com.example.qr_quest;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * This class is used to create an avatar and avatar name for a given QR code
 */
public class Avatar {
    private String qrCode, avatar, avatarName;
    private Map<Integer, List<String>> avatarNameDict = new HashMap<Integer, List<String>>() {{
        put(0, Arrays.asList("Huge", "Fat", "Enormous", "Chubby", "Huge", "Tiny", "large", "Big", "Small", "Crazy"));
        put(1, Arrays.asList("Long", "Short", "One", "Three", "Four", "Frozen", "Cool","Hot", "Flat", "Round"));
        put(2, Arrays.asList("Legged", "Armed", "Winged", "Headed", "Leaved", "Toothed", "Eyed", "Tongued", "Glow", "Antennaed"));
        put(3, Arrays.asList("Explosive", "Fast", "Dangerous", "Slimy", "Tricky", "Weak", "Strong", "Flying", "Angry", "Fierce"));
        put(4, Arrays.asList("Aerial", "Sea", "Desert", "Mountain", "Wilderness", "Land", "Cave", "Medieval", "Hairy", "Bald"));
        put(5, Arrays.asList("Monster", "Ghost", "Beastboy", "Shark", "Turtle", "Robocop", "KingKong", "Cannonbolt", "Stinkflfy"));
    }};

    private Map<Integer, List<String>> avatarFigure = new HashMap<Integer, List<String>>() {{
        put(0, Arrays.asList("/", "|", ":", "&", "}", ")", "|", "(", "?", "!"));
        put(1, Arrays.asList("-", "=", "~", "#", "M", "M", "N", "W", "U", "V"));
        put(2, Arrays.asList("+", "-", "@", "'", ".", "o", "*","^", "e", "a"));
        put(3, Arrays.asList("T", "Y", "J", "L", "V", "7", "?", "v", "^", "X"));
        put(4, Arrays.asList("<>", "=", "()", "o", "L", "n", "u", "~", "--", "x"));
        put(5, Arrays.asList("2", "3", "c", "b", "p", "k", "e", "6", "8", "r"));
    }};


    /**
     * Constructor for the avatar. Saves only the first 6 characters of the QR code as that is what is used in generating
     * avatars and avatar names.
     * @param qrCode
     */
    Avatar(String qrCode) {
        this.qrCode = qrCode.substring(0, 6);
        generateAvatarName();
        generateAvatar();
    }

    private void generateAvatar() {
        String eye = avatarNameDict.get(0).get(Character.getNumericValue(this.qrCode.charAt(0)));
        String side = avatarNameDict.get(1).get(Character.getNumericValue(this.qrCode.charAt(1)));
        String hair = avatarNameDict.get(2).get(Character.getNumericValue(this.qrCode.charAt(2)));
        String nose = avatarNameDict.get(3).get(Character.getNumericValue(this.qrCode.charAt(3)));
        String mouth = avatarNameDict.get(4).get(Character.getNumericValue(this.qrCode.charAt(4)));
        String ear = avatarNameDict.get(5).get(Character.getNumericValue(this.qrCode.charAt(5)));

        this.avatar = (
                side + hair + hair + hair + hair + side + "\t"
                        + side + " " + eye + " " + eye + " " + side + "\t"
                        + side + " " + " " + nose + " " + " " + side + "\t"
                        + side + " " + " " + mouth + " " + " " + side + "\t"
                        + side + hair + hair + hair + hair + side
        );
    }

    /**
     * this is the function to generate the avatar name
     */
    private Void generateAvatarName() {
        this.avatarName = "";
        for(int i=0; i<6; i++) {
            this.avatarName += avatarNameDict.get(i).get(Character.getNumericValue(this.qrCode.charAt(i)));
        }
        return null;
    }

    public String getAvatarName() {
        return this.avatarName;
    }

    public String getAvatar() {
        return this.avatar;
    }
}