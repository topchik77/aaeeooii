package program;

import program.DoubleRoom;

public class RoomFactory {
    public static Room createRoom(String type) {
        return switch (type) {
            case "Single" -> new SingleRoom();
            case "Double" -> new DoubleRoom();
            case "Family" -> new FamilyRoom();
            default -> null;
        };
    }
}
