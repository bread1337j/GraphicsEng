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
                (int)(pos[0] * 100),
                (int)(pos[1] * 100)
        );
        cord.cord[0] = (cord.cord[0] - cord.cord[0]%100);
        cord.cord[1] = (cord.cord[1] - cord.cord[1]%100);
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
        center.cord[0] = (center.cord[0] - center.cord[0]%100);
        center.cord[1] = (center.cord[1] - center.cord[1]%100);
        int p = 0;
        for(int x=(center.cord[0]-50*xw); x<center.cord[0]+50*xw-0.01f; x+=100){
            for(int y=(center.cord[1]-50*yw); y<center.cord[1]+50*yw-0.01f; y+=100){
                //System.out.println("( " + x + " , " + y + " )");
                cc[p++] = getChunk(new Coordinate(x, y));
            }
        }
        return cc;
    }
}
