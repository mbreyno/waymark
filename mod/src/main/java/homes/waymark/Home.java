package homes.waymark;

/**
 * A single saved home location.
 *
 * @param name      display name chosen by the player
 * @param dimension dimension id, e.g. "minecraft:overworld"
 * @param x         x coordinate
 * @param y         y coordinate
 * @param z         z coordinate
 * @param yaw       head yaw at creation time
 * @param pitch     head pitch at creation time
 */
public record Home(String name, String dimension, double x, double y, double z, float yaw, float pitch) {
}
