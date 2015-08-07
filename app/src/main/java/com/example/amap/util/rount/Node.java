package com.example.amap.util.rount;


import java.util.LinkedList;

public class Node {
// public MyPoint _Pos;   //position of the node
 public int g;
 public int h;
 public int f;
 public int X;
 public int Y;
 //public Node _parentnode;    //the parent node
public Node _parentnode;
 public Node(int x, int y, int g, int h)
 {
  this.Y = y;
  this.X = x;
  if (!(g == 0 && h == 0)) {
   this.g = g;
   this.h = h;
   this.f =  (this.g + this.h);
  }
 }
 public Node(int x,int y){
  this.Y = y;
  this.X = x;
 }
 //initialize the NOde
 public int position; // heap position
 //get the cost of the Path
 public void setG(int g) {
  this.g = g;
  this.f = (this.g + this.h);
 }
 public int GetCost(Node node)
 {
//  int m=node._Pos.x-_Pos.x;
//  int n=node._Pos.y-_Pos.y;
//  return (int)Math.sqrt(m*m+n*n);
  int m=Math.abs(node.X-this.X);
  int n=Math.abs(node.Y-this.Y);
//  if(m>n)return 11*m+5*n;
//  else return 5*m+11*n;
//  return m>n?8*m+7*n:7*m+8*n;
//  int max=m>n?m:n;
//  int min=m<n?m:n;
//  int d2=4*(int)(Math.sqrt(2)*min)+3*(max-min);
//  int d2=4*min+3*(max-min);
//  return m+n;
  return 10*(m+n);
 }
 //check if the node is the destination point
 public boolean equals(Object node)
 {
   if (this.X == ((Node) node).X && this.Y == ((Node) node).Y)
  {
   return true;
  }
   return false;
 }
 public LinkedList getLimit()
 {
  LinkedList limit=new LinkedList();
  int x=X;
  int y=Y;
  limit.add(new Node(x,y-1));   //up
  limit.add(new Node(x+1,y-1));   //right_up
  limit.add(new Node(x+1,y));   //right
  limit.add(new Node(x+1,y+1));   //right_down
  limit.add(new Node(x,y+1));   //down
  limit.add(new Node(x-1,y+1));   //left_down
  limit.add(new Node(x-1,y));   //left
  limit.add(new Node(x-1,y-1));   //left_up
  return limit;
 }
 
}


 