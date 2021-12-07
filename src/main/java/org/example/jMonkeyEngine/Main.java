package org.example.jMonkeyEngine;

import com.jme3.app.SimpleApplication;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.debug.Arrow;
import com.jme3.scene.shape.Box;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class Main extends SimpleApplication {
    public static final float LIGHT_SPEED = 3;

    public static void main(String[] args) {
        new Main().start();
    }

    @Override
    public void simpleInitApp() {
        drawMesh(new Arrow(new Vector3f(100, 0, 0)), ColorRGBA.Red);
        drawMesh(new Arrow(new Vector3f(0, 100, 0)), ColorRGBA.Green);
        drawMesh(new Arrow(new Vector3f(0, 0, 100)), ColorRGBA.Blue);

        readInputAsPositions()
                .forEach(point -> drawMesh(new Box(1, 1, point.z), ColorRGBA.randomColor(), new Vector3f(2 * point.x, 2 * point.y, point.z))
                );
    }

    private List<Vector3f> readInputAsPositions() {
        try {
            List<String[]> input = Files.lines(Path.of("input.csv"))
                    .skip(1)
                    .map(line -> line.split(","))
                    .collect(Collectors.toList());
            float maxTime = input.stream()
                    .map(line1 -> Float.parseFloat(line1[2]))
                    .max(Comparator.naturalOrder())
                    .orElseThrow();
            float maxDeep = maxTime * LIGHT_SPEED;
            return input.stream()
                    .map(line ->
                            new Vector3f(
                                    Float.parseFloat(line[0]),
                                    Float.parseFloat(line[1]),
                                    maxDeep - Float.parseFloat(line[2]) * LIGHT_SPEED
                            ))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void drawMesh(Mesh mesh, ColorRGBA color) {
        drawMesh(mesh, color, Vector3f.ZERO);
    }

    private void drawMesh(Mesh mesh, ColorRGBA color, Vector3f position) {
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", color);

        Geometry geom = new Geometry(UUID.randomUUID().toString(), mesh);
        geom.setMaterial(mat);
        geom.move(position);

        rootNode.attachChild(geom);
    }
}
