import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.*;

public class Main{
    
    HashMap<String, String> things = new HashMap<String, String>();

    public static Command getElement(String element){ //converts something to a command.
        Pattern p = Pattern.compile("([+-]?)([A-Za-z]*)"); //+Banana, -salt
        Matcher m = p.matcher(element);
        if(m.matches()){
            System.out.println(m.group(1));
            if(m.group(1).equals("+")){
                return new Command("add", m.group(2));
            } else if(m.group(1).equals("-")){
                return new Command("remove", m.group(2));
            } else {
                return new Command("generic", m.group(2));
            }
        } else {
            return new Command("undefined", m.group(2)); //handle unknown command
        }
    }
    public static void main(String args[]){

        HashMap<String, String> thingPairs = new HashMap<String, String>();
        Scanner sc = new Scanner(System.in);
        Broker broke = new Broker();
        int i;
        boolean exit = false;
        thingPairs.put("banana", "yellow");
        thingPairs.put("sugar", "white");
        thingPairs.put("salt", "white");
        thingPairs.put("apple", "red");
        thingPairs.put("tomato", "red");
        thingPairs.put("leaf", "green");
        ArrayList<Thing> things = ThingFactory.getThings(thingPairs);
        // for(i=0; i<things.size(); i++){
        //     System.out.println(things.get(i).getType() + " is color " + things.get(i).getColor());
        // }
        while(!exit){
            System.out.print("\n>>>");
            String element = sc.nextLine();
            Command cmd = getElement(element);
            // System.out.println("Command type: " + cmd.type + "\nCommand value: " + cmd.value);
            for(i=0; i<things.size();i++){
                if(things.get(i).getType().equals(cmd.value)){
                    if(cmd.type.equals("remove")){
                        broke.unsubscribe(things.get(i));
                    } else if(cmd.type.equals("add")){
                        broke.subscribe(things.get(i));
                    }
                }
            }
            if(cmd.type.equals("generic")){
                if(cmd.value.equals("list")){
                    broke.notifySubs();
                } else if(cmd.value.equals("Frog")){
                    broke.frog();
                } else if(cmd.value.equals("exit")){
                    exit = true;
                } else {
                    broke.action(cmd.value);
                }
            }
        }
    }

}

class Thing{
    private String type;
    private String color;

    public Thing(ThingBuilder b){
        this.type = b.type;
        this.color = b.color;
    }

    public String getType(){
        return this.type;
    }

    public String getColor(){
        return this.color;
    }

    public void update(){
        System.out.println(this.type + ": Sometimes I'm " + this.color);
    }

    public static class ThingBuilder{

        private String type;
        private String color;

        public ThingBuilder(){
            //do nothing
        }

        public ThingBuilder setType(String type){
            this.type = type;
            return this;
        }

        public ThingBuilder setColor(String color){
            this.color = color;
            return this;
        }

        public Thing build(){
            return new Thing(this);
        }

    }

}

class ThingFactory{
    public static ArrayList<Thing> getThings(HashMap<String, String> things){
        ArrayList<Thing> thingList = new ArrayList<Thing>();
        Thing temp;
        for(String key : things.keySet()){
            temp = new Thing.ThingBuilder().setType(key).setColor(things.get(key)).build();
            thingList.add(temp);
        }
        return thingList;
    }
}

class Broker{
    private ArrayList<Thing> subscribers;
    public Broker(){
        //Constructor code if neccessary
        subscribers = new ArrayList<Thing>();
    }
    public void subscribe(Thing t){
        this.subscribers.add(t);
    }
    public void unsubscribe(Thing t){
        this.subscribers.remove(t);
    }
    private boolean isPresent(ArrayList<String>unique, String element){
        for(int i=0; i<unique.size(); i++){
            if(unique.get(i).equals(element)){
                return true;
            }
        }
        return false;
    }
    private ArrayList<String> getUniqueColors(){
        ArrayList<String> unique = new ArrayList<String>();
        int i;
        for(i=0;i<subscribers.size();i++){
            if(!isPresent(unique, subscribers.get(i).getColor())){
                unique.add(subscribers.get(i).getColor());
            }
        }
        return unique;
    }
    public void frog(){
        ArrayList<String> unique = getUniqueColors();
        int i;
        for(i=0;i<unique.size();i++){
            System.out.println("Frog: Ribbit! I'm " + unique.get(i) + " today!");
        }
    }
    public void action(String col){
        int i;
        for(i=0;i<subscribers.size();i++){
            if(subscribers.get(i).getColor().equals(col)){
                System.out.println(subscribers.get(i).getType() + ": Hey! I'm sometimes " + col);
            }
        }
    }
    public void notifySubs(){
        for(int i=0; i<subscribers.size();i++)
        {
            System.out.println("Subscriber: " + subscribers.get(i).getType() + " -> " + subscribers.get(i).getColor());
        }
    }
}


/*
let's define 3 classes of commands: add(+), remove(-), and generic(nothing) commands
*/

class Command{
    public String type;
    public String value;
    public Command(String type, String value){
        this.type = type;
        this.value = value;
    }
}