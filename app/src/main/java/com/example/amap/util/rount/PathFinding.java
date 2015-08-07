package com.example.amap.util.rount;

import android.util.Log;

import org.codehaus.jackson.map.util.LRUMap;

import java.util.LinkedList;
import java.util.List;

public class PathFinding {
    private Node[][] nodes=null;
    private LinkedList closedList=null;
    public Node[] heapTree=null;
    public int heapSize;
    private int [][]_map=null;
    private int [][]_mapToOC=null;//用来放 isOpened  isClosed
    private int []_limit=null;
    private Node startNode=null;
    private Node endNode=null;
    private final int isOpened =2;
    private final int isClosed =3;
    HeuryCache heuryCache=HeuryCache.getInstance();
//    LRUMap a =new LRUMap(2,4);

    private ShangeUtil su= ShangeUtil.getInstance();
    public PathFinding(int i) {
//       _map=(int[][]) su.shapeList.get(i);//这样写不可取，是共享的
        _map=(int[][])su.getShapeList().get(i); //get 同样共享 后打算另开个_mapToOC 来存 速度比较快
//        Log.i("zjx","ab"+startNode.toString());
        _limit = su.HIT;
        closedList = new LinkedList();
        initMap();

    }
    private void initMap() {
        heapSize = 0;
        heapTree = new Node[su.SHANGESIZE * su.SHANGESIZE + 1];
        nodes=new Node[su.SHANGESIZE][su.SHANGESIZE];
        _mapToOC=new int[su.SHANGESIZE][su.SHANGESIZE];
    }
    private void clearMap(){
        for(int i=0;i<su.SHANGESIZE;i++)
            for(int j=0;j<su.SHANGESIZE;j++){
//..让java回收机制去回收而不是遍历
            }
    }
    public List searchPath(MyPoint startPos,MyPoint destiPos)
    {

        startNode=new Node(startPos.x,startPos.y);
        endNode=new Node(destiPos.x,destiPos.y);
//        initMap();

        openListInsert(startNode, 0, null);
        int st=10;
        while (heapSize != 0)
        {
            // remove the initialized component
//            Node firstNode = (Node) openedList.removeFirst();
            Node firstNode =  openListRemove();
//            setOpenRemove(startNode.X,startNode.Y);
            // check the equality
            if (firstNode.X==destiPos.x&&firstNode.Y==destiPos.y) {
                //
                return makePath(firstNode);
            } else {
                //
                // add to the closedList
                closedList.add(firstNode);
                setClose(firstNode.X, firstNode.Y);
                // get the mobile area of firstNode
                LinkedList _limit = firstNode.getLimit();
                // visit

                for (int i = 0; i < _limit.size(); i++){
                    st=(i%2==1?14:10);
                    //get the adjacent node
                    Node neighborNode = (Node) _limit.get(i);
//                    int OpenIndex =openedList.indexOf(neighborNode);
                    boolean isOpen = isOpen(neighborNode.X, neighborNode.Y);
                    //check if it can work
                    boolean isClosed = isClose(neighborNode.X, neighborNode.Y);
                    //
                    boolean isHit = isHit(neighborNode.X,neighborNode.Y);
                    //all of them are negative
//                 if(isClosed||isHit)...else
                    if (!isClosed && !isHit) {
                        if(!isOpen){
                            openListInsert(neighborNode, firstNode.g+st, firstNode);
                        }
                        else{
                            int tmpG = firstNode.g+st;
                            if (this.nodes[neighborNode.X][neighborNode.Y].g > tmpG) {
                                this.nodes[neighborNode.X][neighborNode.Y].setG(tmpG);
                                this.nodes[neighborNode.X][neighborNode.Y]._parentnode = firstNode;
                                heapPercolateUp(nodes[neighborNode.X][neighborNode.Y].position);
                            }
                        }
                    }
                }
            }
        }
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
    private void setOpen(int x,int y)
    {
        _mapToOC[y][x]=isOpened;
    }
    private void setOpenRemove(int x,int y)
    {
        _mapToOC[y][x]=0;
    }
    private void setClose(int x,int y)
    {
        _mapToOC[y][x]=isClosed;
    }
    private boolean isOpen(int x,int y)
    {
            if(_mapToOC[y][x]==isOpened)
            {
                return true;
            }
        return false;
    }
    private boolean isClose(int x,int y)
    {
        if(_mapToOC[y][x]==isClosed)
        {
            return true;
        }
        return false;
    }
    public void openListInsert(Node a, int g, Node parent) {
//        Integer cacheH=heuryCache.hashMap.get(a.X*100+a.Y);
//        Log.i("zjx","cacheH:"+cacheH);
        this.nodes[a.X][a.Y] = new Node(a.X, a.Y, g,a.GetCost(endNode));
        this.nodes[a.X][a.Y]._parentnode = parent;
        heapInsert(this.nodes[a.X][a.Y]);
        this._mapToOC[a.Y][a.X] = isOpened;
    }

    public Node openListRemove() {
        Node temp = heapDeleteMin();
        setOpenRemove(temp.X,temp.Y);
//        this._map[temp.Y][temp.X] |= 0;
        this.nodes[temp.X][temp.Y] = null;
        return temp;
    }

    // Binary Heap
    public void heapPercolateUp(int position) {
        if (position <= this.heapSize) {
            while (position > 1) {
                if (this.heapTree[position].f < this.heapTree[position / 2].f) {
                    Node temp = this.heapTree[position];
                    this.heapTree[position / 2].position = position;
                    this.heapTree[position] = this.heapTree[position / 2];
                    temp.position = position / 2;
                    this.heapTree[position / 2] = temp;
                    position = position / 2;
                } else
                    return;
            }
        }
    }

    public boolean heapInsert(Node element) {
        if (this.heapSize == heapTree.length - 1 || element == null) {
            return false;
        }
        this.heapSize++;
        int hole = this.heapSize;
        while (hole > 1 && element.f < this.heapTree[hole / 2].f) {
            this.heapTree[hole / 2].position = hole;
            this.heapTree[hole] = this.heapTree[hole / 2];
            hole = hole / 2;
        }
        this.heapTree[hole] = element;
        this.heapTree[hole].position = hole;
        return true;
    }

    public Node heapDeleteMin() {
        if (this.heapSize == 0) {
            return null;
        }
        Node smallest = this.heapTree[1];
        this.heapTree[this.heapSize].position = 1;
        this.heapTree[1] = this.heapTree[this.heapSize];
        this.heapTree[this.heapSize] = null;
        this.heapSize--;
        int current = 1;
        int child;
        while (2 * current <= this.heapSize) {
            child = 2 * current;
            if (child != this.heapSize
                    && this.heapTree[child].f > this.heapTree[child + 1].f) {
                child++;
            }
            if (this.heapTree[current].f > this.heapTree[child].f) {
                Node temp = this.heapTree[current];
                this.heapTree[child].position = current;
                this.heapTree[current] = this.heapTree[child];
                temp.position = child;
                this.heapTree[child] = temp;
            } else {
                break;
            }
            current = child;
        }
        return smallest;
    }
}