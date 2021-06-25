/*
    Author: Grant Fields
    Date: 8/4/2020
 */

package OrkEngine.tools;

import OrkEngine.math.vectors.Vertex;
import OrkEngine.modeling.Mesh;
import OrkEngine.modeling.Triangle;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

//This object is used to read text files and store them in appropriate mesh
//this only works with a special case of obj file where the face values are followed by forward slashes
public class MeshLoader {

    public MeshLoader(){

    }

    public Mesh loadMesh(String sTitle, String sFilePath, float fScale){

        String sMeshPath = "obj/" + sFilePath;

        Mesh m = null;

        try {

            BufferedReader br;

            sMeshPath = sMeshPath.substring(sMeshPath.indexOf(" ") + 1);

            ArrayList<Vertex> vertices = new ArrayList<>();
            ArrayList<Triangle> triangles = new ArrayList<>();

            InputStream is = getClass().getClassLoader().getResourceAsStream(sMeshPath);

            InputStreamReader isr = new InputStreamReader(is);

            br = new BufferedReader(isr);

            String line = br.readLine();

            while (line != null) {

                //if is vertices data
                if(line.contains("v")){

                    float[] vec = new float[3];

                    line = line.substring(line.indexOf(" ") + 1);

                    vec[0] = Float.parseFloat(line.substring(0, line.indexOf(" "))) * fScale;

                    line = line.substring(line.indexOf(" ") + 1);

                    vec[1] = Float.parseFloat(line.substring(0, line.indexOf(" "))) * fScale;

                    line = line.substring(line.indexOf(" ") + 1);

                    vec[2] = Float.parseFloat(line) * fScale;

                    vertices.add(new Vertex(vec[0], vec[1], vec[2]));
                }

                //if is face data
                if(line.contains("f")){

                    int[] trianglePoint = new int[3];

                    line = line.substring(line.indexOf(" ") + 1);

                    trianglePoint[0] = Integer.parseInt(line.substring(0, line.indexOf("/"))) -1;

                    line = line.substring(line.indexOf(" ") + 1);

                    trianglePoint[1] = Integer.parseInt(line.substring(0, line.indexOf("/"))) - 1;

                    line = line.substring(line.indexOf(" ") + 1);

                    trianglePoint[2] = Integer.parseInt(line.substring(0, line.indexOf("/"))) - 1;

                    Triangle t = new Triangle(trianglePoint[0], trianglePoint[1], trianglePoint[2], vertices);

                    t.setColor(.8f, .7f, .2f, 1, 1);

                    triangles.add(t);
                }

                line = br.readLine();
            }

            m = new Mesh(sTitle, sFilePath, fScale, vertices, triangles);

        } catch (IOException e) {

            e.printStackTrace();
        }

        System.out.println("Object Mesh: " + sFilePath + " loaded");

        return m;
    }

}
