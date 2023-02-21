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


    /**
     * Constructor for the avatar. Saves only the first 6 characters of the QR code as that is what is used in generating
     * avatars and avatar names.
     * @param qrCode
     */
    public Avatar(String qrCode) {
        this.qrCode = qrCode.substring(0, 6);
    }

    private String generateAvatar() {
        return "/";
    }

    /**
     * this is the function to generate the avatar name
     */
    private Void generateAvatarName() {
        for(int i=0; i<6; i++) {
            this.avatarName += avatarNameDict.get(i).get(Character.getNumericValue(this.qrCode.charAt(i)));
        }
        return null;
    }
}
