package sample;

import java.util.*;

/**
 @authors: Sean McFall, Justin Milliman
 CS1122 - L02 - Introduction to Programming II
 Date: 11/03/2020
 Assignment: Chapter 15 Lab
 -----------------------------------------------------------
 In this lab, we make several methods related to iterators, sets and stacks and
 test them using the setEquals method.
 **/

public class Chapter15Lab {
    /**
     * setEquals
     * @param setA : the first set we compare
     * @param setB : the second set we compare
     * @return : returns a boolean if the sets are the same set
     */
    public static boolean setEquals(Set setA, Set setB){
        boolean result = false;

        if(setA.size() != setB.size()){
            return false;
        }

        Iterator iteratorA = setA.iterator();
        Iterator iteratorB = setB.iterator();

        while(iteratorA.hasNext()){
            if(iteratorA.next() != iteratorB.next()){
                return false;
            }
        }

        iteratorA = setA.iterator();
        iteratorB = setB.iterator();

        while(iteratorB.hasNext()){
            if(iteratorB.next() != iteratorA.next()){
                return false;
            }
        }
        return !iteratorA.hasNext() || iteratorB.hasNext();
    }


    /**
     * setEqualsTest
     */
    public static void setEqualsTest ( ){
            Set<Integer> s1 = new HashSet<>();
            s1.addAll ( Arrays.asList (1, 1, 2, 3, 5, 8, 13 ));
            Set <Integer> s2 = new HashSet<>();
            s2.addAll (Arrays.asList (13, 13, 8, 5, 3, 2, 1 ));
            Set <Integer> s3 = new HashSet<>();
            s3.addAll (Arrays.asList (12,17,16,5,9, 84443412,0));

            //SetEquals Test
            System.out.println( "result of setEquals : " + setEquals (s1, s2));
            System.out.println( "result of .equals: " + s1.equals(s2));

            //SetUnion Test
            System.out.println("result of setUnion: " + setUnion(s1,s3));
            System.out.println("result of setIntersection: " + setIntersection(s2,s3));

            //getInverted Test
            HashMap<Integer, String> testCase = new HashMap<>();
            testCase.put(1,"one");
            testCase.put(2,"two");
            testCase.put(3,"three");
            System.out.println("returns result of getInverted: " + getInverted(testCase, "one"));

            //reverseList Test
            List list1 = new ArrayList<String>();
            list1.addAll(Arrays.asList("Mary", "had", "a", "little", "lamb"));
            System.out.println("returns result for reverseList: \n" + reverseList(list1));

            //reverseQueue test
            Queue<String> queue = new LinkedList();
            queue.offer("Mary");
            queue.offer("had");
            queue.offer("a");
            queue.offer("little");
            queue.offer("lamb");
            System.out.print("returns result for reverseQueue: \n" + reverseQueue(queue) + "\n");
        }

    /**
     * setUnion
     * @param setA : the first set we compare
     * @param setb : the second set we compare
     * @return : returns the set of all intersecting elements
     */
    public static Set setUnion(Set setA, Set setb){
        Set set = new HashSet<>();
        for(Object e : setA){
            if(!set.contains(e)){
                set.add(e);
            }
        }
        for(Object e : setb){
            if(!set.contains(e)){
                set.add(e);
            }
        }
        return set;
    }

    /**
     * setIntersection
     * @param setA  : the first set we compare
     * @param setB : the second set we compare
     * @return : returns the set of elements set A and B have in common
     */
    public static Set setIntersection(Set setA, Set setB){
        Set set = new HashSet<>();
        for(Object e : setA){
            if(setB.contains(e) ){
                set.add(e);
            }
        }
        return set;
    }

    /**
     * getInverted
     * @param map : takes in a map with a (key, value)
     * @param value : the value you want to find in a map
     * @return : returns a set of keys that contain the values you want to find
     */
    public static List getInverted(Map map, Object value){
        List list = new ArrayList();
        Set<Map.Entry<Integer, Object>> set = map.entrySet();
        for(Map.Entry e : set){
            if(e.getValue().equals(value)){
                list.add(e.getKey());
            }
        }
        return list;
    }

    /**
     * reverseList
     * @param strings : pass in a list of strings you wish to sort
     * @return : returns the list of strings backwards
     */
    public static List reverseList(List<String> strings){
        List list = new ArrayList<String>();
        Stack st = new Stack();
        for(String e : strings){
            st.push(e);
        }
        while(!st.isEmpty()){
            list.add(st.pop());
        }
        return list;
    }

    /**
     * reverseQueue
     * @param queue1 : enter the queue you want to be flipped
     * @return : returns a backwards queue
     */
    public static Queue reverseQueue(Queue queue1) {
        Queue<String> queue2 = new LinkedList();
        while (queue1.size() > 0) {
            for (int i = 0; i < queue1.size() - 1; i++) {
                queue1.offer(queue1.poll());
            }
            queue2.offer((String) queue1.poll());
        }
        return queue2;
    }


        public static void main(String[] args) {
        Chapter15Lab tester = new Chapter15Lab();
        tester.setEqualsTest();
    }
}
