package com.example.amap.util;


import java.util.LinkedList;

public class Node implements Comparable{
 public MyPoint _Pos;   //position of the node
 public int sourcePoint;
 public int destiPoint;
 //public Node _parentnode;    //the parent node
public Node _parentnode;
 private Node()
 {

 }
 //initialize the NOde
 public Node(MyPoint _Pos)
 {
  this._Pos=_Pos;
 }
 //get the cost of the Path
 public int GetCost(Node node)
 {
//  int m=node._Pos.x-_Pos.x;
//  int n=node._Pos.y-_Pos.y;
//  return (int)Math.sqrt(m*m+n*n);
  int m=Math.abs(node._Pos.x-_Pos.x);
  int n=Math.abs(node._Pos.y-_Pos.y);
//  if(m>n)return 11*m+5*n;
//  else return 5*m+11*n;
//  return m>n?8*m+7*n:7*m+8*n;
  int max=m>n?m:n;
//  int min=m<n?m:n;
//  int d2=(int)(Math.sqrt(2)*min)+m+n-2*min;
  return m+n;
 }
 //check if the node is the destination point
 public boolean equals(Object node)
 {
   if (_Pos.x == ((Node) node)._Pos.x && _Pos.y == ((Node) node)._Pos.y)
  {
   return true;
  }
   return false;
 }
 //get the minist cost
 public int compareTo(Object node)
 {
  int a1=sourcePoint+destiPoint;
  int a2=((Node)node).sourcePoint+((Node)node).destiPoint;
  if(a1<a2){
   return -1;
  }else if(a1==a2)
  {return 0;}
  else return 1;

 }
 public LinkedList getLimit()
 {
  LinkedList limit=new LinkedList();
  int x=(int)_Pos.x;
  int y=(int)_Pos.y;
  limit.add(new Node(new MyPoint(x,y-1)));   //up
  limit.add(new Node(new MyPoint(x+1,y-1)));   //right_up
  limit.add(new Node(new MyPoint(x+1,y)));   //right
  limit.add(new Node(new MyPoint(x+1,y+1)));   //right_down
  limit.add(new Node(new MyPoint(x,y+1)));   //down
  limit.add(new Node(new MyPoint(x-1,y+1)));   //left_down
  limit.add(new Node(new MyPoint(x-1,y)));   //left
  limit.add(new Node(new MyPoint(x-1,y-1)));   //left_up









  return limit;
 }
 
}


 