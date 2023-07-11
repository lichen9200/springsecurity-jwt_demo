package com.bbbuuuyyy.taobao;

public class Father {
    private String name = "fath";
    protected String getName()
    {
        System.out.println("我是父亲。。。");
        return this.name;
    }
//儿子 有name属性，调用getname，返回的是父亲的name？在getname返回值为name时。。。？
    //this.name也是父亲。。。
}
class SubSon extends Father {
    private String nickName;
    private static String name = "son";
    public String getNickName()
    {
        return nickName;
    }
//    @Override
//    public String getName()
//    {
//        System.out.println("我是儿子。。。");
//        return nickName;
//
//    }
/*
 1.多态只能调用父子类共有的public方法。
 2.动态绑定：运行时，被调用的是子类。
 */
    public static void main(String[] args) {
       //Father father =  new SubSon();
        SubSon son =  new SubSon();
        System.out.println(son.getName());
    }


}