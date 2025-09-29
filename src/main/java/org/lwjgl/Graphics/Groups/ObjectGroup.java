package org.lwjgl.Graphics.Groups;

import org.lwjgl.Graphics.Objects.AObject;
import org.lwjgl.Util.Coordinate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ObjectGroup {
    Map<Coordinate, CullingChunk> map = new HashMap<>(500);

    public void add(AObject obj){
        float[] pos = obj.getCenter();
        Coordinate cord = new Coordinate(
                ((float) Math.round(pos[0] * 10) / 10),
                ((float) Math.round(pos[1] * 10) / 10)
        );
        if(map.containsKey(cord)){
            map.get(cord).addObj(obj);
            //System.out.println("Found CC " + map.get(cord) + " and put in object " + obj +", current list: " + map.get(cord).arr);
        }else {
            CullingChunk cc = new CullingChunk(cord);
            map.put(cord, cc);
            cc.addObj(obj);
        }
    }

    public CullingChunk getChunk(Coordinate cord){
        if(!map.containsKey(cord)){
            map.put(cord, new CullingChunk(cord));
        }
        return map.get(cord);
    }

    public CullingChunk[] getRect(Coordinate center, int xw, int yw){
        CullingChunk[] cc = new CullingChunk[xw*yw];
        int p = 0;
        for(float x=(center.cord[0]-0.05f*xw); x<center.cord[0]+0.05f*xw-0.01f; x+=0.1f){
            for(float y=(center.cord[1]-0.05f*yw); y<center.cord[1]+0.05f*yw-0.01f; y+=0.1f){
                //System.out.println("( " + x + " , " + y + " )");
                cc[p++] = getChunk(new Coordinate(x, y));
            }
        }
        return cc;
    }
}
