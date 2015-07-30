package com.example.amap.util.rount;
import java.util.LinkedList;
import java.util.List;

public class PathFinding {
    private OpenedList openedList;
    private LinkedList closedList;
    private int [][]_map;
    private int []_limit;
    public PathFinding(int [][]map,int []limit)
    {
        _map=map;
        _limit=limit;
        openedList=new OpenedList();
        closedList=new LinkedList();

    }
    public List searchPath(MyPoint startPos,MyPoint destiPos)
    {
        Node startNode=new Node(startPos);
        Node destiNode=new Node(destiPos);
        startNode.sourcePoint=0;
        startNode.destiPoint=startNode.GetCost(destiNode);
        startNode._parentnode=null;
        openedList.add(startNode);
        while (!openedList.isEmpty())
        {
            // remove the initialized component
            Node firstNode = (Node) openedList.removeFirst();
            // check the equality
            if (firstNode.equals(destiNode)) {
                //
                return makePath(firstNode);
            } else {
                //
                // add to the closedList
                closedList.add(firstNode);
                // get the mobile area of firstNode
                LinkedList _limit = firstNode.getLimit();
                // visit
                for (int i = 0; i < _limit.size(); i++){
                    //get the adjacent node
                    Node neighborNode = (Node) _limit.get(i);
                    //
                    boolean isOpen = openedList.contains(neighborNode);
                    //check if it can work
                    boolean isClosed = closedList.contains(neighborNode);
                    //
                    boolean isHit = isHit(neighborNode._Pos.x,
                            neighborNode._Pos.y);
                    //all of them are negative
                    if (!isOpen && !isClosed && !isHit) {
                        //set the costFromStart
                        neighborNode.sourcePoint= firstNode.sourcePoint + 1;
                        //set the costToObject
                        neighborNode.destiPoint = neighborNode
                                .GetCost(destiNode);
                        //change the neighborNode's parent nodes
                        neighborNode._parentnode = firstNode;
                        //add to level
                        openedList.add(neighborNode);
                    }
                }
            }
        }
        //clear the data
        openedList.clear();
        closedList.clear();
        //
        return  null;
    }
    private LinkedList makePath(Node node)
    {
        LinkedList path=new LinkedList();
        while(node._parentnode!=null)
        {
            path.addFirst(node);
            node=node._parentnode;
        }
        path.addFirst(node);
        return path;
    }
    private boolean isHit(int x,int y)
    {
        for(int i=0;i<_limit.length;i++)
        {
            if(_map[y][x]==_limit[i])
            {
                return true;
            }
        }
        return false;
    }
    private class OpenedList extends LinkedList{
        private static final long serialVersionUID = 1L;
        public void add(Node node)
        {
            for(int i=0;i<size();i++)
            {
                if(node.compareTo(get(i))<=0)
                {
                    add(i,node);
                    return;
                }
            }
            addLast(node);
        }
    }
}