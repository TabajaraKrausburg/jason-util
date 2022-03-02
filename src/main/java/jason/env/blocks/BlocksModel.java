package jason.env.blocks;

import jason.asSyntax.Term;

import java.util.*;

public class BlocksModel {

    public final static String DROPZONE = "dropzone";
    public final static String CORRIDOR = "corridor";
    public final static String PACKING = "packing";

    private final Random RNG = new Random(17);

    private final int packagingProbability;

    private int totalTaskCount = 0;
    private Task currentTask;
    private int totalBlockCount = 0;
    private int currentBlockCount = 0;
    private final int maxBlockCount;
    private final String[] colours;

    private final Map<String, Room> rooms = new HashMap<>();
    private final List<Room> spawnRooms = new ArrayList<>();
    private final Room corridor;

    private final Map<Integer, Robot> robots = new HashMap<>();
    private final Map<String, Robot> agentToRobot = new HashMap<>();


    public BlocksModel(int maxBlockCount, int numberOfRobots, int numberOfCommonRooms, int packagingProbability,
                       String[] colours) {
        this.packagingProbability = packagingProbability;
        this.colours = colours;
        this.maxBlockCount = maxBlockCount;
        for (int i = 0; i < numberOfRobots; i++)
            createRobot();
        for (int i = 0; i < numberOfCommonRooms; i++) {
            var room = createRoom(null);
            spawnRooms.add(room);
            generateBlock(room);
        }
        createRoom(DROPZONE);
        createRoom(PACKING);
        corridor = createRoom(CORRIDOR);
        createNextTask();
    }

    public Task getCurrentTask() {
        return this.currentTask;
    }

    private Robot createRobot() {
        var id = this.robots.size();
        var robot = new Robot(id, corridor);
        this.robots.put(id, robot);
        return robot;
    }

    private Room createRoom(String name) {
        if (name == null)
            name = String.format("room%d", this.rooms.size());
        var room = new Room(name);
        this.rooms.put(name, room);
        return room;
    }

    public void generateBlock(String colour) {
        var room = spawnRooms.get(RNG.nextInt(spawnRooms.size()));
        generateBlock(room, colour);
    }

    public void generateBlock(Room room) {
        var colour = getRandomColour();
        generateBlock(room, colour);
    }

    public void generateBlock(Room room, String colour) {
        currentBlockCount++;
        var block = new Block(String.format("block%d", this.totalBlockCount++), colour);
        room.putDown(block);
    }

    public void registerAgent(String agentName) {
        var optionalRobot = this.robots.values().stream().filter(Robot::isFree).findAny();
        Robot robot;
        if (optionalRobot.isEmpty())
            robot = createRobot();
        else
            robot = optionalRobot.get();
        registerAgent(agentName, robot.getId());
    }

    private void registerAgent(String agentName, int robotNo) {
        if (this.isRegistered(agentName)) return;
        var robot = this.robots.get(robotNo);
        if (robot == null || robot.getAgent() != null) return;
        robot.registerAgent(agentName);
        this.agentToRobot.put(agentName, robot);
    }

    public boolean isRegistered(String agentName) {
        return this.agentToRobot.containsKey(agentName);
    }

    private String getRandomColour() {
        return colours[RNG.nextInt(colours.length)];
    }

    private void createNextTask() {
        this.currentTask = new Task("t" + totalTaskCount++, getRandomColour(),
                RNG.nextInt(100) < packagingProbability);
        generateBlock(currentTask.color);
        System.out.printf("The new colour is %s. Packaging: %s\n", currentTask.color, currentTask.packaging);
    }

    public boolean actPutDown(String agent) {
        var robot = agentToRobot.get(agent);
        var block = robot.putDown();
        if (PACKING.equals(robot.getRoom().getName())) {
            if (!robot.getRoom().getBlocks().isEmpty()) return false;
        }
        if (DROPZONE.equals(robot.getRoom().getName())) {
            if (block != null && currentTask.color.equals(block.colour) && block.isPackaged() == currentTask.packaging) {
                System.out.printf("Delivered block of colour %s\n", block.colour);
                createNextTask();
            }
            else {
                System.out.printf("Delivery failed: %s", currentTask);
            }
            currentBlockCount--;
        }
        else
            robot.getRoom().putDown(block);
        return true;
    }

    public boolean actPickUp(String agent) {
        var robot = agentToRobot.get(agent);
        return robot.pickUp() != null;
    }

    public boolean actGotoBlock(String agent, List<Term> terms) {
        if (terms == null || terms.size() != 1)
            return false;
        var robot = agentToRobot.get(agent);
        var blockName = terms.get(0).toString();
        var block = robot.getRoom().getBlock(blockName);
        if (block == null)
            return false;
        robot.gotoBlock(block);
        return true;
    }

    public boolean actGoto(String agent, List<Term> terms) {
        if (terms == null || terms.size() != 1)
            return false;
        var robot = agentToRobot.get(agent);
        var roomName = terms.get(0).toString();
        var room = rooms.get(roomName);
        if (room == null)
            return false;
        robot.setRoom(room);
        return true;
    }

    public boolean actActivate(String agent) {
        var robot = agentToRobot.get(agent);
        if (!robot.getRoom().getName().equals(PACKING))
            return false;
        var packingRoom = robot.getRoom();
        var blocks = packingRoom.getBlocks();
        if (blocks.isEmpty())
            return false;
        blocks.get(0).packageBlock();
        return true;
    }

    public List<Room> getRooms() {
        return new ArrayList<>(rooms.values());
    }

    public List<Robot> getRobots() {
        return new ArrayList<>(robots.values());
    }

    public void generateBlocks() {
        if (currentBlockCount < maxBlockCount) {
            generateBlock(spawnRooms.get(RNG.nextInt(spawnRooms.size())));
        }
    }

    public record Task(String id, String color, boolean packaging){}
}