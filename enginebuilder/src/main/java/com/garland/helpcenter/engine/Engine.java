package com.garland.helpcenter.engine;


import java.io.*;
import java.util.TreeMap;

import com.garland.helpcenter.datastructure.tree.NodeCallback;
import com.garland.helpcenter.datastructure.tree.Tree;


/**
 * Created by lemon on 11/3/2017.
 */

public class Engine implements Serializable {
    private static final long serialVersionUID="Engine".hashCode();

    public TreeMap<String,Tree<Description>> data;
    public TreeMap<String,TreeMap<Integer,CommonCenter>> commonCenters;
    public TreeMap<String,String> timeUnit;

    public Engine() {
        data=new TreeMap<>();
        commonCenters=new TreeMap<>();
        timeUnit=new TreeMap<>();
    }

    public Description findLoc(String unit, final String roll) {
        try{
            return data.get(unit).search(new NodeCallback<Description>(){
                
                @Override
                public int compare(Description description) {
                    if (description.from.equals(roll) || description.to.equals(roll) || (description.from.compareTo(roll) < 0 && description.to.compareTo(roll) > 0))
                        return 0;
                    else if (roll.compareTo(description.from) < 0) 
                        return -1;
                    return 1;
                }
            }).data;
        } catch(Exception e) {
            return null;
        }
        
    }

    public CommonCenter getCenter(String unit,int centerId) {
        return commonCenters.get(unit).get(centerId);
    }

    public void write() throws Exception {
        if(data==null||commonCenters==null) throw new Exception("Please initialize all properties...");
        try{
			ObjectOutputStream outData=new ObjectOutputStream(new FileOutputStream("resources/data.data"));
			ObjectOutputStream outCenter=new ObjectOutputStream(new FileOutputStream("resources/center.data"));
			ObjectOutputStream outTime=new ObjectOutputStream(new FileOutputStream("resources/time.data"));
            outCenter.writeObject(commonCenters);
            outData.writeObject(data);
            outTime.writeObject(timeUnit);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    public void read() {
        try{
			ObjectInputStream inData=new ObjectInputStream(new FileInputStream("resources/data.data"));
			ObjectInputStream inCenter=new ObjectInputStream(new FileInputStream("resources/center.data"));
			ObjectInputStream inTime=new ObjectInputStream(new FileInputStream("resources/time.data"));
            this.data= (TreeMap<String, Tree<Description>>) inData.readObject();
            this.commonCenters= (TreeMap<String, TreeMap<Integer, CommonCenter>>) inCenter.readObject();
            this.timeUnit= (TreeMap<String, String>) inTime.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
	
	@SuppressWarnings("unchecked")
    public void read(InputStream dataIo,InputStream centerIO,InputStream timeIO) {
        try{
			ObjectInputStream inData=new ObjectInputStream(dataIo);
			ObjectInputStream inCenter=new ObjectInputStream(centerIO);
			ObjectInputStream inTime=new ObjectInputStream(timeIO);
            this.data= (TreeMap<String, Tree<Description>>) inData.readObject();
            this.commonCenters= (TreeMap<String, TreeMap<Integer, CommonCenter>>) inCenter.readObject();
            this.timeUnit= (TreeMap<String, String>) inTime.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
